package com.example.muazzam.dissertationapp.Model;

public class DisplayUsers {

    private String Name,Email,Address,PhoneNumber,Key;

    public DisplayUsers() {
    }

    public DisplayUsers(String name, String email, String address, String phoneNumber, String key) {
        Name = name;
        Email = email;
        Address = address;
        PhoneNumber = phoneNumber;
        Key = key;
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

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
