package com.example.food4you.Models;

public class Location {

    private int id;
    private String location;
    private double latitude;
    private double longitude;
    public Location() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoc() {
        return location;
    }

    public void setLoc(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return location;
    }
}
