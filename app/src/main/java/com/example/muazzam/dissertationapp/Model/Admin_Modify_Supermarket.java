package com.example.muazzam.dissertationapp.Model;

public class Admin_Modify_Supermarket {

    private String name,ID, district;

    public Admin_Modify_Supermarket(String name, String ID, String district) {
        this.name = name;
        this.ID = ID;
        this.district = district;
    }

    public Admin_Modify_Supermarket() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
