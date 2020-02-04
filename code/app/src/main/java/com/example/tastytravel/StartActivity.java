package com.example.tastytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Continue without account
        TextView continueText = findViewById(R.id.continueWithoutAccount);
        Button signInBtn = findViewById(R.id.signInBtn);
        TextView signUpText = findViewById(R.id.signUpText);

        continueText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainScreen);
            }
        });

        // Sign In
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInScreen = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(signInScreen);
            }
        });

        // Sign Up
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpScreen = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpScreen);
            }
        });
    }
}