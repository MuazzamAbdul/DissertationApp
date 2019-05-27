package com.example.muazzam.dissertationapp.Model;

public class Cart {

    private String ID,Name,Date,Time,Supermarket,SupermarketID,Quantity,Price;

    public Cart(String ID, String name, String date, String time, String supermarket, String supermarketid,  String quantity, String price) {
        this.ID = ID;
        Name = name;
        Date = date;
        Time = time;
        Supermarket = supermarket;
        SupermarketID = supermarketid;
        Quantity = quantity;
        Price = price;
    }

    public Cart() {
    }

    public String getSupermarketID() {
        return SupermarketID;
    }

    public void setSupermarketID(String supermarketID) {
        SupermarketID = supermarketID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getSupermarket() {
        return Supermarket;
    }

    public void setSupermarket(String supermarket) {
        Supermarket = supermarket;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
