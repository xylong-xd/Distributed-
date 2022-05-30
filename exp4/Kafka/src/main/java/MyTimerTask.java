import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.Properties;
import java.util.TimerTask;

/**
 * @author xylong
 * @date 2022/4/25 16:40
 */
public class MyTimerTask extends TimerTask {
    public static Properties initConfiig(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092");
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
    public MyTimerTask(String periodDemo) {

        Properties props = initConfiig();
        KafkaProducer<String,String> producer = new KafkaProducer<>(props);
        String re ="";
        for (int i = 0; i <4; i++) {
            re = re + Consumer.res.get(i) + " ";
        }

            ProducerRecord<String, String> record = new ProducerRecord<>("res", re);
            producer.send(record);
            System.out.println("结果已发送");
            System.out.println(record.value());

        }


    @Override
    public void run() {
    }
}
