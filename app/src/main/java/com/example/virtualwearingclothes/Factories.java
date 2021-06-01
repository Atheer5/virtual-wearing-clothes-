package com.example.virtualwearingclothes;

public class Factories {
    private String factoryId;
    private String name;
    private String phoneNumber;
    private String password;
    private String address;


    public Factories() {
    }

    public Factories(String customerId, String name, String phoneNumber, String password, String address) {
        this.factoryId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
    }



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





