package com.example.astro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//  https://developer.android.com/training/data-storage/sqlite

//  Once a database is created successfully its located in data/data//databases/ accessible from Android Device Monitor.

public class DatabaseHelper extends SQLiteOpenHelper {

    //     Database Information
    private static final String DB_NAME = "WEATHER.DB";

    //     database version
    private static final int DB_VERSION = 1;

    //     Table Name
    public static final String COMMON_TABLE_NAME = "COMMON";
    public static final String PRESENT_TABLE_NAME = "PRESENT";
    public static final String SECOND_DAY_TABLE_NAME = "SECOND_DAY_TABLE_NAME";
    public static final String THIRD_DAY_TABLE_NAME = "THIRD_DAY_TABLE_NAME";

    //     Table columns

    public static final String _ID = "_id";

    // COMMON
    public static final String CITY_ID = "CITY_ID";
    public static final String NAME = "NAME";
    public static final String LAT = "LAT";
    public static final String LON = "LON";
    public static final String COUNTRY = "COUNTRY";
    public static final String TIMEZONE = "TIMEZONE";
    public static final String SUNRISE = "SUNRISE";
    public static final String SUNSET = "SUNSET";

    //PRESENT/SECOND/THIRD DAY
    public static final String DT_TXT = "DT_TXT";
    public static final String TEMPERATURE = "TEMPERATURE";
    public static final String FEELS_LIKE = "FEELS_LIKE";
    public static final String TEMP_MIN = "TEMP_MIN";
    public static final String TEMP_MAX = "TEMP_MAX";
    public static final String PRESSURE = "PRESSURE";
    public static final String SEA_LEVEL = "SEA_LEVEL";
    public static final String GRND_LEVEL = "GRND_LEVEL";
    public static final String HUMIDITY = "HUMIDITY";
    public static final String WEATHER_MAIN = "WEATHER_MAIN";
    public static final String WEATHER_DESCRIPTION = "WEATHER_DESCRIPTION";
    public static final String WEATHER_ICON = "WEATHER_ICON";
    public static final String WIND_SPEED = "WIND_SPEED";
    public static final String WIND_DEG = "WIND_DEG";

    //     Creating table query
    private static final String CREATE_COMMON_TABLE_NAME =
            "create table" + " " + COMMON_TABLE_NAME + "("
                    + CITY_ID + " " + "TEXT,"
                    + NAME + " " + "TEXT,"
//                    + DATE_OF_INSERT + " " + "date,"
                    + LAT + " " + "INTEGER,"
                    + LON + " " + "INTEGER,"
                    + COUNTRY + " " + "TEXT,"
                    + TIMEZONE + " " + "INTEGER);";

    private static final String CREATE_PRESENT_TABLE_NAME =
            "create table" + " " + PRESENT_TABLE_NAME + "("
                    + CITY_ID + " " + "TEXT,"
                    + SUNRISE + " " + "INTEGER,"
                    + SUNSET + " " + "INTEGER,"
                    + DT_TXT + " " + "TEXT,"
                    + TEMPERATURE + " " + "INTEGER,"
                    + FEELS_LIKE + " " + "INTEGER,"
                    + TEMP_MIN + " " + "INTEGER,"
                    + TEMP_MAX + " " + "INTEGER,"
                    + PRESSURE + " " + "INTEGER,"
                    + HUMIDITY + " " + "INTEGER,"
                    + SEA_LEVEL + " " + "INTEGER,"
                    + GRND_LEVEL + " " + "INTEGER,"
                    + WEATHER_MAIN + " " + "TEXT,"
                    + WEATHER_DESCRIPTION + " " + "TEXT,"
                    + WEATHER_ICON + " " + "TEXT,"
                    + WIND_SPEED + " " + "INTEGER,"
                    + WIND_DEG + " " + "INTEGER);";

    private static final String CREATE_SECOND_DAY_TABLE_NAME =
            "create table" + " " + SECOND_DAY_TABLE_NAME + "("
                    + CITY_ID + " " + "TEXT,"
                    + SUNRISE + " " + "INTEGER,"
                    + SUNSET + " " + "INTEGER,"
                    + DT_TXT + " " + "TEXT,"
                    + TEMPERATURE + " " + "INTEGER,"
                    + FEELS_LIKE + " " + "INTEGER,"
                    + TEMP_MIN + " " + "INTEGER,"
                    + TEMP_MAX + " " + "INTEGER,"
                    + PRESSURE + " " + "INTEGER,"
                    + HUMIDITY + " " + "INTEGER,"
                    + SEA_LEVEL + " " + "INTEGER,"
                    + GRND_LEVEL + " " + "INTEGER,"
                    + WEATHER_MAIN + " " + "TEXT,"
                    + WEATHER_DESCRIPTION + " " + "TEXT,"
                    + WEATHER_ICON + " " + "TEXT,"
                    + WIND_SPEED + " " + "INTEGER,"
                    + WIND_DEG + " " + "INTEGER);";

    private static final String CREATE_THIRD_DAY_TABLE_NAME =
            "create table" + " " + THIRD_DAY_TABLE_NAME + "("
                    + CITY_ID + " " + "TEXT,"
                    + SUNRISE + " " + "INTEGER,"
                    + SUNSET + " " + "INTEGER,"
                    + DT_TXT + " " + "TEXT,"
                    + TEMPERATURE + " " + "INTEGER,"
                    + FEELS_LIKE + " " + "INTEGER,"
                    + TEMP_MIN + " " + "INTEGER,"
                    + TEMP_MAX + " " + "INTEGER,"
                    + PRESSURE + " " + "INTEGER,"
                    + HUMIDITY + " " + "INTEGER,"
                    + SEA_LEVEL + " " + "INTEGER,"
                    + GRND_LEVEL + " " + "INTEGER,"
                    + WEATHER_MAIN + " " + "TEXT,"
                    + WEATHER_DESCRIPTION + " " + "TEXT,"
                    + WEATHER_ICON + " " + "TEXT,"
                    + WIND_SPEED + " " + "INTEGER,"
                    + WIND_DEG + " " + "INTEGER);";



//    private static final String CREATE_TABLE =
//            "create table" + " " + TABLE_NAME + "("
//                    + _ID + " " + "INTEGER PRIMARY KEY AUTOINCREMENT,"
//                    + CITY_ID + " " + "TEXT,"
//                    + NAME + " " + "TEXT,"
////                    + DATE_OF_INSERT + " " + "date,"
//                    + LAT + " " + "INTEGER,"
//                    + LON + " " + "INTEGER,"
//                    + COUNTRY + " " + "TEXT,"
//                    + TIMEZONE + " " + "INTEGER,"
//                    + SUNRISE + " " + "INTEGER,"
//                    + SUNSET + " " + "INTEGER,"
//                    + DT_TXT + " " + "TEXT,"
//                    + TEMPERATURE + " " + "INTEGER,"
//                    + FEELS_LIKE + " " + "INTEGER,"
//                    + TEMP_MIN + " " + "INTEGER,"
//                    + TEMP_MAX + " " + "INTEGER,"
//                    + PRESSURE + " " + "INTEGER,"
//                    + HUMIDITY + " " + "INTEGER,"
//                    + SEA_LEVEL + " " + "INTEGER,"
//                    + GRND_LEVEL + " " + "INTEGER,"
//                    + WEATHER_MAIN + " " + "TEXT,"
//                    + WEATHER_DESCRIPTION + " " + "TEXT,"
//                    + WEATHER_ICON + " " + "TEXT,"
//                    + WIND_SPEED + " " + "INTEGER,"
//                    + WIND_DEG + " " + "INTEGER);";

    //    This takes the Context (e.g., an Activity)
    public DatabaseHelper(@Nullable Context context) {
//        When the application runs the first time – At this point, we do not yet have a database.
//        So we will have to create the tables, indexes, starter data, and so on.
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
        db.execSQL(COMMON_TABLE_NAME);
        db.execSQL(PRESENT_TABLE_NAME);
        db.execSQL(SECOND_DAY_TABLE_NAME);
        db.execSQL(THIRD_DAY_TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        Execute a single SQL statement that is NOT a SELECT or any other SQL statement that returns data.
        db.execSQL("DROP TABLE IF EXISTS " + COMMON_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PRESENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SECOND_DAY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + THIRD_DAY_TABLE_NAME);
        onCreate(db);
    }
}