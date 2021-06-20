package com.example.virtualwearingclothes;

public class LocationData {
    double x;
    double y;
    double distance;
    String name;
    String phnum;
    String address;


    public LocationData(double distance, String name, String phnum, String address) {
        this.distance = distance;
        this.name = name;
        this.phnum = phnum;
        this.address = address;
    }

    public LocationData(double x, double y)
    { this.x = x;
        this.y = y; }

    public LocationData(double x, double y, String  name)
    {
        this.x = x;
        this.y = y;
        this.name = name;
    }
    public LocationData(double distance, String name)
    {
        this.distance = distance;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public double getDistance() {
        return distance;
    }

    public String toString(){
        return "[" + getDistance() + "," + getName() + "]";
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getPhnum() {
        return phnum;
    }

    public String getAddress() {
        return address;
    }
}
