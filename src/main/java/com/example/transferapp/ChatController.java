package com.example.transferapp;

import bankback.MyConnection;
import bankback.TransactionDAO;
import bankback.chat.ChatClient;
import bankback.chat.ChatRemote;
import bankback.chat.Message;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ChatController implements Initializable {

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
    private Button btn_send;


    ArrayList<Integer> listContact;

    @FXML
    private VBox vb_contacts;


    @FXML
    private TextField tf_message;


    //////discussion////////////////////////////////////
    @FXML
    private ListView<Message> lv_discussion;

    ObservableList<String> messages;

//////////////////////////////////////////
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

            TransactionDAO transactionDAO = new TransactionDAO(conn);
            ResultSet rs1 =  transactionDAO.receivers(phoneNumberInt);
            ResultSet rs2 =  transactionDAO.senders(phoneNumberInt);

            listContact = new ArrayList<>();

            while (rs1.next()){
                int receivers = rs1.getInt(1);
                if(!listContact.contains(receivers)){
                    listContact.add(receivers);
                }
            }

            while (rs2.next()){
                int senders = rs2.getInt(1);
                if(!listContact.contains(senders)){
                    listContact.add(senders);
                }
            }

            File f = new File("C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\contactCredentials");
            try {
                for (Integer num : listContact) {
                    FileWriter fw = new FileWriter(f,false); // append false : overwrite file
                    System.out.println(num);
                    fw.write( Integer.toString(num));
                    fw.close();

                    HBox newLoadedVbox =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("contact.fxml")));
                    vb_contacts.getChildren().add(newLoadedVbox);

                }
            }catch (IOException e){
                System.out.println(e.getMessage());
                throw new RuntimeException();

            }


        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void send(ActionEvent event) {
        String fileNameReceiver = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\contactCredentials";

        String phoneNumberReceiverString;

        int phoneNumberReceiverInt = 0;


        try (BufferedReader br = new BufferedReader(new FileReader(fileNameReceiver))) {

            phoneNumberReceiverString = br.readLine();
            System.out.println(phoneNumberReceiverString);
            phoneNumberReceiverInt = Integer.parseInt(phoneNumberReceiverString);

            System.out.println(phoneNumberReceiverInt + "read by contact controller ");

        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }


        String fileNameSender = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\number.txt";

        String phoneNumberSenderString;

        int phoneNumberSenderInt;


        try (BufferedReader br = new BufferedReader(new FileReader(fileNameSender))) {

            phoneNumberSenderString = br.readLine();
            System.out.println(phoneNumberSenderString);
            phoneNumberSenderInt = Integer.parseInt(phoneNumberSenderString);


        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new RuntimeException();

        }



       try {
            ChatRemote r = (ChatRemote) Naming.lookup("rmi://127.0.0.1:1099/chat");

            String textMessage = tf_message.getText();

            Message message = new Message(
                    textMessage,
                    phoneNumberReceiverInt,
                    phoneNumberSenderInt,
                    String.valueOf(LocalTime.now()),
                    String.valueOf(LocalDate.now())
            );

            r.createNewMessage(message);

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void openChat(MouseEvent event) throws Exception {
        String fileNameReceiver = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\contactCredentials";

        String phoneNumberReceiverString;

        int phoneNumberReceiverInt = 0;


        try (BufferedReader br = new BufferedReader(new FileReader(fileNameReceiver))) {

            phoneNumberReceiverString = br.readLine();
            System.out.println(phoneNumberReceiverString);
            phoneNumberReceiverInt = Integer.parseInt(phoneNumberReceiverString);

            System.out.println(phoneNumberReceiverInt + "read by contact controller ");

        } catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }


        String fileNameSender = "C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\number.txt";

        String phoneNumberSenderString;

        int phoneNumberSenderInt;


        try (BufferedReader br = new BufferedReader(new FileReader(fileNameSender))) {

            phoneNumberSenderString = br.readLine();
            System.out.println(phoneNumberSenderString);
            phoneNumberSenderInt = Integer.parseInt(phoneNumberSenderString);


        }catch (IOException e){
            System.out.println(e.getMessage());
            throw new RuntimeException();

        }

    /*
        try {
           ChatRemote r = (ChatRemote) Naming.lookup("rmi://127.0.0.1:1099/chat");

            while(true){
                ArrayList<Message> messages = r.getMessageById(phoneNumberSenderInt, phoneNumberReceiverInt);
                lv_discussion = new ListView<Message>((ObservableList<Message>) messages);
            }

        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            System.out.println(e.getMessage());
        }
*/

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Perform the time-consuming operation here
                // ...
                String name;
                int PORT = 5000;
                System.out.println("Welcome to the chat room!");
                System.out.print("Please enter your name: ");
                Scanner scanner = new Scanner(System.in);
                name = scanner.nextLine();

                Socket socket = new Socket("localhost", PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println(name);
                Platform.runLater(() -> {
                    // code to update UI components goes here
                    tf_message.setText("sdfsdfdsfsd");

                });

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            while (true) {
                                String message = in.readLine();
                                if (message == null) {
                                    System.out.println("Disconnected from the chat room.");
                                    System.exit(0);
                                }
                                System.out.println(message);
                            }
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    }
                }).start();

                Platform.runLater(() -> {
                    // code to update UI components goes here
                    tf_message.setText("end");
                });

                new Thread(new Runnable() {
                    public void run() {
                        while (true) {
                            String message = scanner.nextLine();
                            if (message.equalsIgnoreCase("/exit")) {
                                System.out.println("Leaving the chat room...");
                                break;
                            }
                            out.println(message);
                        }
                    }
                }).start();

                socket.close();

                return null;
            }
        };


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
    void restoreColorTransaction(MouseEvent event) {
        btn_transactions.setStyle("-fx-background-color:   #58a16c;");
    }

    @FXML
    void changeColorTransaction(MouseEvent event) {
        btn_transactions.setStyle("-fx-background-color:  #6fc988;");
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


}
