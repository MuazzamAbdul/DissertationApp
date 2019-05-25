package com.example.muazzam.dissertationapp.Model;

public class Recipes {

    private String Name,Description;

    public Recipes() {
    }

    public Recipes(String name, String description) {
        Name = name;
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
