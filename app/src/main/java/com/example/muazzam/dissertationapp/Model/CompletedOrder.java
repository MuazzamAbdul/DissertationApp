package com.example.muazzam.dissertationapp.Model;

public class CompletedOrder {

    private String name,total,dateTime;

    public CompletedOrder(String name, String total,String dateTime) {
        this.name = name;
        this.total = total;
        this.dateTime = dateTime;
    }

    public CompletedOrder() {
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
