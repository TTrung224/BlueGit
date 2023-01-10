package com.example.bluegit.model;

import android.net.Uri;

public class Account {
    private String name;
    private String nameDisplay;
    private int age;
    private String phone;
    private String address;
    private Uri profileImg;

    public Account(Uri profileImg, String nameDisplay, String name, int age, String phone, String address) {
        this.name = name;
        this.nameDisplay = nameDisplay;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.profileImg = profileImg;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
