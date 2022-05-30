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
