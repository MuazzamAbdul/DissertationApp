package com.example.muazzam.dissertationapp.Model;

import android.net.Uri;

public class AdminAddProduct {

    private String id,name,desc,category;
    private Uri imageUri;

    public AdminAddProduct() {
    }

    public AdminAddProduct(String id, String name, String desc, String category, Uri imageUri) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.category = category;
        this.imageUri = imageUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
