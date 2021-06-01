package com.example.virtualwearingclothes;


import java.util.Date;

public class Order {
    private String orderid;
    //private String orederprice;
    //private String ordersize;
    private String customerid;
    private String productid;
    private String descriptionid;
    private int isdileverd;
    private String orderdate;

    public Order() {
    }


    public Order(String orderid,  String customerid, String productid, String descriptionid, String orderdate) {
        this.orderid = orderid;
        this.customerid = customerid;
        this.productid = productid;
        this.descriptionid = descriptionid;
        this.orderdate = orderdate;
    }



    public Order(String orderid, String customerid, String productid, String descriptionid, int isdileverd, String orderdate) {
        this.orderid = orderid;
        this.customerid = customerid;
        this.productid = productid;
        this.descriptionid = descriptionid;
        this.isdileverd = isdileverd;
        this.orderdate = orderdate;
    }

    public String getOrderid() {
        return orderid;
    }


    public String getCustomerid() {
        return customerid;
    }

    public String getProductid() {
        return productid;
    }

    public String getDescriptionid() {
        return descriptionid;
    }

    public int getIsdileverd() {
        return isdileverd;
    }

    public String getOrderdate() {
        return orderdate;
    }



    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }


    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public void setDescriptionid(String descriptionid) {
        this.descriptionid = descriptionid;
    }

    public void setIsdileverd(int isdileverd) {
        this.isdileverd = isdileverd;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }
}
