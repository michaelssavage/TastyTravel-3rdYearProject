package com.example.tastytravel.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JsonParser {

    public ArrayList<String> getCoordinates(JSONObject response) throws JSONException{

        // isolate coordinates from JSONObject into an arraylist
        ArrayList<String> coordinateList = new ArrayList<>();
        try {
            JSONArray features = response.getJSONArray("features");
            JSONObject obj = features.getJSONObject(0);
            JSONObject geometry = obj.getJSONObject("geometry");
            JSONArray array = geometry.getJSONArray("coordinates");

            // coordinates is the JSON list in format like [[1.0,2.0], [3.0,4.0], [ etc...
            JSONArray coordinates = array.getJSONArray(0);
            for (int i = 0; i < coordinates.length(); i++) {
                String point = coordinates.getString(i);

                //remove the '[' and ']' and add to the coordinate list.
                coordinateList.add(point.replace("[","").replace("]",""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("coordinates", " " + coordinateList);
        return coordinateList;
    }

    public LinkedHashMap<String,String> getPlaces(JSONObject response) throws JSONException{

        // isolate places from JSONObject into an arraylist
        ArrayList<String> placesList = new ArrayList<>();
        LinkedHashMap<String,String> place_LocationMap =new LinkedHashMap<>();
        try {
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {

                // add the name of each place to the list.
                JSONObject array = results.getJSONObject(i);
                String name = array.getString("name");
                placesList.add(name);

                // store the coordinates of each place in a map.
                JSONObject geometry = array.getJSONObject("geometry");
                String longLatString = geometry.getString("location");
                String[] location = longLatString.split(",");
                String lat = location[0].substring(7) + ",";
                String lng = location[1].substring(6);
                lng = lng.substring(0,lng.length()-1);
                place_LocationMap.put(name, lat + lng);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("places", " " + placesList);
        Log.d("locationlist", " " + place_LocationMap);

        // this is a class that holds the ordered arrayList then the unordered Map.

        return place_LocationMap;
    }
}