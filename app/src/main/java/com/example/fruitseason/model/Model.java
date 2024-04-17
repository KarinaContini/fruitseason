package com.example.fruitseason.model;

import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Model implements Serializable {
    private String name,phone,address, city, province;
    @Nullable private String image;
    private List<SellerFruit> fruits = new ArrayList<>();

    public Model(String n,String ph, String a, String c, String p, @Nullable String i){
        name = n;
        phone = ph;
        address= a;
        city= c;
        province= p;
        image = i;
    }


    public List<SellerFruit> getFruits() {
        return fruits;
    }

    public void setFruits(List<SellerFruit> fruits) {
        this.fruits = fruits;
    }

    @Exclude
    public String getFullAddress(){
        //"Av. República do Líbano, 1291 - Parque Ibirapuera, São Paulo - SP, Brazil"
        return address + ", "+ city + " - " + province;

    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
