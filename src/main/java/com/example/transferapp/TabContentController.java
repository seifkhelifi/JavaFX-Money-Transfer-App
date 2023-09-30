package com.example.transferapp;

import bankback.ClientDAO;
import bankback.MyConnection;
import bankback.TransactionDAO;
import com.example.transferapp.models.TransactionModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TabContentController implements Initializable {


    @FXML
    private ImageView iv_logo;

    @FXML
    private Label lb_date;

    @FXML
    private Label lb_firstname;

    @FXML
    private Label lb_id;

    @FXML
    private Label lb_lastname;

    @FXML
    private Label lb_message;

    @FXML
    private Label lb_sr_firstname;

    @FXML
    private Label lb_sr_lastname;

    @FXML
    private Label lb_time;

    @FXML
    private Label lb_type;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Connection conn = MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );

        String fileName1 = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\idtransaction.txt";

        String fileName2 = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\number.txt";

        String idString;

        String currentPhoneNumberString;

        int idInt;

        int currentPhoneNumberInt;

        try  {
            BufferedReader br1 = new BufferedReader(new FileReader(fileName1));
            BufferedReader br2 = new BufferedReader(new FileReader(fileName2));

            idString = br1.readLine();
            currentPhoneNumberString = br2.readLine();

            System.out.println("id in initialize : " +idString);

            idInt = Integer.parseInt(idString);
            currentPhoneNumberInt = Integer.parseInt(currentPhoneNumberString);

            TransactionDAO transactionDAO = new TransactionDAO(conn);

            ResultSet rsTransaction = transactionDAO.getSingleTransaction(idInt);

            int id = 0;
            String date = "";
            String time = "";
            String message = "";
            int senderPhoneNumber = 0;
            int receiverPhoneNumber = 0;

            while (rsTransaction.next()) {
                id = rsTransaction.getInt(1);
                date = rsTransaction.getString(5);
                time = rsTransaction.getString(9);
                message = rsTransaction .getString(6);
                senderPhoneNumber = rsTransaction.getInt(7);
                receiverPhoneNumber = rsTransaction.getInt(8);
            }

            ClientDAO clientDAO = new ClientDAO(conn);

            //sender info
            ResultSet rsSender = clientDAO.getClientInfo(senderPhoneNumber);

            String firstNameSender = "";
            String lastnameSender = "";

            while (rsSender.next()) {
                firstNameSender = rsSender.getString(2);
                lastnameSender = rsSender.getString(3);
            }

            //receiver info
            ResultSet rsReceiver = clientDAO.getClientInfo(receiverPhoneNumber);

            String firstNameReceiver = "";
            String lastnameReceiver = "";

            while (rsReceiver.next()) {
                firstNameReceiver = rsReceiver.getString(2);
                lastnameReceiver = rsReceiver.getString(3);
            }

            if(currentPhoneNumberInt == senderPhoneNumber){
                //outgoing request
                lb_type.setText("Out-going");
                lb_id.setText(idString);

                Image image = new Image("C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\src\\main\\resources\\images\\red.png");
                iv_logo.setImage(image);

                lb_sr_firstname.setText("Receiver Firstname :");
                lb_firstname.setText(firstNameReceiver);

                lb_sr_lastname.setText("Receiver Lastname :");
                lb_lastname.setText(lastnameReceiver);

            }else{
                //incoming request
                lb_type.setText("Incoming");
                lb_id.setText(idString);

                Image image = new Image("C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\src\\main\\resources\\images\\green.png");
                iv_logo.setImage(image);

                lb_sr_firstname.setText("Sender Firstname :");
                lb_firstname.setText(firstNameSender);

                lb_sr_lastname.setText("Sender Lastname :");
                lb_lastname.setText(lastnameSender);

            }

            lb_date.setText(date);
            lb_time.setText(time);
            lb_message.setText(message);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}

