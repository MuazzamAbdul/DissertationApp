package com.example.muazzam.dissertationapp.Model;

public class Wallet {

    private String cardNumber;
    private int funds;

    public Wallet() {
    }

    public Wallet(String cardNumber, int funds) {
        this.cardNumber = cardNumber;
        this.funds = funds;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getFunds() {
        return funds;
    }

    public void setFunds(int funds) {
        this.funds = funds;
    }
}
