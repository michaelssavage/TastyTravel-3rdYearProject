package com.example.tastytravel;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        Place yourLocation = (Place) intent.getSerializableExtra("Location1");
        Place theirLocation = (Place) intent.getSerializableExtra("Location2");

        final LatLng latLng1 = yourLocation.getLatLng();
        final LatLng latLng2 = theirLocation.getLatLng();

        // Add a marker and move the camera
        LatLng location1 = new LatLng(latLng1.latitude, latLng1.longitude);
        LatLng location2 = new LatLng(latLng2.latitude, latLng2.longitude);

        mMap.addMarker(new MarkerOptions().position(location1).title("Your Location"));
        mMap.addMarker(new MarkerOptions().position(location2).title("Their Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location1));
    }
}
