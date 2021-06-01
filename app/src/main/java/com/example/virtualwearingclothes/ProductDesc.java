package com.example.virtualwearingclothes;

public class ProductDesc {
    private String productSize;
    private String productQuantity;
    private String productimagePath;
    private String descId;


    public ProductDesc() {
    }


    public ProductDesc(String productSize, String productQuantity, String productimagePath, String descId) {
        this.productSize = productSize;
        this.productQuantity = productQuantity;
        this.productimagePath = productimagePath;
        this.descId = descId;
    }

    public String getproductSize() {
        return productSize;
    }

    public String getproductQuantity() {
        return productQuantity;
    }

    public String getproductimagePath() {
        return productimagePath;
    }

    public String getdescId() {
        return descId;
    }


    public void setproductSize(String productSize ) {
        this.productSize = productSize ;
    }

    public void setproductQuantity(String productQuantity) {
        this.productQuantity= productQuantity;
    }

    public void setproductimagePath(String productimagePath) {
        this.productimagePath = productimagePath;
    }

    public void setdescId(String descId) {
        this.descId= descId;
    }


}