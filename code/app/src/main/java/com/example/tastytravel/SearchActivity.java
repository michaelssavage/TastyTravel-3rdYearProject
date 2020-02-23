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
    public static final String RADIO_BUTTONS = "RADIO_BUTTONS";

    private ArrayList<Place> userPlaces;
//    private ArrayList<String> userButtonChoice;

    PlacesClient placesClient;

    Button searchBtn;
    TextView closeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userPlaces = new ArrayList<>();
//        userButtonChoice = new ArrayList<>();

        searchBtn = findViewById(R.id.searchBtn);
        closeText = findViewById(R.id.closeText);

        // Define Actions for button clicks
        initialiseViewControls();

        // Places Search Feature
        initialisePlaces();

//        getRadioChoice();
    }

    private void initialiseViewControls() {
        // If search button is clicked
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        // If close text is clicked
        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }
        });
    }

    public void initialisePlaces() {

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
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
            public void onPlaceSelected(@NonNull Place yourLocation) {
                userPlaces.add(yourLocation);
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
            public void onPlaceSelected(@NonNull Place theirLocation) {
                userPlaces.add(theirLocation);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("TAG", "An error occurred: " + status);
            }
        });
    }

//    public void getRadioChoice(View view) {
//
//        boolean checked = ((RadioButton) view).isChecked();
//
//        switch(view.getId()) {
//            case R.id.WalkRadioBtn:
//                if (checked) {
//                    userButtonChoice.add("walking");
//                }
//                break;
//            case R.id.CarRadioBtn:
//                if (checked) {
//                    userButtonChoice.add("driving");
//                }
//                break;
//            case R.id.BikeRadioBtn2:
//                if (checked) {
//                    userButtonChoice.add("cycling");
//                }
//                break;
//        }
//
//        switch(view.getId()) {
//            case R.id.WalkRadioBtn2:
//                if (checked) {
//                    userButtonChoice.add("walking");
//                }
//                break;
//            case R.id.CarRadioBtn2:
//                if (checked) {
//                    userButtonChoice.add("driving");
//                }
//                break;
//            case R.id.BikeRadioBtn2:
//                if (checked) {
//                    userButtonChoice.add("cycling");
//                }
//                break;
//        }
//    }

    public void openMap() {
        Intent showMap = new Intent(getApplicationContext(), MapsActivity.class);

        showMap.putExtra(LOCATIONS_TAG, userPlaces);
//        showMap.putExtra(RADIO_BUTTONS, userButtonChoice);

        if (userPlaces.size() < 2) {
            Toast.makeText(this, "Error: 2 locations must be entered to proceed", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(showMap);
        }
    }
}

