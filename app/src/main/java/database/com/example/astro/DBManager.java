package com.example.astro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBManager {
    private DatabaseHelper databaseHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    //    Before performing any database operations like insert, update, delete records in a table, first open the database connection
    public DBManager open() {
        databaseHelper = new DatabaseHelper(context);
//        Create and/or open a database that will be used for reading and writing.
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    //    Close any open database object.
    public void close() {
        databaseHelper.close();
    }

    /* Inserting new Record into Android SQLite database table
        Returning the primary key value of the new row
        or it will return -1 if there was an error inserting the data.
        This can happen if you have a conflict with pre-existing data in the database.
     */
    // Insert the new row, returning the primary key value of the new row
    public long insertToTable(String TABLE_NAME, ContentValues contentValues) {
        long newRowId = database.insert(TABLE_NAME, null, contentValues);
        return newRowId;
    }

    //    Read information from a database
    public Cursor fetchIDDate(String TABLE_NAME) {
//        Define a projection that specifies which columns from the database
//     you will actually use after this query.
        String[] projection = {
                DatabaseHelper.CITY_ID,
                DatabaseHelper.DT_TXT
        };

        Cursor cursor = database.query(
                TABLE_NAME,                     // The table to query
                projection,                     // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,               // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                DatabaseHelper.CITY_ID + " ASC"                    // The sort order
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchWhereID(long id, String TABLE_NAME) {
        String[] projection = {
                DatabaseHelper.CITY_ID
        };

        Cursor cursor = database.query(
                TABLE_NAME,      // The table to query
                projection,                     // The array of columns to return (pass null to get all)
                DatabaseHelper.CITY_ID + "=?",                   // The columns for the WHERE clause
                new String[] { String.valueOf(id) },               // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );

//        Cursor findEntry = db.query("sku_table", columns, "owner=? and price=?", new String[] { owner, price }, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchWhereName(String name, String TABLE_NAME) {

        String[] projection = {
                DatabaseHelper.NAME
        };

        Cursor cursor = database.query(
                TABLE_NAME,      // The table to query
                projection,                     // The array of columns to return (pass null to get all)
                DatabaseHelper.NAME + "=?",                   // The columns for the WHERE clause
                new String[] { name },               // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchAllWhereName(String name, String TABLE_NAME) {
        Cursor cursor = database.query(
                TABLE_NAME,      // The table to query
                null,                     // The array of columns to return (pass null to get all)
                DatabaseHelper.NAME + "=?",                   // The columns for the WHERE clause
                new String[] { name },               // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    //    Deleting a Record in Android SQLite database table
    public void delete(long _id, String TABLE_NAME) {
        database.delete(TABLE_NAME, DatabaseHelper.CITY_ID + "=" + _id, null);
    }

    //    Updating Record in Android SQLite database table
    //    Returning affected records
    public int update(long _id, String TABLE_NAME, ContentValues contentValues) {
//        contentValues.put(DatabaseHelper.CITY_ID, _id);
        int i = database.update(TABLE_NAME, contentValues, DatabaseHelper.CITY_ID + " = " + _id, null);
        return i;
    }

    public static ContentValues cursorRowToContentValues(Cursor cursor) {
        ContentValues values = new ContentValues();
        String[] columns = cursor.getColumnNames();
        int length = columns.length;
        for (int i = 0; i < length; i++) {
            switch (cursor.getType(i)) {
                case Cursor.FIELD_TYPE_NULL:
                    values.putNull(columns[i]);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    values.put(columns[i], cursor.getLong(i));
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    values.put(columns[i], cursor.getDouble(i));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    values.put(columns[i], cursor.getString(i));
                    break;
                case Cursor.FIELD_TYPE_BLOB:
                    values.put(columns[i], cursor.getBlob(i));
                    break;
            }
        }
        return values;
    }

}
