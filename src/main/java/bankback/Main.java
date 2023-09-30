package bankback;

import bankback.utils.RespnseObject;
import com.example.transferapp.models.TransactionModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection conn =  MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );
        TransactionDAO transactionDAO = new TransactionDAO(conn);
        ResultSet rs = transactionDAO.receivers(29477481);

        while (rs.next()) {
            int senderPhoneNumber = rs.getInt(1);


            System.out.println(senderPhoneNumber);

        }


    }
}
