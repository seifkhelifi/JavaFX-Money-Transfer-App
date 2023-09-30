module com.example.transferapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.rmi;


    opens com.example.transferapp to javafx.fxml;
    exports com.example.transferapp;
    opens com.example.transferapp.models to javafx.base;

}