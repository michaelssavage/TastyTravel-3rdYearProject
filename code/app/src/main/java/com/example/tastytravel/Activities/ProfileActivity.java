package com.example.tastytravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tastytravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView bottomNavBar;
    Button settingsBtn, logoutBtn, deleteAccountBtn, clearFavouritesBtn, clearHistoryBtn;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Identify the page components
        bottomNavBar = findViewById(R.id.bottomNavBar);
        logoutBtn = findViewById(R.id.logoutBtn);
        settingsBtn = findViewById(R.id.settingsBtn);
        deleteAccountBtn = findViewById(R.id.deleteBtn);
        clearFavouritesBtn = findViewById(R.id.deleteFavsBtn);
        clearHistoryBtn = findViewById(R.id.clearHistoryBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Set up bottom nav bar
        setUpNavBar();

        // Define Actions for button clicks
        initialiseViewControls();
    }

    private void initialiseViewControls() {
        // Clear favourites button action
        clearFavouritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the confirmation alert dialog
                showClearFavsAlertDialog();
            }
        });

        // Clear history button action
        clearHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the confirmation alert dialog
                showClearHistoryAlertDialog();
            }
        });

        // Settings button action
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        // Logout button action
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the confirmation alert dialog
                showLogoutAlertDialog();
            }
        });

        // Delete button action
        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the confirmation alert dialog
                showDeleteAlertDialog();
            }
        });
    }

    private void setUpNavBar() {
        bottomNavBar.setSelectedItemId(R.id.menu_profile);
        bottomNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_profile:
                        return true;

                    case R.id.menu_about:
                        startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    // Delete Alert Dialog Properties
    public void showDeleteAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Account Deletion");
        alert.setMessage("Do you want to permanently delete your account?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Account Successfully Deleted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), StartActivity.class));
                        }
                    }
                });
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfileActivity.this, "Account Deletion Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        alert.create().show();
    }

    // Logout Alert Dialog Properties
    public void showLogoutAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Account Sign Out");
        alert.setMessage("Do you want to sign out of your account?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(ProfileActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), StartActivity.class));
                finish();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfileActivity.this, "Log Out Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        alert.create().show();
    }

    // Clear Favourites Alert Dialog Properties
    public void showClearFavsAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Favourites Deletion");
        alert.setMessage("Do you want to permanently delete your saved places?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference userFavs = FirebaseDatabase.getInstance().getReference(user.getUid()).child("Favourites");
                userFavs.removeValue();

                Toast.makeText(ProfileActivity.this, "Favourite Places Cleared", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfileActivity.this, "Favourites Deletion Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        alert.create().show();
    }

    // Clear History Alert Dialog Properties
    public void showClearHistoryAlertDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Search History Deletion");
        alert.setMessage("Do you want to permanently delete your search history?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference userHistory = FirebaseDatabase.getInstance().getReference(user.getUid()).child("History");
                userHistory.removeValue();

                Toast.makeText(ProfileActivity.this, "Search History Cleared", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ProfileActivity.this, "Search History Deletion Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        alert.create().show();
    }

}