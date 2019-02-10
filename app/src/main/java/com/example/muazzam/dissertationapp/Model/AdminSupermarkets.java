package com.example.muazzam.dissertationapp.Model;

import java.util.ArrayList;

public class AdminSupermarkets {

    private ArrayList<String> supName,supId;
    private String id, name;

    public AdminSupermarkets(String id, String name) {
        this.id = id;
        this.name = name;
        supName = new ArrayList<>();
        supId = new ArrayList<>();

        addName(name);
        addId(id);

    }



    public AdminSupermarkets()
    {
        supName = new ArrayList<>();
        supId = new ArrayList<>();
        supName.add("Choose Supermarket");
    }

    public ArrayList<String> getSupName() {
        return supName;
    }

    public void setSupName(ArrayList<String> supName) {
        this.supName = supName;
    }

    public ArrayList<String> getSupId() {
        return supId;
    }

    public void setSupId(ArrayList<String> supId) {
        this.supId = supId;
    }


    public void addId(String id)
    {
        supId.add(id);
    }

    public void addName(String name)
    {
        supName.add(name);
    }

}
