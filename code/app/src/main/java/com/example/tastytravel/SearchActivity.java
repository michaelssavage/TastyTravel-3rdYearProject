package com.example.tastytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    public static final String LOCATIONS_TAG = "LOCATIONS";
    public static final String RADIO1 = "radio1";
    public static final String RADIO2 = "radio2";
    private ArrayList<Place> userPlaces;

    PlacesClient placesClient;
    String api_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        api_key = "AIzaSyChmDeOaON5gqRFAR3o27HHKaojDenZ0ps";
        userPlaces = new ArrayList<>();

        // Define Actions for button clicks
        initialiseViewControls();

        // Places Search Feature
        initialisePlaces();

    }

    private void initialiseViewControls() {
        // If search button is clicked
        Button searchBtn;
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMap = new Intent(getApplicationContext(), MapsActivity.class);
                getRadioChoice(v, showMap);
                openMap(showMap);
            }
        });

        // If close text is clicked
        TextView closeText;
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

        if(!Places.isInitialized()){
            Places.initialize(getApplicationContext(), api_key);
        }

        placesClient = Places.createClient(this);

        // autocompleteSupportFragment1
        final AutocompleteSupportFragment autocompleteSupportFragment1 =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autoCompleteFragment1);

        autocompleteSupportFragment1.setPlaceFields(
                Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME))
                .setHint("Enter your Location");
        autocompleteSupportFragment1.setCountries("IE");
        autocompleteSupportFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place yourplace) {
                userPlaces.add(yourplace);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }
        });

        // autocompleteSupportFragment2
        final AutocompleteSupportFragment autocompleteSupportFragment2 =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autoCompleteFragment2);

        autocompleteSupportFragment2.setPlaceFields(
                Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME))
                .setHint("Enter their Location");
        autocompleteSupportFragment2.setCountries("IE");
        autocompleteSupportFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place theirplace) {
                userPlaces.add(theirplace);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }
        });
    }
    public void getRadioChoice(View view, Intent showMap) {

        RadioButton walkbtn, carBtn, bikeBtn, walkbtn2,carBtn2, bikeBtn2;

        walkbtn = findViewById(R.id.WalkRadioBtn);
        carBtn = findViewById(R.id.CarRadioBtn);
        bikeBtn = findViewById(R.id.BikeRadioBtn);

        walkbtn2 = findViewById(R.id.WalkRadioBtn);
        carBtn2 = findViewById(R.id.CarRadioBtn);
        bikeBtn2 = findViewById(R.id.BikeRadioBtn);

        String str;
        switch(view.getId()) {
            case R.id.WalkRadioBtn:
                if (walkbtn.isChecked()) {
                    str = "walking";
                    showMap.putExtra(RADIO1, str);
                }
                break;
            case R.id.CarRadioBtn:
                if (carBtn.isChecked()){
                    str = "driving";
                    showMap.putExtra(RADIO1, str);
                }
                break;
            case R.id.BikeRadioBtn:
                if (bikeBtn.isChecked()){
                    str = "cycling";
                    showMap.putExtra(RADIO1, str);
                }
                break;
        }
        switch(view.getId()) {
            case R.id.WalkRadioBtn:
                if (walkbtn2.isChecked()) {
                    str = "walking";
                    showMap.putExtra(RADIO1, str);
                }
                break;
            case R.id.CarRadioBtn:
                if (carBtn2.isChecked()){
                    str = "driving";
                    showMap.putExtra(RADIO1, str);
                }
                break;
            case R.id.BikeRadioBtn:
                if (bikeBtn2.isChecked()){
                    str = "cycling";
                    showMap.putExtra(RADIO1, str);
                }
                break;
        }
    }
    public void openMap(Intent showMap){


        showMap.putExtra(LOCATIONS_TAG, userPlaces);
        try {
            startActivity(showMap);
        } catch (NullPointerException e ) {
            Context context = getApplicationContext();
            CharSequence text = "Please Enter two locations.";
            int duration = Toast.LENGTH_LONG;
            Toast.makeText(context, text, duration).show();
        }

    }

}

