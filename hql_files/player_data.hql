CREATE TABLE IF NOT EXISTS kamathk_events_player AS 
(SELECT
    player,
    SUM(CASE WHEN is_goal = 1 THEN 1 ELSE 0 END) AS goals,
    SUM(CASE WHEN is_goal = 1 AND bodypart = 1 THEN 1 ELSE 0 END) AS goals_righty,
    SUM(CASE WHEN is_goal = 1 AND bodypart = 2 THEN 1 ELSE 0 END) AS goals_lefty,
    SUM(CASE WHEN is_goal = 1 AND bodypart = 3 THEN 1 ELSE 0 END) AS goals_header,
    SUM(CASE WHEN is_goal = 1 AND location = 14 AND situation = 2 THEN 1 ELSE 0 END) AS goals_by_penalty,
    SUM(CASE WHEN is_goal = 1 AND situation = 4 THEN 1 ELSE 0 END) AS goals_by_freekick,
    SUM(CASE WHEN event_type = 4 OR event_type = 5 THEN 1 ELSE 0 END) AS yellow_cards, 
    SUM(CASE WHEN event_type = 6 OR event_type = 5 THEN 1 ELSE 0 END) AS red_cards,
    SUM(CASE WHEN event_type = 1 THEN 1 ELSE 0 END) AS shots,
    SUM(CASE WHEN event_type = 1 and shot_outcome != 2  THEN 1 ELSE 0 END) AS shots_on_target
FROM kamathk_football_events
WHERE player IS NOT NULL AND player != '' AND player != 'NA' 
GROUP BY player
);
