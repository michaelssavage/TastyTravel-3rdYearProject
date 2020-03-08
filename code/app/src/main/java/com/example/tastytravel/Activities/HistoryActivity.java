package com.example.tastytravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastytravel.Models.HistoryItem;
import com.example.tastytravel.R;
import com.example.tastytravel.Utils.LatLngParser;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HistoryActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private DatabaseReference mPlaces;
    Marker marker;

    // List view used to show history items
    private ListView historyListView;
    private FirebaseUser currentFirebaseUser;
    // Adapter for the list view
    private FirebaseListAdapter<HistoryItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Identify the list view
        historyListView = findViewById(R.id.historyItems);

        // Get the currently logged in user
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // Get an instance of the database
        // To access a location in the database use getReference()
        // Find the "History" branch under the currentUsers ID
        mPlaces = FirebaseDatabase.getInstance().getReference(currentFirebaseUser.getUid()).child("History");
        // For the values at that node identify a map marker
        // push() without parameters generates a unique ID client side
        // setValue() is used to write data for each marker
        mPlaces.push().setValue(marker);

        // Setup the list view
        initiateListView();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void initiateListView() {
        // Initialise a location for the database query to display the users history
        Query query = FirebaseDatabase.getInstance()
                .getReference(currentFirebaseUser.getUid()).child("History")
                .orderByChild("accessDate");

        // Setting the options when displaying the firebase
        FirebaseListOptions<HistoryItem> options = new FirebaseListOptions.Builder<HistoryItem>()
                .setLayout(R.layout.activity_history_item)   // Styling for list view item
                .setLifecycleOwner(this)                     // The life cycle during which data is observed
                .setQuery(query, HistoryItem.class)          // Set the query using the query address above and the model class used
                .build();                                    // Build the ListView Options

        adapter = new FirebaseListAdapter<HistoryItem>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull HistoryItem model, int position) {
                TextView placeName = v.findViewById(R.id.placeName);
                TextView lastSearched = v.findViewById(R.id.lastSearched);

                String name = "Location Name: " + model.getPlaceName();
                String date = "Search Date: " + model.getAccessDate();
                placeName.setText(name);
                lastSearched.setText(date);
            }
        };
        historyListView.setAdapter(adapter);

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryItem historyItem = (HistoryItem) parent.getItemAtPosition(position);

                String coords = historyItem.getCoordinates();
                String[] coordinates = coords.split(",");
                String lats = coordinates[0];
                String longs = coordinates[1];

                Double latsDouble = Double.parseDouble(lats);
                Double longsDouble = Double.parseDouble(longs);

                LatLng markerPos = new LatLng(latsDouble, longsDouble);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerPos, 12));
            }
        });

    }

    // Firebase list adapter overrides
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    // When finished with the list, stop listening for entries
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        // Focus mapview on Ireland
        LatLng ireland = new LatLng(53.4239,-7.9407);
        // Animate the map camera view
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ireland,6f));

        // Plotting points on Map
        mMap.setOnMarkerClickListener(this);
        mPlaces.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // If there is history items in the database
                if (dataSnapshot.exists()) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        // Create a HistoryItem object for each DataSnapShot
                        HistoryItem place = s.getValue(HistoryItem.class);

                        String placeName = place.getPlaceName();
                        String strLocation = place.getCoordinates();

                        LatLng location = LatLngParser.stringToLatLng(strLocation);

                        // Add a map marker using the data from the database
                        mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(placeName))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        // Add a marker on the map with the LatLng object, using the title and with a red icon
                    }
                } else {
                    // No history data recorded, inform the user
                    Toast.makeText(HistoryActivity.this, "You Have No Recently Searched Places", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DatabaseError", "" + databaseError);
            }
        });
    }

    // No need to set custom behaviour when a marker is clicked
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}