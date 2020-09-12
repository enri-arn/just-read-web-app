package dao;

import bean.Author;
import database.DatabaseContract;
import database.DatabaseUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class AuthorRepository extends AbstractRepository<Author, Integer> {

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
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean save(Author author) {
        try {
            String table = DatabaseContract.AuthorTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.AuthorTable.COL_NAME,
                DatabaseContract.AuthorTable.COL_SURNAME
            };
            String[] values = {
                author.getName(),
                author.getSurname()
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
     * Get all the author of a book and save it in a String
     * using the pattern: author, author, ...
     * 
     * @param isbn: isbn of the book.
     * @return a string that contain all the authors of a book.
     */
    public String getAuthorOfBook(String isbn){
        String authors = "";
        try {
            String table = DatabaseContract.AuthorTable.TABLE_NAME;
            String[] fields = {
                "CONCAT(" + DatabaseContract.AuthorTable.COL_NAME + ",' '," 
                + DatabaseContract.AuthorTable.COL_SURNAME + ") AS " 
                + DatabaseContract.AuthorTable.TABLE_NAME 
            };
            String query = DatabaseUtils.selectQuery(table, fields) + " JOIN " 
                    + DatabaseContract.BookAuthorTable.TABLE_NAME + " ON "
                    + table + "." + DatabaseContract.AuthorTable.COL_ID_AUTHOR 
                    + "=" + DatabaseContract.BookAuthorTable.TABLE_NAME + "."
                    + DatabaseContract.BookAuthorTable.COL_ID_AUTHOR + " JOIN "
                    + DatabaseContract.BookTable.TABLE_NAME + " ON "
                    + DatabaseContract.BookTable.TABLE_NAME + "." 
                    + DatabaseContract.BookTable.COL_ISBN + "="  
                    + DatabaseContract.BookAuthorTable.TABLE_NAME + "."
                    + DatabaseContract.BookAuthorTable.COL_ID_BOOK + " WHERE "
                    + DatabaseContract.BookTable.COL_ISBN + "='" + isbn + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                String nameSurname = this.resultSet.getString(DatabaseContract.AuthorTable.TABLE_NAME);
                authors += nameSurname + " ";
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
     * Return all the authors of a given book via isbn.
     * 
     * @param isbn: isbn of the book
     * @return a List of instance of Author.
     */
    public List<Author> getAuthorsOfBook(String isbn){
        ArrayList<Author> authors = new ArrayList<>();
        try {
            String table = DatabaseContract.AuthorTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " JOIN " 
                    + DatabaseContract.BookAuthorTable.TABLE_NAME + " ON "
                    + table + "." + DatabaseContract.AuthorTable.COL_ID_AUTHOR 
                    + "=" + DatabaseContract.BookAuthorTable.TABLE_NAME + "."
                    + DatabaseContract.BookAuthorTable.COL_ID_AUTHOR + " JOIN "
                    + DatabaseContract.BookTable.TABLE_NAME + " ON "
                    + DatabaseContract.BookTable.TABLE_NAME + "." 
                    + DatabaseContract.BookTable.COL_ISBN + "="  
                    + DatabaseContract.BookAuthorTable.TABLE_NAME + "."
                    + DatabaseContract.BookAuthorTable.COL_ID_BOOK + " WHERE "
                    + DatabaseContract.BookTable.COL_ISBN + "='" + isbn + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Author a = new Author();
                a.setId(this.resultSet.getInt(DatabaseContract.AuthorTable.COL_ID_AUTHOR));
                a.setName(this.resultSet.getString(DatabaseContract.AuthorTable.COL_NAME));
                a.setSurname(this.resultSet.getString(DatabaseContract.AuthorTable.COL_SURNAME));
                authors.add(a);
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
     * Find author by name and surname.
     * 
     * @param name of the author.
     * @param surname of the author.
     * @return the author find or null otherwise.
     */
    public Author find(String name, String surname) {
        Author a = new Author();
        try {
            String table = DatabaseContract.AuthorTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE " 
                    + DatabaseContract.AuthorTable.COL_NAME + "='" + name
                    + "' AND " + DatabaseContract.AuthorTable.COL_SURNAME + "='" + surname + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                a.setId(this.resultSet.getInt(DatabaseContract.AuthorTable.COL_ID_AUTHOR));
                a.setName(this.resultSet.getString(DatabaseContract.AuthorTable.COL_NAME));
                a.setSurname(this.resultSet.getString(DatabaseContract.AuthorTable.COL_SURNAME));
                return a;
            }
            return null;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
    }
}
