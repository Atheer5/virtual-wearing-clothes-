package com.example.virtualwearingclothes;

public class ShowOrder {
    private String orederprice;
    private String ordersize;
    private String customerName;
    private String orderImage;
    private String customerNumber;
    private String orderid;
    private String orderdate;
    private String status;



    public ShowOrder(String status,String orederprice, String ordersize, String customerName, String orderImage, String customerNumber, String orderid, String orderdate) {
        this.status = status;
        this.orederprice = orederprice;
        this.ordersize = ordersize;
        this.customerName = customerName;
        this.orderImage = orderImage;
        this.customerNumber = customerNumber;
        this.orderid = orderid;
        this.orderdate = orderdate;
    }

    public ShowOrder(String orederprice, String ordersize, String customerName, String orderImage, String customerNumber, String orderid) {
        this.orederprice = orederprice;
        this.ordersize = ordersize;
        this.customerName = customerName;
        this.orderImage = orderImage;
        this.customerNumber = customerNumber;
        this.orderid=orderid;
    }

    public ShowOrder() {
    }

    public void setorderid(String orderid) {
        this.orderid = orderid;
    }
    public void setOrederprice(String orederprice) {
        this.orederprice = orederprice;
    }

    public void setOrdersize(String ordersize) {
        this.ordersize = ordersize;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setOrderImage(String orderImage) {
        this.orderImage = orderImage;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getOrderdate() { return orderdate;}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderdate(String orderdate) {this.orderdate = orderdate;}


    public String getOrederprice() {
        return orederprice;
    }

    public String getOrdersize() {
        return ordersize;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getOrderImage() {
        return orderImage;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getOrderid() {
        return orderid;
    }
}
