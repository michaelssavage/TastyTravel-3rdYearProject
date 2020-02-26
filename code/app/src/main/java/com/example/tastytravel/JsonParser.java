package com.example.tastytravel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public ArrayList<String> getPlaces(JSONObject response) throws JSONException{

        // isolate places from JSONObject into an arraylist
        ArrayList<String> placesList = new ArrayList<>();
        try {
            JSONArray results = response.getJSONArray("results");
            JSONObject obj = results.getJSONObject(0);
            JSONObject geometry = obj.getJSONObject("geometry");
            JSONArray names = geometry.getJSONArray("name");

            JSONArray places = names.getJSONArray(0);
            for (int i = 0; i < places.length(); i++) {
                String place = places.getString(i);

                //remove the '[' and ']' and add to the coordinate list.
                placesList.add(place);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("places", " " + placesList);
        return placesList;
    }

}
