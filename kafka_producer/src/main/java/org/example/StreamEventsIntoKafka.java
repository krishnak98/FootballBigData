package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.fasterxml.jackson.databind.ObjectMapper;

// Inspired by http://stackoverflow.com/questions/14458450/what-to-use-instead-of-org-jboss-resteasy-client-clientrequest
public class StreamEventsIntoKafka {
    static class Task extends TimerTask {
        private Client client;


        // Adapted from http://hortonworks.com/hadoop-tutorial/simulating-transporting-realtime-events-stream-apache-kafka/
        Properties props = new Properties();
        int index;
        int block_size;
        String TOPIC = "mpcs53014_football_stream";
        KafkaProducer<String, String> producer;

        public Task() {
            client = ClientBuilder.newClient();
            // enable POJO mapping using Jackson - see
            // https://jersey.java.net/documentation/latest/user-guide.html#json.jackson
            client.register(JacksonFeature.class);
            props.put("bootstrap.servers", bootstrapServers);
            props.put("acks", "all");
            props.put("retries", 0);
            props.put("batch.size", 16384);
            props.put("linger.ms", 1);
            props.put("buffer.memory", 33554432);
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

            producer = new KafkaProducer<>(props);
            index = 0;
            block_size = 10;
        }


        public void run() {
            String csvFile = "./football_events_speed_data.csv";
            int i = 0;
            String line;
            String csvSplitBy = ","; // Use appropriate delimiter based on your CSV file
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                while ((line = br.readLine()) != null) {
                    i++;
                    if (i < this.index) continue;
                    if (i > this.index + this.block_size) break;
                    String[] eventData = line.split(csvSplitBy);
                    FootballEvent footballEvent = createFootballEventFromCSV(eventData);
                    if (footballEvent != null) {
                        String jsonEvent = convertFootballEventToJson(footballEvent);
                        if (jsonEvent != null) {
                            ProducerRecord<String, String> data = new ProducerRecord<>(TOPIC, jsonEvent);
                            producer.send(data, (recordMetadata, exception) -> {
                                if (exception == null) {
                                    System.out.println("Record written to offset " +
                                            recordMetadata.offset() + " timestamp " +
                                            recordMetadata.timestamp());
                                } else {
                                    System.err.println("An error occurred");
                                    exception.printStackTrace(System.err);
                                }
                            });
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.index += this.block_size;
        }

        private FootballEvent createFootballEventFromCSV(String[] eventData) {
            // Logic to map CSV data to FootballEvent fields
            // Parse eventData array and create FootballEvent object
            // Implement this method according to your CSV structure
            // Return the FootballEvent object
            // Example logic to create FootballEvent from CSV data:
            try {
                return new FootballEvent(
                        eventData[0], eventData[1], Short.parseShort(eventData[2]), Short.parseShort(eventData[3]),
                        eventData[4], Short.parseShort(eventData[5]), eventData[6], Short.parseShort(eventData[7]),
                        eventData[8], eventData[9], eventData[10], eventData[11], eventData[12], eventData[13],
                        eventData[14], eventData[15], Short.parseShort(eventData[16]), eventData[17], eventData[18],
                        Short.parseShort(eventData[19]), eventData[20], Short.parseShort(eventData[21])
                );
            } catch (Exception e) {
                for(String s : eventData)
                    System.out.println(s);
                e.printStackTrace();
                return null;
            }
        }

        private String convertFootballEventToJson(FootballEvent footballEvent) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                // Convert FootballEvent object to JSON string
                return mapper.writeValueAsString(footballEvent);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    static String bootstrapServers = new String("localhost:9092");

    public static void main(String[] args) {
        if (args.length > 0)  // This lets us run on the cluster with a different kafka
            bootstrapServers = args[0];
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Task(), 0, 2 * 1000);
    }
}


