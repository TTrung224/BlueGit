package com.example.bluegit.model;

import android.net.Uri;

public class User {
    private String id;
    private String displayName;
    private Uri profileImageSrc;
    private String email;
    private String phoneNumber;

    public User(String displayName, String email, Uri profileImageSrc) {
        this.displayName = displayName;
        this.email = email;
        this.profileImageSrc = profileImageSrc;
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

    public Uri getProfileImageSrc() {
        return profileImageSrc;
    }

    public void setProfileImageSrc(Uri profileImageSrc) {
        this.profileImageSrc = profileImageSrc;
    }
}
