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

    // User
    public User(String displayName, String email, Uri profileImageSrc) {
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

    // Account
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

    // Seller
    public int getNumCompleted() {
        return numCompleted;
    }

    public int getNumDelivery() {
        return numDelivery;
    }

    public int getNumConfirmed() {
        return numConfirmed;
    }

    public int getNumPending() {
        return numPending;
    }

    public void setNumCompleted(int numCompleted) {
        this.numCompleted = numCompleted;
    }

    public void setNumDelivery(int numDelivery) {
        this.numDelivery = numDelivery;
    }

    public void setNumConfirmed(int numConfirmed) {
        this.numConfirmed = numConfirmed;
    }

    public void setNumPending(int numPending) {
        this.numPending = numPending;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Integer> getAmount() {
        return amount;
    }

    public void setAmount(ArrayList<Integer> amount) {
        this.amount = amount;
    }

    public int getTotalProductQuantity() {
        return totalProductQuantity;
    }

    public void setTotalProductQuantity(int totalProductQuantity) {
        this.totalProductQuantity = totalProductQuantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

}
