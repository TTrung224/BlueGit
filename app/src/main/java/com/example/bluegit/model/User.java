package com.example.bluegit.model;

import android.net.Uri;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;

public class User {
    // User
    private String id;
    private String displayName;
    private Uri profileImageSrc;
    private String email;
    private String phoneNumber;
    
    // Account
    private String name;
    private int age;
    private String phone;
    private String address;

    // Seller
    private int numCompleted;
    private int numDelivery;
    private int numConfirmed;
    private int numPending;

    private ArrayList<Product> products;
    private ArrayList<Integer> amount;
    private float totalPrice;
    private String voucher;
    private LocalDateTime createdDate;
    private String status; // preparing delivering received
    private int totalProductQuantity;

    // TODO: reconsider data type
    private String customerId;
    private String sellerId;

    public User() {
    }

    // User
    public User(String id, String displayName, String email, Uri profileImageSrc) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.profileImageSrc = profileImageSrc;
    }

    // Account
    public User(Uri profileImgSrc, String displayName, String name, int age, String phone, String address) {
        this.name = name;
        this.displayName = displayName;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.profileImageSrc = profileImgSrc;
    }

    // Seller
    public User(String id, ArrayList<Product> products, ArrayList<Integer> amount,
                  String voucher, String customerId, String sellerId) {
        this.id = id;
        this.products = products;
        this.amount = amount;
        this.voucher = voucher;
        this.customerId = customerId;
        this.sellerId = sellerId;

        this.status = "preparing";

        createdDate = LocalDateTime.now();

        float price = 0;
        for(int i = 0; i < products.size(); i++){
            price += products.get(i).getProductPrice()*amount.get(i);
        }
        this.totalPrice = price;


        int quantity = 0;
        for(int i: amount){
            quantity += i;
        }
        this.totalProductQuantity = quantity;
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
