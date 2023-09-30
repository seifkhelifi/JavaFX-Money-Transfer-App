package bankback;



import bankback.utils.Hash;
import com.example.transferapp.models.LoginModel;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Objects;

public class LoginDAO {
    Connection conn = null;
    Statement st = null;
    PreparedStatement ps;

    public LoginDAO(Connection conn) {
        this.conn = conn;
        if(conn != null){
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                System.out.println("erreur SQL  : " + e.getMessage());
            }
        }
    }

    public boolean LogIn(LoginModel loginModel){
        int a = -1;
        int phoneNumber = loginModel.phoneNumber;
        String password = loginModel.password;
        if(conn != null){
            try {
                ResultSet rs1 = st.executeQuery("SELECT Password FROM client WHERE PhoneNumber ='"+ phoneNumber +"'");
                if(!rs1.next()){
                    throw new SQLException("account not found");
                }
                String hashedPassword =rs1.getString(1);
                String passwordToVerify = Hash.hashPassword(password);
                if(!Objects.equals(hashedPassword, passwordToVerify)){
                    throw new SQLException("Password wrong");
                }
            } catch (SQLException e) {
                System.out.println("Login " + e.getMessage());
                return false;
            } catch (NoSuchAlgorithmException e) {
                System.out.println("no such algorithm");
                return false;
            }
        }
        return true;
    }
}
