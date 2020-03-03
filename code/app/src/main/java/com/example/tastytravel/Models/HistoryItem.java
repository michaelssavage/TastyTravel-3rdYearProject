package com.example.tastytravel.Models;

public class HistoryItem {

    private String head;
    private String accessDate;
    public String coordinates;

    public HistoryItem(){

    }

    public HistoryItem(String head, String coordinates, String accessDate) {
        this.head = head
                .replace(".", "")
                .replace("#", "")
                .replace("$", "")
                .replace("[", "")
                .replace("]", "");
        this.accessDate = accessDate;
        this.coordinates = coordinates;
    }

    public String getHead() {
        return head;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getAccessDate() {
        return accessDate;
    }


}