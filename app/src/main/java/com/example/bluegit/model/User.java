package com.example.bluegit.model;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;

import org.checkerframework.checker.units.qual.A;

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
    private ArrayList<DocumentReference> orderRef;


    public User() {
    }

    public User(String id, String displayName, String email, String phoneNumber, String profileImageSrc) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageSrc = profileImageSrc;
        this.balance = 1000000;
        this.orderRef = new ArrayList<>();
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<DocumentReference> getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(ArrayList<DocumentReference> orderRef) {
        this.orderRef = orderRef;
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
}
