package com.example.tastytravel;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;


/**
 * The most basic example of adding a map to an activity.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MARKER_SOURCE = "markers-source";
    private static final String MARKER_STYLE_LAYER = "markers-style-layer";
    private static final String MARKER_IMAGE = "custom-marker";

    private MapView mapView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_map);

        /* Map: This represents the map in the application. */
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
            /* Image: An image is loaded and added to the map. */

            Intent intent = getIntent();
            String locationOne = intent.getStringExtra(SearchActivity.LocationOne);
            String locationTwo = intent.getStringExtra(SearchActivity.LocationTwo);

            style.addImage(MARKER_IMAGE, BitmapFactory.decodeResource(
                    MapActivity.this.getResources(), R.drawable.custom_marker));

            addMarkers(style, locationOne, locationTwo);
        }
    });
}

    private void addMarkers(@NonNull Style loadedMapStyle, String locationOne, String locationTwo) {
        List<Feature> features = new ArrayList<>();

        /* this is the long lat of location 1*/
        String[] coordinates1 = locationOne.split(" , ");
        double long1 = Double.valueOf(coordinates1[0]);
        double lat1 = Double.valueOf(coordinates1[1]);
        features.add(Feature.fromGeometry(Point.fromLngLat(long1, lat1)));

        /* this is the long lat of location 2*/
        String[] coordinates2 = locationTwo.split(" , ");
        double long2 = Double.valueOf(coordinates2[0]);
        double lat2 = Double.valueOf(coordinates2[1]);
        features.add(Feature.fromGeometry(Point.fromLngLat(long2, lat2)));

        /* Source: A data source specifies the geographic coordinate where the image marker gets placed. */
        loadedMapStyle.addSource(new GeoJsonSource(MARKER_SOURCE, FeatureCollection.fromFeatures(features)));

        /* Style layer: A style layer ties together the source and image and specifies how they are displayed on the map. */
        loadedMapStyle.addLayer(new SymbolLayer(MARKER_STYLE_LAYER, MARKER_SOURCE)
                .withProperties(
                        PropertyFactory.iconAllowOverlap(true),
                        PropertyFactory.iconIgnorePlacement(true),
                        PropertyFactory.iconImage(MARKER_IMAGE),
                        // Adjust the second number of the Float array based on the height of your marker image.
                        // This is because the bottom of the marker should be anchored to the coordinate point, rather
                        // than the middle of the marker being the anchor point on the map.
                        PropertyFactory.iconOffset(new Float[] {0f, -52f})
                ));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}