package dao;

import bean.Book;
import bean.Category;
import database.DatabaseContract;
import database.DatabaseUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class BookCategoryRepository extends AbstractRepository<Category, String> {
    
    /**
     * Find all the categories of a given book.
     * 
     * @param book
     * @return 
     */
    public List<Category> findCategories(Book book){
        ArrayList<Category> categories = new ArrayList<>();
        try {
            String table = DatabaseContract.BookCategoryTable.TABLE_NAME;
            connect(DatabaseUtils.selectQuery(table) + " WHERE " + DatabaseContract.BookCategoryTable.COL_ID_BOOK + " = " + book.getIsbn());
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Category category = new Category();
                category.setId(this.resultSet.getInt(DatabaseContract.CategoryTable.COL_ID_CATEGORY));
                category.setName(this.resultSet.getString(DatabaseContract.CategoryTable.COL_NAME));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return categories;
    }
    
    /**
     * Bind a book with a genre given.
     * 
     * @param category
     * @param book
     * @return 
     */
    public boolean setCategory(Category category, Book book){
        try {
            String table = DatabaseContract.BookCategoryTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.BookCategoryTable.COL_ID_BOOK,
                DatabaseContract.BookCategoryTable.COL_ID_CATEGORY
            };
            String[] values = {
                book.getIsbn(),
                String.valueOf(category.getId())
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
     * Not implemented because not necessary.
     * @return UnsupportedOperationException.
     */
    @Override
    public List<Category> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Not implemented because not necessary.
     * @return UnsupportedOperationException.
     */
    @Override
    public Category find(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Not implemented because not necessary.
     * @param elem
     * @return UnsupportedOperationException.
     */
    @Override
    public boolean save(Category elem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Delete asscoiation from book to category.
     * 
     * @param id isbn of the book.
     * @return true if delete was successful, false otherwise.
     */
    @Override
    public boolean delete(String id) {
        BookRepository bookRepository = new BookRepository();
        Book book = bookRepository.find(id);
        try {
            String table = DatabaseContract.BookCategoryTable.TABLE_NAME;
            String fields = DatabaseContract.BookCategoryTable.COL_ID_BOOK;
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
