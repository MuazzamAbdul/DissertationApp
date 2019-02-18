package com.example.muazzam.dissertationapp.Model;

public class Products {

    private String Name,ID,Category,Description;

    public Products(String name, String ID, String category, String description) {
        Name = name;
        this.ID = ID;
        Category = category;
        Description = description;
    }

    public Products() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
