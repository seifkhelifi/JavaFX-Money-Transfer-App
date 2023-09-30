package com.example.transferapp.models;

public class SignUpModel {
    public String firstName;
    public String lastName;
    public int phoneNumber;
    public String password;
    public double withdrawLimit;

    public SignUpModel(String firstName, String lastName, int phoneNumber, String password, double withdrawLimit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.withdrawLimit = withdrawLimit;
    }

}
