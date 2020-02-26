package com.example.tastytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    public static final String RADIO_BUTTON1 = "RADIO_BUTTON1";
    public static final String RADIO_BUTTON2 = "RADIO_BUTTON2";
    public static final String DATA = "DATA";

    private ArrayList<Place> userPlaces;

    PlacesClient placesClient;

    Button searchBtn;
    TextView closeText;

    RadioGroup radioGroup1, radioGroup2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        userPlaces = new ArrayList<>();

        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);

        searchBtn = findViewById(R.id.searchBtn);
        closeText = findViewById(R.id.closeText);

        // Define Actions for button clicks
        initialiseViewControls();

        // Places Search Feature
        initialisePlaces();


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

    public void openMap() {
        Intent showMap = new Intent(getApplicationContext(), MapsActivity.class);

        //buttons are walk, car, bike
        String radio1 = ((RadioButton)findViewById(radioGroup1.getCheckedRadioButtonId())).getText().toString();
        //buttons will be replaced by walking, driving, cycling
        String radio2 = ((RadioButton)findViewById(radioGroup2.getCheckedRadioButtonId())).getText().toString();

        // bundle the long lat locations
        Bundle data = new Bundle();
        data.putSerializable(LOCATIONS_TAG, userPlaces);
        showMap.putExtra(DATA, data);

        // Put the radio button selections
        showMap.putExtra(RADIO_BUTTON1, radio1);
        showMap.putExtra(RADIO_BUTTON2, radio2);

        // Checking if two locations have been selected
        if (userPlaces.size() < 2) {
            Toast.makeText(this, "Error: 2 locations must be entered to proceed", Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(showMap);
        }
    }
}

