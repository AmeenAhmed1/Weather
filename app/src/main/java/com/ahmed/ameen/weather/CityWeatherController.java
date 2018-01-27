package com.ahmed.ameen.weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CityWeatherController extends AppCompatActivity {

    private static final String TAG = "CityWeatherController";

    EditText txtCity;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather_controller);

        txtCity = findViewById(R.id.cityText);
        back =findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = txtCity.getText().toString();
                Intent intent = new Intent(CityWeatherController.this, MainActivity.class);
                intent.putExtra("cityName", cityName);
                startActivity(intent);
                finish();
                Log.d(TAG, "onClick: Finished()");
            }
        });



        /*txtCity.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                /*
                **  Getting the city name and pass it to the main activityc
                *//*
                String cityName = txtCity.getText().toString();
                Intent intent = new Intent(CityWeatherController.this, MainActivity.class);
                intent.putExtra("cityName", cityName);
                startActivity(intent);
                return false;
            }
        });*/
    }
}
