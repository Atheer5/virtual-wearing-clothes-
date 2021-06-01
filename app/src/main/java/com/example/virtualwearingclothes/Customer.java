package com.example.virtualwearingclothes;

public class Customer {


  private String customerId;
  private String name;
  private String phoneNumber;
  private String password;
  private String address;


    public Customer() {
    }

    public Customer(String customerId, String name, String phoneNumber, String password, String address) {
        this.customerId = customerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
    }



    public String getCustomerId() {
        return customerId;
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


    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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
}//class Customer






