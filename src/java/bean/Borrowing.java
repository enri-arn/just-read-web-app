package bean;

import java.sql.Date;

/**
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia.
 */
public class Borrowing {

    private int id;
    private Book book;
    private User user;
    private Date startDate;
    private Date endDate;
    private int renewal;
    private boolean isDeliver;

    public Borrowing() {
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getRenewal() {
        return renewal;
    }

    public boolean isDeliver() {
        return isDeliver;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsDeliver(boolean isDeliver) {
        this.isDeliver = isDeliver;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setRenewal(int renewal) {
        this.renewal = renewal;
    }

    @Override
    public String toString() {
        return "Borrowing{" + "id=" + id + ", book=" + book.getIsbn() + " title " + book.getTitle() + ", user=" + user + ", startDate=" + startDate + ", endDate=" + endDate + ", renewal=" + renewal + '}';
    }

}
