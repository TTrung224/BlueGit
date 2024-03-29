package com.example.bluegit.model;

import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;

public class Product {
    private String productId;
    private String productName;
    private int productPrice;
    private String imageSource;
    private String description;
    private String specification;
    private int quantity;
    private boolean disabled = false;
    private DocumentReference sellerId;

    public Product() {}

    public Product(String id, String productName, String description, String specification,
                   int productPrice, String imageSource, int quantity, DocumentReference sellerId) {
        this.productId = id;
        this.productName = productName;
        this.description = description;
        this.specification = specification;
        this.productPrice = productPrice;
        this.imageSource = imageSource;
        this.quantity = quantity;
        this.sellerId = sellerId;
    }



    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
    public String getImageSource() {
        return this.imageSource;
    }
    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DocumentReference getSellerId() {
        return sellerId;
    }

    public void setSellerId(DocumentReference sellerId) {
        this.sellerId = sellerId;
    }
}
