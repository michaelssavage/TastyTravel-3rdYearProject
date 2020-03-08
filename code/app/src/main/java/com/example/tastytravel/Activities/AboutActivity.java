package com.example.tastytravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tastytravel.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AboutActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    BottomNavigationView bottomNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Get an instance of the FireBaseAuth class
        // Allows us to use methods of that class
        mAuth = FirebaseAuth.getInstance();

        // Initialising bottom navigation bar
        bottomNavBar = findViewById(R.id.bottomNavBar);

        // Set up bottom nav bar and its dependencies function
        setUpNavBar();
    }

    private void setUpNavBar() {
        bottomNavBar.setSelectedItemId(R.id.menu_about);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.menu_about:
                        return true;

                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        // Specifies the transition animation performed when transitioning between tabs
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_profile:
                        // checking if user is already logged in
                        if(mAuth.getCurrentUser() != null) {
                            // if they are logged in show the profile tab
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        else{
                            // otherwise a sign in is required to use the profile tab
                            startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                }
                return false;
            }
        });
    }

}
