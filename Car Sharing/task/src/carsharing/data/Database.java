package carsharing.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:./src/carsharing/db/";
    private static final String USER = "";
    private static final String PASS = "";

    private String databaseFileName;
    private Connection connection;

    public Database(String databaseFileName) {
        this.databaseFileName = databaseFileName;
    }

    public Connection getConnection() {
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //Open a connection
            connection = DriverManager.getConnection(DB_URL + this.databaseFileName);
            connection.setAutoCommit(true);
        } catch(
                SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch(Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch(SQLException se){
            se.printStackTrace();
        }
    }
}
