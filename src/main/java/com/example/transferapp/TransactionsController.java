package com.example.transferapp;

import bankback.ClientDAO;
import bankback.MyConnection;
import bankback.TransactionDAO;
import com.example.transferapp.models.TransactionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable {

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
    private TableColumn<TransactionModel, Double> tc_amount;

    @FXML
    private TableColumn<TransactionModel, Integer> tc_receiver;

    @FXML
    private TableColumn<TransactionModel, Integer> tc_sender;

    @FXML
    private TableView<TransactionModel> tv_transactions;

    @FXML
    private TabPane tp;


    @FXML
    private VBox vb_content;

    ObservableList<TransactionModel> listTransaction;

    @FXML
    private Button btn_chat;

    @FXML
    private Button btn_downlaod_transaction;



    private Stage stage;
    private Scene scene;
    private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources)  {

        Connection conn =  MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );

        String fileName = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\number.txt";

        String phoneNumberString;

        int phoneNumberInt;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            phoneNumberString = br.readLine();
            System.out.println(phoneNumberString);
            phoneNumberInt = Integer.parseInt(phoneNumberString);


            //initializing Transactions
            TransactionDAO transactionDAO = new TransactionDAO(conn);
            ResultSet rsTransactions = transactionDAO.getMyTransactions(phoneNumberInt);

            listTransaction =  FXCollections.observableArrayList();

            while (rsTransactions.next()) {
                int senderPhoneNumber = rsTransactions.getInt(7);
                int id = rsTransactions.getInt(1);
                double amount = rsTransactions.getInt(4);
                String date = rsTransactions.getString(5);
                String message = rsTransactions .getString(6);
                int receiverPhoneNumber = rsTransactions.getInt(8);

                TransactionModel transactionModel = new TransactionModel(id,senderPhoneNumber,receiverPhoneNumber,amount,date,message);
                listTransaction.add(0,transactionModel);
            }

            tc_sender.setCellValueFactory(new PropertyValueFactory<TransactionModel, Integer>("senderPhoneNumber"));
            tc_receiver.setCellValueFactory(new PropertyValueFactory<TransactionModel, Integer>("receiverPhoneNumber"));
            tc_amount.setCellValueFactory(new PropertyValueFactory<TransactionModel, Double>("amount"));

            tv_transactions.setItems(listTransaction);

            VBox newLoadedVbox =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("tab-content.fxml")));


        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void showDetails(MouseEvent event) throws IOException {

        int rowIndex = tv_transactions.getSelectionModel().getSelectedIndex();

        System.out.println("Selected row index: " + rowIndex);

        TransactionModel transactionModel = listTransaction.get(rowIndex);

        int id = transactionModel.id;
        String idString = Integer.toString(id);

        File f = new File("C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\idtransaction.txt");

        try {
            FileWriter fw = new FileWriter(f,false); // append false : overwrite file
            fw.write(idString);
            fw.close();
        }catch (IOException e){
            throw new RuntimeException();
        }

        VBox newLoadedVbox =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("tab-content.fxml")));
        tp.getTabs().add(new Tab("Transaction : " + idString ,newLoadedVbox));

    }

    @FXML
    void changeColorDeposit(MouseEvent event) {btn_deposit.setStyle("-fx-background-color:  #6fc988;");}

    @FXML
    void changeColorDashBoard(MouseEvent event) {btn_dashboard.setStyle("-fx-background-color:  #6fc988;");}

    @FXML
    void changeColorLogOut(MouseEvent event) {
        btn_logout.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void changeColorProfile(MouseEvent event) {
        btn_profile.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void restoreColorDeposit(MouseEvent event) {
        btn_deposit.setStyle("-fx-background-color:   #58a16c;");
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
    void restoreColorProfile(MouseEvent event) {
        btn_profile.setStyle("-fx-background-color:   #58a16c;");
    }

    @FXML
    void restoreColorChat(MouseEvent event) {
        btn_chat.setStyle("-fx-background-color:   #58a16c;");
    }

    @FXML
    void changeColorChat(MouseEvent event) {
        btn_chat.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void donwloadTransactions(ActionEvent event) {


        Connection conn =  MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );

        String fileName = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\number.txt";

        String phoneNumberString;

        int phoneNumberInt;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            phoneNumberString = br.readLine();
            System.out.println(phoneNumberString);
            phoneNumberInt = Integer.parseInt(phoneNumberString);


            //initializing Transactions
            TransactionDAO transactionDAO = new TransactionDAO(conn);
            ResultSet rsTransactions = transactionDAO.getMyTransactions(phoneNumberInt);

            listTransaction =  FXCollections.observableArrayList();
            File f = new File("C:\\Users\\Seif Khelifi\\Desktop\\fichier.html");
            StringBuilder transactionFile = new StringBuilder();


            while (rsTransactions.next()) {
                int id = rsTransactions.getInt(1);
                int senderPhoneNumber = rsTransactions.getInt(7);
                int receiverPhoneNumber = rsTransactions.getInt(8);
                double amount = rsTransactions.getInt(4);
                String message = rsTransactions .getString(6);
                String date = rsTransactions.getString(5);
                String time = rsTransactions.getString(9);

                transactionFile.append("\n").append(id).append(" ").append(senderPhoneNumber).append(" ").append(receiverPhoneNumber).append(" ").append(amount).append(" ").append(message).append(" ").append(date).append(" ").append(time).append("\n");
            }

            try {
                FileWriter fw = new FileWriter(f,true); // append false : overwrite file
                fw.write("<html>" +
                        "<title>Transactions</title>" +
                        "<body>" +
                        "<p>"  + transactionFile + "</p>" +
                        "</body>" + "</html>");
                fw.close();
            }catch (IOException e){
                throw new RuntimeException();
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
    void openProfile(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("profile-view.fxml"));
        root = loader.load();
        // root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
