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
public class SearchableRepository extends AbstractRepository<Book, String> {
    
    /**
     * Find a book in the database by checking is title or 
     * isbn or publisher.
     * 
     * @param search: query with title or publisher ot isbn
     * @return a list of instance of Book.
     */
    public List<Book> findBookBy(String search){
        ArrayList<Book> books = new ArrayList<>();
        try {
            String table = DatabaseContract.BookTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table);
            connect(query);
            query += " JOIN " + DatabaseContract.BookAuthorTable.TABLE_NAME +
                    " ON " + DatabaseContract.BookTable.TABLE_NAME + "."
                    + DatabaseContract.BookTable.COL_ISBN + "=" 
                    + DatabaseContract.BookAuthorTable.TABLE_NAME + "." 
                    + DatabaseContract.BookAuthorTable.COL_ID_BOOK + " JOIN "
                    + DatabaseContract.AuthorTable.TABLE_NAME + " ON "
                    + DatabaseContract.AuthorTable.TABLE_NAME + "."
                    + DatabaseContract.AuthorTable.COL_ID_AUTHOR + "=" 
                    + DatabaseContract.BookAuthorTable.TABLE_NAME + "."
                    + DatabaseContract.BookAuthorTable.COL_ID_AUTHOR + " JOIN "
                    + DatabaseContract.PublisherTable.TABLE_NAME + " ON "
                    + DatabaseContract.PublisherTable.TABLE_NAME + "." 
                    + DatabaseContract.PublisherTable.COL_ID_PUBLISHER + "="
                    + DatabaseContract.BookTable.TABLE_NAME + "."
                    + DatabaseContract.BookTable.COL_PUBLISHER + " WHERE " 
                    + "(" + DatabaseContract.BookTable.COL_TITLE + " LIKE '%" 
                    + search + "%') OR (CONCAT(" + DatabaseContract.AuthorTable.TABLE_NAME
                    + "." + DatabaseContract.AuthorTable.COL_NAME + ",' ',"
                    + DatabaseContract.AuthorTable.TABLE_NAME + "." + DatabaseContract.AuthorTable.COL_SURNAME
                    + ") LIKE '%"
                    + search + "%') OR (" + DatabaseContract.PublisherTable.TABLE_NAME
                    + "." + DatabaseContract.PublisherTable.COL_NAME + " LIKE '%"
                    + search + "%') GROUP BY " + DatabaseContract.BookTable.COL_ISBN;
            System.out.println(query);
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
     * Not implemented because not necessary
     * 
     * @return 
     */
    @Override
    public List<Book> findAll() {
        return null;
    }

    /**
     * Not implemented because not necessary
     * 
     * @param id
     * @return 
     */
    @Override
    public Book find(String id) {
        return null;
    }

    /**
     * Not implemented because not necessary
     * 
     * @param elem
     * @return 
     */
    @Override
    public boolean save(Book elem) {
        return false;
    }

    /**
     * Not implemented because not necessary
     * 
     * @param id
     * @return 
     */
    @Override
    public boolean delete(String id) {
        return false;
    }
    
}
