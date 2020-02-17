package com.example.tastytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.location.LocationComponent;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    public static final String LocationOne = "searchActivity.locationOne";
    public static final String LocationTwo = "searchActivity.locationTwo";

    TextView searchText, closeText;
    EditText locationText, locationText2;
    Button locationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.searchText);
        searchText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        closeText = findViewById(R.id.closeText);
        closeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }
        });
        locationBtn = findViewById(R.id.locationBtn);
        locationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                enableLocation();
            }
        });
    }

    public void openMap() {
        //DCU is -6.2564 , 53.3861
        locationText = findViewById(R.id.locationText);
        String locationOne = locationText.getText().toString();

        // o'connell street is -6.2607 , 53.3508
        locationText2 = findViewById(R.id.locationText2);
        String locationTwo = locationText2.getText().toString();

        Intent showMap = new Intent(getApplicationContext(), MapActivity.class);
        showMap.putExtra(LocationOne, locationOne);
        showMap.putExtra(LocationTwo, locationTwo);
        startActivity(showMap);
    }

    public void enableLocation(){

    }
}
