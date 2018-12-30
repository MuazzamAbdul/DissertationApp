package com.example.muazzam.dissertationapp;

public class UserProfile {
    private String uname;
    private String uemail;
    private String upassword;
    private String uaddress;
    private String uphoneNo;

    public UserProfile(String uname, String uemail, String upassword, String uaddress, String uphoneNo) {
        this.uname = uname;
        this.uemail = uemail;
        this.upassword = upassword;
        this.uaddress = uaddress;
        this.uphoneNo = uphoneNo;
    }

    public UserProfile() {
    }
}
