# Football Analytics using Big Data

## Screenshot
![Screenshot](image.png)


## Data Information and Preprocessing

[Football (soccer) dataset](https://www.kaggle.com/datasets/secareanualin/football-events/data) containing 9074 games, totaling 941,009 events from the biggest 5 European football leagues.

Python scripts in utils/ were used to clean the CSV data, including replacing commas within text fields with colons and removing events with missing information
The data was into 2 parts, 99% for the batch layer, and 1% for mocking the speed layer streaming, and moved to hdfs.
I created 3 tables for managing data:
1. football_events - This table stores all the events from the batch layer CSV data.
2. events_player (managed by hive) - Entries from football_events are grouped by player name. Entries without a player name or with an empty/null player name are removed in this table.
3. events_player_hbase - This table is created in HBase and contains the same columns as events_player.

For creating these tables, run the following hql scripts (in order)
1. football_events.hql 
2. player_data.hql
3. create_hbase.hql
4. write_to_hbase.hql 

In app/ folder, my app.js queries the events_player_hbase HBase table, and I display the results using some basic HTML and CSS. 

A Kafka topic named football_stream was created to stream all incoming (mocked) football events.

My event streaming ingestion codebase is kafka_producer, and consumer is kafka_consumer.

kafka_producer
1. FootballEvent - POJO. contains the event fields and their getters/setters
2. StreamEventsIntoKafka - Reads data from football_events_speed_data.csv (PLEASE ENSURE this csv file is present in the same folder as your uberjar). Each event from the CSV file is read and placed onto the message queue in blocks of 10. The program retrieves new blocks every 2 seconds. I chose this interval because it aligns with the expected frequency of realistic data. On average, an event occurs approximately every minute. Considering the likelihood of multiple games running simultaneously, this timeframe seemed to be a reasonable estimate.

kafka_consumer
1. FootballEvent - Plain object in Scala
2. StreamEvents - This reads data from our kafka topic, and then updates the appropriate entries in HBase. One change is that I needed to add boolean columns_to_be_updated, to check if any column needed updating. This is because there are a lot of irrelevant (to my app) events, like substitutions. 


## Issues/Future Work
1. Add more fields to Hbase. 
2. Show player trends through the years. 
3. Currently, if information about the current index and blocks is inside kafka, which is not ideal. This needs to be moved to a file or Zookeeper.
4. Get real time data. I was looking at something like https://towardsdatascience.com/embedding-the-language-of-football-using-nlp-e52dc153afa6, but couldn't execute it due to time constraints.



## Demo Link 
https://www.loom.com/share/763728e9f38d491ebbf8bbccecba1ec1?sid=fd012189-7f53-4f21-a700-45c61064333d
Note: This is a screenshare to demonstrate the functionality of the speed layer on the UI.