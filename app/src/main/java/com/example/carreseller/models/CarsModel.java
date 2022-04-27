package com.example.carreseller.models;

public class CarsModel {

    private String id;
    private String carName;
    private String company;
    private String launchedYear;
    private String fuelType;
    private String engine;
    private String seatingCapacity;
    private String price;
    private String carImage;
    private String dealerId;
    private String category;

    public CarsModel() {
    }

    public CarsModel(String id, String carName, String company, String launchedYear, String fuelType, String engine, String seatingCapacity, String price, String carImage, String dealerId, String category) {
        this.id = id;
        this.carName = carName;
        this.company = company;
        this.launchedYear = launchedYear;
        this.fuelType = fuelType;
        this.engine = engine;
        this.seatingCapacity = seatingCapacity;
        this.price = price;
        this.carImage = carImage;
        this.dealerId = dealerId;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLaunchedYear() {
        return launchedYear;
    }

    public void setLaunchedYear(String launchedYear) {
        this.launchedYear = launchedYear;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getSeatingCapacity() {
        return seatingCapacity;
    }

    public void setSeatingCapacity(String seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
