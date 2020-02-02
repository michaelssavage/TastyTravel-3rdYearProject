package com.example.tastytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.tastytravel.ui.login.LoginActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TextView secondActivityText = findViewById(R.id.secondActivityText);
        secondActivityText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent mainScreen = new Intent(getApplicationContext(), MainScreenActivity.class);
                // pass info to 2nd screen
                startActivity(mainScreen);
            }
        });

        TextView signInBtn = findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
                // pass info to 2nd screen
                startActivity(loginScreen);
            }
        });

        TextView signUpText = findViewById(R.id.signUpText);
        signUpText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent loginScreen = new Intent(getApplicationContext(), LoginActivity.class);
                // pass info to 2nd screen
                startActivity(loginScreen);
            }
        });
    }
}
