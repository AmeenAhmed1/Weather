package com.ahmed.ameen.weather;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ameen on 13-Jan-18.
 */

public class WeatherDataModel {

    //Variables
    String mTemp;
    String mCity;
    String mIconName;
    int mCondition;

    /*
    ** Parsing the Data from the URL [JSON]
     */
    public static WeatherDataModel dataFromJson(JSONObject jsonObject){

        WeatherDataModel mWeatherData = new WeatherDataModel();

        try {
            mWeatherData.mCity = jsonObject.getString("name");
            mWeatherData.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");

            //Getting the temp from JSON and convert it to C°
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundValue = (int) Math.rint(tempResult);
            mWeatherData.mTemp = Integer.toString(roundValue);

            mWeatherData.mIconName = updateWeatherIcon(mWeatherData.mCondition);

            return mWeatherData;

        }catch (JSONException jsonE){
            jsonE.printStackTrace();
            return null;
        }
    }

    /*
    **    Getting Weather Current Icon
     */
    private static String updateWeatherIcon(int condition) {

        if (condition >= 200 && condition < 300) {
            return "ic_storm_weather";
        } else if (condition >= 300 && condition < 600) {
            return "ic_rainy_weather";
        }  else if (condition >= 600 && condition <= 700) {
            return "ic_snow_weather";
        } else if (condition >= 701 && condition < 800) {
            return "ic_haze_weather";
        } else if (condition == 800) {
            return "ic_clear_day";
        } else if (condition >= 801 && condition <= 804) {
            return "ic_partly_cloudy";
        } else if (condition >= 900 && condition <= 902) {
            return "ic_windy_weather";
        }
        return "ic_unknown";
    }


    public String getmTemp() {
        return mTemp;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmIconName() {
        return mIconName;
    }
}
