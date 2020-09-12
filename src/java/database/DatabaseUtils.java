package database;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class DatabaseUtils {

    /**
     * Select all the fields of the table.
     *
     * @param table: table of the database
     * @return String that rapresent a query.
     */
    public static String selectQuery(String table) {
        return "SELECT * FROM " + table;
    }

    /**
     * Select the fields past fields.
     *
     * @param table: table of the database.
     * @param fields: fields to return.
     * @return String that rapresent a query.
     */
    public static String selectQuery(String table, String[] fields) {
        String query = "SELECT ";
        for (int i = 0; i < fields.length; i++) {
            query += fields[i];
            if (i < fields.length - 1) {
                query += ", ";
            }
        }
        return query + " FROM " + table;
    }

    /**
     * Insert in a table the fields past. If fields are alredy present, it
     * performs an update of the row.
     *
     * @param table: table of the database.
     * @param fields: fields of table to insert or update.
     * @param values: values to insert or update.
     * @return String that rapresent a query.
     */
    public static String insertUpdateQuery(String table, String[] fields, String[] values) {
        String query = "INSERT INTO " + table + " (";
        for (int i = 0; i < fields.length; i++) {
            query += fields[i];
            if (i < fields.length - 1) {
                query += ", ";
            }
        }
        query += ") VALUES (";
        for (int i = 0; i < values.length; i++) {
            query += "'" + values[i] + "'";
            if (i < values.length - 1) {
                query += ", ";
            }
        }
        query += ") ON DUPLICATE KEY UPDATE ";
        for (int i = 0; i < fields.length; i++) {
            query += fields[i] + "='" + values[i] + "'";
            if (i < fields.length - 1) {
                query += ", ";
            }
        }
        return query;
    }

    /**
     * Delete fields in a table.
     *
     * @param table: table of the database.
     * @param fields: fields of table to delete.
     * @param values: values to delete.
     * @return String that rapresent a query.
     */
    public static String deleteQuery(String table, String fields, String values) {
        return "DELETE FROM " + table + " WHERE " + fields + " = " + values;
    }
}
