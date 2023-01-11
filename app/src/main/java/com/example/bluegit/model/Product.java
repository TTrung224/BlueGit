package com.example.bluegit.model;

import android.net.Uri;

public class Product {
    private String productName;
    private float productPrice;
    private String imageSource;
    private String description;
    private String specification;
    private int quantity;
    private boolean disabled = false;
    private String sellerId;

    public Product() {}

    public Product(String productName, String description, String specification,
                   float productPrice, String imageSource, int quantity, String sellerId) {
        this.productName = productName;
        this.description = description;
        this.specification = specification;
        this.productPrice = productPrice;
        this.imageSource = imageSource;
        this.quantity = quantity;
        this.sellerId = sellerId;
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

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
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

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
