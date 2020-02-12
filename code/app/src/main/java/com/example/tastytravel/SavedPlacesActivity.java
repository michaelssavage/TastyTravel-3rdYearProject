package com.example.tastytravel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SavedPlacesActivity extends AppCompatActivity {

    TextView closeText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);

        closeText2 = findViewById(R.id.closeText2);
        closeText2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent showMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(showMain);
            }
        });
    }
}
