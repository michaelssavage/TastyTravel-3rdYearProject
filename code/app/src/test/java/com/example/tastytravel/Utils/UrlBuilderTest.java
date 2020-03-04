package com.example.tastytravel.Utils;

import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class UrlBuilderTest {

    @After
    public void tearDown(){
        System.out.println("Finished Testing");
    }

    @Test
    public void getMapboxUrl_CorrectMonaghan() {
        // Input that would usually be given to the app
        String inputTransportMethod = "Car";
        LatLng inputLatLng = new LatLng(-6.9683, 54.2492);

        // What the test actually returns
        String output;

        // Our expected result, set by us
        String expected = "https://api.mapbox.com/isochrone/v1/mapbox/driving/-6.9683,54.2492?contours_minutes=10&contours_colors=009688&polygons=true&access_token=pk.eyJ1Ijoiam9obmRvd2F0ZXIiLCJhIjoiY2szcWNjdHIyMDA3cDNlcGlseWt3cjRiNiJ9.Bu2jIzXSGZNcxQBtGCrwbQ";

        UrlBuilder urlBuilder = new UrlBuilder();
        output = urlBuilder.getMapboxUrl(inputTransportMethod, inputLatLng);

        assertEquals(expected, output);
    }

    @Test
    public void getMapboxUrl_CorrectCavan() {
        // Input that would usually be given to the app
        String inputTransportMethod = "Bike";
        LatLng inputLatLng = new LatLng(-7.3633, 53.9897);

        // What the test actually returns
        String output;

        // Our expected result, set by us
        String expected = "https://api.mapbox.com/isochrone/v1/mapbox/cycling/-7.3633,53.9897?contours_minutes=10&contours_colors=009688&polygons=true&access_token=pk.eyJ1Ijoiam9obmRvd2F0ZXIiLCJhIjoiY2szcWNjdHIyMDA3cDNlcGlseWt3cjRiNiJ9.Bu2jIzXSGZNcxQBtGCrwbQ";

        UrlBuilder urlBuilder = new UrlBuilder();
        output = urlBuilder.getMapboxUrl(inputTransportMethod, inputLatLng);

        assertEquals(expected, output);
    }

    @Test
    public void getMapboxUrl_IncorrectTyrone() {
        // Input that would usually be given to the app
        String inputTransportMethod = "Bike";

        // DCU Coordinates
        LatLng inputLatLng = new LatLng(-6.2564, 53.3861);

        // What the test actually returns
        String output;

        // Our expected result, set by us
        String expected = "https://api.mapbox.com/isochrone/v1/mapbox/cycling/-7.3633,53.9897?contours_minutes=10&contours_colors=009688&polygons=true&access_token=pk.eyJ1Ijoiam9obmRvd2F0ZXIiLCJhIjoiY2szcWNjdHIyMDA3cDNlcGlseWt3cjRiNiJ9.Bu2jIzXSGZNcxQBtGCrwbQ";

        UrlBuilder urlBuilder = new UrlBuilder();
        output = urlBuilder.getMapboxUrl(inputTransportMethod, inputLatLng);

        assertNotEquals(expected, output);
    }


    @Test
    public void getGooglePlacesUrl_Correct() {
        // Input that would usually be given to the app
        String inputPlaceType = "Bar";
        LatLng inputLatLng = new LatLng(53.386841, -6.256248);

        // What the test actually returns
        String output;

        // Our expected result, set by us
        String expected = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.386841,-6.256248&rankby=distance&type=bar&key=AIzaSyChmDeOaON5gqRFAR3o27HHKaojDenZ0ps";

        UrlBuilder urlBuilder = new UrlBuilder();
        output = urlBuilder.getGooglePlacesUrl(inputPlaceType, inputLatLng);

        assertEquals(expected, output);
    }

    @Test
    public void getGooglePlacesUrl_IncorrectPlaceType() {
        // Input that would usually be given to the app
        String inputPlaceType = "Cafe";
        LatLng inputLatLng = new LatLng(53.386841, -6.256248);

        // What the test actually returns
        String output;

        // Our expected result, set by us
        String expected = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.386841,-6.256248&rankby=distance&type=bar&key=AIzaSyChmDeOaON5gqRFAR3o27HHKaojDenZ0ps";

        UrlBuilder urlBuilder = new UrlBuilder();
        output = urlBuilder.getGooglePlacesUrl(inputPlaceType, inputLatLng);

        assertNotEquals(expected, output);
    }

    @Test
    public void getGooglePlacesUrl_IncorrectCoordinates() {
        // Input that would usually be given to the app
        String inputPlaceType = "Cafe";
        LatLng inputLatLng = new LatLng(53.3067, -6.2210);

        // What the test actually returns
        String output;

        // Our expected result, set by us
        String expected = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=53.386841,-6.256248&rankby=distance&type=bar&key=AIzaSyChmDeOaON5gqRFAR3o27HHKaojDenZ0ps";

        UrlBuilder urlBuilder = new UrlBuilder();
        output = urlBuilder.getGooglePlacesUrl(inputPlaceType, inputLatLng);

        assertNotEquals(expected, output);
    }
}