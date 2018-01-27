package com.ahmed.ameen.weather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class LoadingScreen extends AppCompatActivity {

    private static final String TAG = "LoadingScreen";
    private static final int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        Log.d(TAG, "onCreate: ");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "run: Loading");

                Intent intent = new Intent(LoadingScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }
}
