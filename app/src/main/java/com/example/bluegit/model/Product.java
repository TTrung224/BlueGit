package com.example.bluegit.model;

public class Product {
    private String productName;
    private float productPrice;
    private String imageSource;

    public Product(String productName, float productPrice, String imageSource) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.imageSource = imageSource;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }
}
