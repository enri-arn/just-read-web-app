package dao;

import bean.Author;
import bean.Book;
import database.DatabaseContract;
import database.DatabaseUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class BookAuthorRepository extends AbstractRepository<Author, Integer> {

    /**
     * Finds all the elements of type Author in the database.
     *
     * @return list of instances of Author.
     */
    @Override
    public List<Author> findAll() {
        ArrayList<Author> authors = new ArrayList<>();
        try {
            String table = DatabaseContract.AuthorTable.TABLE_NAME;
            connect(DatabaseUtils.selectQuery(table));
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Author author = new Author();
                author.setId(this.resultSet.getInt(DatabaseContract.AuthorTable.COL_ID_AUTHOR));
                author.setName(this.resultSet.getString(DatabaseContract.AuthorTable.COL_NAME));
                author.setSurname(this.resultSet.getString(DatabaseContract.AuthorTable.COL_SURNAME));
                authors.add(author);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return authors;
    }

    /**
     * Find the Author elem with id passed.
     *
     * @param id: id of element to search.
     * @return if exists, Author elem with id past.
     */
    @Override
    public Author find(Integer id) {
        Author author = new Author();
        try {
            String table = DatabaseContract.AuthorTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.AuthorTable.COL_ID_AUTHOR + "='" + id + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                author.setId(this.resultSet.getInt(DatabaseContract.AuthorTable.COL_ID_AUTHOR));
                author.setName(this.resultSet.getString(DatabaseContract.AuthorTable.COL_NAME));
                author.setSurname(this.resultSet.getString(DatabaseContract.AuthorTable.COL_SURNAME));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return author;
    }

    /**
     * Insert or update Author elem.
     *
     * @param author: element to insert or update.
     * @param book
     * @return true, if operation was successful, false otherwise.
     */
    public boolean save(Author author, Book book) {
        try {
            String table = DatabaseContract.BookAuthorTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.BookAuthorTable.COL_ID_AUTHOR,
                DatabaseContract.BookAuthorTable.COL_ID_BOOK
            };
            String[] values = {
                String.valueOf(author.getId()),
                book.getIsbn()
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
     * Insert or update Author elem.
     *
     * @param author: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean save(Author author) {
        try {
            String table = DatabaseContract.AuthorTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.AuthorTable.COL_ID_AUTHOR,
                DatabaseContract.AuthorTable.COL_NAME,
                DatabaseContract.AuthorTable.COL_SURNAME
            };
            String[] values = {
                String.valueOf(author.getId()),
                author.getName(), author.getSurname()
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
     * Delete Author element with id passed.
     *
     * @param id: id of the elem to delete.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean delete(Integer id) {
        try {
            String table = DatabaseContract.AuthorTable.TABLE_NAME;
            String field = DatabaseContract.AuthorTable.COL_ID_AUTHOR;
            connect(DatabaseUtils.deleteQuery(table, field, String.valueOf(id)));
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
     * Delete an association from book to author.
     * 
     * @param id isbn of the book.
     * @return true if delete is successful or false otherwise.
     */
    public boolean delete(String id) {
        BookRepository bookRepository = new BookRepository();
        Book book = bookRepository.find(id);
        try {
            String table = DatabaseContract.BookAuthorTable.TABLE_NAME;
            String fields = DatabaseContract.BookAuthorTable.COL_ID_BOOK;
            String values = book.getIsbn();
            connect(DatabaseUtils.deleteQuery(table, fields, values));
            this.statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return true;
    }

}
