import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import com.fasterxml.jackson.databind.{ DeserializationFeature, ObjectMapper }
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.ConnectionFactory
import org.apache.hadoop.hbase.client.Get
import org.apache.hadoop.hbase.client.Increment
import org.apache.hadoop.hbase.util.Bytes

object StreamEvents {
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  val hbaseConf: Configuration = HBaseConfiguration.create()
  hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
  hbaseConf.set("hbase.zookeeper.quorum", "localhost")

  val hbaseConnection = ConnectionFactory.createConnection(hbaseConf)
  val goalsByPlayer = hbaseConnection.getTable(TableName.valueOf("kamathk_events_player_hbase"))

    def incrementDelaysByRoute(fevent: FootballEvent) : String = {
      var details = ""
      if(fevent.player.isEmpty) return ""
      else {
        var columns_to_be_updated = false
        val inc = new Increment(Bytes.toBytes(fevent.player))
        if(fevent.is_goal == 1) {
          columns_to_be_updated = true
          details += "goals,"
          inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("goals"), 1)
          if (fevent.bodypart == "1.0") {
            columns_to_be_updated = true
            details += "goals_righty,"
            inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("goals_righty"), 1)
          } else if (fevent.bodypart == "2.0") {
            columns_to_be_updated = true
            details += "goals_lefty,"
            inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("goals_lefty"), 1)
          } else if (fevent.bodypart == "3.0") {
            columns_to_be_updated = true
            details += "goals_header,"
            inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("goals_header"), 1)
          }
          if (fevent.location == "14.0" && fevent.situation == "2.0") {
            // penalty
            columns_to_be_updated = true
            details += "goals_by_penalty,"
            inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("goals_by_penalty"), 1)
          }
          if (fevent.situation == "4.0") {
            // free kick
            columns_to_be_updated = true
            details += "goals_by_freekick,"
            inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("goals_by_freekick"), 1)
          }
        }
        if(fevent.event_type== 4 || fevent.event_type == 5) {
          // yellow card
          columns_to_be_updated = true
          details += "yellow_cards,"
          inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("yellow_cards"), 1)
        }
        if (fevent.event_type == 5 || fevent.event_type == 6) {
          // red card
          columns_to_be_updated = true
          details += "red_cards,"
          inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("red_cards"), 1)
        }
        if(fevent.event_type == 1) {
          columns_to_be_updated = true
          details += "shots,"
          inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("shots"), 1)
          if(fevent.shot_outcome != "2.0") {
            columns_to_be_updated = true
            details += "shots_on_target,"
            inc.addColumn(Bytes.toBytes("player_info"), Bytes.toBytes("shots_on_target"), 1)
          }
        }
        if(columns_to_be_updated) {
          // some column needs to be updated. If the
          goalsByPlayer.increment(inc)
          return "Updated speed layer " + details + " for " + fevent.player
        } else {
          return ""
        }

      }
    }

  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.println(s"""
        |Usage: StreamFlights <brokers>
        |  <brokers> is a list of one or more Kafka brokers
        |
        """.stripMargin)
      System.exit(1)
    }

    val Array(brokers) = args

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("StreamEvents")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    // Create direct kafka stream with brokers and topics
    val topicsSet = Set("mpcs53014_kamathk_football_stream")
    // Create direct kafka stream with brokers and topics
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc, PreferConsistent,
      Subscribe[String, String](topicsSet, kafkaParams)
    )
    // Get the lines, split them into words, count the words and print
    val serializedRecords = stream.map(_.value);

    val fevents = serializedRecords.map(rec => mapper.readValue(rec, classOf[FootballEvent]))
    // Update speed table
    val processedEvents = fevents.map(incrementDelaysByRoute)
    processedEvents.print()
    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}
