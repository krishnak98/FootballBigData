CREATE EXTERNAL TABLE football_events (
    id_odsp STRING, 
    id_event STRING, 
    sort_order SMALLINT, 
    game_minute SMALLINT,
    text STRING, 
    event_type SMALLINT, 
    event_type2 STRING, 
    side SMALLINT, 
    event_team STRING, 
    opponent STRING, 
    player STRING,
    player2 STRING,
    player_in STRING, 
    player_out STRING, 
    shot_place STRING, 
    shot_outcome STRING, 
    is_goal SMALLINT, 
    location STRING, 
    bodypart STRING, 
    assist_method SMALLINT, 
    situation STRING, 
    fast_break SMALLINT
)
ROW FORMAT SERDE 'org.apache.hadoop.hive.serde2.OpenCSVSerde'
WITH SERDEPROPERTIES (
   "separatorChar" = "\,",
   "quoteChar"     = "\""
)
STORED AS TEXTFILE
LOCATION '/home/hadoop/kamathk/football_data/batch_data';

