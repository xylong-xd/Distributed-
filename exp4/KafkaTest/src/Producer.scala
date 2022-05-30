import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties

/**
 * @author xylong
 * @date 2022/4/21 16:41
 */
object Producer {
  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.setProperty("bootstrap.servers","localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    val kp = new KafkaProducer[String, String](props)
    while(true){
      var str = scala.util.Random.nextInt(100).toString
      println(str)
      kp.send(new ProducerRecord[String,String]("random",str))
      println("发送")
      Thread.sleep(1)
    }
  }
}
