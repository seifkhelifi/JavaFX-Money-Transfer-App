package bankback;

import bankback.utils.RespnseObject;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionDAO {
    Connection conn = null;
    Statement st = null;
    PreparedStatement ps;

    public TransactionDAO(Connection conn) {
        this.conn = conn;
        if(conn != null){
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                System.out.println("erreur SQL  : " + e.getMessage());
            }
        }
    }

    public RespnseObject createTransaction(int senderPhoneNumber, int receiverPhoneNumber, double sum, String message){
        int a = -1;
        RespnseObject respnseObject = new RespnseObject();
        if(conn != null){
            try {
                if(senderPhoneNumber == receiverPhoneNumber){
                    respnseObject.state = false;
                    respnseObject.message = "transaction impossible";
                    throw new SQLException("transaction impossible");
                }
                // getting sender client
                ResultSet rs1 = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ senderPhoneNumber +"'");
                if(!rs1.next()){
                    respnseObject.state = false;
                    respnseObject.message = "account not found";
                    throw new SQLException("account not found");
                }
                int senderOwner =rs1.getInt(1);
                //getting sender balance
                ResultSet rs2 = st.executeQuery("SELECT Balance FROM checkingaccounts WHERE Owner ='"+ senderOwner +"'");
                if(!rs2.next()){
                    respnseObject.state = false;
                    respnseObject.message = "account not found";
                    throw new SQLException("account not found");
                }
                double senderBalance =rs2.getInt(1);
                if(senderBalance < sum){
                    respnseObject.state = false;
                    respnseObject.message = "non sufficient funds";
                    throw new SQLException("non sufficient funds");
                }else{
                    senderBalance -= sum;
                    //updating sender balance
                    int b = st.executeUpdate("UPDATE checkingaccounts SET Balance = '" + senderBalance + "'  WHERE Owner ='"+ senderOwner +"'");
                    //getting receiver client
                    ResultSet rs3 = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ receiverPhoneNumber +"'");
                    if(!rs3.next()){
                        respnseObject.state = false;
                        respnseObject.message = "account not found";
                        throw new SQLException("account not found");
                    }
                    int receiverOwner =rs3.getInt(1);
                    //getting sender balance
                    ResultSet rs4 = st.executeQuery("SELECT Balance FROM checkingaccounts WHERE Owner ='"+ receiverOwner +"'");
                    if(!rs4.next()){
                        respnseObject.state = false;
                        respnseObject.message = "account not found";
                        throw new SQLException("account not found");
                    }
                    double receiverBalance =rs4.getInt(1);
                    //updating receiver balance
                    receiverBalance +=sum;
                    int c = st.executeUpdate("UPDATE checkingaccounts SET Balance = '" + receiverBalance + "'  WHERE Owner ='"+ receiverOwner +"'");

                    //creating transaction
                    String req = "INSERT INTO transaction (Sender, Receiver, Amount, Date, Message, SenderPhoneNumber,ReceiverPhoneNumber,Time) VALUES(?,?,?,?,?,?,?,?)";
                    ps = conn.prepareStatement(req);
                    ps.setInt(1,senderOwner);
                    ps.setInt(2,receiverOwner);
                    ps.setDouble(3,sum);
                    LocalDate currentDate = LocalDate.now();
                    LocalTime currentTime = LocalTime.now();
                    ps.setString(4, String.valueOf(currentDate));
                    ps.setString(5, message);
                    ps.setInt(6,senderPhoneNumber);
                    ps.setInt(7,receiverPhoneNumber);
                    ps.setString(8, String.valueOf(currentTime));
                    a = ps.executeUpdate();
                    respnseObject.state = true;
                    respnseObject.message = "transaction complete";
                    return respnseObject;
                }
            } catch (SQLException e) {
                System.out.println("createTransaction " + e.getMessage());
                return respnseObject;
            }
        }
        return respnseObject;
    }

    public ResultSet getMyTransactions(int phoneNumber) {
        if(conn != null){
            try {
                return st.executeQuery("SELECT * FROM transaction WHERE SenderPhoneNumber ='"+ phoneNumber +"' OR ReceiverPhoneNumber = '"+ phoneNumber +"'");
            } catch (SQLException e) {
                System.out.println("getMyTransaction " + e.getMessage());
            }
        }
        return null;
    }

    public ResultSet getSingleTransaction(int id) {
        if(conn != null){
            try {
                return st.executeQuery("SELECT * FROM transaction WHERE id ='" + id + "'");
            } catch (SQLException e) {
                System.out.println("getMyTransaction " + e.getMessage());
            }
        }
        return null;
    }

    public ResultSet totalSpent(int phoneNumber) {
        if(conn != null){
            try {
                String sql = "SELECT SUM(Amount) AS totalspent FROM transaction WHERE SenderPhoneNumber ='" + phoneNumber + "'";
                ps = conn.prepareStatement(sql);
                return ps.executeQuery();
            } catch (SQLException e) {
                System.out.println("getMyTransaction " + e.getMessage());
            }
        }
        return null;
    }

    public ResultSet totalGained(int phoneNumber){
        if(conn != null){
            try {
                String sql = "SELECT SUM(Amount) AS totalspent FROM transaction WHERE SenderPhoneNumber <>'" + phoneNumber + "'";
                ps = conn.prepareStatement(sql);
                return ps.executeQuery();
            } catch (SQLException e) {
                System.out.println("getMyTransaction " + e.getMessage());
            }
        }
        return null;
    }

    public ResultSet senders(int phoneNumber){
        if(conn != null){
            try {
                String sql = "SELECT  DISTINCT SenderPhoneNumber FROM transaction WHERE  ReceiverPhoneNumber  ='" + phoneNumber + "'";
                ps = conn.prepareStatement(sql);
                return ps.executeQuery();
            } catch (SQLException e) {
                System.out.println("getMyTransaction " + e.getMessage());
            }
        }
        return null;
    }

    public ResultSet receivers(int phoneNumber){
        if(conn != null){
            try {
                String sql = "SELECT  DISTINCT ReceiverPhoneNumber FROM transaction WHERE  SenderPhoneNumber  ='" + phoneNumber + "'";
                ps = conn.prepareStatement(sql);
                return ps.executeQuery();
            } catch (SQLException e) {
                System.out.println("getMyTransaction " + e.getMessage());
            }
        }
        return null;
    }

}
