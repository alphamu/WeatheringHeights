package com.alimuzaffar.weatherapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alimuzaffar.weatherapp.WeatherApplication;
import com.alimuzaffar.weatherapp.model.RecentLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Muzaffar on 6/11/2015.
 */
public class RecentLocationsHelper {
    private static final String TABLE_NAME = "locations";

    private static final String UPDATE_TIME = "last_update";
    private static final String LOCATION_NAME = "location_name";
    private static final String LOCATION_ID = "location_id";

    private static final String TEXT_TYPE = " TEXT NOT NULL";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    LOCATION_ID + " INTEGER PRIMARY KEY," +
                    UPDATE_TIME + " INTEGER," +
                    LOCATION_NAME + TEXT_TYPE + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void addLocationToDB(long locId, String location) {
        // Gets the data repository in write mode
        SQLiteDatabase db = WeatherAppDbHelper.getInstance(WeatherApplication.getAppContext()).getMyWritableDatabase();
        synchronized (db) {
            ContentValues values = new ContentValues();
            values.put(LOCATION_ID, locId);
            values.put(LOCATION_NAME, location);
            values.put(UPDATE_TIME, System.currentTimeMillis()/1000);

            db.replace(TABLE_NAME, null, values);
        }
    }

    public static RecentLocation getLocation(long locId) {
        WeatherAppDbHelper helper = WeatherAppDbHelper.getInstance(WeatherApplication.getAppContext());
        //we only use the writable database as performance is not an issue
        //and this one closes automatically when the app shuts down so we
        //dont have to worry about closing it.
        SQLiteDatabase db = helper.getWritableDatabase();
        synchronized (db) {
            String[] projection = {
                    LOCATION_ID,
                    LOCATION_NAME
            };
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,
                    LOCATION_ID + " = ?",
                    new String[]{String.valueOf(locId)},
                    null,
                    null,
                    null
            );

            if (c.moveToFirst()) {
                return new RecentLocation(c.getLong(0), c.getString(1));
            }

            return RecentLocation.ERROR;
        }
    }

    public static List<RecentLocation> getAllLocations() {
        WeatherAppDbHelper helper = WeatherAppDbHelper.getInstance(WeatherApplication.getAppContext());
        //we only use the writable database as performance is not an issue
        //and this one closes automatically when the app shuts down so we
        //dont have to worry about closing it.
        SQLiteDatabase db = helper.getWritableDatabase();
        synchronized (db) {
            String[] projection = {
                    LOCATION_ID,
                    LOCATION_NAME
            };
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    UPDATE_TIME + " DESC"
            );

            List<RecentLocation> locs = new ArrayList<>();
            if (c.moveToFirst()) {
                do {
                    locs.add(new RecentLocation(c.getLong(0), c.getString(1)));
                } while (c.moveToNext());
            }
            return locs;
        }
    }

    public static boolean deleteLocation(long locId) {
        WeatherAppDbHelper helper = WeatherAppDbHelper.getInstance(WeatherApplication.getAppContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        int result = db.delete(TABLE_NAME, LOCATION_ID + " = ?", new String[]{String.valueOf(locId)});
        return result > 0;
    }

}
