package com.example.virtualwearingclothes;

import java.util.ArrayList;

public class product {
    private String productName;
    private String productPrice;
    private String productInfo;
    private String productId;
    private ArrayList<ProductDesc>  productdesc;


    public product() {
    }

    public product(String productId,String productName, String productPrice, String productInfo ) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productInfo = productInfo;
        this.productdesc=null;
    }

    public String getproductId() {
        return productId;
    }

    public String getproductName() {
        return productName;
    }

    public String getproductPrice() {
        return productPrice;
    }

    public String getproductInfo() {
        return productInfo;
    }

    public ArrayList<ProductDesc> getproductdesc() {
        return productdesc;
    }


    public void setproductId(String productId ) {
        this.productId  = productId ;
    }

    public void setproductName(String productName) {
        this.productName = productName;
    }

    public void setproductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setproductInfo(String productInfo) {
        this.productInfo= productInfo;
    }
    public void setproductdesc(ArrayList<ProductDesc>productdesc) {
        this.productdesc=productdesc;
    }
}