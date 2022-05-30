import org.apache.kafka.clients.consumer.KafkaConsumer

import java.util.{Collections, Properties}

/**
 * @author xylong
 * @date 2022/4/21 16:41
 */
object Comsumer {
  def main(args: Array[String]): Unit = {

    val props = new Properties()
    props.setProperty("bootstrap.servers","localhost:9092")
    props.setProperty("group.id","1")
    props.setProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer")
    props.setProperty("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer")


    val con = new KafkaConsumer[String, String](props)

    con.subscribe(Collections.singletonList("random"))
    while (true){
      val records = con.poll(1)
      val rec = records.iterator().next()
      println(rec)
    }
  }

}
