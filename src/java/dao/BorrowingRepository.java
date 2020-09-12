package dao;

import bean.Borrowing;
import database.DatabaseContract;
import database.DatabaseUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class BorrowingRepository extends AbstractRepository<Borrowing, Integer> {

    /**
     * Finds all the elements of type Borrowing in the database.
     *
     * @return list of instances of Borrowing.
     */
    @Override
    public List<Borrowing> findAll() {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table);
            System.out.println(query);
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_ID_BORROW));
                BookRepository bookRepository = new BookRepository();
                borrowing.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                borrowing.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_USER)));
                borrowing.setStartDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_START_DATE));
                borrowing.setEndDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_END_DATE));
                borrowing.setRenewal(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_RENEWAL));
                borrowing.setIsDeliver(this.resultSet.getBoolean(DatabaseContract.BorrowingTable.COL_IS_DELIVER));
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return borrowings;
    }

    /**
     * Find the Borrowing elem with id passed.
     *
     * @param id: id of element to search.
     * @return if exists, Borrowing elem with id past.
     */
    @Override
    public Borrowing find(Integer id) {
        Borrowing borrow = new Borrowing();
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.BorrowingTable.COL_ID_BORROW + "='" + id + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                borrow.setId(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_ID_BORROW));
                BookRepository bookRepository = new BookRepository();
                borrow.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                borrow.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_USER)));
                borrow.setStartDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_START_DATE));
                borrow.setEndDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_END_DATE));
                borrow.setRenewal(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_RENEWAL));
                borrow.setIsDeliver(this.resultSet.getBoolean(DatabaseContract.BorrowingTable.COL_IS_DELIVER));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return borrow;
    }

    /**
     * Find the Borrowing elem of the selected user and book.
     *
     * @param user: user of the borrowing.
     * @param isbn: isbn of the book.
     * @return if exists, Borrowing elem with id past.
     */
    public Borrowing find(String user, String isbn) {
        Borrowing borrow = null;
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.BorrowingTable.COL_BOOK + "='" + isbn + "'"
                    + " AND " + DatabaseContract.BorrowingTable.COL_USER + "="
                    + "'" + user + "' AND " + DatabaseContract.BorrowingTable.COL_END_DATE
                    + " > CURRENT_DATE";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            if (this.resultSet.next()) {
                borrow = new Borrowing();
                borrow.setId(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_ID_BORROW));
                BookRepository bookRepository = new BookRepository();
                borrow.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                borrow.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_USER)));
                borrow.setStartDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_START_DATE));
                borrow.setEndDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_END_DATE));
                borrow.setRenewal(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_RENEWAL));
                borrow.setIsDeliver(this.resultSet.getBoolean(DatabaseContract.BorrowingTable.COL_IS_DELIVER));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return borrow;
    }

    /**
     * Find the Borrowing elem of the selected user and book.
     *
     * @param isbn: isbn of the book.
     * @return if exists, Borrowing elem with id past.
     */
    public boolean find(String isbn) {
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.BorrowingTable.COL_BOOK + "='" + isbn + "'"
                    + " AND " + DatabaseContract.BorrowingTable.COL_END_DATE
                    + " > CURRENT_DATE";
            System.out.println("\n" + query + "\n");
            connect(query);
            this.resultSet = this.statement.executeQuery();
            if (this.resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return false;
    }

    /**
     * Insert or update Borrowing elem.
     *
     * @param elem: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean save(Borrowing elem) {
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.BorrowingTable.COL_BOOK,
                DatabaseContract.BorrowingTable.COL_USER,
                DatabaseContract.BorrowingTable.COL_START_DATE,
                DatabaseContract.BorrowingTable.COL_END_DATE,
                DatabaseContract.BorrowingTable.COL_RENEWAL,
                DatabaseContract.BorrowingTable.COL_IS_DELIVER
            };
            String[] values = {
                elem.getBook().getIsbn(),
                elem.getUser().getEmail(), String.valueOf(elem.getStartDate()),
                String.valueOf(elem.getEndDate()), String.valueOf(elem.getRenewal()),
                String.valueOf(elem.isDeliver()? 1 : 0)
            };
            connect(DatabaseUtils.insertUpdateQuery(table, fields, values));
            System.out.println("query update = " + DatabaseUtils.insertUpdateQuery(table, fields, values));
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
     * Delete Borrowing element with id passed.
     *
     * @param id: id of the elem to delete.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean delete(Integer id) {
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String field = DatabaseContract.BorrowingTable.COL_ID_BORROW;
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
     * Find the Borrowing of selected user.
     *
     * @param email: email of user.
     * @return list of instances of Borrowing.
     */
    public List<Borrowing> findByUser(String email) {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        BookRepository bookRepository = new BookRepository();
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table)
                    + " WHERE " + DatabaseContract.BorrowingTable.TABLE_NAME + "."
                    + DatabaseContract.BorrowingTable.COL_USER + "=" + "'"
                    + email + "' AND " + DatabaseContract.BorrowingTable.COL_END_DATE
                    + " <= CURRENT_DATE";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_ID_BORROW));
                borrowing.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                borrowing.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_USER)));
                borrowing.setStartDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_START_DATE));
                borrowing.setEndDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_END_DATE));
                borrowing.setRenewal(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_RENEWAL));
                borrowing.setIsDeliver(this.resultSet.getBoolean(DatabaseContract.BorrowingTable.COL_IS_DELIVER));
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return borrowings;
    }

    /**
     * Find the Borrowing of selected user.
     *
     * @param email: email of user.
     * @return list of instances of Borrowing.
     */
    public List<Borrowing> findActiveByUser(String email) {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        BookRepository bookRepository = new BookRepository();
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table)
                    + " WHERE " + DatabaseContract.BorrowingTable.TABLE_NAME + "."
                    + DatabaseContract.BorrowingTable.COL_USER + "=" + "'"
                    + email + "' AND " + DatabaseContract.BorrowingTable.COL_IS_DELIVER
                    + " = 0";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_ID_BORROW));
                borrowing.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                borrowing.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_USER)));
                borrowing.setStartDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_START_DATE));
                borrowing.setEndDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_END_DATE));
                borrowing.setRenewal(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_RENEWAL));
                borrowing.setIsDeliver(this.resultSet.getBoolean(DatabaseContract.BorrowingTable.COL_IS_DELIVER));
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return borrowings;
    }

    /**
     * Check the renewal of the borrowing.
     *
     * @param elem: borrowing elem
     * @return true if is possible, false otherwise.
     */
    public boolean checkBorrowRenewal(Borrowing elem) {
        boolean borrowCheck = false;
        try {
            String table = DatabaseContract.RuleTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.RuleTable.COL_MAX_RENEWAL
            };
            String query = DatabaseUtils.selectQuery(table, fields);
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                int maxRen = this.resultSet.getInt(DatabaseContract.RuleTable.COL_MAX_RENEWAL);
                borrowCheck = maxRen > elem.getRenewal();
                System.out.println("borrowCheck = " + maxRen + " > " + elem.getRenewal() + " ? " + borrowCheck);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return borrowCheck;
    }

    /**
     * Check the renewal of the borrowing.
     *
     * @param elem: borrowing elem
     * @return true if is possible, false otherwise.
     */
    public boolean checkLowerRenewalDay(Borrowing elem) {
        boolean borrowCheck = false;
        try {
            String table = DatabaseContract.RuleTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.RuleTable.COL_LOW_RENEWAL
            };
            String query = DatabaseUtils.selectQuery(table, fields);
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                Calendar c = Calendar.getInstance();
                long diff = c.getTimeInMillis() - elem.getStartDate().getTime();
                int days = this.resultSet.getInt(DatabaseContract.RuleTable.COL_LOW_RENEWAL);
                int borrowDays = (int) (diff / (24 * 60 * 60 * 1000));
                System.out.println("BorrowDays = " + borrowDays + "   " + days);
                borrowCheck = days <= borrowDays;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } finally {
            disconnect();
        }
        return borrowCheck;
    }
    
        /**
     * Insert or update Borrowing elem.
     *
     * @param elem: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    public boolean saveAll(Borrowing elem) {
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.BorrowingTable.COL_ID_BORROW,
                DatabaseContract.BorrowingTable.COL_BOOK,
                DatabaseContract.BorrowingTable.COL_USER,
                DatabaseContract.BorrowingTable.COL_START_DATE,
                DatabaseContract.BorrowingTable.COL_END_DATE,
                DatabaseContract.BorrowingTable.COL_RENEWAL,
                DatabaseContract.BorrowingTable.COL_IS_DELIVER
            };
            String[] values = {
                String.valueOf(elem.getId()),
                elem.getBook().getIsbn(),
                elem.getUser().getEmail(), String.valueOf(elem.getStartDate()),
                String.valueOf(elem.getEndDate()), String.valueOf(elem.getRenewal()), 
                String.valueOf(elem.isDeliver()? 1 : 0)
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
     * Get all the borrowing not deliver of the selected user.
     * 
     * @param email of the user.
     * @return a list of instanceof Borrowing.
     */
    public List<Borrowing> getBorrowingNotDeliver(String email){
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        BookRepository bookRepository = new BookRepository();
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table)
                    + " WHERE " + DatabaseContract.BorrowingTable.TABLE_NAME + "."
                    + DatabaseContract.BorrowingTable.COL_USER + "=" + "'"
                    + email + "' AND " +  DatabaseContract.BorrowingTable.COL_IS_DELIVER 
                    + "= 0";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_ID_BORROW));
                borrowing.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                borrowing.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_USER)));
                borrowing.setStartDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_START_DATE));
                borrowing.setEndDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_END_DATE));
                borrowing.setRenewal(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_RENEWAL));
                borrowing.setIsDeliver(this.resultSet.getBoolean(DatabaseContract.BorrowingTable.COL_IS_DELIVER));
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return borrowings;
    }
    
    /**
     * Finds all the active borrowing in the database.
     *
     * @return list of instances of Borrowing.
     */
    public List<Borrowing> findAllActive() {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        BookRepository bookRepository = new BookRepository();
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table)
                    + " WHERE " + DatabaseContract.BorrowingTable.COL_END_DATE
                    + " >= CURRENT_DATE AND " + DatabaseContract.BorrowingTable.COL_IS_DELIVER
                    + "= 0";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_ID_BORROW));
                borrowing.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                borrowing.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_USER)));
                borrowing.setStartDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_START_DATE));
                borrowing.setEndDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_END_DATE));
                borrowing.setRenewal(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_RENEWAL));
                borrowing.setIsDeliver(this.resultSet.getBoolean(DatabaseContract.BorrowingTable.COL_IS_DELIVER));
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return borrowings;
    }
    
    /**
     * Finds all the archivied borrowing in the database.
     *
     * @return list of instances of Borrowing.
     */
    public List<Borrowing> findAllArchivied() {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        BookRepository bookRepository = new BookRepository();
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table)
                    + " WHERE " + DatabaseContract.BorrowingTable.COL_END_DATE
                    + " < CURRENT_DATE AND " + DatabaseContract.BorrowingTable.COL_IS_DELIVER 
                    + "=1";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_ID_BORROW));
                borrowing.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                borrowing.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_USER)));
                borrowing.setStartDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_START_DATE));
                borrowing.setEndDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_END_DATE));
                borrowing.setRenewal(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_RENEWAL));
                borrowing.setIsDeliver(this.resultSet.getBoolean(DatabaseContract.BorrowingTable.COL_IS_DELIVER));
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return borrowings;
    }
    
    /**
     * Finds all the archivied borrowing in the database.
     *
     * @return list of instances of Borrowing.
     */
    public List<Borrowing> findAllExpired() {
        ArrayList<Borrowing> borrowings = new ArrayList<>();
        BookRepository bookRepository = new BookRepository();
        try {
            String table = DatabaseContract.BorrowingTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table)
                    + " WHERE " + DatabaseContract.BorrowingTable.COL_END_DATE
                    + " < CURRENT_DATE AND " + DatabaseContract.BorrowingTable.COL_IS_DELIVER 
                    + "=0";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Borrowing borrowing = new Borrowing();
                borrowing.setId(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_ID_BORROW));
                borrowing.setBook(bookRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_BOOK)));
                UserRepository userRepository = new UserRepository();
                borrowing.setUser(userRepository.find(this.resultSet.getString(DatabaseContract.BorrowingTable.COL_USER)));
                borrowing.setStartDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_START_DATE));
                borrowing.setEndDate(this.resultSet.getDate(DatabaseContract.BorrowingTable.COL_END_DATE));
                borrowing.setRenewal(this.resultSet.getInt(DatabaseContract.BorrowingTable.COL_RENEWAL));
                borrowing.setIsDeliver(this.resultSet.getBoolean(DatabaseContract.BorrowingTable.COL_IS_DELIVER));
                borrowings.add(borrowing);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return borrowings;
    }
    
}
