package com.example.carreseller.models;

public class UserModel {
    private String name;
    private String email;
    private String mobile;
    private String id;
    private String imageUrl;

    public UserModel() {
    }

    public UserModel(String name, String email, String mobile, String id, String imageUrl) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
