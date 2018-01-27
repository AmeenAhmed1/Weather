package com.ahmed.ameen.weather;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //Constants
    final int REQUES_CODE = 12;
    String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    String APP_ID = "1f1897244ed6b6f136d38fd4fd834621";

    long MIN_TIME = 5000;  //Time between Location Update (5 Seconds)
    float MIN_DISTANC = 1000; //Distance between Location Change (1 Km)

    //To Provide the location
    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListener;

    //UI Variables
    TextView mCityText, mTempText;
    ImageView mImageView;
    Button mCityButton;


    private WeatherDataModel weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCityText = findViewById(R.id.cityName);
        mTempText = findViewById(R.id.cityTemp);
        mImageView = findViewById(R.id.imageStatus);
        mCityButton = findViewById(R.id.buttonCityData);
        mCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityWeatherController.class);
                startActivity(intent);
            }
        });
        checkPermission();
        //updateUI(weatherData);
    }


    @Override
    protected void onResume() {
        super.onResume();
        
        Log.d(TAG, "onResume");
        
        Intent cityNameIntent = getIntent();
        String city = cityNameIntent.getStringExtra("cityName");

        if(city != null){
            getCurrentCity(city);
            return;
        }else{
            /*
            ** Then the user did not put any extra data so get the current
            ** and get the weather
            */
            getCurrentLocation();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(TAG, "onPause: ");
        
        if(mLocationManager != null)
            mLocationManager.removeUpdates(mLocationListener);
    }

    private void getCurrentCity(String city) {
        //Getting the city weather.
        RequestParams cityParams = new RequestParams();
        cityParams.put("q", city);
        cityParams.put("appid", APP_ID);
        getTheWeather(cityParams);
    }

    /*
        ** Getting the current location
         */

    private void getCurrentLocation() {
        Log.d(TAG, "getCurrentLocation: 1");

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d(TAG, "onLocationChanged : Location Changed");

                //Getting the Location Data
                String longitude = String.valueOf(location.getLongitude());
                String latitude = String.valueOf(location.getLatitude());

                Log.d(TAG, "onLocationChanged: Longitude is = " + longitude);
                Log.d(TAG, "onLocationChanged: Latitude is = " + latitude);

                RequestParams mParams = new RequestParams();
                mParams.put("lat", latitude);
                mParams.put("lon", longitude);
                mParams.put("appid", APP_ID);
                getTheWeather(mParams);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANC, mLocationListener);
    }

    /*
    ** Getting the weather data from current location
     */
    private void getTheWeather(RequestParams mParams) {

        Log.d(TAG, "getTheWeather:");
        AsyncHttpClient mClient = new AsyncHttpClient();

        mClient.get(WEATHER_URL, mParams, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.d(TAG, "onSuccess: " + response.toString());

                weatherData = WeatherDataModel.dataFromJson(response);
                //Update the info after getting data from the URL
                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response){
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    /*
    ** Updating the UI with the information we got
     */
    private void updateUI(WeatherDataModel weather){
        mCityText.setText(weather.getmCity());
        mTempText.setText(weather.getmTemp());

        String icon = weather.getmIconName();
        Log.d(TAG, "updateUI: icon name" + icon);

        int resID = getResources().getIdentifier(icon, "drawable", getPackageName());
        mImageView.setImageResource(resID);
    }


    /*
    ** A Method to CHeck the GPS Permission to Access the Location
     */
    protected void checkPermission(){

        Log.d(TAG, "checkPermission: Checking Pemissions");
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        //Check if the user has granted the permission or not
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            //The user denid the permission
            Log.d(TAG, "checkPermission: Pemission Denid");
            ActivityCompat.requestPermissions(this, perms, REQUES_CODE);

        }else {

            //The permission is granted
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUES_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(TAG, "onRequestPermissionsResult: Requestion a permissions is Approved");
                getCurrentLocation();
            }else{
                Log.d(TAG, "onRequestPermissionsResult: Permission denied");
            }
        }
    }
}
