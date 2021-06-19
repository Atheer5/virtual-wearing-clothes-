package com.example.virtualwearingclothes;

public class Factories {
    private String factoryId;
    private String name;
    private String phoneNumber;
    private String password;
    private String address;
    private double x;
    private double y;

    public Factories() {
    }

    public Factories(String customerId, String name, String phoneNumber, String password, String address) {
        this.factoryId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
    }

    public Factories(String factoryId, String name, String phoneNumber, String password, String address, double x, double y) {
        this.factoryId = factoryId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
        this.x = x;
        this.y = y;
    }

    public double getX() { return x;}

    public double getY() { return y; }

    public String getFactoryId() {
        return factoryId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }


    public void setX(double x) {this.x = x;}

    public void setY(double y) {this.y = y;}

    public void setCustomerId(String factoryId) {
        this.factoryId= factoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(String address) {
        this.address = address;

    }
}//class





