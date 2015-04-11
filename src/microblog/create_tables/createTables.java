package microblog.create_tables;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;


import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class createTables
 *
 */
@WebListener
public class createTables implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public createTables() {
        // TODO Auto-generated constructor stub
    }

    private boolean tableAlreadyExists(SQLException e) {
        boolean exists;
        if(e.getSQLState().equals("X0Y32")) {
            exists = true;
        } else {
            exists = false;
        }
        return exists;
    }
    
	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  { 
    
    	ServletContext cntx = event.getServletContext();
    	
    	try{
    		//obtain CustomerDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup("java:comp/env/jdbc/MicroBlogDatasource");
    		Connection conn = ds.getConnection();
    		
    		boolean created = false;
    		try{
    			//create Customers table
    			Statement stmt = conn.createStatement();
    			
    			
    			//stmt.executeUpdate("DROP TABLE MESSAGES");
    			
    			//stmt.executeUpdate("DROP TABLE USERS");
    			
    			//stmt.executeUpdate("DROP TABLE FOLLOWING");
    			//stmt.executeUpdate("DROP TABLE FOLLOWER");
    			
    			stmt.executeUpdate("CREATE TABLE MESSAGES( msgID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), Posted_by_userID INTEGER NOT NULL, Posted_by_Nickname varchar(255) NOT NULL, Date varchar(255) NOT NULL, Content varchar(255) NOT NULL, Republished_count INTEGER NOT NULL, PRIMARY KEY (msgID) )");
    			
    			stmt.executeUpdate("CREATE TABLE USERS( userID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), UserName varchar(255) NOT NULL, Password varchar(255) NOT NULL, Nickname varchar(255) NOT NULL, Description varchar(32672), PhotoURL varchar(255), Following_count INTEGER, Follower_count INTEGER, PRIMARY KEY (userID) )");
    			
    			stmt.executeUpdate("CREATE TABLE FOLLOWING( ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userID INTEGER NOT NULL,Following_userID INTEGER NOT NULL, PRIMARY KEY (ID) )");
    			stmt.executeUpdate("CREATE TABLE FOLLOWER( ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), userID INTEGER NOT NULL,Follower_userID INTEGER NOT NULL, PRIMARY KEY (ID) )");
    			
    			//commit update
        		conn.commit();
        		stmt.close();
    		}catch (SQLException e){
    			//check if exception thrown since table was already created (so we created the database already 
    			//in the past
    			created = tableAlreadyExists(e);
    			if (!created){
    				throw e;//re-throw the exception so it will be caught in the
    				//external try..catch and recorded as error in the log
    			}
    		}
    		

    		//close connection
    		conn.close();

    	}
    	catch(SQLException e)
    	{
    		cntx.log("Error during database initialization",e);
    	}
    	catch(NamingException e)
    	{
    		cntx.log("Error during database initialization",e);
    	}
    	
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event)  { 
        //shut down database
   	 try {
			DriverManager.getConnection("jdbc:derby:" + "MicroBlogDB" +";shutdown=true");
		} catch (SQLException e) {
			ServletContext cntx = event.getServletContext();
			cntx.log("Error shutting down database",e);
		}
    }
	
}
