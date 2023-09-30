package bankback;

import com.example.transferapp.models.SignUpModel;

import java.sql.*;

public class SignInDAO {
    Connection conn = null;
    Statement st = null;
    PreparedStatement ps;

    public SignInDAO(Connection conn) {
        this.conn = conn;
        if(conn != null){
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                System.out.println("erreur SQL  : " + e.getMessage());
            }
        }
    }

    public boolean SignIn(SignUpModel signUpModel){
        int a = -1;
        if(conn != null){
            try {
                ResultSet rs1 = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ signUpModel.phoneNumber +"'");
                if(rs1.next()){
                    throw new SQLException("account already exists");
                }
                ClientDAO clientDAO= new ClientDAO(conn);
                boolean clientCreated =  clientDAO.createClient(signUpModel);
            } catch (SQLException e) {
                System.out.println("createCheckingAccount : " + e.getMessage());
                return false;
            }
        }
        return true;
    }
}
