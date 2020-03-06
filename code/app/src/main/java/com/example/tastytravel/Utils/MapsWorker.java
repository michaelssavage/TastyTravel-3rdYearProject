package com.example.tastytravel.Utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import org.json.JSONObject;
import java.util.ArrayList;

public class MapsWorker {

    // Adding the 2 locations on the map
    public static void addMarkers(ArrayList<Place> mPlaces, GoogleMap googleMap) {
        final LatLng getYourLocationLatLng = mPlaces.get(0).getLatLng();
        final LatLng getTheirLocationLatLng = mPlaces.get(1).getLatLng();

        // Add a marker and move the camera
        assert getYourLocationLatLng != null;
        LatLng yourLocationLatLng = new LatLng(getYourLocationLatLng.latitude, getYourLocationLatLng.longitude);
        assert getTheirLocationLatLng != null;
        LatLng theirLocationLatLng = new LatLng(getTheirLocationLatLng.latitude, getTheirLocationLatLng.longitude);

        googleMap.addMarker(new MarkerOptions().position(yourLocationLatLng).title("Your Location"));
        googleMap.addMarker(new MarkerOptions().position(theirLocationLatLng).title("Their Location"));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(yourLocationLatLng, 12f));
    }

    public static void addJsonLayer(GoogleMap googleMap, JSONObject response, Boolean isochroneOn) {
        if(isochroneOn) {
            GeoJsonLayer layer = new GeoJsonLayer(googleMap, response);
            layer.addLayerToMap();
        }
    }

}
