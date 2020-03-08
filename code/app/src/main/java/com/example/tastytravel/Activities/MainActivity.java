package com.example.tastytravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastytravel.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavBar;
    FirebaseAuth mAuth;
    FirebaseUser currentFirebaseUser;
    Button searchBtn;
    ImageView savedPlacesBtn, historyBtn;
    TextView savedPlacesText, appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Current firebase user
        mAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = mAuth.getCurrentUser();

        // Identify the activity objects
        bottomNavBar = findViewById(R.id.bottomNavBar);
        searchBtn = findViewById(R.id.searchBtn);
        savedPlacesBtn = findViewById(R.id.savedPlacesBtn);
        historyBtn = findViewById(R.id.historyBtn);
        savedPlacesText = findViewById(R.id.savedPlacesText);
        appName = findViewById(R.id.appName);

        // makes the two images rounded.
        savedPlacesBtn.setClipToOutline(true);
        historyBtn.setClipToOutline(true);

        // set app title to bold
        appName.setTypeface(null, Typeface.BOLD);

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

    // Defining custom behaviour for onBackPressed actions on main screen
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        // Add a delay for double back press
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    // Controlling access to favourites and history buttons
    private void initialiseViewControls() {
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentFirebaseUser != null){
                    Intent historyIntent = new Intent(getApplicationContext(), HistoryActivity.class);
                    startActivity(historyIntent);
                } else{
                    Intent signInIntent = new Intent(getApplicationContext(), SignInActivity.class);
                    startActivity(signInIntent);
                }
            }
        });

        // If the Settings Button is Clicked
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        // If the food collage image or text is clicked
        savedPlacesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startSavedPlaces(); }});

        savedPlacesText.setOnClickListener(new View.OnClickListener() {
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