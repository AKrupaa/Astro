package com.example.astro;

import android.database.Cursor;

public class UtilAstro {
    //    private Context context;
//
//    public UtilAstro() {
//    }

    //    if exist returning true otherwise false
    public static boolean isCityExistInDB(long cityID, DBManager dbManager) {
        String tableName = DatabaseHelper.BRAND_NEW_COMMON_TABLE_NAME;
        Cursor cursor = dbManager.fetchIDs(tableName);
        cursor.moveToFirst();

        for (; !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getLong(cursor.getColumnIndex(DatabaseHelper.CITY_ID)) == cityID) {
                return true;
            }
        }
        return false;
    }

    // returning CITY_ID of found city otherwise -1
    public static long isCityExistInDB(String city, DBManager dbManager) {
        String tableName = DatabaseHelper.BRAND_NEW_COMMON_TABLE_NAME;
        Cursor cursor = dbManager.fetchNames(tableName);
        cursor.moveToFirst();

        for (; !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)).equals(city)) {
                long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.CITY_ID));
                return id;
            }
        }
        return -1;
    }

    public static boolean fetchFromDatabaseAndUpdateViewModel(long cityID, DBManager dbManager, SharedViewModel model) {

        Cursor cCommon = dbManager.fetchAllWhereID(cityID, DatabaseHelper.BRAND_NEW_COMMON_TABLE_NAME);
        Cursor cPresent = dbManager.fetchAllWhereID(cityID, DatabaseHelper.PRESENT_TABLE_NAME);
        Cursor cSecond = dbManager.fetchAllWhereID(cityID, DatabaseHelper.SECOND_DAY_TABLE_NAME);
        Cursor cThird = dbManager.fetchAllWhereID(cityID, DatabaseHelper.THIRD_DAY_TABLE_NAME);

        model.setCommon(DBManager.cursorRowToContentValues(cCommon));
        model.setPresent(DBManager.cursorRowToContentValues(cPresent));
        model.setSecondDay(DBManager.cursorRowToContentValues(cSecond));
        model.setThirdDay(DBManager.cursorRowToContentValues(cThird));

        return true;
    }
}
