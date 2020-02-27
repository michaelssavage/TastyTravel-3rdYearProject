package com.example.tastytravel;

public class PlaceInformation {

    public String userID;
    public String placeName;
    public double longitude;
    public double latitude;

    public PlaceInformation() {

    }

    public PlaceInformation(String userID, String placeName, double longitude, double latitude) {
        this.userID = userID;
        this.placeName = placeName;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
