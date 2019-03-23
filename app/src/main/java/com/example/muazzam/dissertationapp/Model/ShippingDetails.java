package com.example.muazzam.dissertationapp.Model;

public class ShippingDetails {

    private String fname,lname,address,city,price;

    public ShippingDetails(String fname, String lname, String address, String city,String price) {
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.city = city;
        this.price = price;
    }

    public ShippingDetails() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
