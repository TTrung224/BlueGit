package com.example.bluegit.model;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;

public class Seller {
    private int numCompleted;
    private int numDelivery;
    private int numConfirmed;
    private int numPending;

    private String id;
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

    public Seller(String id, ArrayList<Product> products, ArrayList<Integer> amount,
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
