INSERT OVERWRITE TABLE kamathk_events_player_hbase
SELECT 
    player,
    goals,
    goals_righty,
    goals_lefty, 
    goals_header,
    goals_by_penalty,
    goals_by_freekick,
    yellow_cards,
    red_cards,
    shots,
    shots_on_target
FROM kamathk_events_player
