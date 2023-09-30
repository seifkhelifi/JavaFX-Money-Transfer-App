package com.example.transferapp;

import bankback.*;
import com.example.transferapp.models.TransactionModel;
import com.example.transferapp.utils.FomatAccountNumber;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileContorller implements Initializable {

    @FXML
    private Button btn_chat;

    @FXML
    private Button btn_dashboard;

    @FXML
    private Button btn_deposit;

    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_profile;

    @FXML
    private Button btn_transactions;

    @FXML
    private Label lb_hello;


    @FXML
    private Label lb_lastname;

    @FXML
    private Label lb_phonenumber;

    @FXML
    private Label lb_withdraw_limit;

    @FXML
    private Label lb_firstname;

    @FXML
    private Label lb_id;


    private Stage stage;
    private Scene scene;
    private Parent root;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Connection conn =  MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );

        String fileName = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\number.txt";

        String phoneNumberString;

        int phoneNumberInt;

        int id = 0;
        String firstName = "";
        String lastName = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            phoneNumberString = br.readLine();
            System.out.println(phoneNumberString);
            phoneNumberInt = Integer.parseInt(phoneNumberString);

            //intitlazing hello label
            ClientDAO clientDAO = new ClientDAO(conn);
            ResultSet rs = clientDAO.getClientInfo(phoneNumberInt);

            while (rs.next()) {
                id = rs.getInt(1);
                firstName = rs.getString(2);
                lastName = rs.getString(3);

            }

            lb_firstname.setText(firstName);
            lb_id.setText(String.valueOf(id));
            lb_lastname.setText(lastName);
            lb_phonenumber.setText(phoneNumberString);


            //ititlaizing saving account
            SavingAccountDAO savingAccountDAO = new SavingAccountDAO(conn);

            ResultSet svrs = savingAccountDAO.getSavingAccountInfo(phoneNumberInt);

            while (svrs.next()) {
                double withdrawlimit = svrs.getDouble(4);
                lb_withdraw_limit.setText(String.valueOf(withdrawlimit));
            }



        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void backToDashBoard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashBoard-home.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void openChat(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chat-view.fxml"));
        root = loader.load();
        // root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }



    @FXML
    void showDeposit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("deposit-view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void showTransactions(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashBoard-transactions.fxml"));
        root = loader.load();
        // root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    @FXML
    void changeColorChat(MouseEvent event) {
        btn_chat.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void restoreColorChat(MouseEvent event) {
        btn_chat.setStyle("-fx-background-color:   #58a16c;");
    }

    @FXML
    void changeColorDashBoard(MouseEvent event) {
        btn_dashboard.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void changeColorLogOut(MouseEvent event) {
        btn_logout.setStyle("-fx-background-color:  #6fc988;");
    }



    @FXML
    void changeColorTransaction(MouseEvent event) {
        btn_transactions.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void restoreColorDashBoard(MouseEvent event) {
        btn_dashboard.setStyle("-fx-background-color:   #58a16c;");
    }

    @FXML
    void restoreColorLogOut(MouseEvent event) {
        btn_logout.setStyle("-fx-background-color:   #58a16c;");
    }



    @FXML
    void restoreColorTransaction(MouseEvent event) {
        btn_transactions.setStyle("-fx-background-color:   #58a16c;");
    }

    public void changeColorDeposit(MouseEvent mouseEvent) {
        btn_deposit.setStyle("-fx-background-color:  #6fc988;");
    }

    public void restoreColorDeposit(MouseEvent mouseEvent) {
        btn_deposit.setStyle("-fx-background-color:   #58a16c;");
    }
}
