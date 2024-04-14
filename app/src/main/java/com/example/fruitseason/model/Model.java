package com.example.fruitseason.model;

import java.io.Serializable;

public class Model implements Serializable {
    private String name,email,phone,address, city, province;
    private String image;

    public Model(String n,String ph, String a, String c, String p){
        name = n;
        phone = ph;
        address= a;
        city= c;
        province= p;
    }

    public Model() {
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
