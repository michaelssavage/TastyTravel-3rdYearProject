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
import com.example.tastytravel.Models.ListItem;
import com.example.tastytravel.Adapters.RecyclerViewAdapter;
import com.example.tastytravel.Utils.LatLngParser;
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

    // Final strings used multiple times
    public static final String DATA = "DATA";
    public static final String LOCATIONS_TAG = "LOCATIONS";
    public static final String RADIO_BUTTON1 = "RADIO_BUTTON1";
    public static final String RADIO_BUTTON2 = "RADIO_BUTTON2";
    public static final String PLACE_TYPE_TAG = "PLACE_TYPE";
    public static final String ISOCHRONE_PREF = "isochroneSwitch";
    public static final String MIDPOINT_PREF = "midpointSwitch";

    // Recycler view for search results
    private RecyclerView recyclerView;
    // Recycler view control adapter
    RecyclerView.Adapter adapter;
    private List<ListItem> listItems;

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

    // Radio buttons are walk, car, bike for transport method
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

        // Get objects passed using DATA tag
        Bundle data = getIntent().getBundleExtra(DATA);

        // Get the rest of data passed
        // The transport buttons selection and meeting place type
        Intent parameters = getIntent();
        myButtonSelection = parameters.getStringExtra(RADIO_BUTTON1);
        theirButtonSelection = parameters.getStringExtra(RADIO_BUTTON2);

        placeType = parameters.getStringExtra(PLACE_TYPE_TAG);

        if (data != null) {
            // Get both places selected using the AutoCompleteFragment
            // Items are Place objects
            mPlaces = (ArrayList<Place>) data.getSerializable(LOCATIONS_TAG);
        }

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

    // if back button pressed, start new Search Activity
    @Override
    public void onBackPressed() {
        finish();
        Intent mainScreen = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(mainScreen);
    }

    // Action caused by place clicked in the recycler view
    @Override
    public void OnPlaceClick(int position) {
        String selectedPlaceName = listItems.get(position).getHead();
        String selectedPlaceCoordinates = listItems.get(position).getCoordinates();

        // Parse a LatLng from the listItem String
        LatLng markerPos = LatLngParser.stringToLatLng(selectedPlaceCoordinates);

        // Remove the old marker before adding a new marker
        if(currentMarker != null){
            currentMarker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(markerPos)
                .title(selectedPlaceName);

        currentMarker = googleMap.addMarker(markerOptions);
        currentMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        // Move the map focus
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPos, 15));
    }

    // Scoring algorithm that affects the positioning of the base locality marker
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

        // If they are the same the score is 0.5
        return score;
    }

    private void plotMidpoint(JSONObject response, LatLng location, Boolean midPointMarkerOn) throws JSONException {
        // Parse the coordinates from the JSONObject
        ArrayList<String> coordinatesList = JsonParser.getCoordinates(response);
        // Get the midpoints between the coordinates and a given location
        getMidpoints(coordinatesList, location);

        // Add the points from each midpoint to the array.
        LatLng midpoint = new LatLng(midLong, midLat);
        points.add(midpoint);

        if(points.size() == 2){
            // Scoring the button selection
            double score = getRadioButtonScore(myButtonSelection, theirButtonSelection);
            // Set the base point locality
            LatLng bestMidpoint = SphericalUtil.interpolate(points.get(0), points.get(1), score);

            // Check settings if the user wants the blue locality marker plotted
            if(midPointMarkerOn) {
                googleMap.addMarker(new MarkerOptions()
                        .position(bestMidpoint)
                        .title("Best Midpoint Locality")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }

            // Build the Google Places URL
            String googlePlacesUrl = UrlBuilder.getGooglePlacesUrl(placeType, bestMidpoint);

            // Use the url to make an asynchronous JSON request to the google places API
            JsonObjectRequest googleGeojsonPlaces = new JsonObjectRequest
                    (Request.Method.GET, googlePlacesUrl, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                LinkedHashMap<String,String> placeList = JsonParser.getPlaces(response);
                                // No nearby places results returned, tell the user
                                if(placeList.size() == 0){
                                    Toast.makeText(MapsActivity.this,
                                            "There are no Results. Try some different parameters.", Toast.LENGTH_LONG).show();
                                }
                                // Otherwise display the search results using the RecyclerView
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

    // Show the results on the recycler view
    private void displayResults(LinkedHashMap<String,String> placeList) {
        for (Map.Entry<String, String> entry : placeList.entrySet()) {
            String name = entry.getKey();
            String coordinates = entry.getValue();

            // Create ListItem Objects from the data
            ListItem listItem = new ListItem(name, coordinates);
            // Add these to the array list
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