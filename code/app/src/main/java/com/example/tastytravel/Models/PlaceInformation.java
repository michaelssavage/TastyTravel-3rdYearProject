package com.example.tastytravel.Models;

public class PlaceInformation {

    private String placeName;
    private String coordinates;

    public PlaceInformation() {

    }

    public PlaceInformation(String head, String coordinates) {
        this.placeName = placeName
                .replace(".", "")
                .replace("#", "")
                .replace("$", "")
                .replace("[", "")
                .replace("]", "");
        this.coordinates = coordinates;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getCoordinates() {
        return coordinates;
    }
}