package com.alimuzaffar.weatherapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherAppDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "weather.db";

    private static WeatherAppDbHelper sInstance;
    private static SQLiteDatabase myWritableDb;

    public static synchronized WeatherAppDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WeatherAppDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public WeatherAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    } 
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RecentLocationsHelper.SQL_CREATE_ENTRIES);
    } 
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is 
        // to simply to discard the data and start over 
        db.execSQL(RecentLocationsHelper.SQL_DELETE_ENTRIES);
        onCreate(db);
    } 
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Returns a writable database instance in order not to open and close many
     * SQLiteDatabase objects simultaneously
     *
     * @return a writable instance to SQLiteDatabase
     */
    public SQLiteDatabase getMyWritableDatabase() {
        if ((myWritableDb == null) || (!myWritableDb.isOpen())) {
            myWritableDb = this.getWritableDatabase();
        }

        return myWritableDb;
    }

    @Override
    public void close() {
        super.close();
        if (myWritableDb != null) {
            myWritableDb.close();
            myWritableDb = null;
        }
    }
} 
