package com.example.bluegit.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Order {
    private String id;
    private ArrayList<String> productIDs;
    private ArrayList<Integer> amount;
    private String voucher;
    private String address;
    private String status;
    private Timestamp createdDate;
    private int totalPrice;

    // TODO: reconsider data type
    private DocumentReference customerId;
    private DocumentReference sellerId;

    public Order(String id, ArrayList<String> products, ArrayList<Integer> amount, int totalPrice,
                 String voucher, DocumentReference customerId, DocumentReference sellerId) {
        this.id = id;
        this.productIDs = products;
        this.amount = amount;
        this.totalPrice = totalPrice;
        this.voucher = voucher;
        this.customerId = customerId;
        this.status = "pending";
        this.sellerId = sellerId;
        createdDate = Timestamp.now();

    }

    public ArrayList<String> getProductIDs() {
        return productIDs;
    }

    public void setProductIDs(ArrayList<String> productIDs) {
        this.productIDs = productIDs;
    }

    public DocumentReference getCustomerId() {
        return customerId;
    }

    public void setCustomerId(DocumentReference customerId) {
        this.customerId = customerId;
    }

    public DocumentReference getSellerId() {
        return sellerId;
    }

    public void setSellerId(DocumentReference sellerId) {
        this.sellerId = sellerId;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalPrice(){
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Integer> getAmount() {
        return amount;
    }

    public void setAmount(ArrayList<Integer> amount) {
        this.amount = amount;
    }

    public int getProductQuantity(){
        return productIDs.size();
    }
}

