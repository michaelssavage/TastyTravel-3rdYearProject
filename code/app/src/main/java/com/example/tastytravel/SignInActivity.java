package com.example.tastytravel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

    Button signInBtn;
    EditText emailAddress, accountPassword;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailAddress = findViewById(R.id.enterEmail);
        accountPassword = findViewById(R.id.enterPassword);
        progressBar = findViewById(R.id.progressBar);
        signInBtn = findViewById(R.id.signInBtn);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailAddress.getText().toString().trim();
                String password = accountPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    emailAddress.setError("Email is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    accountPassword.setError("Password is Required");
                    return;
                }

                if(password.length() < 6){
                    accountPassword.setError("Password must be 6 characters or greater");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, "User Successfully Logged in", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
