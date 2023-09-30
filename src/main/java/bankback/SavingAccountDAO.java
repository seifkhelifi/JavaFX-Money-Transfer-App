package bankback;

import bankback.utils.RespnseObject;

import java.sql.*;

public class SavingAccountDAO {

    Connection conn = null;
    Statement st = null;

    public SavingAccountDAO(Connection conn) {
        this.conn = conn;
        if(conn != null){
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                System.out.println("erreur SQL  : " + e.getMessage());
            }
        }
    }

    //  withdrawLimit is provided by default 0 from the client
    public void createSavingAccount(int owner, double withdrawLimit){
        int a = -1;
        if(conn != null){
            try {
                String req = "INSERT INTO savingsaccounts (Owner, WithdrawLimit) VALUES(?,?)";
                PreparedStatement ps = conn.prepareStatement(req);
                ps.setInt(1,owner);
                ps.setDouble(2,withdrawLimit);
                a = ps.executeUpdate();
                System.out.println("saving account created");
            } catch (SQLException e) {
                System.out.println("createSavingAccount : " + e.getMessage());
            }
        }
    }

    public RespnseObject depositMoneyFromCheckingAccount(double amount, int phoneNumber) {
        int a = -1;
        RespnseObject respnseObject = new RespnseObject();
        if(conn != null){
            try {
                ResultSet rs1 = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ phoneNumber +"'");
                int clientId = 0;

                while(rs1.next()) {
                    clientId =rs1.getInt(1);
                }

                ResultSet rs2 = st.executeQuery("SELECT Balance FROM checkingaccounts WHERE Owner  ='"+ clientId + "'");
                double checkingBalance = 0;

                while(rs2.next()) {
                    checkingBalance =rs2.getDouble(1);
                }

                if(amount > checkingBalance){
                    respnseObject.state = false;
                    respnseObject.message = "not enough funds";
                    throw new SQLException("not enough funds");
                }else{
                    double newCheckingBalance = checkingBalance - amount;
                    double newSavingBalance = 0;
                    double savingBalance = 0;

                    ResultSet rs3 = st.executeQuery("SELECT Balance FROM savingsaccounts WHERE Owner  ='"+ clientId + "'");

                    while(rs3.next()) {
                        savingBalance =rs3.getDouble(1);
                    }

                    newSavingBalance = savingBalance + amount;

                    int c = st.executeUpdate("UPDATE savingsaccounts SET Balance = '" + newSavingBalance + "'  WHERE Owner ='"+ clientId +"'");
                    int d = st.executeUpdate("UPDATE checkingaccounts SET Balance = '" + newCheckingBalance + "'  WHERE Owner ='"+ clientId +"'");

                    respnseObject.state = true;
                    respnseObject.message = "transaction done";
                    return respnseObject;
                }
            }catch (SQLException e) {
                System.out.println("move int saving account : " + e.getMessage());
                return respnseObject;
            }
        }
        return respnseObject;
    }


    public ResultSet getSavingAccountInfo(int phoneNumber) {
        if(conn != null){
            try {
                ResultSet rs1 = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ phoneNumber +"'");
                int clientId = 0;

                while(rs1.next()) {
                    clientId =rs1.getInt(1);
                }

                return st.executeQuery("SELECT * FROM savingsaccounts WHERE Owner  ='"+ clientId + "'");
            } catch (SQLException e) {
                System.out.println("getSavingAccountInfo " + e.getMessage());
            }
        }
        return null;
    }

}
