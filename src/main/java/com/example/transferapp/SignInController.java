package com.example.transferapp;

import bankback.LoginDAO;
import bankback.MyConnection;
import bankback.SignInDAO;
import com.example.transferapp.models.LoginModel;
import com.example.transferapp.models.SignUpModel;
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
import java.util.Objects;

public class SignInController {

    @FXML
    private Button btn_signup;

    @FXML
    private TextField tf;

    @FXML
    private TextField tf_first_name;

    @FXML
    private TextField tf_last_name;

    @FXML
    private TextField tf_password;

    @FXML
    private TextField tf_phone_number;

    @FXML
    private Label warning_phone_number;

    @FXML
    private TextField tf_withdraw_limit;

    @FXML
    private Label warning_withdraw_limit;

    @FXML
    private Label tf_warning_empty;

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    void showDashBoard(ActionEvent event) {
        Connection conn =  MyConnection.connect(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost/bank",
                "root",
                ""
        );

        int phoneNumber = 0;

        if(
                Objects.equals(tf_first_name.getText(), "") ||
                        Objects.equals(tf_last_name.getText(), "") ||
                        Objects.equals(tf_phone_number.getText(), "") ||
                        Objects.equals(tf_withdraw_limit.getText(), "") ||
                        Objects.equals(tf_password.getText(), "")
        ){

            warning_phone_number.setText("");
            warning_withdraw_limit.setText("");
            tf_warning_empty.setText("");

            tf_warning_empty.setText("some or all fields are empty");
        }else{
            try {
                phoneNumber = Integer.parseInt(tf_phone_number.getText());


                double withdrawLimit = 0;

                withdrawLimit = Double.parseDouble(tf_withdraw_limit.getText());

                SignUpModel signUpModel = new SignUpModel(
                        tf_first_name.getText(),
                        tf_last_name.getText(),
                        phoneNumber,
                        tf_password.getText(),
                        withdrawLimit
                );
                SignInDAO signInDAO = new SignInDAO(conn);
                boolean signedUp = signInDAO.SignIn(signUpModel);
                if (!signedUp) {

                    warning_phone_number.setText("");
                    warning_withdraw_limit.setText("");
                    tf_warning_empty.setText("");

                    warning_phone_number.setText("account with same phone number exists");
                }else{
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
                        throw new RuntimeException();
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());

                warning_phone_number.setText("");
                warning_withdraw_limit.setText("");
                tf_warning_empty.setText("");

                warning_phone_number.setText("phone number must be number");
                warning_withdraw_limit.setText("withdraw limit must be number");
            }
        }
    }

}
