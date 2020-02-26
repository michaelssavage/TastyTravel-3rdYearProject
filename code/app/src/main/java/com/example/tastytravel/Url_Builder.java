package com.example.tastytravel;

import android.util.Log;
import com.google.android.gms.maps.model.LatLng;

public class Url_Builder {

    private static final String TAG = Url_Builder.class.getSimpleName();

    private static final String MAPS_API = "https://api.mapbox.com/isochrone/v1/mapbox/";
    private static final String AND_CONTOURS_MINUTES = "?contours_minutes=10";
    private static final String AND_CONTOURS_COLORS = "&contours_colors=009688";
    private static final String AND_POLYGONS_TRUE = "&polygons=true";
    private static final String AND_ACCESS_TOKEN = "&access_token=";
    private static final String ACCESS_TOKEN = "pk.eyJ1Ijoiam9obmRvd2F0ZXIiLCJhIjoiY2szcWNjdHIyMDA3cDNlcGlseWt3cjRiNiJ9.Bu2jIzXSGZNcxQBtGCrwbQ";

    // "https://api.mapbox.com/isochrone/v1/mapbox/
    // walking, driving, cycling/
    // -6.2767,53.4070
    // ?contours_minutes=5,10,15
    // &contours_colors=6706ce,04e813,4286f4
    // &polygons=true
    // &access_token=
    // pk.eyJ1Ijoiam9obmRvd2F0ZXIiLCJhIjoiY2szcWNjdHIyMDA3cDNlcGlseWt3cjRiNiJ9.Bu2jIzXSGZNcxQBtGCrwbQ";

    public static String getMapboxUrl(String transportMethod, LatLng latLng){
        StringBuilder url = new StringBuilder(MAPS_API);

        // Get string value from radio button
        url.append(transportMethod.replace("Car", "driving/").replace("Walk","walking/").replace("Bike","cycling/"));
        url.append(latLng.toString().substring(9).replace("(", "").replace(")", ""));

        // 5, 10, 15
        url.append(AND_CONTOURS_MINUTES);

        url.append(AND_CONTOURS_COLORS);
        url.append(AND_POLYGONS_TRUE);
        url.append(AND_ACCESS_TOKEN);
        url.append(ACCESS_TOKEN);

        Log.d(TAG + "Url", String.valueOf(url));

    return String.valueOf(url);
    }
}
