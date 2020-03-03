package com.example.tastytravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastytravel.Models.HistoryItem;
import com.example.tastytravel.R;
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

public class HistoryActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private DatabaseReference mPlaces;
    Marker marker;

    private ListView historyListView;
    private FirebaseUser currentFirebaseUser;
    private FirebaseListAdapter<HistoryItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mPlaces = FirebaseDatabase.getInstance().getReference(currentFirebaseUser.getUid()).child("History");
        mPlaces.push().setValue(marker);

        historyListView = findViewById(R.id.historyItems);

        Query query = FirebaseDatabase.getInstance()
                .getReference(currentFirebaseUser.getUid()).child("History")
                .orderByKey();

        FirebaseListOptions<HistoryItem> options = new FirebaseListOptions.Builder<HistoryItem>()
                .setLayout(R.layout.activity_history_item)
                .setLifecycleOwner(this)
                .setQuery(query, HistoryItem.class)
                .build();

        adapter = new FirebaseListAdapter<HistoryItem>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull HistoryItem model, int position) {
                TextView placeName = v.findViewById(R.id.placeName);
                TextView lastSearched = v.findViewById(R.id.lastSearched);

                HistoryItem historyItem = (HistoryItem) model;
                placeName.setText("Location Name: " + historyItem.getPlaceName());
                lastSearched.setText("Search Date: " + historyItem.getAccessDate());
            }
        };
    historyListView.setAdapter(adapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    // Firebase list adapter overrides
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ireland,6f));

        // Plotting points on Map
        mMap.setOnMarkerClickListener(this);
        mPlaces.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        HistoryItem place = s.getValue(HistoryItem.class);

                        String placeName = place.getPlaceName();
                        String strLocation = place.getCoordinates();

                        String[] latlong = strLocation.split(",");
                        double latitude = Double.parseDouble(latlong[0]);
                        double longitude = Double.parseDouble(latlong[1]);

                        LatLng location = new LatLng(latitude, longitude);

                        mMap.addMarker(new MarkerOptions()
                                .position(location)
                                .title(placeName))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    }
                } else {
                    Toast.makeText(HistoryActivity.this, "You Have No Recently Searched Places", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

}