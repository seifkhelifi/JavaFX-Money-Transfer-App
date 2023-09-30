package com.example.transferapp.models;

public class TransactionModel {

    public int id;
    public int senderPhoneNumber;
    public int receiverPhoneNumber;
    public double amount;
    public String date;

    public String message;

    public int getId() {
        return id;
    }

    public int getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public int getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public TransactionModel(int id, int senderPhoneNumber, int receiverPhoneNumber, double amount, String date, String message) {
        this.id = id;
        this.senderPhoneNumber = senderPhoneNumber;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.amount = amount;
        this.date = date;
        this.message = message;
    }
}
