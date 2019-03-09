package com.example.muazzam.dissertationapp.Model;

public class Supermarkets {

    private String ID,Name,Location;

    public Supermarkets(String ID, String name, String location) {
        this.ID = ID;
        Name = name;
        Location = location;
    }

    public Supermarkets() {
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
