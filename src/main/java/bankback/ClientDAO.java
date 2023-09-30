package bankback;



import bankback.utils.Hash;
import com.example.transferapp.models.SignUpModel;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;

public class ClientDAO {

    Connection conn = null;
    Statement st = null;
    PreparedStatement ps;

    public ClientDAO(Connection conn) {
        this.conn = conn;
        if(conn != null){
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                System.out.println("erreur SQL  : " + e.getMessage());
            }
        }
    }


    public boolean createClient(SignUpModel signUpModel) {
        int a = -1;
        if(conn != null){
            try {

                String firstName = signUpModel.firstName;
                String lastName = signUpModel.lastName;
                int phoneNumber = signUpModel.phoneNumber;
                String password = signUpModel.password;
                double withdrawLimit = signUpModel.withdrawLimit;

                ResultSet rs = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ phoneNumber +"'");
                if(rs.next()){
                    throw new SQLException("user already exists");
                }
                String req = "INSERT INTO client (FirstName, LastName, PhoneNumber, Password, Date) VALUES(?,?,?,?,?)";
                ps = conn.prepareStatement(req);
                ps.setString(1,firstName);
                ps.setString(2,lastName);
                ps.setInt(3,phoneNumber);
                String hashedPassword = Hash.hashPassword(password);
                ps.setString(4,hashedPassword);
                LocalDate currentDate = LocalDate.now();
                ps.setString(5, String.valueOf(currentDate));
                a = ps.executeUpdate();
                System.out.println("insert into client");

                CheckingAccountDAO checkingAccountDAO = new CheckingAccountDAO(conn);
                ResultSet rs1 = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ phoneNumber +"'");
                if(rs1.next()){
                    int owner =rs1.getInt(1);
                    checkingAccountDAO.createCheckingAccount(owner);
                }


                SavingAccountDAO savingAccountDAO = new SavingAccountDAO(conn);
                ResultSet rs2 = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ phoneNumber +"'");
                if(rs2.next()){
                    int owner =rs2.getInt(1);
                    savingAccountDAO.createSavingAccount(owner,withdrawLimit);
                }

            } catch (SQLException e) {
                System.out.println("getMyTransaction " + e.getMessage());
                return false;
            } catch (NoSuchAlgorithmException e) {
                System.out.println("no such algorithm");
                return false;
            }
        }
        return true;
    }

    public ResultSet getClientInfo(int phoneNumber) {
        int a = -1;
        if (conn != null) {
            try {
                return st.executeQuery("SELECT * FROM client WHERE PhoneNumber ='" + phoneNumber + "'");
            } catch (SQLException e) {
                System.out.println("getMyTransaction " + e.getMessage());
                return null;
            }
        }
        return null;
    }
}
