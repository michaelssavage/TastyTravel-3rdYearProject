package com.example.tastytravel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tastytravel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    Button signInButton;
    EditText emailField, passwordField;
    TextView signUpText;
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
        signUpText = findViewById(R.id.signUpText);
        mAuth = FirebaseAuth.getInstance();

        // Customise signup text to make it clearer
        customiseSignupText();

        // Define Actions for button clicks
        initialiseViewControls();
    }

    private void customiseSignupText() {
        Spannable word = new SpannableString("Don't have an account?");
        word.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpText.setText(word);
        Spannable wordTwo = new SpannableString(" SIGN UP");
        wordTwo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.aqua_blue)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        signUpText.append(wordTwo);
    }

    private void initialiseViewControls() {
        // Sign In Control
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                // Call the sigin function using email and password provided
                signUserIn();
            }
        });

        // Sign Up Control
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpScreen = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(signUpScreen);
            }
        });
    }

    private void signUserIn() {

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
                    Toast.makeText(SignInActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

}