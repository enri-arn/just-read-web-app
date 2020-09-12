package dao;

import bean.Publisher;
import database.DatabaseContract;
import database.DatabaseUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class PublisherRepository extends AbstractRepository<Publisher, Integer> {

    /**
     * Finds all the elements of type Publisher in the database.
     *
     * @return list of instances of Publisher.
     */
    @Override
    public List<Publisher> findAll() {
        ArrayList<Publisher> publishers = new ArrayList<>();
        try {
            String table = DatabaseContract.PublisherTable.TABLE_NAME;
            connect(DatabaseUtils.selectQuery(table));
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Publisher publisher = new Publisher();
                publisher.setId(this.resultSet.getInt(DatabaseContract.PublisherTable.COL_ID_PUBLISHER));
                publisher.setName(this.resultSet.getString(DatabaseContract.PublisherTable.COL_NAME));
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return publishers;
    }

    /**
     * Find the Publisher elem with id passed.
     *
     * @param id: id of element to search.
     * @return if exists, Publisher elem with id past.
     */
    @Override
    public Publisher find(Integer id) {
        Publisher publisher = new Publisher();
        try {
            String table = DatabaseContract.PublisherTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.PublisherTable.COL_ID_PUBLISHER + "='" + id + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                publisher.setId(this.resultSet.getInt(DatabaseContract.PublisherTable.COL_ID_PUBLISHER));
                publisher.setName(this.resultSet.getString(DatabaseContract.PublisherTable.COL_NAME));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return publisher;
    }
    
    /**
     * Find the Publisher elem with id passed.
     *
     * @param name
     * @return if exists, Publisher elem with id past.
     */
    public Publisher findByName(String name) {
        Publisher publisher = new Publisher();
        try {
            String table = DatabaseContract.PublisherTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.PublisherTable.COL_NAME + "='" + name + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                publisher.setId(this.resultSet.getInt(DatabaseContract.PublisherTable.COL_ID_PUBLISHER));
                publisher.setName(this.resultSet.getString(DatabaseContract.PublisherTable.COL_NAME));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return publisher;
    }

    /**
     * Insert or update Publisher elem.
     *
     * @param publisher: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean save(Publisher publisher) {
        try {
            String table = DatabaseContract.PublisherTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.PublisherTable.COL_NAME
            };
            String[] values = {
                publisher.getName()
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
     * Delete Publisher element with id passed.
     *
     * @param id: id of the elem to delete.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean delete(Integer id) {
        try {
            String table = DatabaseContract.PublisherTable.TABLE_NAME;
            String field = DatabaseContract.PublisherTable.COL_ID_PUBLISHER;
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

}
