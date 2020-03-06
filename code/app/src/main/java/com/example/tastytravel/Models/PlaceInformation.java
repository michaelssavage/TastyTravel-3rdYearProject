package com.example.tastytravel.Models;

public class PlaceInformation {

    public String placeName;
    public String coordinates;

    public PlaceInformation() {

    }

    public PlaceInformation(String placeName, String coordinates) {
        this.placeName = placeName;
        this.coordinates = coordinates;
    }
}
