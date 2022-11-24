package model.singletons;
import java.sql.*;

public class DBConnectionSingleton {
	  
    private static DBConnectionSingleton _instance;
    private Connection conn = null;
    private String dbUrl = "jdbc:mysql://localhost:3306/barbershopApp";  
    private String userName = "XXX";
    private String password = "YYY";

    private DBConnectionSingleton() throws SQLException {
        try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, userName, password);
        } catch (ClassNotFoundException ex) {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }
    }
   
    public Connection getConnection() {
        return conn;
    }

    public static DBConnectionSingleton getInstance() throws SQLException {
        if (_instance == null) {
            _instance = new DBConnectionSingleton();
        } else if (_instance.getConnection().isClosed()) {
            _instance = new DBConnectionSingleton();
        }
        return _instance;
    }
	
}
