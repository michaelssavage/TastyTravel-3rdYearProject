package com.example.tastytravel;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String ISOCHRONE_PREF = "isochroneSwitch";
    public static final String MIDPOINT_PREF = "midpointSwitch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        //The switch IDs that will be used.
        final Switch isochroneSwitch = findViewById(R.id.isochroneSwitch);
        final Switch midpointSwitch = findViewById(R.id.midpointSwitch);

        //The 'file' that SharedPreferences will be written to.
        final SharedPreferences myPrefs = getSharedPreferences("myPref", 0);
        final SharedPreferences.Editor editor = myPrefs.edit();

        //Determine what the switches will be set to.
        boolean IsoCheck = myPrefs.getBoolean(ISOCHRONE_PREF, false);
        isochroneSwitch.setChecked(IsoCheck);
        boolean midCheck = myPrefs.getBoolean(MIDPOINT_PREF, false);
        midpointSwitch.setChecked(midCheck);

        isochroneSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(ISOCHRONE_PREF, isChecked);
                editor.apply();

                if (myPrefs.getBoolean(ISOCHRONE_PREF, isChecked)) {
                    Toast.makeText(getApplicationContext(), "Isochrone builder is ON", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Isochrone builder is OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

        midpointSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(MIDPOINT_PREF, isChecked);
                editor.apply();

                if (myPrefs.getBoolean(MIDPOINT_PREF, isChecked)) {
                    Toast.makeText(getApplicationContext(), "Midpoint Marker is ON", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Midpoint Marker is OFF", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}