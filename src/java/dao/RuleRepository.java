package dao;

import bean.Rule;
import database.DatabaseContract;
import database.DatabaseUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class RuleRepository extends AbstractRepository<Rule, Integer> {

    /**
     * Finds all the elements of type Rule in the database.
     *
     * @return list of instances of Rule.
     */
    @Override
    public List<Rule> findAll() {
        ArrayList<Rule> rules = new ArrayList<>();
        try {
            String table = DatabaseContract.RuleTable.TABLE_NAME;
            connect(DatabaseUtils.selectQuery(table));
            this.resultSet = this.statement.executeQuery();
            while (this.resultSet.next()) {
                Rule rule = new Rule();
                rule.setId(this.resultSet.getInt(DatabaseContract.RuleTable.COL_ID_RULE));
                rule.setBorrowDays(this.resultSet.getInt(DatabaseContract.RuleTable.COL_BORROW_DAYS));
                rule.setAddress(this.resultSet.getString(DatabaseContract.RuleTable.COL_ADDRESS));
                rule.setPhoneNumber(this.resultSet.getString(DatabaseContract.RuleTable.COL_PHONE_NUMBER));
                rule.setName(this.resultSet.getString(DatabaseContract.RuleTable.COL_NAME));
                rule.setBuisnessHour(this.resultSet.getString(DatabaseContract.RuleTable.COL_BUSINESS_HR));
                rule.setMaxRenewal(this.resultSet.getInt(DatabaseContract.RuleTable.COL_MAX_RENEWAL));
                rule.setLowerRenewal(this.resultSet.getInt(DatabaseContract.RuleTable.COL_LOW_RENEWAL));
                rules.add(rule);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return rules;
    }

    /**
     * Find the Rule elem with id passed.
     *
     * @param id: id of element to search.
     * @return if exists, Rule elem with id past.
     */
    @Override
    public Rule find(Integer id) {
        Rule rule = new Rule();
        try {
            String table = DatabaseContract.RuleTable.TABLE_NAME;
            String query = DatabaseUtils.selectQuery(table) + " WHERE "
                    + DatabaseContract.RuleTable.COL_ID_RULE + "='" + id + "'";
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                rule.setId(this.resultSet.getInt(DatabaseContract.RuleTable.COL_ID_RULE));
                rule.setBorrowDays(this.resultSet.getInt(DatabaseContract.RuleTable.COL_BORROW_DAYS));
                rule.setAddress(this.resultSet.getString(DatabaseContract.RuleTable.COL_ADDRESS));
                rule.setPhoneNumber(this.resultSet.getString(DatabaseContract.RuleTable.COL_PHONE_NUMBER));
                rule.setName(this.resultSet.getString(DatabaseContract.RuleTable.COL_NAME));
                rule.setBuisnessHour(this.resultSet.getString(DatabaseContract.RuleTable.COL_BUSINESS_HR));
                rule.setMaxRenewal(this.resultSet.getInt(DatabaseContract.RuleTable.COL_MAX_RENEWAL));
                rule.setLowerRenewal(this.resultSet.getInt(DatabaseContract.RuleTable.COL_LOW_RENEWAL));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return rule;
    }

    /**
     * Insert or update Rule elem.
     *
     * @param rule: element to insert or update.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean save(Rule rule) {
        try {
            String table = DatabaseContract.RuleTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.RuleTable.COL_ID_RULE,
                DatabaseContract.RuleTable.COL_BORROW_DAYS,
                DatabaseContract.RuleTable.COL_ADDRESS,
                DatabaseContract.RuleTable.COL_PHONE_NUMBER,
                DatabaseContract.RuleTable.COL_NAME,
                DatabaseContract.RuleTable.COL_BUSINESS_HR,
                DatabaseContract.RuleTable.COL_MAX_RENEWAL,
                DatabaseContract.RuleTable.COL_LOW_RENEWAL
            };
            String[] values = {
                String.valueOf(rule.getId()),
                String.valueOf(rule.getBorrowDays()), rule.getAddress(),
                rule.getPhoneNumber(), rule.getName(),
                rule.getBuisnessHour(),
                String.valueOf(rule.getMaxRenewal()),
                String.valueOf(rule.getLowerRenewal())
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
     * Delete Rule element with id passed.
     *
     * @param id: id of the elem to delete.
     * @return true, if operation was successful, false otherwise.
     */
    @Override
    public boolean delete(Integer id) {
        try {
            String table = DatabaseContract.RuleTable.TABLE_NAME;
            String field = DatabaseContract.RuleTable.COL_ID_RULE;
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
     * Get the number of borrow days of the library
     * 
     * @return an int rapresent the days of borrow.
     */
    public int getBorrowDays(){
        int maxRen = 0;
        try {
            String table = DatabaseContract.RuleTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.RuleTable.COL_BORROW_DAYS
            };
            String query = DatabaseUtils.selectQuery(table, fields);
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                maxRen = this.resultSet.getInt(DatabaseContract.RuleTable.COL_BORROW_DAYS);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return maxRen;
        } finally {
            disconnect();
        }
        return maxRen;
    }

    /**
     * Get the max borrowing days permitted by library
     * 
     * @return an integer rapresent the max borrowing days permitted.
     */
    public int getMaxBorrowing() {
        int maxBorrowing = 0;
        try {
            String table = DatabaseContract.RuleTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.RuleTable.COL_MAX_BORROWING
            };
            String query = DatabaseUtils.selectQuery(table, fields);
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                maxBorrowing = this.resultSet.getInt(DatabaseContract.RuleTable.COL_MAX_BORROWING);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        } finally {
            disconnect();
        }
        return maxBorrowing;
    }

    /**
     * Get the buisness hour of the library.
     * 
     * @param id of the library.
     * @return a string rapresent the buisness hour.
     */
    public String getBuisnessHour(int id) {
        String buisnesshour = null;
        try {
            String table = DatabaseContract.RuleTable.TABLE_NAME;
            String[] fields = {
                DatabaseContract.RuleTable.COL_BUSINESS_HR
            };
            String query = DatabaseUtils.selectQuery(table, fields);
            connect(query);
            this.resultSet = this.statement.executeQuery();
            this.resultSet.first();
            if (this.resultSet != null) {
                buisnesshour = this.resultSet.getString(DatabaseContract.RuleTable.COL_BUSINESS_HR);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return null;
        } finally {
            disconnect();
        }
        return buisnesshour;
    }

}
