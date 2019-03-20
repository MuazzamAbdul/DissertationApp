package com.example.muazzam.dissertationapp.Model;

public class DisplaySuperProdPrice {

    private String name,price,distance;

    public DisplaySuperProdPrice() {
    }

    public DisplaySuperProdPrice(String name, String price,String distance) {
        this.name = name;
        this.price = price;
        this.distance = distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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
