package com.example.tastytravel.Activities;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tastytravel.R;
import com.example.tastytravel.Utils.JsonParser;
import com.example.tastytravel.Models.PlaceInformation;
import com.example.tastytravel.Adapters.RecyclerViewAdapter;
import com.example.tastytravel.Utils.MapsWorker;
import com.example.tastytravel.Utils.UrlBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.maps.android.SphericalUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RecyclerViewAdapter.OnPlaceListener {

    public static final String DATA = "DATA";
    public static final String LOCATIONS_TAG = "LOCATIONS";
    public static final String RADIO_BUTTON1 = "RADIO_BUTTON1";
    public static final String RADIO_BUTTON2 = "RADIO_BUTTON2";
    public static final String PLACE_TYPE_TAG = "PLACE_TYPE";
    public static final String ISOCHRONE_PREF = "isochroneSwitch";
    public static final String MIDPOINT_PREF = "midpointSwitch";

    private RecyclerView recyclerView;
    private String placeType;
    // ArrayList to store both user locations
    private ArrayList<Place> mPlaces;

    // Instance of our map
    private GoogleMap googleMap;
    Marker currentMarker = null;

    // Integers to get the midpoint later
    private double midLong = 0.0;
    private double midLat = 0.0;
    private ArrayList<LatLng> points = new ArrayList<>();

    RecyclerView.Adapter adapter;
    private List<PlaceInformation> listItems;

    // radio buttons are walk, car, bike
    String myButtonSelection;
    String theirButtonSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        recyclerView = findViewById(R.id.resultsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Get objects passed
        Bundle data = getIntent().getBundleExtra(DATA);

        Intent parameters = getIntent();
        myButtonSelection = parameters.getStringExtra(RADIO_BUTTON1);
        theirButtonSelection = parameters.getStringExtra(RADIO_BUTTON2);

        placeType = parameters.getStringExtra(PLACE_TYPE_TAG);

        if (data != null) {
            mPlaces = (ArrayList<Place>) data.getSerializable(LOCATIONS_TAG);
        }
        Log.e("mplaces", "" + mPlaces);
        // Adding the 2 locations on the map
        final LatLng getYourLocationLatLng = mPlaces.get(0).getLatLng();
        final LatLng getTheirLocationLatLng = mPlaces.get(1).getLatLng();

        // Build a API url based on passed parameters
        assert myButtonSelection != null;
        assert getYourLocationLatLng != null;
        String myUrl = UrlBuilder.getMapboxUrl(myButtonSelection, new LatLng(getYourLocationLatLng.longitude, getYourLocationLatLng.latitude));

        assert theirButtonSelection != null;
        assert getTheirLocationLatLng != null;
        String theirUrl = UrlBuilder.getMapboxUrl(theirButtonSelection, new LatLng(getTheirLocationLatLng.longitude, getTheirLocationLatLng.latitude));

        // shared preferences created in settings.
        SharedPreferences myPrefs = getSharedPreferences("myPref", 0);
        final Boolean isochroneSwitcher = myPrefs.getBoolean(ISOCHRONE_PREF, false);
        final Boolean midpointSwitcher = myPrefs.getBoolean(MIDPOINT_PREF, false);

        // Async url request
        // Volley library used to reduce typing of boiler plate code
        // Retrieve geojson data for user 1
        JsonObjectRequest geojson1 = new JsonObjectRequest
                (Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MapsWorker.addJsonLayer(googleMap, response,isochroneSwitcher);
                        try {
                            plotMidpoint(response, getTheirLocationLatLng, midpointSwitcher);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                        MapsWorker.addJsonLayer(googleMap, response, isochroneSwitcher);
                        try {
                            plotMidpoint(response, getYourLocationLatLng, midpointSwitcher);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    public void onMapReady(final GoogleMap mMap) {
        googleMap = mMap;
        MapsWorker.addMarkers(mPlaces, mMap);
    }

    //if back button pressed, start new Search Activity
    @Override
    public void onBackPressed() {
        finish();
        Intent mainScreen = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(mainScreen);
    }

    @Override
    public void OnPlaceClick(int position) {
        Log.d("onPlaceClicked","" + listItems.get(position).getPlaceName());

        String selectedPlaceName = listItems.get(position).getPlaceName();
        String selectedPlaceCoordinates = listItems.get(position).getCoordinates();

        String[] latlong =  selectedPlaceCoordinates.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        LatLng markerPos = new LatLng(latitude, longitude);
        if(currentMarker != null){
            currentMarker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(markerPos)
                .title(selectedPlaceName);

        currentMarker = googleMap.addMarker(markerOptions);
        currentMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPos, 15));

    }

    private double getRadioButtonScore(String radio1, String radio2){

        double score = 0.5;

        switch (radio1) {
            case "Walk":
                // you walk + they drive
                if (radio2.equals("Car")) {
                    score = 0.2;
                }
                //you walk + they bike
                else if (radio2.equals("Bike")) {
                    score = 0.3;
                }
                break;

            case "Bike":
                //you bike + they drive
                if (radio2.equals("Car")) {
                    score = 0.4;
                }
                //you bike + they walk
                else if (radio2.equals("Walk")) {
                    score = 0.7;
                }
                break;

            case "Car":
                //you drive + they bike
                if (radio2.equals("Bike")) {
                    score = 0.6;
                }
                // you drive = they walk
                else if (radio2.equals("Walk")) {
                    score = 0.8;
                }
                break;
        }

        //if they are the same = 0.5
        Log.d("radioscore", "" + score);
        return score;
    }

    // Try and move this to maps worker
    private void plotMidpoint(JSONObject response, LatLng location, Boolean midPointMarkerOn) throws JSONException {
        JsonParser jsonParser = new JsonParser();
        ArrayList<String> coordinatesList = jsonParser.getCoordinates(response);
        getMidpoints(coordinatesList, location);

        Log.d("midpoints", "" + midLong + " + " + midLat);
        Log.d("midpoints", "" + midLong + " + " + midLat);

        //Add the points from each midpoint to the array.
        LatLng midpoint = new LatLng(midLong, midLat);
        points.add(midpoint);

        Log.d("midpoints", "" + midLong + " + " + midLat);
        if(points.size() == 2){

            double score = getRadioButtonScore(myButtonSelection, theirButtonSelection);
            LatLng bestMidpoint = SphericalUtil.interpolate(points.get(0), points.get(1), score);

            if(midPointMarkerOn) {
                googleMap.addMarker(new MarkerOptions()
                        .position(bestMidpoint)
                        .title("Best point between two")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }

            String googleMapsUrl = UrlBuilder.getGooglePlacesUrl(placeType, bestMidpoint);

            JsonObjectRequest googleGeojsonPlaces = new JsonObjectRequest
                    (Request.Method.GET, googleMapsUrl, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JsonParser jsonParser1 = new JsonParser();
                            try {
                                LinkedHashMap<String,String> placeList = jsonParser1.getPlaces(response);
                                if(placeList.size() == 0){
                                    Toast.makeText(MapsActivity.this,
                                            "There are no Results. Try some different parameters.", Toast.LENGTH_LONG).show();
                                }
                                displayResults(placeList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
            requestQueue.add(googleGeojsonPlaces);
        }
    }

    private void displayResults(LinkedHashMap<String,String> placeList) {
        for (Map.Entry<String, String> entry : placeList.entrySet()) {
            String name = entry.getKey();
            String coordinates = entry.getValue();

           PlaceInformation listItem = new PlaceInformation(name, coordinates);
           listItems.add(listItem);

            // Recycler View Adapter Initialisation
            adapter = new RecyclerViewAdapter(listItems, this);
            recyclerView.setAdapter(adapter);
        }
    }

    public void getMidpoints(ArrayList<String> coordinateList, LatLng location){
        // find the smallest distance from the coordinates to the opposite point

        float[] distance1 = new float[2];
        float[] distance2 = new float[2];
        String[] closestPoint = coordinateList.get(0).split(",");
        double longitude = Double.parseDouble(closestPoint[0]);
        double latitude = Double.parseDouble(closestPoint[1]);

        // get initial distance with first coordinates and then compare the rest
        Location.distanceBetween(latitude, longitude, location.latitude, location.longitude, distance1);

        for(int i = 1; i < coordinateList.size() ; i++) {

            String[] point = coordinateList.get(i).split(",");
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
        try {
            midLong = Double.parseDouble(closestPoint[1]);
            midLat = Double.parseDouble(closestPoint[0]);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}