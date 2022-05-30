import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * @author xylong
 * @date 2022/4/26 9:22
 */
public class Controller implements Runnable{
    public static Properties initConfig(){
        Properties prop = new Properties();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        //latest收最新的数据 none会报错 earliest最早的数据
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "G1");
        return prop;
    }
    public Controller(String topic) {
        Properties prop = initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);

        if(topic.equals("random")) {
            consumer.subscribe(Collections.singleton(topic));
            RealtimeChart chart = new RealtimeChart("Real-time Chart", "result", 500);
            while (true) {
                ConsumerRecords<String, String> poll = consumer.poll(100);
                for (ConsumerRecord<String, String> record : poll) {
//                System.out.println(record.value());
//                System.out.println(record.value().substring(7,record.value().length()));
                    double data = Double.parseDouble(record.value().substring(7, record.value().length()));
                    chart.plot(data);
                }
            }
        }
        if(topic.equals("res")) {
            while (true) {
                consumer.subscribe(Collections.singleton(topic));
                ConsumerRecords<String, String> poll = consumer.poll(10);
                for (ConsumerRecord<String, String> record : poll) {
//                System.out.println(record.value());
//                System.out.println(record.value().substring(7,record.value().length()));
                    System.out.println(record.value());
                }
            }
        }
    }

    @Override
    public void run() {

    }
}
