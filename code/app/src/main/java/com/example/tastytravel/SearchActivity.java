package com.example.tastytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    public static final String LocationOne = "searchActivity.locationOne";
    public static final String LocationTwo = "searchActivity.locationTwo";

    TextView searchText, closeText;
    EditText locationText, locationText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        locationText = findViewById(R.id.locationText);
        locationText2 = findViewById(R.id.locationText2);

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
    }

    public void openMap() {
        //DCU is -6.2564 , 53.3861
        String locationOne = locationText.getText().toString();

        // o'connell street is -6.2607 , 53.3508
        String locationTwo = locationText2.getText().toString();

        Intent showMap = new Intent(getApplicationContext(), MapsActivity.class);
        showMap.putExtra(LocationOne, "-6.2564 , 53.3861");
        showMap.putExtra(LocationTwo, "-6.2607 , 53.3508");
        startActivity(showMap);
    }
}
