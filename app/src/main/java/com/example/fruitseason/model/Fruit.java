package com.example.fruitseason.model;

import java.io.Serializable;

public class Fruit implements Serializable {

    private String fruitId;
    private String name;
    private String image;

    public Fruit() {
    }

    public String getFruitId() {
        return fruitId;
    }

    public void setFruitId(String fruitId) {
        this.fruitId = fruitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
