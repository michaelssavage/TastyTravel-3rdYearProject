package com.example.tastytravel;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.data.geojson.GeoJsonLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String DATA = "DATA";
    public static final String LOCATIONS_TAG = "LOCATIONS";
    public static final String RADIO_BUTTON1 = "RADIO_BUTTON1";
    public static final String RADIO_BUTTON2 = "RADIO_BUTTON2";

    // ArrayList to store both user locations
    private ArrayList<Place> mPlaces;

    // Instance of our map
    private GoogleMap googleMap;
    private JSONObject response;

    // Integers to get the midpoint later
    private Double midLong = 0.0;
    private Double midLat = 0.0;
    private ArrayList<LatLng> points = new ArrayList<>();

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

        Intent buttons = getIntent();
        String myButtonSelection = buttons.getStringExtra(RADIO_BUTTON1);
        String theirButtonSelection = buttons.getStringExtra(RADIO_BUTTON2);


        if (data != null) {
            mPlaces = (ArrayList<Place>) data.getSerializable(LOCATIONS_TAG);
        }

        // Checking values using Logging
        Log.d(LOCATIONS_TAG, String.valueOf(mPlaces));


        // Adding the 2 locations on the map
        final LatLng getYourLocationLatLng = mPlaces.get(0).getLatLng();
        final LatLng getTheirLocationLatLng = mPlaces.get(1).getLatLng();

        // Build a API url based on passed parameters
        String myUrl = Url_Builder.getMapboxUrl(myButtonSelection, new LatLng(getYourLocationLatLng.longitude, getYourLocationLatLng.latitude));
        String theirUrl = Url_Builder.getMapboxUrl(theirButtonSelection, new LatLng(getTheirLocationLatLng.longitude, getTheirLocationLatLng.latitude));

        // Async url request
        // Volley library used to reduce typing of boiler plate code

        // Retrieve geojson data for user 1
        JsonObjectRequest geojson1 = new JsonObjectRequest
                (Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        addJsonLayer(response);
                        String one = "one";
                        plotMidpoint(response, getTheirLocationLatLng, one);
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
                        String two = "two";
                        plotMidpoint(response, getYourLocationLatLng, two);
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

    private void plotMidpoint(JSONObject response, LatLng location, String number) {
        ArrayList<String> coordinatesList = getCoordinates(response);
        getMidpoints(coordinatesList, location);

        Log.d("midpoints", "" + midLong + " + " + midLat);
        //is used already
        if (number == "one"){
            LatLng midpoint1 = new LatLng(midLong, midLat);
            points.add(midpoint1);
        }
        else{
            LatLng midpoint2 = new LatLng(midLong, midLat);
            points.add(midpoint2);
        }
        if(points.size() == 2){
            LatLng point = SphericalUtil.interpolate(points.get(0), points.get(1), 0.5);
            googleMap.addMarker(new MarkerOptions().position(point).title("Best point between two"));
        }
    }

    public ArrayList<String> getCoordinates(JSONObject response) {

        // isolate coordinates from JSONObject into an arraylist
        ArrayList<String> coordinateList = new ArrayList<>();
        try {
            JSONArray features = response.getJSONArray("features");
            JSONObject obj = features.getJSONObject(0);
            JSONObject geometry = obj.getJSONObject("geometry");
            JSONArray array = geometry.getJSONArray("coordinates");

            // coordinates is the JSON list in format like [[1.0,2.0], [3.0,4.0], [ etc...
            JSONArray coordinates = array.getJSONArray(0);
            for (int i = 0; i < coordinates.length(); i++) {
                String point = coordinates.getString(i);

                //remove the '[' and ']' and add to the coordinate list.
                coordinateList.add(point.substring(1, point.length() - 1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("coordinates", " " + coordinateList);
        return coordinateList;

    }
    public void getMidpoints(ArrayList<String> coordinateList, LatLng location){
        // find the smallest distance from the coordinates to the opposite point
        String[] closestPoint = new String[2];
        float[] distance1 = new float[2];
        float[] distance2 = new float[2];
        String[] point = coordinateList.get(0).split(",");
        Double longitude = Double.parseDouble(point[0]);
        Double latitude = Double.parseDouble(point[1]);

        // get initial distance with first coordinates and then compare the rest
        Location.distanceBetween(latitude, longitude, location.latitude, location.longitude, distance1);

        for(int i = 1; i < coordinateList.size() ; i++) {

            point = coordinateList.get(i).split(",");
            longitude = Double.parseDouble(point[0]);
            latitude = Double.parseDouble(point[1]);
            Location.distanceBetween(latitude, longitude, location.latitude, location.longitude, distance2);

            // if distance is more, then update with smaller distance.
            if (distance1[0] > distance2[0]) {
                distance1[0] = distance2[0];
                closestPoint = point;
            }
        }
        // Update the coordinates that are closest to the opposite point.
        midLong = Double.parseDouble(closestPoint[1]);
        midLat = Double.parseDouble(closestPoint[0]);
    }
}
