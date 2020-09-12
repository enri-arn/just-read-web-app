package dao;

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
public class CategoryRepository extends AbstractRepository<Category, Integer> {

    /**
     * Finds all the elements of type Category in the database.
     *
     * @return list of instances of Category.
     */
    @Override
    public List<Category> findAll() {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            String table = DatabaseContract.CategoryTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " ORDER BY " +
                    DatabaseContract.CategoryTable.COL_NAME + " ASC";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Category category = new Category();
                category.setId(this.resultSet.getInt(DatabaseContract.CategoryTable.COL_ID_CATEGORY));
                category.setName(this.resultSet.getString(DatabaseContract.CategoryTable.COL_NAME));
                category.setIcon(this.resultSet.getString(DatabaseContract.CategoryTable.COL_ICON));
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
     * Find the Category elem with id passed.
     *
     * @param id: id of element to search.
     * @return if exists, Category elem with id past.
     */
    @Override
    public Category find(Integer id) {
        Category category = new Category();
        try {
            String table = DatabaseContract.CategoryTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.CategoryTable.COL_ID_CATEGORY + "='" + id + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                category.setId(this.resultSet.getInt(DatabaseContract.CategoryTable.COL_ID_CATEGORY));
                category.setName(this.resultSet.getString(DatabaseContract.CategoryTable.COL_NAME));
                category.setIcon(this.resultSet.getString(DatabaseContract.CategoryTable.COL_ICON));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return category;
    }
    
    /**
     * Find the Category elem with id passed.
     *
     * @param name
     * @return if exists, Category elem with id past.
     */
    public Category findByName(String name) {
        Category category = new Category();
        try {
            String table = DatabaseContract.CategoryTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.CategoryTable.COL_NAME + "='" + name + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                category.setId(this.resultSet.getInt(DatabaseContract.CategoryTable.COL_ID_CATEGORY));
                category.setName(this.resultSet.getString(DatabaseContract.CategoryTable.COL_NAME));
                category.setIcon(this.resultSet.getString(DatabaseContract.CategoryTable.COL_ICON));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return category;
    }

    /**
     * Insert or update Category elem.
     *
     * @param elem: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean save(Category elem) {
        try {
            String table = DatabaseContract.CategoryTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.CategoryTable.COL_NAME,
                DatabaseContract.CategoryTable.COL_ICON
            };
            String[] values = {
                elem.getName(),
                elem.getIcon()
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
     * Delete Category element with id passed.
     *
     * @param id: id of the elem to delete.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean delete(Integer id) {
        try {
            String table = DatabaseContract.CategoryTable.TABLE_NAME;
            String field = DatabaseContract.CategoryTable.COL_ID_CATEGORY;
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
     * Find all the category of the selected book.
     *
     * @param isbn: isbn of the book.
     * @return a list of instance of book.
     */
    public List<Category> getBookCategory(String isbn) {
        ArrayList<Category> categories = new ArrayList<>();
        try {
            String table = DatabaseContract.CategoryTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.CategoryTable.TABLE_NAME + "." + DatabaseContract.CategoryTable.COL_ID_CATEGORY,
                DatabaseContract.CategoryTable.COL_NAME, DatabaseContract.CategoryTable.COL_ICON
            };
            String query = DatabaseUtils.selectQuery(table, fields);
            query += " JOIN " + DatabaseContract.BookCategoryTable.TABLE_NAME
                    + " ON " + DatabaseContract.CategoryTable.TABLE_NAME + "."
                    + DatabaseContract.CategoryTable.COL_ID_CATEGORY + "="
                    + DatabaseContract.BookCategoryTable.TABLE_NAME + "."
                    + DatabaseContract.BookCategoryTable.COL_ID_CATEGORY
                    + " WHERE " + DatabaseContract.BookCategoryTable.TABLE_NAME
                    + "." + DatabaseContract.BookCategoryTable.COL_ID_BOOK + "='"
                    + isbn + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Category category = new Category();
                category.setId(this.resultSet.getInt(DatabaseContract.CategoryTable.COL_ID_CATEGORY));
                category.setName(this.resultSet.getString(DatabaseContract.CategoryTable.COL_NAME));
                category.setIcon(this.resultSet.getString(DatabaseContract.CategoryTable.COL_ICON));
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

}
