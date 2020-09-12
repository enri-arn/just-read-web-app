package database;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Arnaudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class DatabaseConfig {

    private static String url;
    private static String user;
    private static String pwd;

    /**
     * Registers the given driver with the DriverManager.
     */
    public static void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            System.out.println("Driver correttamente registrato");
        } catch (SQLException e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    /**
     * get the url for the connection.
     * 
     * @return the url.
     */
    public static String getUrl() {
        return url;
    }

    /**
     * get the user for the connection.
     * 
     * @return the user.
     */
    public static String getUser() {
        return user;
    }

    /**
     * get the password for the connection.
     * 
     * @return password.
     */
    public static String getPassword() {
        return pwd;
    }

    /**
     * set the parameter url of the connnection.
     * 
     * @param url : url of the connnection.
     */
    public static void setUrl(String url) {
        DatabaseConfig.url = url;
    }

    /**
     * set the user of the connection.
     * 
     * @param user : user of the connection.
     */
    public static void setUser(String user) {
        DatabaseConfig.user = user;
    }

    /**
     * set the password of the connection.
     * 
     * @param pwd : password of the connection.
     */
    public static void setPwd(String pwd) {
        DatabaseConfig.pwd = pwd;
    }

}
