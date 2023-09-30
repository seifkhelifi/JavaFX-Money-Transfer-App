package bankback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public static Connection connect(String driverName,String url ,String user, String password) {
        Connection conn = null;
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connceted");
        } catch (SQLException e) {
            System.out.println("erreur driver " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("erreur runtime " + e.getMessage());
        }
        return conn;
    }
}

