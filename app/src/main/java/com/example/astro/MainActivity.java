package com.example.astro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String MY_PREFERENCES = "MY_PREFERENCES";
    public static final String LAST_CITY_KEY = "LAST_CITY_KEY";
    public static final String INPUTED_TIME = "INPUTED_TIME";
    private EditText city;
    private Button confirm;
    private Button force;
    private WeatherForecast weatherForecast;
    private SharedViewModel sharedViewModel;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        weatherForecast = new WeatherForecast(getApplicationContext(), sharedViewModel);
        initialization();
        hideProgressBar();

        Context context = getApplicationContext();

//        You can create a new shared preference file or access an existing one by calling method
        sharedPreferences = context.getSharedPreferences(MY_PREFERENCES, context.MODE_PRIVATE);

//        int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);
        city.setText(sharedPreferences.getString(LAST_CITY_KEY, ""));

        spinner = findViewById(R.id.spinnerTime);
        addItemsToTimeSpinner();

        setConfirm();
        setForce();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initialization() {
        city = (EditText) findViewById(R.id.city);
        confirm = (Button) findViewById(R.id.buttonConfirm);
        force = (Button) findViewById(R.id.buttonForce);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setConfirm() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                // pobieraj, nie patyczkuj się
                final String sCity = city.getText().toString();

                final String finalSCity = sCity;
                weatherForecast.saveInternetWeatherContentInViewModel(sCity, new WeatherForecast.onSavedResponse() {
                    @Override
                    public void onSaved() {
                        // przejdz do nowej intencji
                        hideProgressBar();
                        // Navigate from MainActivity to OverviewActivity
                        Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
                        intent.putExtra(DatabaseHelper.CITY_ID, sharedViewModel.getCommon().getAsLong(DatabaseHelper.CITY_ID));

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(LAST_CITY_KEY, sCity);
                        editor.apply(); // writes the updates to disk asynchronously

                        String readTime = String.valueOf(spinner.getSelectedItem()).substring(0, 2);
                        intent.putExtra(INPUTED_TIME, readTime);

                        startActivity(intent);
                    }

                    @Override
                    public void onFailed() {
                        // sprobuj baze danych
                        // nie -> blad
                        // tak -> przejdz
                        DBManager dbManager = new DBManager(getApplicationContext());
                        dbManager.open();
                        long cityID = UtilAstro.isCityExistInDB(finalSCity, dbManager);
                        if (cityID < 0) {
                            Toast.makeText(getApplicationContext(), "Offline: brak danych", Toast.LENGTH_SHORT).show();
                            hideProgressBar();
                        } else {
                            // umiesc dane w ViewModel
                            // pobierz dane z bazy i dopiero zaaktualizuj
                            UtilAstro.fetchFromDatabaseAndUpdateViewModel(cityID, dbManager, sharedViewModel);
                            // przejdz do instancji
                            hideProgressBar();
                            // Navigate from MainActivity to OverviewActivity
                            Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
                            intent.putExtra(DatabaseHelper.CITY_ID, sharedViewModel.getCommon().getAsLong(DatabaseHelper.CITY_ID));

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(LAST_CITY_KEY, sCity);
                            editor.apply(); // writes the updates to disk asynchronously

                            String readTime = String.valueOf(spinner.getSelectedItem()).substring(0, 2);
                            intent.putExtra(INPUTED_TIME, readTime);

                            startActivity(intent);
                        }
                    }
                });

            }
        });
    }

    private void setForce() {
        force.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                final String sCity = city.getText().toString();

                //working
                weatherForecast.saveInternetWeatherContentInViewModel(sCity, new WeatherForecast.onSavedResponse() {
                    //zablokuj ekran
                    @Override
                    public void onSaved() {
                        // odblokuj ekran
                        hideProgressBar();
                        // Navigate from MainActivity to OverviewActivity
                        Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
                        intent.putExtra(DatabaseHelper.CITY_ID, sharedViewModel.getCommon().getAsLong(DatabaseHelper.CITY_ID));

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(LAST_CITY_KEY, sCity);
                        editor.apply(); // writes the updates to disk asynchronously

                        String readTime = String.valueOf(spinner.getSelectedItem()).substring(0, 2);
                        intent.putExtra(INPUTED_TIME, readTime);

                        startActivity(intent);
                    }

                    @Override
                    public void onFailed() {
                        // odblokuj ekran
                        // zglos blad jakis tam dodatkowy. kurwa nie wiem, wymysl cos
                        Toast.makeText(getApplicationContext(), "Sprawdz dane/Polaczenie z Internetem", Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                });
            }
        });
    }

    private void addItemsToTimeSpinner() {
        List<String> timeDelayList = new ArrayList<String>();
        timeDelayList.add("1 minuta");
        timeDelayList.add("2 minuty");
        timeDelayList.add("5 minut");
        timeDelayList.add("10 minut");
        timeDelayList.add("30 minut");
//        Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, timeDelayList);
//        Specify the layout to use when the list of choices appears
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Apply the adapter to the spinner
        spinner.setAdapter(dataAdapter);
    }
}
