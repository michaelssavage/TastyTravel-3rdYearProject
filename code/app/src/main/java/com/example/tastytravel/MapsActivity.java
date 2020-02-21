package com.example.tastytravel;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Place> Places;
    Place yourLocation;
    Place theirLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle data = getIntent().getExtras();
        Places = data.getParcelableArrayList(SearchActivity.LOCATIONS_TAG);
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

        yourLocation = Places.get(0);
        theirLocation = Places.get(1);

        final LatLng getYourLocationLatLng = yourLocation.getLatLng();
        final LatLng getTheirLocationLatLng = theirLocation.getLatLng();
//
//        // Add a marker and move the camera
        LatLng yourLocationLatLng = new LatLng(getYourLocationLatLng.latitude, getYourLocationLatLng.longitude);
        LatLng theirLocationLatLng = new LatLng(getTheirLocationLatLng.latitude, getTheirLocationLatLng.longitude);
//
        mMap.addMarker(new MarkerOptions().position(yourLocationLatLng).title("Your Location"));
        mMap.addMarker(new MarkerOptions().position(theirLocationLatLng).title("Their Location"));
//
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(yourLocationLatLng, 12f));

        mMap.addCircle(new CircleOptions()
                .center(new LatLng(getYourLocationLatLng.latitude, getYourLocationLatLng.longitude))
                .radius(1200)
                .strokeWidth(10)
                .strokeColor(Color.GREEN)
                .clickable(true));

    }
}
