package com.example.astro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class TomorrowFragment extends Fragment {

    private final String BASE_URL_IMAGE = "http://openweathermap.org/img/w/";
    private final String PNG = ".png";
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
    private TextView tPressure;
    private TextView tHumidity;
    private TextView tWindSpeed;
    private TextView tWindDeg;
    private NetworkImageView imageView;
    private ImageLoader imageLoader;

    public TomorrowFragment() {
        // Required empty public constructor
    }

    public static TomorrowFragment newInstance() {
        TomorrowFragment fragment = new TomorrowFragment();
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

        timeText = model.getSecondDay().getAsString(DatabaseHelper.DT_TXT);
        temperature = model.getSecondDay().getAsInteger(DatabaseHelper.TEMPERATURE);
        tempMin = model.getSecondDay().getAsInteger(DatabaseHelper.TEMP_MIN);
        tempMax = model.getSecondDay().getAsInteger(DatabaseHelper.TEMP_MAX);
        pressure = model.getSecondDay().getAsInteger(DatabaseHelper.PRESSURE);
        humidity = model.getSecondDay().getAsInteger(DatabaseHelper.HUMIDITY);
        seaLevel = model.getSecondDay().getAsInteger(DatabaseHelper.SEA_LEVEL);
        grndLevel = model.getSecondDay().getAsInteger(DatabaseHelper.GRND_LEVEL);
        weatherMain = model.getSecondDay().getAsString(DatabaseHelper.WEATHER_MAIN);
        weatherDescription = model.getSecondDay().getAsString(DatabaseHelper.WEATHER_DESCRIPTION);
        weatherIcon = model.getSecondDay().getAsString(DatabaseHelper.WEATHER_ICON);
        windSpeed = model.getSecondDay().getAsInteger(DatabaseHelper.WIND_SPEED);
        windDeg = model.getSecondDay().getAsInteger(DatabaseHelper.WIND_DEG);
    }

    private void binding(View v) {
        tName = v.findViewById(R.id.name);
        tFetchedTime = v.findViewById(R.id.fetchedTime);
        tCoordLat = v.findViewById(R.id.coordLat);
        tCoordLon = v.findViewById(R.id.coordLon);
        tTemperature = v.findViewById(R.id.temperature);
        tWeatherMain = v.findViewById(R.id.weatherMain);
        tWeatherDescription = v.findViewById(R.id.weatherDescription);
        tPressure = v.findViewById(R.id.pressure);
        tHumidity = v.findViewById(R.id.humidity);
        tWindSpeed = v.findViewById(R.id.windSpeed);
        tWindDeg = v.findViewById(R.id.windDeg);

        imageView = (NetworkImageView) v.findViewById(R.id.imageView);
    }

    @SuppressLint("DefaultLocale")
    private void settingTexts() {
        tName.setText(String.format("%s, %s", name, country));
        tFetchedTime.setText(timeText);

        if (lon < 0) {
            lon = Math.abs(lon);
            tCoordLon.setText(String.format("Lon %d W", lon));
        } else {

            tCoordLon.setText(String.format("Lon %d E", lon));
        }


        if (lat < 0) {
            lat = Math.abs(lat);
            tCoordLat.setText(String.format("Lat %d S", lat));
        } else {
            tCoordLat.setText(String.format("Lat %d N", lat));
        }

        tTemperature.setText(String.valueOf(temperature));
        tWeatherMain.setText(weatherMain);
        tWeatherDescription.setText(weatherDescription);

        tPressure.setText(String.format("Pressure %d hPa", pressure));
        tHumidity.setText(String.format("Humidity %d %%", humidity));
        tWindSpeed.setText(String.format("Wind speed %d meter/s", windSpeed));
        tWindDeg.setText(String.format("Wind degree %d°", windDeg));

        loadImage(weatherIcon);
    }

    private void loadImage(String textIcon) {
        String url = BASE_URL_IMAGE + textIcon.trim() + PNG;
        if (url.equals("")) {
            Toast.makeText(requireContext(), "Disconnected!", Toast.LENGTH_LONG).show();
            return;
        }


        imageLoader = ImageRequest.getInstance(getContext())
                .getImageLoader();
        imageLoader.get(url, ImageLoader.getImageListener(
                imageView,  // where
                0/*R.drawable.ic_launcher_background*/, // default
                android.R.drawable.ic_dialog_alert // error image
        ));
        imageView.setImageUrl(url, imageLoader); // load image
    }
}

