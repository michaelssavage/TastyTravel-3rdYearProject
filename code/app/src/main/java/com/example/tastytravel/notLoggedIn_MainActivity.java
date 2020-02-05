package com.example.tastytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class notLoggedIn_MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_logged_in__main);

        // Search Button
        Button searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MapActivity.class);
                // pass info to 2nd screen
                startActivity(startIntent);
            }
        });

        // Settings
        Button settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                // pass info to 2nd screen
                startActivity(settingsIntent);
            }
        });

        // About
        Button aboutBtn = findViewById(R.id.aboutBtn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent aboutScreen = new Intent(getApplicationContext(), AboutActivity.class);
                // pass info to 2nd screen
                startActivity(aboutScreen);
            }
        });

    }
}