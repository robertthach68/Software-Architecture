package com.example.productservice.model;

public class Product {
    private String productNumber;
    private String name;
    private int stockQuantity;

    public Product() {
    }

    public Product(String productNumber, String name, int stockQuantity) {
        this.productNumber = productNumber;
        this.name = name;
        this.stockQuantity = stockQuantity;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}