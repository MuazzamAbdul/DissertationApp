package com.example.muazzam.dissertationapp.Model;

public class Users {

    private String Name,Email;

    public Users() {
    }

    public Users(String name, String email) {
        Name = name;
        Email = email;

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

}
