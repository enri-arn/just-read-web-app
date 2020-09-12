package bean;

/**
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia.
 */
public class Rating {
    
    private int id;
    private Book book;
    private User user;
    private boolean littleHeart;
    private float rate;
    private String comment;

    public Rating() {
    }

    public int getId() {
        return id;
    }
    
    public Book getBook() {
        return book;
    }

    public User getUser() {
        return user;
    }

    public boolean isLittleHeart() {
        return littleHeart;
    }

    public float getRate() {
        return rate;
    }

    public String getComment() {
        return comment;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLittleHeart(boolean littleHeart) {
        this.littleHeart = littleHeart;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
