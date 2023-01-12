package com.example.bluegit.model;

import com.google.firebase.firestore.DocumentReference;

import org.threeten.bp.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private String id;
    private ArrayList<Product> products;
    private ArrayList<Integer> amount;
    private String voucher;
    private String address;
    private String status;
    private LocalDateTime createdDate;

    // TODO: reconsider data type
    private DocumentReference customerId;
    private DocumentReference sellerId;

    public Order(String id, ArrayList<Product> products, ArrayList<Integer> amount,
                 String voucher, DocumentReference customerId, DocumentReference sellerId) {
        this.id = id;
        this.products = products;
        this.amount = amount;
        this.voucher = voucher;
        this.customerId = customerId;
        this.status = "pending";
        this.sellerId = sellerId;

    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
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
        int total = 0;
        for(Product product : products){
            total += product.getProductPrice();
        }
        return total;
    }

    public ArrayList<Integer> getAmount() {
        return amount;
    }

    public void setAmount(ArrayList<Integer> amount) {
        this.amount = amount;
    }

    public int getProductQuantity(){
        return products.size();
    }
}

