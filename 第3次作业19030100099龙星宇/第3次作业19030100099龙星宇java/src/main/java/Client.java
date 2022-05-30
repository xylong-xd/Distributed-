import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * @author xylong
 * @date 2022/4/2 14:19
 */
public class Client {

    public static void main(String[] args) {
        // 连接redis
        Jedis je = new Jedis("hadoop102", 6379);
        BookServiceImpl service = new BookServiceImpl();

        Map<String, String> result = je.hgetAll("book");

        System.out.println(result);

        if(service.delete(1)){
            System.out.println("删除成功");
        }
        Map<String, String> result1 = je.hgetAll("book");
        System.out.println("=>"+result1);

    }
}
