package com.example.fruitseason.model;


import java.io.Serializable;

public class SellerFruit implements Serializable {
    private String fruitId;
    private String name;
    private String price;

    public SellerFruit() {


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFruitId() {
        return fruitId;
    }

    public void setFruitId(String fruitId) {
        this.fruitId = fruitId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
