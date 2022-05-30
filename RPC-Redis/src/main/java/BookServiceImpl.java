import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author xylong
 * @date 2022/4/2 12:44
 */
public class BookServiceImpl implements BookService {

    Jedis jedis = new Jedis("hadoop102", 6379);

    @Override
    public boolean add(Book b) {
        jedis.hset("book",b.getBookID(), b.getName());
        System.out.println("添加成功");
        return true;
    }

    @Override
    public Book queryByID(int bookID) {
        Map<String,String> books = jedis.hgetAll("book");
        String name = books.get(String.valueOf(bookID));
        return new Book(bookID, name);
    }

    @Override
    public Map<String, String> queryByName(String name) {
        Map<String,String> books = jedis.hgetAll("book");
        Iterator<String> it = books.keySet().iterator();

        Map<String, String > booklist = new HashMap<>();
        while (it.hasNext()){
            String key = it.next();
            String value = books.get(key);
            if(value.equals(name)){
                booklist.put(key, value);
            }
        }
        return booklist;
    }

    @Override
    public boolean delete(int bookID) {
        Map<String,String> books = jedis.hgetAll("book");
        String name = books.get(String.valueOf(bookID));
        jedis.hdel("book", String.valueOf(bookID));
        return true;
    }
}
