package com.example.astro;

import android.content.ContentValues;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class todayFragment extends Fragment {

    private long cityID;
    private String name;
    private int lat;
    private int lon;
    private String country;
    private int timezone;
    private int sunrise;
    private int sunset;
    private String timeText;
    private int temperature;
    private int feelsLike;
    private int tempMin;
    private int tempMax;
    private int pressure;
    private int humidity;
    private int seaLevel;
    private int grndLevel;
    private String weatherMain;
    private String weatherDescription;
    private String weatherIcon;
    private int windSpeed;
    private int windDeg;

    private TextView tName;
    private TextView tFetchedTime;
    private TextView tCoordLat;
    private TextView tCoordLon;
    private TextView tTemperature;
    private TextView tWeatherMain;
    private TextView tWeatherDescription;
    private ImageView tPrettyIcon;
    private TextView tPressure;
    private TextView tHumidity;
    private TextView tWindSpeed;
    private TextView tWindDeg;

    public todayFragment() {
        // Required empty public constructor
    }

    public static todayFragment newInstance() {
        todayFragment fragment = new todayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_today, container, false);
        binding(v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        initialization(model);

        settingTexts();
    }

    private void initialization(SharedViewModel model) {
        cityID = model.getCommon().getAsLong(DatabaseHelper.CITY_ID);
        name = model.getCommon().getAsString(DatabaseHelper.NAME);
        lat = model.getCommon().getAsInteger(DatabaseHelper.LAT);
        lon = model.getCommon().getAsInteger(DatabaseHelper.LON);
        country = model.getCommon().getAsString(DatabaseHelper.COUNTRY);
        timezone = model.getCommon().getAsInteger(DatabaseHelper.TIMEZONE);
        sunrise = model.getCommon().getAsInteger(DatabaseHelper.SUNRISE);
        sunset = model.getCommon().getAsInteger(DatabaseHelper.SUNSET);

        timeText = model.getPresent().getAsString(DatabaseHelper.DT_TXT);
        temperature = model.getPresent().getAsInteger(DatabaseHelper.TEMPERATURE);
        tempMin = model.getPresent().getAsInteger(DatabaseHelper.TEMP_MIN);
        tempMax = model.getPresent().getAsInteger(DatabaseHelper.TEMP_MAX);
        pressure = model.getPresent().getAsInteger(DatabaseHelper.PRESSURE);
        humidity = model.getPresent().getAsInteger(DatabaseHelper.HUMIDITY);
        seaLevel = model.getPresent().getAsInteger(DatabaseHelper.SEA_LEVEL);
        grndLevel = model.getPresent().getAsInteger(DatabaseHelper.GRND_LEVEL);
        weatherMain = model.getPresent().getAsString(DatabaseHelper.WEATHER_MAIN);
        weatherDescription = model.getPresent().getAsString(DatabaseHelper.WEATHER_DESCRIPTION);
        weatherIcon = model.getPresent().getAsString(DatabaseHelper.WEATHER_ICON);
        windSpeed = model.getPresent().getAsInteger(DatabaseHelper.WIND_SPEED);
        windDeg = model.getPresent().getAsInteger(DatabaseHelper.WIND_DEG);
    }

    private void binding(View v) {
        tName = v.findViewById(R.id.name);
        tFetchedTime = v.findViewById(R.id.fetchedTime);
        tCoordLat = v.findViewById(R.id.coordLat);
        tCoordLon = v.findViewById(R.id.coordLon);
        tTemperature = v.findViewById(R.id.temperature);
        tWeatherMain = v.findViewById(R.id.weatherMain);
        tWeatherDescription = v.findViewById(R.id.weatherDescription);
        tPrettyIcon = v.findViewById(R.id.prettyIcon);
        tPressure = v.findViewById(R.id.pressure);
        tHumidity = v.findViewById(R.id.humidity);
        tWindSpeed = v.findViewById(R.id.windSpeed);
        tWindDeg = v.findViewById(R.id.windDeg);
    }

    private void settingTexts() {
        tName.setText(name);
        tFetchedTime.setText(timeText);
        tCoordLat.setText(String.format("Lat %d", lat));
        tCoordLon.setText(String.format("Lon %d", lon));
        tTemperature.setText(String.valueOf(temperature));
        tWeatherMain.setText(weatherMain);
        tWeatherDescription.setText(weatherDescription);
//        tPrettyIcon.setText(prett);
        tPressure.setText(String.format("Pressure %d", pressure));
        tHumidity.setText(String.format("Humidity %d", humidity));
        tWindSpeed.setText(String.format("Wind speed %d", windSpeed));
        tWindDeg.setText(String.format("Wind degree %d", windDeg));
    }
}
