package com.example.tastytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    TextView searchText, closeText;
    PlacesClient placesClient;
    String api_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Places Search Feature
        initialisePlaces();

        // If search button is clicked
        searchText = findViewById(R.id.searchText);
        searchText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        // If close button is clicked
        closeText = findViewById(R.id.closeText);
        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }
        });

    }

    public void initialisePlaces(){
        api_key = "AIzaSyChmDeOaON5gqRFAR3o27HHKaojDenZ0ps";

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), api_key);
        }

        placesClient = Places.createClient(this);

        // autocompleteSupportFragment1
        final AutocompleteSupportFragment autocompleteSupportFragment1 =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autoCompleteFragment1);

        autocompleteSupportFragment1.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteSupportFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();

                Log.i("PlacesApi", "onPlaceSelected: " + latLng.latitude + "\n" + latLng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        // autocompleteSupportFragment2
        final AutocompleteSupportFragment autocompleteSupportFragment2 =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autoCompleteFragment2);

        autocompleteSupportFragment2.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteSupportFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();

                Log.i("PlacesApi", "onPlaceSelected: " + latLng.latitude + "\n" + latLng.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    public void openMap() {
        //DCU is -6.2564 , 53.3861
        // o'connell street is -6.2607 , 53.3508

        Intent showMap = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(showMap);
    }

}

