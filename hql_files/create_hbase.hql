CREATE EXTERNAL TABLE IF NOT EXISTS kamathk_events_player_hbase (
    player STRING,
    goals BIGINT,
    goals_righty BIGINT,
    goals_lefty BIGINT, 
    goals_header BIGINT,
    goals_by_penalty BIGINT,
    goals_by_freekick BIGINT,
    yellow_cards BIGINT,
    red_cards BIGINT,
    shots BIGINT,
    shots_on_target BIGINT
)
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES (
    'hbase.columns.mapping' = ':key,player_info:goals#b, 
    player_info:goals_righty#b, player_info:goals_lefty#b, player_info:goals_header#b,
    player_info:goals_by_penalty#b,player_info:goals_by_freekick#b,player_info:yellow_cards#b,
    player_info:red_cards#b,player_info:shots#b,player_info:shots_on_target#b'
)
TBLPROPERTIES (
    'hbase.table.name' = 'kamathk_events_player_hbase'
);

