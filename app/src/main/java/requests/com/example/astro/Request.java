package com.example.astro;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Request {

//    http://api.openweathermap.org/data/2.5/forecast?q=Lodz&units=metric&appid=89535a3904f21dd32e9cdbf11894c1a0

    Context context;
    private String BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?";
    private String APP_ID = "&appid=89535a3904f21dd32e9cdbf11894c1a0";
    private String UNITS = "&units=metric";
    private String LANG = "&lang=eng";
    private RequestQueue mQueue;

    public Request(Context context) {
        this.context = context;
        mQueue = Volley.newRequestQueue(context);
    }

    public void getResponse(String city, final VolleyResponseCallback volleyCallback) {
        String url = BASE_URL + "q=" + city + UNITS + APP_ID + LANG;
        JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                volleyCallback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Error 404, sprawdz polaczenie lub wprowadzone dane", Toast.LENGTH_LONG).show();
//                Log.e("Request.java", error.getCause().toString());
                Toast.makeText(context, "404", Toast.LENGTH_SHORT).show();
                volleyCallback.onFailedResponse();
            }
        });
        mQueue.add(request);
    }

    public void parseResponse(JSONObject response, final VolleyParseCallback volleyParseCallback) {
        ContentValues common = new ContentValues();
        ContentValues present;
        ContentValues secondDay = new ContentValues();
        ContentValues thirdDay = new ContentValues();

        try {
//            cnt Number of lines returned by this API call
            int cnt = response.getInt("cnt");
            JSONObject city = response.getJSONObject("city");
            int id = city.getInt("id");
            String name = city.getString("name");
            JSONObject coord = city.getJSONObject("coord");
            int lat = coord.getInt("lat");
            int lon = coord.getInt("lon");
            String country = city.getString("country");
            int timezone = city.getInt("timezone");
            int sunrise = city.getInt("sunrise");
            int sunset = city.getInt("sunset");

            common.put(DatabaseHelper.CITY_ID, id);
            common.put(DatabaseHelper.NAME, name);
            common.put(DatabaseHelper.LAT, lat);
            common.put(DatabaseHelper.LON, lon);
            common.put(DatabaseHelper.COUNTRY, country);
            common.put(DatabaseHelper.TIMEZONE, timezone);
            common.put(DatabaseHelper.SUNRISE, sunrise);
            common.put(DatabaseHelper.SUNSET, sunset);

            present = parseOneDay(response, 0);

            int noDay = 1;

            for (int numberOfLine = 0; numberOfLine < cnt; numberOfLine++) {
                if (contain12Hour(response, numberOfLine)) {
                    if (noDay == 1) {
                        secondDay = parseOneDay(response, numberOfLine);
                        noDay++;
                    } else if (noDay == 2) {
                        thirdDay = parseOneDay(response, numberOfLine);
                        break;
                    }
                }
            }

            //        three days in a ROW
            volleyParseCallback.onSuccessParse(common, present, secondDay, thirdDay);

        } catch (JSONException e) {
            Log.e("Reqest.java", "parseResponse Error");
            Log.e("Request.java", e.getMessage());
        }
    }

    private boolean contain12Hour(JSONObject response, int iteratingThroughCnt) {
        try {
            JSONArray list = response.getJSONArray("list");
            JSONObject listObject = list.getJSONObject(iteratingThroughCnt);
            String dtTxt = listObject.getString("dt_txt");

            if (dtTxt.contains("12:00:00"))
                return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    private ContentValues parseOneDay(JSONObject response, int iteratingThroughCnt) {
        ContentValues contentValues = new ContentValues();

        try {
            JSONArray list = response.getJSONArray("list");

            JSONObject city = response.getJSONObject("city");
            int id = city.getInt("id");
            JSONObject listObject = list.getJSONObject(iteratingThroughCnt);
            String dtTxt = listObject.getString("dt_txt");
            JSONObject main = listObject.getJSONObject("main");
            int temp = main.getInt("temp");
            int feelsLike = main.getInt("feels_like");
            int tempMin = main.getInt("temp_min");
            int tempMax = main.getInt("temp_max");
            int pressure = main.getInt("pressure");
            int seaLevel = main.getInt("sea_level");
            int grndLevel = main.getInt("grnd_level");
            int humidity = main.getInt("humidity");

            JSONObject weather = listObject.getJSONArray("weather").getJSONObject(0);
            String weatherMain = weather.getString("main");
            String weatherDescription = weather.getString("description");

            //        http://openweathermap.org/img/w/04d.png
            String weatherIcon = weather.getString("icon");

            JSONObject wind = listObject.getJSONObject("wind");
            int speed = wind.getInt("speed");
            int deg = wind.getInt("deg");

            contentValues.put(DatabaseHelper.CITY_ID, id);
            contentValues.put(DatabaseHelper.DT_TXT, dtTxt);
            contentValues.put(DatabaseHelper.TEMPERATURE, temp);
            contentValues.put(DatabaseHelper.FEELS_LIKE, feelsLike);
            contentValues.put(DatabaseHelper.TEMP_MIN, tempMin);
            contentValues.put(DatabaseHelper.TEMP_MAX, tempMax);
            contentValues.put(DatabaseHelper.PRESSURE, pressure);
            contentValues.put(DatabaseHelper.SEA_LEVEL, seaLevel);
            contentValues.put(DatabaseHelper.GRND_LEVEL, grndLevel);
            contentValues.put(DatabaseHelper.HUMIDITY, humidity);
            contentValues.put(DatabaseHelper.WEATHER_MAIN, weatherMain);
            contentValues.put(DatabaseHelper.WEATHER_DESCRIPTION, weatherDescription);
            contentValues.put(DatabaseHelper.WEATHER_ICON, weatherIcon);
            contentValues.put(DatabaseHelper.WIND_SPEED, speed);
            contentValues.put(DatabaseHelper.WIND_DEG, deg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentValues;
    }

    public interface VolleyResponseCallback {
        void onSuccessResponse(JSONObject response);
        void onFailedResponse();
    }

    public interface VolleyParseCallback {
        void onSuccessParse(ContentValues common, ContentValues present, ContentValues secondDay, ContentValues thirdDay);
    }
}
