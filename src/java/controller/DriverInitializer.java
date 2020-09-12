package controller;

import database.DatabaseConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Aranudo Enrico, Giraudo Paolo, Impeduglia Alessia
 */
public class DriverInitializer implements ServletContextListener{

    /**
     * Initialize the context of all the servlets and register the
     * driver from database.
     * 
     * @param sce event class for notifications about changes to 
     * the servlet context of a web application.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("servlet context inizialized");
        ServletContext ctx = sce.getServletContext();
        DatabaseConfig.setUrl(ctx.getInitParameter("db-url"));
        DatabaseConfig.setUser(ctx.getInitParameter("user"));
        DatabaseConfig.setPwd(ctx.getInitParameter("pwd"));
        DatabaseConfig.registerDriver();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //not necessary
    }
    
}
