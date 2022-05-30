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
