package com.example.transferapp.utils;

public class FomatAccountNumber {
    public static String formatAccountNumber(String number){

        String formattedAccount;

        formattedAccount = number.substring(0,4);
        formattedAccount += " ";
        formattedAccount += number.substring(4,8);
        formattedAccount += " ";
        formattedAccount += number.substring(8,12);
        formattedAccount += " ";
        formattedAccount += number.substring(12,16);

        return formattedAccount;
    }
}
