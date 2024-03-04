'use strict';
const http = require('http');
var assert = require('assert');
const express= require('express');
const app = express();
const mustache = require('mustache');
const filesystem = require('fs');
const url = require('url');
const port = Number(3091);

const hbase = require('hbase')
var hclient = hbase({ host: process.argv[3], port: Number(process.argv[4]), encoding: 'latin1'})
// var hclient = hbase({
// 	host: 'localhost',
// 	port: 8070,
// 	encoding: 'latin1'
//   });
  

function counterToNumber(c) {
	return Number(Buffer.from(c, 'latin1').readBigInt64BE());
}
function rowToMap(value) {
    if (!value) {
        console.error('Received undefined or null value from HBase');
        return {}; // Return an empty object or handle the error accordingly
    }
    var stats = {};
    value.forEach(function (item) {
		stats[item['column']] = counterToNumber(item['$'])
	});
    return stats;
}


hclient.table('kamathk_events_player_hbase').row('lionel messi').get((error, value) => {
	console.info(rowToMap(value))
})


app.use(express.static('public'));
app.get('/player_stats.html',function (req, res) {
    const player=req.query['player'].toLowerCase();

	hclient.table('kamathk_events_player_hbase').row(player).get(function (err, cells) {
		const playerInfo = rowToMap(cells);
		const shotsOnTarget = playerInfo["player_info:shots_on_target"];
		const totalShots = playerInfo["player_info:shots"];
		const goals = playerInfo["player_info:goals"];
		const shotAccuracy = ((shotsOnTarget / totalShots) * 100).toFixed(2);
		const convRate = ((goals / shotsOnTarget) * 100).toFixed(2);

	
		var template = filesystem.readFileSync("result.mustache").toString();
		var html = mustache.render(template,  {
			player: req.query['player'],
			goals: goals,
			yellow_cards: playerInfo["player_info:yellow_cards"],
			red_cards : playerInfo["player_info:red_cards"],
			total_shots : totalShots,
			shots_on_target: shotsOnTarget,
			lefty_goals: playerInfo["player_info:goals_lefty"],
			righty_goals: playerInfo["player_info:goals_righty"],
			header_goals : playerInfo["player_info:goals_header"],
			penalty_goals: playerInfo["player_info:goals_by_penalty"],
			freekick_goals: playerInfo["player_info:goals_by_freekick"],
			shot_accuracy: shotAccuracy,
			conv_rate: convRate
		});
		res.send(html);
	});
});


app.listen(port);
