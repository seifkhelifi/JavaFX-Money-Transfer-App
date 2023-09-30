package bankback;

import bankback.utils.RespnseObject;

import java.sql.*;

public class CheckingAccountDAO {

    Connection conn = null;
    Statement st = null;

    public CheckingAccountDAO(Connection conn) {
        this.conn = conn;
        if(conn != null){
            try {
                st = conn.createStatement();
            } catch (SQLException e) {
                System.out.println("erreur SQL  : " + e.getMessage());
            }
        }
    }

    public void createCheckingAccount(int owner){
        int a = -1;
        if(conn != null){
            try {
                String req = "INSERT INTO checkingaccounts (Owner) VALUES(?)";
                PreparedStatement ps = conn.prepareStatement(req);
                ps.setInt(1,owner);
                a = ps.executeUpdate();
                System.out.println("checking account created");
            } catch (SQLException e) {
                System.out.println("createCheckingAccount : " + e.getMessage());
            }
        }
    }

    public RespnseObject depositMoneyFromSavingAccount(double amount, int phoneNumber) {
        int a = -1;
        RespnseObject respnseObject = new RespnseObject();
        if(conn != null){
            try {
                ResultSet rs1 = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ phoneNumber +"'");
                int clientId = 0;

                while(rs1.next()) {
                    clientId =rs1.getInt(1);
                }

                ResultSet rs2 = st.executeQuery("SELECT Balance,WithdrawLimit FROM savingsaccounts WHERE Owner  ='"+ clientId + "'");
                double savingBalance = 0;
                double withdrawLimit = 0;

                while(rs2.next()) {
                    savingBalance =rs2.getDouble(1);
                    withdrawLimit =rs2.getDouble(2);
                }

                if(amount > withdrawLimit){
                    respnseObject.state = false;
                    respnseObject.message = "withdraw limit exceeded";
                    throw new SQLException("withdraw limit exceeded");
                }else{
                    if(amount > savingBalance){
                        respnseObject.state = false;
                        respnseObject.message = "not enough funds";
                        throw new SQLException("not enough funds");
                    }else{
                        double newSavingBalance = savingBalance - amount;
                        double newCheckingBalance = 0;
                        double checkingBalance = 0;

                        ResultSet rs3 = st.executeQuery("SELECT Balance FROM checkingaccounts WHERE Owner  ='"+ clientId + "'");

                        while(rs3.next()) {
                            checkingBalance =rs3.getDouble(1);
                        }

                        newCheckingBalance = checkingBalance + amount;

                        int c = st.executeUpdate("UPDATE savingsaccounts SET Balance = '" + newSavingBalance + "'  WHERE Owner ='"+ clientId +"'");
                        int d = st.executeUpdate("UPDATE checkingaccounts SET Balance = '" + newCheckingBalance + "'  WHERE Owner ='"+ clientId +"'");

                        respnseObject.state = true;
                        respnseObject.message = "transaction done";
                        return respnseObject;
                    }
                }

            } catch (SQLException e) {
                System.out.println("move int saving account : " + e.getMessage());
                return respnseObject;
            }
        }
        return respnseObject;
    }
    public ResultSet getCheckingAccountInfo(int phoneNumber) {
        if(conn != null){
            try {
                ResultSet rs1 = st.executeQuery("SELECT ID FROM client WHERE PhoneNumber ='"+ phoneNumber +"'");
                int clientId = 0;

                while(rs1.next()) {
                    clientId =rs1.getInt(1);
                }

                return st.executeQuery("SELECT * FROM checkingaccounts WHERE Owner  ='"+ clientId + "'");
            } catch (SQLException e) {
                System.out.println("getCheckingAccountInfo " + e.getMessage());
            }
        }
        return null;
    }

    public void depositFromBank(double amount, int phoneNumber){
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

                double newCheckingBalance = checkingBalance + amount;

                int d = st.executeUpdate("UPDATE checkingaccounts SET Balance = '" + newCheckingBalance + "'  WHERE Owner ='"+ clientId +"'");

            } catch (SQLException e) {
                System.out.println("move int saving account : " + e.getMessage());
            }
        }
    }

}
