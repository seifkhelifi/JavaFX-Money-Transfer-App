package com.example.transferapp;

import bankback.ClientDAO;
import bankback.MyConnection;
import bankback.TransactionDAO;
import com.example.transferapp.models.TransactionModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class ContactController implements Initializable {

    @FXML
    private ImageView iv_logo;

    @FXML
    private Label tf_name;

    @FXML
    private Label tf_number;

    @FXML
    private HBox vb_contact;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Connection conn =  MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );

        String fileName = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\contactCredentials";

        String phoneNumberString;

        int phoneNumberInt;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            phoneNumberString = br.readLine();
            System.out.println(phoneNumberString);
            phoneNumberInt = Integer.parseInt(phoneNumberString);

            ClientDAO clientDAO = new ClientDAO(conn);

            //contact info
            ResultSet rsSender = clientDAO.getClientInfo(phoneNumberInt);

            String firstNameSender = "";
            String lastnameSender = "";

            while (rsSender.next()) {
                firstNameSender = rsSender.getString(2);
                lastnameSender = rsSender.getString(3);
            }

            tf_name.setText(firstNameSender + " " + lastnameSender);
            tf_number.setText(phoneNumberString);


        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void changeColor(MouseEvent event) {
        vb_contact.setStyle("-fx-background-color:   #f0f0f0;");
        vb_contact.setStyle("-fx-border-color:   grey;");
    }

    @FXML
    void restoreColor(MouseEvent event) {
        vb_contact.setStyle("-fx-background-color:   white;");
        vb_contact.setStyle("-fx-border-color:   grey;");

    }

    @FXML
    void writeNumberIntoFile(MouseEvent event) {
        //on click tekteb num mte3ha
        //scroll pane on cick , tfeychi new conv
        File f = new File("C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\contactCredentials");
        try {
            FileWriter fw = new FileWriter(f,false); // append false : overwrite file
            System.out.println(tf_number.getText());
            fw.write(tf_number.getText());
            fw.close();

        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new RuntimeException();

        }

    }

}
