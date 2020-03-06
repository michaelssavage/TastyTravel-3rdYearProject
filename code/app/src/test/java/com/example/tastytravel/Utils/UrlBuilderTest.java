package com.example.tastytravel.Utils;

import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UrlBuilderTest {

    private static final String TAG = UrlBuilderTest.class.getSimpleName();

    private LatLng coords = new LatLng(-6.2767, 53.407);
    private LatLng coords2 = new LatLng(-5.2347, 52.147);

    private LatLng coordsGoogle = new LatLng(53.386841,-6.256248);

    // Mapbox Isochrone Url Builder Tests
    @Test
    public void urlsAreEqual_MapboxIsochroneWalk() {
        assertEquals("https://api.mapbox.com/isochrone/v1/mapbox/walking/-6.2767,53.407?contours_minutes=10&contours_colors=009688&polygons=true&access_token=pk.eyJ1Ijoiam9obmRvd2F0ZXIiLCJhIjoiY2szcWNjdHIyMDA3cDNlcGlseWt3cjRiNiJ9.Bu2jIzXSGZNcxQBtGCrwbQ",
                UrlBuilder.getMapboxUrl("Walk", coords));
    }

    @Test
    public void urlsAreNotEqual_MapboxIsochroneDrive() {
        assertNotEquals("https://api.mapbox.com/isochrone/v1/mapbox/walking/-54.3456,6.7890?contours_minutes=10&contours_colors=009688&polygons=true&access_token=pk.eyJ1Ijoiam9obmRvd2F0ZXIiLCJhIjoiY2szcWNjdHIyMDA3cDNlcGlseWt3cjRiNiJ9.Bu2jIzXSGZNcxQBtGCrwbQ",
                UrlBuilder.getMapboxUrl("Car", coords));
    }

    @Test
    public void urlsAreNotEqual_MapboxIsochroneEmpty() {
        assertNotEquals("", UrlBuilder.getMapboxUrl("Bike", coords2));
    }

    // Google Places Url Builder Tests
    @Test
    public void urlsAreEqual_googlePlacesUrl() {
        assertEquals("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.386841,-6.256248&rankby=distance&type=restaurant&key=AIzaSyChmDeOaON5gqRFAR3o27HHKaojDenZ0ps",
                UrlBuilder.getGooglePlacesUrl("Restaurant", coordsGoogle));
    }

    @Test
    public void urlsAreNotEqual_googleMapsGeocodeLatLngUrl() {
        assertNotEquals("", UrlBuilder.getGooglePlacesUrl("Cafe", new LatLng(53.3704523, -6.1952533)));
    }

    @Test
    public void urlsAreEqual_googleMapsGeocodeLatLngUrl() {
        assertEquals("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=51.4467841,-7.45249&rankby=distance&type=bar&key=AIzaSyChmDeOaON5gqRFAR3o27HHKaojDenZ0ps",
                UrlBuilder.getGooglePlacesUrl("Bar", new LatLng(51.4467841,-7.45249)));
    }



}
