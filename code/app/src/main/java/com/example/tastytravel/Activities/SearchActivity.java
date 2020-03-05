package com.example.tastytravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastytravel.Models.HistoryItem;
import com.example.tastytravel.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String LOCATIONS_TAG = "LOCATIONS";
    public static final String RADIO_BUTTON1 = "RADIO_BUTTON1";
    public static final String RADIO_BUTTON2 = "RADIO_BUTTON2";
    public static final String DATA = "DATA";
    public static final String PLACE_TYPE_TAG = "PLACE_TYPE";

    private ArrayList<Place> userPlaces;
    private DatabaseReference userHistory;
    FirebaseUser currentFirebaseUser;

    PlacesClient placesClient;

    Intent showMap;
    Spinner placesSpinner;
    Button searchBtn;
    TextView closeText;

    Place yourSelectedPlace, theirSelectedPlace;
    RadioGroup radioGroup1, radioGroup2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        // User History Segment
        if(currentFirebaseUser != null){
            userHistory = FirebaseDatabase.getInstance().getReference(currentFirebaseUser.getUid()).child("History");
        }

        // User Places
        userPlaces = new ArrayList<>();

        showMap = new Intent(getApplicationContext(), MapsActivity.class);

        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);

        searchBtn = findViewById(R.id.searchBtn);
        closeText = findViewById(R.id.closeText);

        placesSpinner = findViewById(R.id.placeTypeSpinner);
        placesSpinner.setOnItemSelectedListener(this);

        // Setup dropdown menu
        setupSpinnerDropdown();

        // Define Actions for button clicks
        initialiseViewControls();

        // Places Search Feature
        initialisePlaces();
    }

    private void setupSpinnerDropdown() {
        List<String> categories = new ArrayList<>();
        categories.add(0, "Select The Type Of Meeting Place");
        categories.add("Bar");
        categories.add("Cafe");
        categories.add("Restaurant");

        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        placesSpinner.setAdapter(dataAdapter);

        placesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select The Type Of Meeting Place")) {
                    // Do nothing
                } else {
                    String item = parent.getItemAtPosition(position).toString();
                    showMap.putExtra(PLACE_TYPE_TAG, item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                onBackPressed();
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

//        autocompleteSupportFragment1.setCountries("IE");
        autocompleteSupportFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place yourLocation) {
                yourSelectedPlace = yourLocation;
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("autocomplete1", "Autocomplete error occurred: " + status);
            }
        });

        // autocompleteSupportFragment2
        final AutocompleteSupportFragment autocompleteSupportFragment2 =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autoCompleteFragment2);

        autocompleteSupportFragment2.setPlaceFields(
                Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME))
                .setHint("Enter their Location");

//        autocompleteSupportFragment2.setCountries("IE");
        autocompleteSupportFragment2.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place theirLocation) {
                theirSelectedPlace = theirLocation;
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("autocomplete2", "Autocomplete error occurred: " + status);
            }
        });
    }

    public void openMap() {

        try {
            //buttons are walk, car, bike
            String radio1 = ((RadioButton) findViewById(radioGroup1.getCheckedRadioButtonId())).getText().toString();
            //buttons will be replaced by walking, driving, cycling
            String radio2 = ((RadioButton) findViewById(radioGroup2.getCheckedRadioButtonId())).getText().toString();

            //json object with name and latlng
            userPlaces.add(yourSelectedPlace);
            userPlaces.add(theirSelectedPlace);
            Log.d("yourplace", "" + yourSelectedPlace);
            Log.d("theirplace", "" + theirSelectedPlace);

            if(currentFirebaseUser != null){
                for(Place userPlace : userPlaces){
                    savePlacestoHistory(userPlace);
                }
            }

            // Put the radio button selections
            showMap.putExtra(RADIO_BUTTON1, radio1);
            showMap.putExtra(RADIO_BUTTON2, radio2);

        } catch (NullPointerException e) {
            Toast.makeText(this, "Error: Did you enter the mode of transport?", Toast.LENGTH_SHORT).show();
        }
        // bundle the long lat locations
        Bundle data = new Bundle();
        data.putSerializable(LOCATIONS_TAG, userPlaces);

        showMap.putExtra(DATA, data);

        // Checking if two locations have been selected
        if (userPlaces.size() < 2) {
            Toast.makeText(this, "Error: Did you enter the locations?", Toast.LENGTH_SHORT).show();
        } else if (!(showMap.hasExtra(PLACE_TYPE_TAG))) {
            Toast.makeText(this, "Error: Did you enter the type of meeting place you would like?", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(showMap);
        }
    }

    private void savePlacestoHistory(Place userPlace) {

        String placeName = userPlace.getName();
        Log.d("place location", placeName);
        LatLng coords = userPlace.getLatLng();
        String coordinates = String.valueOf(coords.latitude + "," + coords.longitude);
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        final HistoryItem place = new HistoryItem(placeName, coordinates, date);
        userHistory.child(placeName).setValue(place);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //if back button pressed, start new Main Activity
    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

