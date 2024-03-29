package com.example.bluegit.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class User {
    // User
    private String id;
    private String displayName;
    private String profileImageSrc;
    private String email;
    private String phoneNumber;
    private List<String> address;
    private int balance;
    private ArrayList<DocumentReference> buyOrderRef;
    private ArrayList<DocumentReference> sellOrderRef;


    public User() {
    }

    public User(String id, String displayName, String email, String phoneNumber, String profileImageSrc) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = new ArrayList<>();
        this.profileImageSrc = profileImageSrc;
        this.balance = 5000000;
        this.buyOrderRef = new ArrayList<>();
        this.sellOrderRef = new ArrayList<>();
        this.address = new ArrayList<>();
    }



    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<DocumentReference> getBuyOrderRef() {
        return buyOrderRef;
    }

    public void setBuyOrderRef(ArrayList<DocumentReference> buyOrderRef) {
        this.buyOrderRef = buyOrderRef;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImageSrc() {
        return profileImageSrc;
    }

    public void setProfileImageSrc(String profileImageSrc) {
        this.profileImageSrc = profileImageSrc;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public ArrayList<DocumentReference> getSellOrderRef() {
        return sellOrderRef;
    }

    public void setSellOrderRef(ArrayList<DocumentReference> sellOrderRef) {
        this.sellOrderRef = sellOrderRef;
    }
}
