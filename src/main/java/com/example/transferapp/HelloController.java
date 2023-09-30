package com.example.transferapp;

import bankback.LoginDAO;
import bankback.MyConnection;
import com.example.transferapp.models.LoginModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

public class HelloController {

    @FXML
    private Button btn_login;

    @FXML
    private Button btn_signup;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_phone_number;

    @FXML
    private Label tf_warning_int;

    @FXML
    private Label tf_warning_credentials;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void login(ActionEvent event) {

        Connection conn =  MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );

        try {

            int phoneNumber = Integer.parseInt(tf_phone_number.getText());
            LoginModel loginModel = new LoginModel(phoneNumber, tf_password.getText());
            LoginDAO loginDAO = new LoginDAO(conn);
            boolean loggendIn = loginDAO.LogIn(loginModel);


            if(loggendIn){
                File f = new File("C:\\Users\\Seif Khelifi\\IdeaProjects\\1-ing\\TransferApp\\number.txt");
                try {
                    FileWriter fw = new FileWriter(f,false); // append false : overwrite file
                    fw.write(tf_phone_number.getText());
                    fw.close();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("dashBoard-home.fxml"));
                    root = loader.load();
                    // root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();

                }catch (IOException e){
                    System.out.println(e.getMessage());
                    throw new RuntimeException();

                }
            }else{
                tf_warning_int.setText("");
                tf_warning_credentials.setText("please verify credentials");
            }
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            tf_warning_credentials.setText("");
            tf_warning_int.setText("phone number must be number");
        }

    }


    @FXML
    void showSignUp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("signIn-view.fxml"));
        root = loader.load();
        // root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
