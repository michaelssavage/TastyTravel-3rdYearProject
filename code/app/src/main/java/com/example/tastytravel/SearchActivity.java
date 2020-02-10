package com.example.tastytravel;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    TextView searchText;
    TextView closeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.searchText);
        searchText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent showMap = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(showMap);
            }
        });

        closeText = findViewById(R.id.closeText);
        closeText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startIntent);
            }
        });
    }
}
