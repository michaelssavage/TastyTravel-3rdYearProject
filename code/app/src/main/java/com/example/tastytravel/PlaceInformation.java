package com.example.tastytravel;

public class PlaceInformation {

    public String placeName;
    public double longitude;
    public double latitude;

    public PlaceInformation() {

    }

    public PlaceInformation(String placeName, double longitude, double latitude) {
        this.placeName = placeName;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
