package com.example.tastytravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tastytravel.Adapters.RecyclerViewAdapter;
import com.example.tastytravel.Models.HistoryItem;
import com.example.tastytravel.Models.ListItem;
import com.example.tastytravel.R;
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
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    private RecyclerView historyRecyclerView;
    private GoogleMap mMap;
    private DatabaseReference mPlaces;
    Marker marker;

    private List<HistoryItem> historyItems;
    RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setHasFixedSize(true);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mPlaces = FirebaseDatabase.getInstance().getReference(currentFirebaseUser.getUid()).child("History");
        mPlaces.push().setValue(marker);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        googleMap.setOnMarkerClickListener(this);

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

                        String placeName = place.getHead();
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