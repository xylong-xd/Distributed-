import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.util.Timer;

/**
 * @author xylong
 * @date 2022/4/25 17:34
 */
public class ResultConsumer {


    public static void main(String[] args) {
        String top1 = "random";
        String top2 = "res";
        Runnable cons1 = new Controller(top1);
        Runnable cons2 = new Controller(top2);
        new Thread(cons2).start();
        new Thread(cons1).start();

    }
}
