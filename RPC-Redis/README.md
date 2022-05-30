## EXP3 RPC（中间件-redis）

![image-20220402144834324](img\image-20220402144834324.png)

### redis 定义

![image-20220402150147894](img\image-20220402150147894.png)

### 建立redis服务器

使用虚拟机，开启服务器

host:hadoop102     port:6379

![image-20220402150436590](img\image-20220402150436590.png)

### 编写程序代码

![image-20220402150642429](img\image-20220402150642429.png)

添加maven依赖

```xm
<dependencies>
    <!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>4.0.1</version>
    </dependency>


</dependencies>
```

Book.java

```java
import java.io.Serializable;

/**
 * @author xylong
 * @date 2022/4/2 13:23
 */
public class Book implements Serializable {

    private Integer bookID;
    private String name;

    public Book(Integer bookID, String name) {
        this.bookID = bookID;
        this.name = name;
    }

    public Book() {
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookID=" + bookID +
                ", name='" + name + '\'' +
                '}';
    }

    public String getBookID() {
        return String.valueOf(bookID);
    }

    public void setBookID(Integer bookID) {
        this.bookID = bookID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

```

BookService.java

```javav
import java.util.Map;

/**
 * @author xylong
 * @date 2022/4/2 13:27
 */
public interface BookService {
    boolean add(Book b);
    Book queryByID(int bookID);
    Map<String,String> queryByName(String name);
    boolean delete(int bookID);

}

```

BookServiceImpl.java

```java
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

```

### 客户端实现用户交互

#### add

```java
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

        //自定义五本书
        Book b = new Book(1, "shuxue");
        Book b1 = new Book(2, "yingyu");
        Book b2 = new Book(3, "yuwen");
        Book b3 = new Book(4, "tiyu");
        Book b4 = new Book(5, "shuxue");
       if (service.add(b1)){
           System.out.println("添加成功");
       };
       if (service.add(b)){
            System.out.println("添加成功");
        };
       if (service.add(b2)){
            System.out.println("添加成功");
        };
       if (service.add(b3)){
            System.out.println("添加成功");
        };
       if (service.add(b4)){
            System.out.println("添加成功");
        };
        Map<String, String> result1 = je.hgetAll("book");

        System.out.println(result1);

    }
}

```

运行结果：

![image-20220402151450871](img\image-20220402151450871.png)

#### queryByID

```java
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
      
        Book a = service.queryByID(1);
        System.out.println(a);

    }
}

```

结果：

![image-20220402151642767](img\image-20220402151642767.png)

#### queryByName

```java
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



        Map<String, String> aaa = service.queryByName("shuxue");
        System.out.println(aaa);



    }
}

```

结果：

![image-20220402151749181](img\image-20220402151749181.png)

#### delete

```java
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

```

结果：

![image-20220402151951842](img\image-20220402151951842.png)