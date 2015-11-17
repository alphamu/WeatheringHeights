package com.alimuzaffar.weatherapp;

import android.app.Application;
import android.content.Context;

import com.alimuzaffar.weatherapp.util.AppSettings;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class WeatherApplication extends Application {
    private static WeatherApplication mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        this.setAppContext(getApplicationContext());
    }

    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;
    }

    public static WeatherApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

}
