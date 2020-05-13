package com.example.astro;

import android.content.Context;
import android.database.Cursor;

import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilAstro {
//    private Context context;
//
    public UtilAstro() { }

//    if exist returning true otherwise false
    static boolean isCityExistInDB(long cityID, Context context) {
        DBManager dbManager = new DBManager(context);
        String whatever = DatabaseHelper.COMMON_TABLE_NAME;
        Cursor cursor = dbManager.fetchIDS(whatever);

        for (; !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getLong(cursor.getColumnIndex(DatabaseHelper.CITY_ID)) == cityID) {
                dbManager.close();
                return true;
            }
        }
        dbManager.close();
        return false;
    }

    // returning CITY_ID of found city otherwise -1
    static long isCityExistInDB(String city, Context context) {
        DBManager dbManager = new DBManager(context);
        Cursor cursor = dbManager.fetchWhereName(city, DatabaseHelper.COMMON_TABLE_NAME);

        for (; !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)).equals(city)) {
                dbManager.close();

                long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.CITY_ID));
                dbManager.close();
                return id;
            }
        }
        dbManager.close();
        return -1;
    }

    public boolean fetchFromDatabaseAndUpdateViewModel(String city, Context context, SharedViewModel model) {

        DBManager dbManager = new DBManager(context);
        dbManager.open();
        Cursor cCommon = dbManager.fetchAllWhereName(city, DatabaseHelper.COMMON_TABLE_NAME);
        Cursor cPresent = dbManager.fetchAllWhereName(city, DatabaseHelper.PRESENT_TABLE_NAME);
        Cursor cSecond = dbManager.fetchAllWhereName(city, DatabaseHelper.SECOND_DAY_TABLE_NAME);
        Cursor cThird = dbManager.fetchAllWhereName(city, DatabaseHelper.THIRD_DAY_TABLE_NAME);

        model.setCommon(DBManager.cursorRowToContentValues(cCommon));
        model.setPresent(DBManager.cursorRowToContentValues(cPresent));
        model.setSecondDay(DBManager.cursorRowToContentValues(cSecond));
        model.setThirdDay(DBManager.cursorRowToContentValues(cThird));

        return true;
    }


//    boolean doINeedToFetchFromInternet(int cityID, Cursor cur) {
//        cur.moveToFirst();
//
//        for (; !cur.isAfterLast(); cur.moveToNext()) {
//            // jezeli masz to miasto ...
//            // jezeli miasto w bazie danych == miasto ktore szuka uzytkownik
//            int value = cur.getInt(cur.getColumnIndex(DatabaseHelper.CITY_ID));
//            if (value == cityID) {
//                // to zgarnij date wpisu dla tego wlasnie miasta
//                String fetchedDate = cur.getString(cur.getColumnIndex(DatabaseHelper.DT_TXT));
////                "dt_txt": "2020-05-14 00:00:00"
//
//                // porownaj aktualny czas z tym w bazie danych
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//                Date dateFromDB = null;
//                try {
//                    dateFromDB = sdf.parse(fetchedDate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                String time = sdf.format(Calendar.getInstance().getTime());
//                Date now = null;
//
//                try {
//                    now = sdf.parse(time);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                long timeFromDB = dateFromDB.getTime();
//                long timeNow = now.getTime();
//
//                long timeDifferenceMilliseconds = Math.abs(timeFromDB - timeNow);
//
//                // jezeli dane nie są stare tj. minelo mniej niz 30 minut od ostatniego sprawdzenia to...
//                // GET FROM DATABASE
//                if (timeDifferenceMilliseconds < (1000 * 60 * 30))
//                    return false;
//
//            }
//        }
//
//        // FETCH FROM INTERNET
//        return true;
//    }

}
