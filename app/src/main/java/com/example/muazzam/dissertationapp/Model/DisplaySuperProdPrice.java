package com.example.muazzam.dissertationapp.Model;

public class DisplaySuperProdPrice {

    private String name,price;

    public DisplaySuperProdPrice() {
    }

    public DisplaySuperProdPrice(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}