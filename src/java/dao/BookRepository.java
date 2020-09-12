package dao;

import bean.Book;
import bean.Publisher;
import database.DatabaseContract;
import database.DatabaseUtils;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class BookRepository extends AbstractRepository<Book, String> {

    /**
     * Finds all the elements of type Book in the database.
     *
     * @return list of instances of Book.
     */
    @Override
    public List<Book> findAll() {
        ArrayList<Book> books = new ArrayList<>();
        try {
            String table = DatabaseContract.BookTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table);
            query += " INNER JOIN " + DatabaseContract.PublisherTable.TABLE_NAME
                    + " ON " + DatabaseContract.PublisherTable.TABLE_NAME + "."
                    + DatabaseContract.PublisherTable.COL_ID_PUBLISHER
                    + "=" + DatabaseContract.BookTable.TABLE_NAME + "."
                    + DatabaseContract.BookTable.COL_PUBLISHER + " AND "
                    + DatabaseContract.BookTable.COL_UNAVAILABLE + " = 0";
            connect(query);
            this.resultSet = this.statement.executeQuery(query);
            while (this.resultSet.next()) {
                Book book = new Book();
                book.setIsbn(this.resultSet.getString(DatabaseContract.BookTable.COL_ISBN));
                Publisher publisher = new Publisher();
                publisher.setId(this.resultSet.getInt(DatabaseContract.PublisherTable.COL_ID_PUBLISHER));
                publisher.setName(this.resultSet.getString(DatabaseContract.PublisherTable.COL_NAME));
                book.setPublisher(publisher);
                book.setLanguage(this.resultSet.getString(DatabaseContract.BookTable.COL_LANGUAGE));
                book.setTitle(this.resultSet.getString(DatabaseContract.BookTable.COL_TITLE));
                book.setPlot(this.resultSet.getString(DatabaseContract.BookTable.COL_PLOT));
                book.setCover(this.resultSet.getString(DatabaseContract.BookTable.COL_COVER_LD));
                book.setCoverHD(this.resultSet.getString(DatabaseContract.BookTable.COL_COVER_HD));
                book.setPages(this.resultSet.getInt(DatabaseContract.BookTable.COL_PAGES));
                SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
                java.sql.Date date = this.resultSet.getDate(DatabaseContract.BookTable.COL_PUBLICATION);
                String year = formatYear.format(date);
                book.setYearsOfPublication(Year.parse(year));
                book.setUnavailable(this.resultSet.getBoolean(DatabaseContract.BookTable.COL_UNAVAILABLE));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return books;
    }

    /**
     * Find the Book elem with id passed.
     *
     * @param id: id of element to search.
     * @return if exists, Book elem with id past.
     */
    @Override
    public Book find(String id) {
        Book book = new Book();
        try {
            String table = DatabaseContract.BookTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table);
            query += " INNER JOIN " + DatabaseContract.PublisherTable.TABLE_NAME
                    + " ON " + DatabaseContract.PublisherTable.TABLE_NAME + "."
                    + DatabaseContract.PublisherTable.COL_ID_PUBLISHER
                    + "=" + DatabaseContract.BookTable.TABLE_NAME + "."
                    + DatabaseContract.BookTable.COL_PUBLISHER + " WHERE "
                    + DatabaseContract.BookTable.TABLE_NAME + "."
                    + DatabaseContract.BookTable.COL_ISBN + "='" + id + "' AND "
                    + DatabaseContract.BookTable.COL_UNAVAILABLE + " = 0";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                book.setIsbn(this.resultSet.getString(DatabaseContract.BookTable.COL_ISBN));
                Publisher publisher = new Publisher();
                publisher.setId(this.resultSet.getInt(DatabaseContract.PublisherTable.COL_ID_PUBLISHER));
                publisher.setName(this.resultSet.getString(DatabaseContract.PublisherTable.COL_NAME));
                book.setPublisher(publisher);
                book.setLanguage(this.resultSet.getString(DatabaseContract.BookTable.COL_LANGUAGE));
                book.setTitle(this.resultSet.getString(DatabaseContract.BookTable.COL_TITLE));
                book.setPlot(this.resultSet.getString(DatabaseContract.BookTable.COL_PLOT));
                book.setCover(this.resultSet.getString(DatabaseContract.BookTable.COL_COVER_LD));
                book.setCoverHD(this.resultSet.getString(DatabaseContract.BookTable.COL_COVER_HD));
                book.setPages(this.resultSet.getInt(DatabaseContract.BookTable.COL_PAGES));
                SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
                java.sql.Date date = this.resultSet.getDate(DatabaseContract.BookTable.COL_PUBLICATION);
                String year = formatYear.format(date);
                book.setYearsOfPublication(Year.parse(year));
                boolean unavailable = this.resultSet.getBoolean(DatabaseContract.BookTable.COL_UNAVAILABLE);
                book.setUnavailable(unavailable);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return book;
    }

    /**
     * Insert or update Book elem.
     *
     * @param book: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean save(Book book) {
        try {
            String table = DatabaseContract.BookTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.BookTable.COL_ISBN,
                DatabaseContract.BookTable.COL_PUBLISHER,
                DatabaseContract.BookTable.COL_LANGUAGE,
                DatabaseContract.BookTable.COL_TITLE,
                DatabaseContract.BookTable.COL_PLOT,
                DatabaseContract.BookTable.COL_COVER_LD,
                DatabaseContract.BookTable.COL_COVER_HD,
                DatabaseContract.BookTable.COL_PAGES,
                DatabaseContract.BookTable.COL_PUBLICATION,
                DatabaseContract.BookTable.COL_UNAVAILABLE
            };
            String[] values = {
                book.getIsbn(), String.valueOf(book.getPublisher().getId()),
                book.getLanguage(), book.getTitle(),
                book.getPlot(), book.getCover(), book.getCoverHD(),
                String.valueOf(book.getPages()),
                String.valueOf(book.getYearsOfPublication()),
                String.valueOf(book.getUnavailable() == true ? 1 : 0)
            };
            connect(DatabaseUtils.insertUpdateQuery(table, fields, values));
            this.statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return true;
    }

    /**
     * Delete Book element with id passed.
     *
     * @param id: id of the elem to delete.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean delete(String id) {
        try {
            if(!isAvaiable(id)){//il libro è fuori dalla biblioteca
                String query = "UPDATE " + DatabaseContract.BookTable.TABLE_NAME 
                        + " SET " + DatabaseContract.BookTable.COL_UNAVAILABLE 
                        + " = '1' WHERE " + DatabaseContract.BookTable.TABLE_NAME 
                        + "." + DatabaseContract.BookTable.COL_ISBN + "='" + id + "'";
                connect(query);
                this.statement.executeUpdate();
            }else return false;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return true;
    }

    /**
     * Find all the books in the chosen category.
     *
     * @param id: category chosen for research.
     * @return list of instances of Book.
     */
    public List<Book> findByCategory(String id) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            String table = DatabaseContract.BookTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.BookTable.COL_ISBN,
                DatabaseContract.BookTable.COL_PUBLISHER,
                DatabaseContract.BookTable.COL_LANGUAGE,
                DatabaseContract.BookTable.COL_TITLE,
                DatabaseContract.BookTable.COL_PLOT,
                DatabaseContract.BookTable.COL_COVER_LD,
                DatabaseContract.BookTable.COL_COVER_HD,
                DatabaseContract.BookTable.COL_PAGES,
                DatabaseContract.BookTable.COL_PUBLICATION,
                DatabaseContract.BookTable.COL_UNAVAILABLE
            };
            String query = DatabaseUtils.selectQuery(table, fields);
            query += " JOIN " + DatabaseContract.BookCategoryTable.TABLE_NAME
                    + " ON " + DatabaseContract.BookCategoryTable.COL_ID_BOOK
                    + "=" + DatabaseContract.BookTable.COL_ISBN + " JOIN "
                    + DatabaseContract.CategoryTable.TABLE_NAME + " ON "
                    + DatabaseContract.CategoryTable.TABLE_NAME + "."
                    + DatabaseContract.CategoryTable.COL_ID_CATEGORY + "="
                    + DatabaseContract.BookCategoryTable.TABLE_NAME + "."
                    + DatabaseContract.BookCategoryTable.COL_ID_CATEGORY
                    + " WHERE " + DatabaseContract.CategoryTable.COL_NAME + "="
                    + "'" + id + "' AND " + DatabaseContract.BookTable.COL_UNAVAILABLE
                    + " = 0";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Book book = new Book();
                PublisherRepository pub = new PublisherRepository();
                book.setIsbn(this.resultSet.getString(DatabaseContract.BookTable.COL_ISBN));
                book.setPublisher(pub.find(this.resultSet.getInt(DatabaseContract.BookTable.COL_PUBLISHER)));
                book.setLanguage(this.resultSet.getString(DatabaseContract.BookTable.COL_LANGUAGE));
                book.setTitle(this.resultSet.getString(DatabaseContract.BookTable.COL_TITLE));
                book.setPlot(this.resultSet.getString(DatabaseContract.BookTable.COL_PLOT));
                book.setCover(this.resultSet.getString(DatabaseContract.BookTable.COL_COVER_LD));
                book.setPages(this.resultSet.getInt(DatabaseContract.BookTable.COL_PAGES));
                SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
                java.sql.Date date = this.resultSet.getDate(DatabaseContract.BookTable.COL_PUBLICATION);
                String year = formatYear.format(date);
                book.setYearsOfPublication(Year.parse(year));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return books;
    }

    /**
     * Find all the favourite book of the user selected.
     *
     * @param email: email of the user.
     * @return list of instances of Book.
     */
    public List<Book> findFavouriteByUser(String email) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            String table = DatabaseContract.BookTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.BookTable.COL_ISBN,
                DatabaseContract.BookTable.COL_PUBLISHER,
                DatabaseContract.BookTable.COL_LANGUAGE,
                DatabaseContract.BookTable.COL_TITLE,
                DatabaseContract.BookTable.COL_PLOT,
                DatabaseContract.BookTable.COL_COVER_LD,
                DatabaseContract.BookTable.COL_PAGES,
                DatabaseContract.BookTable.COL_PUBLICATION
            };
            String query = DatabaseUtils.selectQuery(table, fields);
            query += " JOIN " + DatabaseContract.RatingTable.TABLE_NAME + " ON "
                    + DatabaseContract.BookTable.TABLE_NAME + "."
                    + DatabaseContract.BookTable.COL_ISBN + "="
                    + DatabaseContract.RatingTable.TABLE_NAME + "."
                    + DatabaseContract.RatingTable.COL_BOOK + " WHERE "
                    + DatabaseContract.RatingTable.COL_LITTLE_HEART + "=true AND "
                    + DatabaseContract.RatingTable.TABLE_NAME + "."
                    + DatabaseContract.RatingTable.COL_USER + "='" + email + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Book book = new Book();
                PublisherRepository pub = new PublisherRepository();
                book.setIsbn(this.resultSet.getString(DatabaseContract.BookTable.COL_ISBN));
                book.setPublisher(pub.find(this.resultSet.getInt(DatabaseContract.BookTable.COL_PUBLISHER)));
                book.setLanguage(this.resultSet.getString(DatabaseContract.BookTable.COL_LANGUAGE));
                book.setTitle(this.resultSet.getString(DatabaseContract.BookTable.COL_TITLE));
                book.setPlot(this.resultSet.getString(DatabaseContract.BookTable.COL_PLOT));
                book.setCover(this.resultSet.getString(DatabaseContract.BookTable.COL_COVER_LD));
                book.setPages(this.resultSet.getInt(DatabaseContract.BookTable.COL_PAGES));
                SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
                java.sql.Date date = this.resultSet.getDate(DatabaseContract.BookTable.COL_PUBLICATION);
                String year = formatYear.format(date);
                book.setYearsOfPublication(Year.parse(year));
                books.add(book);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return books;
    }
    
    /**
     * Check if book selected is avaiable.
     * 
     * @param isbn: isbn of the book.
     * @return true if book is avaiable, false otherwise.
     */
    public boolean isAvaiable(String isbn){
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.BorrowingTable.COL_BOOK
            };
            String query = DatabaseUtils.selectQuery(table, fields);
            connect(query);
            this.resultSet = this.statement.executeQuery();
            if(this.resultSet.next()){
                return false;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return true;
    }

}
