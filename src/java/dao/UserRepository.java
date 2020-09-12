package dao;

import bean.User;
import database.DatabaseContract;
import database.DatabaseUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class UserRepository extends AbstractRepository<User, String> {

    /**
     * Finds all the elements of type User in the database.
     *
     * @return list of instances of User.
     */
    @Override
    public List<User> findAll() {
        ArrayList<User> users = new ArrayList<>();
        try {
            String table = DatabaseContract.UserTable.TABLE_NAME;
            connect(DatabaseUtils.selectQuery(table));
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                User user = new User();
                user.setEmail(this.resultSet.getString(DatabaseContract.UserTable.COL_EMAIL));
                user.setToken(this.resultSet.getString(DatabaseContract.UserTable.COL_TOKEN));
                user.setName(this.resultSet.getString(DatabaseContract.UserTable.COL_NAME));
                user.setSurname(this.resultSet.getString(DatabaseContract.UserTable.COL_SURNAME));
                user.setLevel(this.resultSet.getInt(DatabaseContract.UserTable.COL_LEVEL));
                user.setProfileImage(this.resultSet.getString(DatabaseContract.UserTable.COL_PROFILE_IMG));
                user.setAddress(this.resultSet.getString(DatabaseContract.UserTable.COL_ADDRESS));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return users;
    }

    /**
     * Find the User elem with id passed.
     *
     * @param id: id of element to search.
     * @return if exists, User elem with id past.
     */
    @Override
    public User find(String id) {
        User user = new User();
        try {
            String table = DatabaseContract.UserTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE EMAIL='" + id + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                user.setEmail(this.resultSet.getString(DatabaseContract.UserTable.COL_EMAIL));
                user.setToken(this.resultSet.getString(DatabaseContract.UserTable.COL_TOKEN));
                user.setName(this.resultSet.getString(DatabaseContract.UserTable.COL_NAME));
                user.setSurname(this.resultSet.getString(DatabaseContract.UserTable.COL_SURNAME));
                user.setLevel(this.resultSet.getInt(DatabaseContract.UserTable.COL_LEVEL));
                user.setProfileImage(this.resultSet.getString(DatabaseContract.UserTable.COL_PROFILE_IMG));
                user.setAddress(this.resultSet.getString(DatabaseContract.UserTable.COL_ADDRESS));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return user;
    }

    /**
     * Insert or update User elem.
     *
     * @param user: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean save(User user) {
        try {
            String table = DatabaseContract.UserTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.UserTable.COL_EMAIL,
                DatabaseContract.UserTable.COL_TOKEN,
                DatabaseContract.UserTable.COL_NAME,
                DatabaseContract.UserTable.COL_SURNAME,
                DatabaseContract.UserTable.COL_LEVEL,
                DatabaseContract.UserTable.COL_PROFILE_IMG,
                DatabaseContract.UserTable.COL_ADDRESS,};
            String[] values = {
                user.getEmail(), user.getToken(),
                user.getName(), user.getSurname(),
                String.valueOf(user.getLevel()), user.getProfileImage(),
                user.getAddress()
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
     * Delete User element with id passed.
     *
     * @param id: id of the elem to delete.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean delete(String id) {
        try {
            String table = DatabaseContract.UserTable.TABLE_NAME;
            String field = "EMAIL";
            connect(DatabaseUtils.deleteQuery(table, field, id));
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
     * Count all the user in the database.
     * 
     * @return an int that rapresent all the users.
     */
    public int countAll() {
        int count = 0;
        try {
            String table = DatabaseContract.UserTable.TABLE_NAME;
            connect(DatabaseUtils.selectQuery(table));
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                count++;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return count;
        } finally {
            disconnect();
        }
        return count;
    }

    /**
     * Count all active user in the database. Active user are users 
     * with almost one borrowing
     * 
     * @return an integer rapresent all the active user.
     */
    public int countAllActive() {
        int count = 0;
        try {
            String table = DatabaseContract.UserTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table)
                    + " JOIN " + DatabaseContract.BorrowingTable.TABLE_NAME
                    + " ON " + table + "." + DatabaseContract.UserTable.COL_EMAIL
                    + " = " + DatabaseContract.BorrowingTable.TABLE_NAME
                    + "." + DatabaseContract.BorrowingTable.COL_USER
                    + " AND " + DatabaseContract.BorrowingTable.COL_IS_DELIVER
                    + "=0 GROUP BY " + DatabaseContract.BorrowingTable.COL_USER;
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                count++;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return count;
        } finally {
            disconnect();
        }
        return count;
    }
    
    /**
     * Get all active user in the database. Active user are users 
     * with almost one borrowing
     * 
     * @return a list of instance of active user.
     */
    public List<User> getAllActive() {
        ArrayList<User> a = new ArrayList<>();
        try {
            String table = DatabaseContract.UserTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table)
                    + " JOIN " + DatabaseContract.BorrowingTable.TABLE_NAME
                    + " ON " + table + "." + DatabaseContract.UserTable.COL_EMAIL
                    + " = " + DatabaseContract.BorrowingTable.TABLE_NAME
                    + "." + DatabaseContract.BorrowingTable.COL_USER;
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                User user = new User();
                user.setEmail(this.resultSet.getString(DatabaseContract.UserTable.COL_EMAIL));
                user.setToken(this.resultSet.getString(DatabaseContract.UserTable.COL_TOKEN));
                user.setName(this.resultSet.getString(DatabaseContract.UserTable.COL_NAME));
                user.setSurname(this.resultSet.getString(DatabaseContract.UserTable.COL_SURNAME));
                user.setLevel(this.resultSet.getInt(DatabaseContract.UserTable.COL_LEVEL));
                user.setProfileImage(this.resultSet.getString(DatabaseContract.UserTable.COL_PROFILE_IMG));
                user.setAddress(this.resultSet.getString(DatabaseContract.UserTable.COL_ADDRESS));
                a.add(user);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return a;
    }

    /**
     * Check if user have or not the address in the database.
     * 
     * @param email of the user.
     * @return true if user have adress, false otherwise.
     */
    public boolean checkUserStreet(String email) {
        boolean streetOk = false;
        try {
            String table = DatabaseContract.UserTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE EMAIL='" 
                    + email + "' AND " + DatabaseContract.UserTable.COL_ADDRESS
                    + " IS NOT NULL";
            connect(query);
            System.out.println(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                streetOk = true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return streetOk;
    }
    
    /**
     * Chage a role of a user. If user is an admin he becames
     * a normal user, otherwise a normal user becames an admin.
     * 
     * @param email of the user to primote
     * @param newrole of the user selected
     * @return true if promoting was successful, false otherwise.
     */
    public boolean changeRole(String email, int newrole){
        try{
            String table = DatabaseContract.UserTable.TABLE_NAME;
            String[] fields = {DatabaseContract.UserTable.COL_EMAIL, DatabaseContract.UserTable.COL_LEVEL};
            String[] values = {email, String.valueOf(newrole)};
            connect(DatabaseUtils.insertUpdateQuery(table, fields, values));
            this.statement.executeUpdate();
            return true;
        }catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }
    
    /**
     * Count the archived borrowing of the user selected.
     * 
     * @param email of the user.
     * @return an int that rapresent the archived borrowing.
     */
    public int countArchivedBorrowingByUser(String email){
        int count = 0;
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = "SELECT COUNT("+DatabaseContract.BorrowingTable.COL_USER+") FROM " + table + " WHERE " + DatabaseContract.BorrowingTable.COL_USER + "='" +
                    email + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                count = this.resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return count;
        } finally {
            disconnect();
        }
        return count;
    }

}
