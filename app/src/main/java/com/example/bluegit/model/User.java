package com.example.bluegit.model;

import android.net.Uri;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;

public class User {
    // User
    private String id;
    private String displayName;
    private Uri profileImageSrc;
    private String email;
    private String phoneNumber;

    @Exclude
    private ArrayList<Product> products;

    public User() {
    }

    // User
    public User(String id, String displayName, String email, String phoneNumber, Uri profileImageSrc) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileImageSrc = profileImageSrc;
    }


    // User
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

    public Uri getProfileImageSrc() {
        return profileImageSrc;
    }

    public void setProfileImageSrc(Uri profileImageSrc) {
        this.profileImageSrc = profileImageSrc;
    }
}
