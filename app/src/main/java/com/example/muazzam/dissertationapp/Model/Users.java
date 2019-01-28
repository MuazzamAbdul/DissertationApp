package com.example.muazzam.dissertationapp.Model;

public class Users {

    private String Name,Email,Address,PhoneNo;

    public Users() {
    }

    public Users(String name, String email, String address, String phoneNo) {
        Name = name;
        Email = email;
        Address = address;
        PhoneNo = phoneNo;

    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }
}
