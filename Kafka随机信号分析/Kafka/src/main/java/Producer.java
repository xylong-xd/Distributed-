

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.internals.Topic;
import org.apache.kafka.common.serialization.StringSerializer;

import java.awt.*;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * @author xylong
 * @date 2022/4/21 22:26
 */
public class Producer{

    public static Properties initConfiig(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
    public static void main(String[] args) throws InterruptedException, ExecutionException, AWTException {
        Properties props = initConfiig();
        KafkaProducer<String,String> producer = new KafkaProducer<>(props);
        Random r = new Random();

        Robot ro=new Robot();
        while (true){
            int i = r.nextInt(100);
            System.out.println(i);
            ProducerRecord<String, String> re = new ProducerRecord<>("random", "random:"+i);
            producer.send(re);
            System.out.println("yifasong");
            ro.delay(100);
        }
    }

}

