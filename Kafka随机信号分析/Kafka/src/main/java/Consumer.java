import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.security.Signature;
import java.time.Duration;
import java.util.*;

/**
 * @author xylong
 * @date 2022/4/22 11:15
 */
public class Consumer {

    static ArrayList<Integer> sig = new ArrayList<>();
    static ArrayList<String> res = new ArrayList<>();
    public static void main(String[] args) {
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
        //创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Collections.singleton("random"));//订阅


        int index = 0;

        while (true) {
            ConsumerRecords<String, String> poll = consumer.poll(100);
            for (ConsumerRecord<String, String> record : poll) {
//                System.out.println(record.value());
//                System.out.println(record.value().substring(7,record.value().length()));
                sig.add(Integer.valueOf(record.value().substring(7, record.value().length())));
                index++;
                if (index >= 5) {
                    System.out.println("avg：" + avg(5));
                    System.out.println("方差:" + fangcha(5));
                    System.out.println("最大值：" + Collections.max(sig));
                    System.out.println("最小值：" + Collections.min(sig));
                    res.add("avg：" + avg(5));
                    res.add("方差:" + fangcha(5));
                    res.add("最大值：" + Collections.max(sig));
                    res.add("最小值：" + Collections.min(sig));
                    if(index > 5) {
                        res.set(0,"avg：" + avg(5));
                        res.set(1,"方差:" + fangcha(5));
                        res.set(2,"最大值：" + Collections.max(sig));
                        res.set(3,"最小值：" + Collections.min(sig));
                    }
                    Timer timer = new Timer();
//                //5s后每5s执行一次
                    timer.schedule(new MyTimerTask("PeriodDemo"),5000L,5000L);
                }


            }
        }

    }

    public static double avg(int n) {
        double sum = 0;
        for (int i = sig.size() - n; i < sig.size(); i++) {
            sum = sum + sig.get(i);
        }
        return sum / n;
    }

    public static double fangcha(int n) {
        double avg = avg(n);
        double sum = 0;
        for (int i = sig.size() - n; i < sig.size(); i++) {
            sum = sum + (sig.get(i) - avg) * (sig.get(i) - avg);
        }
        return sum / n;
    }

}
