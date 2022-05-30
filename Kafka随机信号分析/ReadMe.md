

## 实验要求

![image-20220530145815842](https://github.com/xylong-xd/Distributed-/blob/main/Kafka%E9%9A%8F%E6%9C%BA%E4%BF%A1%E5%8F%B7%E5%88%86%E6%9E%90/img/D088A313B5F6973ADA9102412AC6F390.png)



## 实现思路

利用kafka作为消息队列中间件，idea创建生产者，消费者来创建，消费消息

## 实现过程

### 启动zookeeper

![image-20220426095459270](https://github.com/xylong-xd/Distributed-/blob/main/Kafka%E9%9A%8F%E6%9C%BA%E4%BF%A1%E5%8F%B7%E5%88%86%E6%9E%90/img/image-20220426095459270.png)

### 启动kafka

![image-20220426095533301](https://github.com/xylong-xd/Distributed-/blob/main/Kafka%E9%9A%8F%E6%9C%BA%E4%BF%A1%E5%8F%B7%E5%88%86%E6%9E%90/img/image-20220426095533301.png)

### 文件目录：

![image-20220426095956910](https://github.com/xylong-xd/Distributed-/blob/main/Kafka%E9%9A%8F%E6%9C%BA%E4%BF%A1%E5%8F%B7%E5%88%86%E6%9E%90/img/image-20220426095956910.png)

### 生产者创建（producer.java）

```java


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


```

### 统计分析微服务

#### Consumer.java

```java
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

```

#### MyTimerTask.java

```java
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

```





### 实时数据显示微服务

#### Controller.java

```java
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
 
```

#### ResultConsumer.java

```java
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

```

#### 画图工具(RealtimeChart.java)

```java

import java.util.LinkedList;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendLayout;
import org.knowm.xchart.style.Styler.LegendPosition;

import javax.swing.*;

/**
 * @author xylong
 * @date 2022/4/25 17:41
 */

public class RealtimeChart {

    private SwingWrapper<XYChart> swingWrapper;
    private XYChart chart;
    private JFrame frame;

    private String title;// 标题
    private String seriesName;// 系列，此处只有一个系列。若存在多组数据，可以设置多个系列
    private List<Double> seriesData;// 系列的数据
    private int size = 1000;// 最多显示多少数据，默认显示1000个数据

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public RealtimeChart(String title, String seriesName) {
        super();
        this.seriesName = seriesName;
        this.title = title;
    }

    public RealtimeChart(String title, String seriesName, int size) {
        super();
        this.title = title;
        this.seriesName = seriesName;
        this.size = size;
    }

    public void plot(double data) {
        if (seriesData == null) {
            seriesData = new LinkedList<>();
        }

        if (seriesData.size() == this.size) {
            seriesData.clear();
        }

        seriesData.add(data);

        if (swingWrapper == null) {

            // Create Chart
            chart = new XYChartBuilder().width(600).height(450).theme(ChartTheme.Matlab).title(title).build();
            chart.addSeries(seriesName, null, seriesData);
            chart.getStyler().setLegendPosition(LegendPosition.OutsideS);// 设置legend的位置为外底部
            chart.getStyler().setLegendLayout(LegendLayout.Horizontal);// 设置legend的排列方式为水平排列

            swingWrapper = new SwingWrapper<XYChart>(chart);
            frame = swingWrapper.displayChart();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 防止关闭窗口时退出程序
        } else {

            // Update Chart
            chart.updateXYSeries(seriesName, null, seriesData, null);
            swingWrapper.repaintChart();
        }
    }
}

```

## 实验结果

#### 启动生产者

每100ms产生一个随机数字，并作为一个消息发布(topic：random)

![image-20220426100335575](https://github.com/xylong-xd/Distributed-/blob/main/Kafka%E9%9A%8F%E6%9C%BA%E4%BF%A1%E5%8F%B7%E5%88%86%E6%9E%90/img/image-20220426100335575.png)

### 启动统计分析微服务

![image-20220426100600819](https://github.com/xylong-xd/Distributed-/blob/main/Kafka%E9%9A%8F%E6%9C%BA%E4%BF%A1%E5%8F%B7%E5%88%86%E6%9E%90/img/image-20220426100600819.png)

设置常量N为5

计算并打印过去N个数据的均值和方差及所有历史数据中的最大最小值

并定时（5s）的将数据分析结果打包发布（topic：res）出去（使用Timer()类）：

```java
//5s后每5s执行一次  
Timer timer = new Timer();
timer.schedule(new MyTimerTask("PeriodDemo"),5000L,5000L);

```

### 启动实时数据显示微服务

使用 xchart 画图

```xml
<!--https://mvnrepository.com/artifact/org.knowm.xchart/xchart -->
        <dependency>
            <groupId>org.knowm.xchart</groupId>
            <artifactId>xchart</artifactId>
            <version>3.8.0</version>
        </dependency>
```

```java
RealtimeChart chart = new RealtimeChart("Real-time Chart", "result", 500);
// 设置显示阈值为500个

```

```java
 chart.plot(data);
// 添加点
```



![image-20220426101257539](https://github.com/xylong-xd/Distributed-/blob/main/Kafka%E9%9A%8F%E6%9C%BA%E4%BF%A1%E5%8F%B7%E5%88%86%E6%9E%90/img/image-20220426101257539.png)

实时接受  topic：res  的结果并打印

![image-20220426101617769](https://github.com/xylong-xd/Distributed-/blob/main/Kafka%E9%9A%8F%E6%9C%BA%E4%BF%A1%E5%8F%B7%E5%88%86%E6%9E%90/img/D088A313B5F6973ADA9102412AC6F390.png))
