package com.example.tastytravel;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import org.json.JSONObject;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String DATA = "DATA";
    public static final String LOCATIONS_TAG = "LOCATIONS";
    public static final String RADIO_BUTTONS = "RADIO_BUTTONS";

    // ArrayList to store both user locations
    private ArrayList<Place> mPlaces;
    private ArrayList<String> radioButtons;

    // Instance of our map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Get objects passed
        Bundle data = getIntent().getBundleExtra(DATA);

        if (data != null) {
            mPlaces = (ArrayList<Place>) data.getSerializable(LOCATIONS_TAG);
            radioButtons = (ArrayList<String>) data.getSerializable(RADIO_BUTTONS);
        }

        // Checking values using Logging
        Log.d(LOCATIONS_TAG, String.valueOf(mPlaces));
        Log.d(RADIO_BUTTONS, String.valueOf(radioButtons));


        // Adding the 2 locations on the map
        final LatLng getYourLocationLatLng = mPlaces.get(0).getLatLng();
        final LatLng getTheirLocationLatLng = mPlaces.get(1).getLatLng();

        // Build a API url based on passed parameters
        String myUrl = Url_Builder.getMapboxUrl(new LatLng(getYourLocationLatLng.longitude, getYourLocationLatLng.latitude));
        String theirUrl = Url_Builder.getMapboxUrl(new LatLng(getTheirLocationLatLng.longitude, getTheirLocationLatLng.latitude));

        // Async url request
        // Volley library used to reduce typing of boiler plate code

        // Retrieve geojson data for user 1
        JsonObjectRequest geojson1 = new JsonObjectRequest
            (Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    addJsonLayer(response);
                }
            },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error Response", error.toString());
                    }
                }
            );

        // Retrieve geojson data for user 2
        JsonObjectRequest geojson2 = new JsonObjectRequest
            (Request.Method.GET, theirUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    addJsonLayer(response);
                }
            },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Error Response", error.toString());
                    }
                }
            );

        // A queue of url requests, add both requests
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(geojson1);
        requestQueue.add(geojson2);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        addMarkers();
    }

    // Adding the 2 locations on the map
    private void addMarkers() {
        final LatLng getYourLocationLatLng = mPlaces.get(0).getLatLng();
        final LatLng getTheirLocationLatLng = mPlaces.get(1).getLatLng();

        // Add a marker and move the camera
        LatLng yourLocationLatLng = new LatLng(getYourLocationLatLng.latitude, getYourLocationLatLng.longitude);
        LatLng theirLocationLatLng = new LatLng(getTheirLocationLatLng.latitude, getTheirLocationLatLng.longitude);

        googleMap.addMarker(new MarkerOptions().position(yourLocationLatLng).title("Your Location"));
        googleMap.addMarker(new MarkerOptions().position(theirLocationLatLng).title("Their Location"));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(yourLocationLatLng, 12f));
    }

    private void addJsonLayer(JSONObject response) {
        GeoJsonLayer layer = new GeoJsonLayer(googleMap, response);
        layer.addLayerToMap();
    }

}
