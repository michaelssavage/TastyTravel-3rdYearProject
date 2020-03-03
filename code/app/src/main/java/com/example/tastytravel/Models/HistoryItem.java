package com.example.tastytravel.Models;

public class HistoryItem {

    private String placeName;
    private String accessDate;
    private String coordinates;

    public HistoryItem(){
    }

    public HistoryItem(String placeName, String coordinates, String accessDate) {
        this.placeName = placeName
                .replace(".", "")
                .replace("#", "")
                .replace("$", "")
                .replace("[", "")
                .replace("]", "");
        this.accessDate = accessDate;
        this.coordinates = coordinates;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getAccessDate() {
        return accessDate;
    }

}