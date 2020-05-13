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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        weatherForecast = new WeatherForecast(getApplicationContext(), sharedViewModel);
        initialization();

        setConfirm();
        setForce();
    }

    private void initialization() {
        city = findViewById(R.id.city);
        lon = findViewById(R.id.inputedLongitude);
        lat = findViewById(R.id.inputedLatitude);
        confirm = findViewById(R.id.buttonConfirm);
        force = findViewById(R.id.buttonForce);
    }

    private void setConfirm() {
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pobieraj, nie patyczkuj się
                String sCity = city.getText().toString();
                sCity = sCity.replaceAll(" ", "");

                final String finalSCity = sCity;
                weatherForecast.saveInternetWeatherContentInViewModel(sCity, new WeatherForecast.onSavedResponse() {
                    @Override
                    public void onSaved() {
                        // przejdz do nowej intencji
                    }

                    @Override
                    public void onFailed() {
                        // sprobuj baze danych
                        // nie -> blad
                        // tak -> przejdz
                        long cityID = UtilAstro.isCityExistInDB(finalSCity, getApplicationContext());
                        if(cityID < 0) {
                            Toast.makeText(getApplicationContext(), "Offline: brak danych", Toast.LENGTH_SHORT).show();
                        } else {
                            // umiesc dane w ViewModel
                            // pobierz dane z bazy i dopiero zaaktualizuj
                            UtilAstro utilAstro = new UtilAstro();
                            utilAstro.fetchFromDatabaseAndUpdateViewModel(finalSCity, getApplicationContext(), sharedViewModel);

                            // przejdz do instancji
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
                String sCity = city.getText().toString();
                sCity = sCity.replaceAll(" ", "");

                //working
                weatherForecast.saveInternetWeatherContentInViewModel(sCity, new WeatherForecast.onSavedResponse() {
                    //zablokuj ekran
                    @Override
                    public void onSaved() {
                        // do dypozycji SharedViewModel
                        Intent intent = new Intent();

                        // odblokuj ekran
                    }

                    @Override
                    public void onFailed() {
                        // odblokuj ekran
                        // zglos blad jakis tam dodatkowy. kurwa nie wiem, wymysl cos
                        Toast.makeText(getApplicationContext(), "Sprawdz dane/Polaczenie z Internetem", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
