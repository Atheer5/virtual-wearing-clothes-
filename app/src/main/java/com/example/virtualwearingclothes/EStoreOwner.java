package com.example.virtualwearingclothes;


import android.media.Image;

public class EStoreOwner {
    private String EStoreOwnerId;
    private String name;
    private String phoneNumber;
    private String password;
    private String address;
    private String ProfilePic;


    public EStoreOwner() {
    }

    public EStoreOwner(String eStoreOwnerId, String name, String phoneNumber, String password, String address ) {
        this.EStoreOwnerId = eStoreOwnerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;

    }

    public EStoreOwner(String eStoreOwnerId, String name, String phoneNumber, String password, String address, String pro,String profilePic) {
        this.EStoreOwnerId = eStoreOwnerId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
        this.ProfilePic = profilePic;

    }


    public String getEStoreOwnerId() {
        return EStoreOwnerId;
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

    public String getProfilePic() {
        return ProfilePic;
    }




    public void setEStoreOwnerId(String eStoreOwnerId) {
        this.EStoreOwnerId = eStoreOwnerId;
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

    public void setAddress(String address) {this.address = address; }

    public void setProfilePic(String profilePic) {
        this.ProfilePic = profilePic;
    }


}//end class
