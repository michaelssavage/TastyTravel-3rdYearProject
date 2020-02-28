package com.example.tastytravel.Utils;

import com.google.android.gms.maps.model.LatLng;

public class ListItem {

    private String head;
    private LatLng coordinates;

    public ListItem(String head) {

        this.head = head;
    }

    public String getHead() {

        return head;
    }
    public LatLng getCoordinates(){
        return coordinates;
    }
}
