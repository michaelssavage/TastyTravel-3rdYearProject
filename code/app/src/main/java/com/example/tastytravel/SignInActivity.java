package com.example.tastytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    Button signInButton;
    EditText emailField, passwordField;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailField = findViewById(R.id.enterEmail);
        passwordField = findViewById(R.id.enterPassword);
        progressBar = findViewById(R.id.signInProgress);
        signInButton = findViewById(R.id.signInBtn);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                // authenticate the user
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "Successfully Logged in", Toast.LENGTH_LONG).show();
                            Intent mainScreen = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(mainScreen);
                        }
                        else {
                            Toast.makeText(SignInActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}