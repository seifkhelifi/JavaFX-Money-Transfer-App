package com.example.transferapp;

import bankback.CheckingAccountDAO;
import bankback.MyConnection;
import bankback.SavingAccountDAO;
import bankback.utils.RespnseObject;
import com.example.transferapp.utils.FomatAccountNumber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DepositController implements Initializable {
    @FXML
    private Button btn_dashboard;

    @FXML
    private Button btn_dashboard1;

    @FXML
    private Button btn_dashboard2;

    @FXML
    private Button btn_dashboard3;

    @FXML
    private Button btn_deposit;

    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_profile;

    @FXML
    private Button btn_transactions;

    @FXML
    private Label lb_balance_ck;

    @FXML
    private Label lb_balance_ck1;

    @FXML
    private Label lb_balance_sv;

    @FXML
    private Label lb_balance_sv1;

    @FXML
    private Label lb_cka_number;

    @FXML
    private Label lb_cka_number1;

    @FXML
    private Label lb_hello;

    @FXML
    private Label lb_sva_number;

    @FXML
    private Label lb_sva_number1;

    @FXML
    private TextField tf_ck_to_sv;

    @FXML
    private TextField tf_sv_to_ck;

    @FXML
    private Label lb_waning_1;

    @FXML
    private Label lb_warning_2;

    @FXML
    private TextField tf_acc_number;

    @FXML
    private TextField tf_amount;

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

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            phoneNumberString = br.readLine();
            System.out.println(phoneNumberString);
            phoneNumberInt = Integer.parseInt(phoneNumberString);

            //ititlaizing checking account
            CheckingAccountDAO checkingAccountDAO = new CheckingAccountDAO(conn);

            ResultSet ckrs = checkingAccountDAO.getCheckingAccountInfo(phoneNumberInt);

            while (ckrs.next()) {
                long ckNumber = ckrs.getLong(1);
                double balance = ckrs.getDouble(3);


                String accNumber = FomatAccountNumber.formatAccountNumber(String.valueOf(ckNumber));

                lb_cka_number.setText(accNumber);
                lb_balance_ck.setText(balance + " DT");

                lb_cka_number1.setText(accNumber);
                lb_balance_ck1.setText(balance + " DT");
            }

            //ititlaizing saving account
            SavingAccountDAO savingAccountDAO = new SavingAccountDAO(conn);

            ResultSet svrs = savingAccountDAO.getSavingAccountInfo(phoneNumberInt);

            while (svrs.next()) {
                long ckNumber = svrs.getLong(1);
                double balance = svrs.getDouble(3);

                String accNumber = FomatAccountNumber.formatAccountNumber(String.valueOf(ckNumber));

                lb_sva_number.setText(accNumber);
                lb_balance_sv.setText(balance + " DT");

                lb_sva_number1.setText(accNumber);
                lb_balance_sv1.setText(balance + " DT");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void transferToChecking(ActionEvent event) {

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

            String amountString = tf_sv_to_ck.getText();
            double amountDouble = Double.parseDouble(amountString);

            CheckingAccountDAO checkingAccountDAO = new CheckingAccountDAO(conn);
            RespnseObject respnseObject = checkingAccountDAO.depositMoneyFromSavingAccount(amountDouble,phoneNumberInt);

            if(respnseObject.state){

                lb_warning_2.setText("Transaction success");
                lb_warning_2.setTextFill(Color.GREEN);

                lb_waning_1.setText("");

                //updating checking account
                ResultSet ckrs = checkingAccountDAO.getCheckingAccountInfo(phoneNumberInt);

                while (ckrs.next()) {
                    double balance = ckrs.getDouble(3);

                    lb_balance_ck.setText(balance + " DT");

                    lb_balance_ck1.setText(balance + " DT");
                }


                //updating saving accuont
                SavingAccountDAO savingAccountDAO = new SavingAccountDAO(conn);

                ResultSet svrs = savingAccountDAO.getSavingAccountInfo(phoneNumberInt);

                while (svrs.next()) {
                    double balance = svrs.getDouble(3);

                    lb_balance_sv.setText(balance + " DT");

                    lb_balance_sv1.setText(balance + " DT");
                }


            }else{
                switch (respnseObject.message) {
                    case "withdraw limit exceeded" -> {
                        lb_warning_2.setText("withdraw limit exceeded");
                        lb_warning_2.setTextFill(Color.RED);

                        lb_waning_1.setText("");
                    }
                    case "not enough funds" -> {
                        lb_warning_2.setText("not enough funds");
                        lb_warning_2.setTextFill(Color.RED);

                        lb_waning_1.setText("");
                    }
                    default -> System.out.println("error");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    @FXML
    void transferToSaving(ActionEvent event) {

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

            String amountString = tf_ck_to_sv.getText();
            double amountDouble = Double.parseDouble(amountString);

            SavingAccountDAO savingAccountDAO = new SavingAccountDAO(conn);
            RespnseObject respnseObject = savingAccountDAO.depositMoneyFromCheckingAccount(amountDouble,phoneNumberInt);

            if(respnseObject.state){

                lb_waning_1.setText("Transaction success");
                lb_waning_1.setTextFill(Color.GREEN);

                lb_warning_2.setText("");

                //updating checking account
                CheckingAccountDAO checkingAccountDAO = new CheckingAccountDAO(conn);
                ResultSet ckrs = checkingAccountDAO.getCheckingAccountInfo(phoneNumberInt);

                while (ckrs.next()) {
                    double balance = ckrs.getDouble(3);

                    lb_balance_ck.setText(balance + " DT");

                    lb_balance_ck1.setText(balance + " DT");
                }


                //updating saving accuont
                ResultSet svrs = savingAccountDAO.getSavingAccountInfo(phoneNumberInt);

                while (svrs.next()) {
                    double balance = svrs.getDouble(3);

                    lb_balance_sv.setText(balance + " DT");

                    lb_balance_sv1.setText(balance + " DT");
                }


            }else{
                if ("not enough funds".equals(respnseObject.message)) {
                    lb_warning_2.setText("");

                    lb_waning_1.setText("not enough funds");
                    lb_waning_1.setTextFill(Color.RED);
                } else {
                    System.out.println("error");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void depositFromBank(ActionEvent event) {
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

            String amountString = tf_amount.getText();
            double amountDouble = Double.parseDouble(amountString);

            CheckingAccountDAO checkingAccountDAO = new CheckingAccountDAO(conn);
            checkingAccountDAO.depositFromBank(amountDouble,phoneNumberInt);

            //updating checking account
            ResultSet ckrs = checkingAccountDAO.getCheckingAccountInfo(phoneNumberInt);

            while (ckrs.next()) {
                double balance = ckrs.getDouble(3);

                lb_balance_ck.setText(balance + " DT");

                lb_balance_ck1.setText(balance + " DT");
            }


            //updating saving accuont
            SavingAccountDAO savingAccountDAO = new SavingAccountDAO(conn);
            ResultSet svrs = savingAccountDAO.getSavingAccountInfo(phoneNumberInt);

            while (svrs.next()) {
                long ckNumber = svrs.getLong(1);
                double balance = svrs.getDouble(3);

                lb_sva_number.setText(String.valueOf(ckNumber));
                lb_balance_sv.setText(balance + " DT");

                lb_sva_number1.setText(String.valueOf(ckNumber));
                lb_balance_sv1.setText(balance + " DT");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
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
    void showDashBoard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashBoard-home.fxml"));
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
    void changeColorProfile(MouseEvent event) {
        btn_profile.setStyle("-fx-background-color:  #6fc988;");
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
    void restoreColorProfile(MouseEvent event) {
        btn_profile.setStyle("-fx-background-color:   #58a16c;");
    }

    @FXML
    void restoreColorTransaction(MouseEvent event) {
        btn_transactions.setStyle("-fx-background-color:   #58a16c;");
    }

}
