package com.example.astro;

import android.content.ContentValues;
import android.content.Context;

import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

public class WeatherForecast {
    private Request request;
    private Context context;
    private SharedViewModel sharedViewModel;

    public WeatherForecast(Context context, SharedViewModel sharedViewModel) {
        this.context = context;
        this.sharedViewModel = sharedViewModel;
        request = new Request(context);
    }

    public void saveInternetWeatherContentInViewModel(String city, final onSavedResponse onSavedResponse) {
        request.getResponse(city, new Request.VolleyResponseCallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                request.parseResponse(response, new Request.VolleyParseCallback() {
                    @Override
                    public void onSuccessParse(ContentValues common, ContentValues present, ContentValues secondDay, ContentValues thirdDay) {
                        sharedViewModel.setCommon(common);
                        sharedViewModel.setPresent(present);
                        sharedViewModel.setSecondDay(secondDay);
                        sharedViewModel.setThirdDay(thirdDay);

                        // jezeli dzis jest rozny

                        onSavedResponse.onSaved();
                    }
                });
            }

            @Override
            public void onFailedResponse() {
                onSavedResponse.onFailed();
            }
        });
    }

    public interface onSavedResponse {
        void onSaved();
        void onFailed();
    }

}
