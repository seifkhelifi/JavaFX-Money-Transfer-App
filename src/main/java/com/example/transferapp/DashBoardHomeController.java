package com.example.transferapp;

import bankback.*;
import bankback.utils.RespnseObject;
import com.example.transferapp.models.TransactionModel;
import com.example.transferapp.utils.FomatAccountNumber;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;


public class DashBoardHomeController implements Initializable {

    @FXML
    private Button btn_deposit;

    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_profile;

    @FXML
    private Button btn_send_money;

    @FXML
    private Button btn_transactions;

    @FXML
    private Label lb_hello;

    //@FXML
    //private ListView<TransactionModel> lv_transactions;
    ////////////////////////////////////////////////////////////////////////////
    @FXML
    private TableColumn<TransactionModel, Double> tc_amount;

    @FXML
    private TableColumn<TransactionModel, String> tc_date;

    @FXML
    private TableColumn<TransactionModel, String> tc_message;

    @FXML
    private TableColumn<TransactionModel, Integer> tc_receiver;

    @FXML
    private TableColumn<TransactionModel, Integer> tc_sender;

    @FXML
    private TableView<TransactionModel> tv_transactions;
    ////////////////////////////////////////////////////////////////////////////////
    @FXML
    private Label warning_amount;

    @FXML
    private Label warning_phone_number;

    @FXML
    private TextField tf_amount;

    @FXML
    private TextField tf_receiver;

    @FXML
    private TextArea ta_message;

    ObservableList<TransactionModel> listTransaction;

    @FXML
    private Label lb_sva_number;

    @FXML
    private Label lb_balance_ck;

    @FXML
    private Label lb_balance_sv;

    @FXML
    private Label lb_cka_number;

    @FXML
    private Label lb_total_gained;

    @FXML
    private Label lb_total_spent;

    @FXML
    private Button btn_chat;


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

        int id;
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
                System.out.println(id + firstName + lastName);
            }

            String Hello = "Hello " + firstName + " " + lastName + " !";
            lb_hello.setText(Hello);

            //initializing Transactions
            TransactionDAO transactionDAO = new TransactionDAO(conn);
            ResultSet rsTransactions = transactionDAO.getMyTransactions(phoneNumberInt);

             listTransaction =  FXCollections.observableArrayList();

            while (rsTransactions.next()) {
                int senderPhoneNumber = rsTransactions.getInt(7);
                int idtransaction = rsTransactions.getInt(1);
                double amount = rsTransactions.getInt(4);
                String date = rsTransactions.getString(5);
                String message = rsTransactions .getString(6);
                int receiverPhoneNumber = rsTransactions.getInt(8);

                TransactionModel transactionModel = new TransactionModel(idtransaction,senderPhoneNumber,receiverPhoneNumber,amount,date,message);
                listTransaction.add(0,transactionModel);
            }

            tc_sender.setCellValueFactory(new PropertyValueFactory<TransactionModel, Integer>("senderPhoneNumber"));
            tc_receiver.setCellValueFactory(new PropertyValueFactory<TransactionModel, Integer>("receiverPhoneNumber"));
            tc_amount.setCellValueFactory(new PropertyValueFactory<TransactionModel, Double>("amount"));
            tc_date.setCellValueFactory(new PropertyValueFactory<TransactionModel, String>("date"));
            tc_message.setCellValueFactory(new PropertyValueFactory<TransactionModel, String>("message"));

            tv_transactions.setItems(listTransaction);



            //ititlaizing checking account
            CheckingAccountDAO checkingAccountDAO = new CheckingAccountDAO(conn);

            ResultSet ckrs = checkingAccountDAO.getCheckingAccountInfo(phoneNumberInt);

            while (ckrs.next()) {
                long ckNumber = ckrs.getLong(1);
                double balance = ckrs.getDouble(3);

                String accNumber = FomatAccountNumber.formatAccountNumber(String.valueOf(ckNumber));

                lb_cka_number.setText(accNumber);
                lb_balance_ck.setText(balance + " DT");
            }

            //ititlaizing saving account
            SavingAccountDAO savingAccountDAO = new SavingAccountDAO(conn);

            ResultSet svrs = savingAccountDAO.getSavingAccountInfo(phoneNumberInt);

            while (svrs.next()) {
                long ckNumber = svrs.getLong(1);
                double balance = svrs.getDouble(3);

                String accNumber = FomatAccountNumber.formatAccountNumber(String.valueOf(ckNumber));

                lb_sva_number.setText(String.valueOf(accNumber));
                lb_balance_sv.setText(balance + " DT");
            }


            //intitlaizing totals
            ResultSet rsTotalGained  = transactionDAO.totalGained(phoneNumberInt);

            while (rsTotalGained.next()) {
                double balance = rsTotalGained.getDouble(1);
                lb_total_gained.setText(balance + " DT");
            }


            ResultSet rsTotalSpent  = transactionDAO.totalSpent(phoneNumberInt);

            while (rsTotalSpent.next()) {
                double balance = rsTotalSpent.getDouble(1);
               lb_total_spent.setText(balance + " DT");
            }




        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void sendMoney(ActionEvent event) {

        Connection conn = MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );

        String receiverPhoneNumberString = tf_receiver.getText();
        String amountString = tf_amount.getText();
        String message = ta_message.getText();

        int receiverPhoneNumberInt;
        double amountDouble;

        String fileName = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\number.txt";

        String senderPhoneNumberString;

        int snderPhoneNumberInt;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            senderPhoneNumberString = br.readLine();
            snderPhoneNumberInt = Integer.parseInt(senderPhoneNumberString);
            System.out.println(snderPhoneNumberInt);

            receiverPhoneNumberInt = Integer.parseInt(receiverPhoneNumberString);
            amountDouble = Double.parseDouble(amountString);

            TransactionDAO transactionDAO = new TransactionDAO(conn);

            RespnseObject respnseObject = new RespnseObject();
            respnseObject = transactionDAO.createTransaction(snderPhoneNumberInt,receiverPhoneNumberInt,amountDouble,message);


            if(respnseObject.state){

                warning_amount.setText("");
                warning_phone_number.setText("");

                warning_phone_number.setText(respnseObject.message);
                warning_phone_number.setTextFill(Color.GREEN);

                listTransaction.clear();

                ResultSet rsTransactions = transactionDAO.getMyTransactions(snderPhoneNumberInt);

                while (rsTransactions.next()) {
                    int senderPhoneNumber = rsTransactions.getInt(7);
                    int idtransaction = rsTransactions.getInt(1);
                    double amount = rsTransactions.getInt(4);
                    String date = rsTransactions.getString(5);
                    String messagetr = rsTransactions .getString(6);
                    int receiverPhoneNumber = rsTransactions.getInt(8);

                    TransactionModel transactionModel = new TransactionModel(idtransaction,senderPhoneNumber,receiverPhoneNumber,amount,date,messagetr);
                    listTransaction.add(0,transactionModel);

                    tv_transactions.refresh();

                    //update balance
                    CheckingAccountDAO checkingAccountDAO = new CheckingAccountDAO(conn);

                    ResultSet ckrs = checkingAccountDAO.getCheckingAccountInfo(senderPhoneNumber);

                    while (ckrs.next()) {
                        double balance = ckrs.getDouble(3);

                        lb_balance_ck.setText(balance + " DT");
                    }

                    //update total spent only (because in dashboard we can only send)
                    ResultSet rsTotalSpent  = transactionDAO.totalSpent(senderPhoneNumber);

                    while (rsTotalSpent.next()) {
                        double balance = rsTotalSpent.getDouble(1);
                        lb_total_spent.setText(balance + " DT");
                    }

                }

            }else{
                switch (respnseObject.message) {
                    case "transaction impossible", "account not found" -> {

                        warning_amount.setText("");
                        warning_phone_number.setText("");

                        warning_phone_number.setText(respnseObject.message);
                        warning_phone_number.setTextFill(Color.RED);
                    }
                    case "non sufficient funds" -> {

                        warning_amount.setText("");
                        warning_phone_number.setText("");

                        warning_amount.setText(respnseObject.message);
                    }
                    default -> System.out.println("error");
                }

            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            //update
            //if update true , insert into tableView
            //use tv.refresh();
        }catch (NumberFormatException e) {
            System.out.println(e.getMessage());

            warning_amount.setText("");
            warning_phone_number.setText("");

            warning_amount.setText("field is empty");
            warning_phone_number.setText("field is empty");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
    void showDeposit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("deposit-view.fxml"));
        root = loader.load();
        // root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
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



    @FXML
    void changeColorDeposit(MouseEvent event) {
        btn_deposit.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void changeColorLogOut(MouseEvent event) {
        btn_logout.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void changeColorProfile(MouseEvent event) {
        btn_profile.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void changeColorTransaction(MouseEvent event) {
        btn_transactions.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void restoreColorDeposit(MouseEvent event) {
        btn_deposit.setStyle("-fx-background-color:   #58a16c;");
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
        void restoreColorTransaction(MouseEvent event) {
        btn_transactions.setStyle("-fx-background-color:   #58a16c;");
    }

    @FXML
    void restoreColorSendMoney(MouseEvent event) {
        btn_send_money.setStyle("-fx-background-color:   #58a16c;");
    }

    @FXML
    void changeColorSendMoney(MouseEvent event) {
        btn_send_money.setStyle("-fx-background-color:  #6fc988;");
    }

    @FXML
    void refresh(MouseEvent event) {
        Connection conn =  MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );

        String fileName = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\number.txt";

        String phoneNumberString;

        int phoneNumberInt;

        int id;
        String firstName = "";
        String lastName = "";

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
                int idtransaction = rsTransactions.getInt(1);
                double amount = rsTransactions.getInt(4);
                String date = rsTransactions.getString(5);
                String message = rsTransactions .getString(6);
                int receiverPhoneNumber = rsTransactions.getInt(8);

                TransactionModel transactionModel = new TransactionModel(idtransaction,senderPhoneNumber,receiverPhoneNumber,amount,date,message);
                listTransaction.add(0,transactionModel);
            }

            tc_sender.setCellValueFactory(new PropertyValueFactory<TransactionModel, Integer>("senderPhoneNumber"));
            tc_receiver.setCellValueFactory(new PropertyValueFactory<TransactionModel, Integer>("receiverPhoneNumber"));
            tc_amount.setCellValueFactory(new PropertyValueFactory<TransactionModel, Double>("amount"));
            tc_date.setCellValueFactory(new PropertyValueFactory<TransactionModel, String>("date"));
            tc_message.setCellValueFactory(new PropertyValueFactory<TransactionModel, String>("message"));

            tv_transactions.setItems(listTransaction);


        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
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
    void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        root = loader.load();
        // root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
