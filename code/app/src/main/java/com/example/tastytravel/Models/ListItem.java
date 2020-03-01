package com.example.tastytravel.Models;

public class ListItem {

    private String head;
    private String coordinates;

    public ListItem(String head, String coordinates) {
        this.head = head;
        this.coordinates = coordinates;
    }

    public String getHead() {
        return head;
    }

    public String getCoordinates() {
        return coordinates;
    }
}