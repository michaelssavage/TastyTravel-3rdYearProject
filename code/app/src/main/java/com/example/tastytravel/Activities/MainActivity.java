package com.example.tastytravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tastytravel.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavBar;
    FirebaseAuth mAuth;
    Button searchBtn;
    ImageView foodCollageImage;
    Button saveplacesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        bottomNavBar = findViewById(R.id.bottomNavBar);
        searchBtn = findViewById(R.id.searchBtn);
        foodCollageImage = findViewById(R.id.foodCollageImage);
        saveplacesText = findViewById(R.id.saveplacesText);

        // Set up bottom nav bar
        setUpNavBar();

        // Define Actions for button clicks
        initialiseViewControls();
    }

    private void setUpNavBar() {
        bottomNavBar.setSelectedItemId(R.id.menu_home);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.menu_home:
                        return true;

                    case R.id.menu_about:
                        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_profile:
                        // checking if user is already logged in
                        if (mAuth.getCurrentUser() != null) {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        } else {
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                }
                return false;
            }
        });
    }

    private void initialiseViewControls() {
        // If the Settings Button is Clicked
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        // If the food collage image is clicked
        foodCollageImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startSavedPlaces(); }});

        saveplacesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startSavedPlaces(); }});
    }

    public void startSavedPlaces() {
        if (mAuth.getCurrentUser() != null) {
            Intent savedPlacesIntent = new Intent(getApplicationContext(), SavedPlacesActivity.class);
            startActivity(savedPlacesIntent);
        } else {
            Intent signIn = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(signIn);
        }
    }
}