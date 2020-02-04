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

public class SignUpActivity extends AppCompatActivity {

    EditText emailAddress, accountPassword;
    Button signUpBtn;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailAddress = findViewById(R.id.signUpEmail);
        accountPassword = findViewById(R.id.signUpPassword);
        signUpBtn = findViewById(R.id.signUpBtn);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        // checking if user is already logged in
        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        signUpBtn.setOnClickListener(new View.OnClickListener() {
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

                // registering the user in FireBase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Profile Successfully Created", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
    }
}
