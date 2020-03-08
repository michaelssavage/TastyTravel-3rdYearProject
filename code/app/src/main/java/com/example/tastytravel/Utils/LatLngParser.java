package com.example.tastytravel.Utils;

import com.google.android.gms.maps.model.LatLng;

public class LatLngParser {

    public static LatLng stringToLatLng(String strLocation){
        String[] latlong = strLocation.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        return new LatLng(latitude, longitude);
    }
}
