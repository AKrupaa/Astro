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

                        // jezeli istnieje w bazie taki rekord -> aktualizuj
                        // inaczej dodaj.

                        long fetchedCityID = common.getAsLong(DatabaseHelper.CITY_ID);
                        DBManager dbManager = new DBManager(context);
                        dbManager.open();

                        if(UtilAstro.isCityExistInDB(fetchedCityID, dbManager)) {
                            // update
                            dbManager.update(fetchedCityID, DatabaseHelper.BRAND_NEW_COMMON_TABLE_NAME, common);
                            dbManager.update(fetchedCityID, DatabaseHelper.PRESENT_TABLE_NAME, present);
                            dbManager.update(fetchedCityID, DatabaseHelper.SECOND_DAY_TABLE_NAME, secondDay);
                            dbManager.update(fetchedCityID, DatabaseHelper.THIRD_DAY_TABLE_NAME, thirdDay);
                        } else {
                            // add
                            dbManager.insertToTable(DatabaseHelper.BRAND_NEW_COMMON_TABLE_NAME, common);
                            dbManager.insertToTable(DatabaseHelper.PRESENT_TABLE_NAME, present);
                            dbManager.insertToTable(DatabaseHelper.SECOND_DAY_TABLE_NAME, secondDay);
                            dbManager.insertToTable(DatabaseHelper.THIRD_DAY_TABLE_NAME, thirdDay);
                        }

                        dbManager.close();
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
