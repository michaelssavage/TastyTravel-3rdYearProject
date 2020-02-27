package com.example.tastytravel.Utils;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;

public class Url_Builder {

    private static final String TAG = Url_Builder.class.getSimpleName();

    // Mapbox url variables
    private static final String MAPBOX_MAPS_API = "https://api.mapbox.com/isochrone/v1/mapbox/";
    private static final String AND_CONTOURS_MINUTES = "?contours_minutes=10";
    private static final String AND_CONTOURS_COLORS = "&contours_colors=009688";
    private static final String AND_POLYGONS_TRUE = "&polygons=true";
    private static final String AND_ACCESS_TOKEN = "&access_token=";
    private static final String ACCESS_TOKEN = "pk.eyJ1Ijoiam9obmRvd2F0ZXIiLCJhIjoiY2szcWNjdHIyMDA3cDNlcGlseWt3cjRiNiJ9.Bu2jIzXSGZNcxQBtGCrwbQ";

    // https://api.mapbox.com/isochrone/v1/mapbox/
    // walking, driving, cycling/
    // -6.2767,53.4070
    // ?contours_minutes=5,10,15
    // &contours_colors=6706ce,04e813,4286f4
    // &polygons=true
    // &access_token=
    // pk.eyJ1Ijoiam9obmRvd2F0ZXIiLCJhIjoiY2szcWNjdHIyMDA3cDNlcGlseWt3cjRiNiJ9.Bu2jIzXSGZNcxQBtGCrwbQ;

    public static String getMapboxUrl(String transportMethod, LatLng latLng){
        StringBuilder url = new StringBuilder(MAPBOX_MAPS_API);

        // Get string value from radio button
        url.append(transportMethod.replace("Car", "driving/").replace("Walk","walking/").replace("Bike","cycling/"));
        url.append(latLng.toString().substring(9)
                .replace("(", "")
                .replace(")", ""));

        // 5, 10, 15
        url.append(AND_CONTOURS_MINUTES);
        url.append(AND_CONTOURS_COLORS);
        url.append(AND_POLYGONS_TRUE);
        url.append(AND_ACCESS_TOKEN);
        url.append(ACCESS_TOKEN);

        Log.d(TAG + "Mapbox", String.valueOf(url));

        return String.valueOf(url);
    }

    // Google maps url variables
    private static final String GOOGLE_MAPS_API = "https://maps.googleapis.com/maps/api/";
    private static final String PLACE_NEARBYSEARCH = "place/nearbysearch/";
    private static final String JSON = "json";
    private static final String LOCATION_EQUALS = "?location=";
    private static final String RANK_BY_DISTANCE = "&rankby=distance";
    private static final String TYPE_EQUALS = "&type=";
    private static final String KEY = "&key=AIzaSyChmDeOaON5gqRFAR3o27HHKaojDenZ0ps";
    private static final String BAR = "|";

    // https://maps.googleapis.com/maps/api/place/nearbysearch/json
    // ?location= 53.386841,-6.256248
    // &rankby=distance
    // &type=bar
    // &key=AIzaSyChmDeOaON5gqRFAR3o27HHKaojDenZ0ps

    public static String getGooglePlacesUrl(String placeType, LatLng latLng) {
        StringBuilder url = new StringBuilder(GOOGLE_MAPS_API + PLACE_NEARBYSEARCH + JSON + LOCATION_EQUALS);

        // Coordinates next in form Long lat
        url.append(latLng.toString().substring(9)
                .replace("(", "")
                .replace(")", ""));
        url.append(RANK_BY_DISTANCE);
        url.append(TYPE_EQUALS);

        // Get place type from dropdown
        url.append(placeType
                .replace("Bar", "bar")
                .replace("Restaurant", "restaurant")
                .replace("Cafe", "cafe"));

        url.append(KEY);

        Log.d(TAG + "Google", String.valueOf(url));

        return String.valueOf(url);
    }
}
