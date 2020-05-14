package com.example.astro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.astro.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private EditText city;
    private EditText lon;
    private EditText lat;
    private Button confirm;
    private Button force;
    private WeatherForecast weatherForecast;
    private SharedViewModel sharedViewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        weatherForecast = new WeatherForecast(getApplicationContext(), sharedViewModel);
        initialization();
        hideProgressBar();

        setConfirm();
        setForce();
    }

    private void initialization() {
        city = (EditText) findViewById(R.id.city);
        lon = (EditText) findViewById(R.id.inputedLongitude);
        lat = (EditText) findViewById(R.id.inputedLatitude);
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
                String sCity = city.getText().toString();
                sCity = sCity.replaceAll(" ", "");

                final String finalSCity = sCity;
                weatherForecast.saveInternetWeatherContentInViewModel(sCity, new WeatherForecast.onSavedResponse() {
                    @Override
                    public void onSaved() {
                        // przejdz do nowej intencji
                        hideProgressBar();
                        // Navigate from MainActivity to OverviewActivity
                        Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
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
                        if(cityID < 0) {
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
                String sCity = city.getText().toString();
                sCity = sCity.replaceAll(" ", "");

                //working
                weatherForecast.saveInternetWeatherContentInViewModel(sCity, new WeatherForecast.onSavedResponse() {
                    //zablokuj ekran
                    @Override
                    public void onSaved() {
                        // odblokuj ekran
                        hideProgressBar();
                        // Navigate from MainActivity to OverviewActivity
                        Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
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
}
