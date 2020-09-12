package bean;

import java.time.Year;

/**
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia.
 */
public class Book {

    private String isbn;
    private Publisher publisher;
    private String language;
    private String title;
    private String plot;
    private String cover;
    private String coverHD;
    private int pages;
    private Year yearsOfPublication;
    private boolean unavailable;

    public Book() {
    }

    public boolean getUnavailable() {
        return unavailable;
    }

    public void setUnavailable(boolean unavailable) {
        this.unavailable = unavailable;
    }

    public String getIsbn() {
        return isbn;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public String getLanguage() {
        return language;
    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public String getCover() {
        return cover;
    }

    public String getCoverHD() {
        return coverHD;
    }

    public int getPages() {
        return pages;
    }

    public Year getYearsOfPublication() {
        return yearsOfPublication;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCoverHD(String coverHD) {
        this.coverHD = coverHD;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public void setYearsOfPublication(Year yearsOfPublication) {
        this.yearsOfPublication = yearsOfPublication;
    }

}
