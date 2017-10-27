package com.alimuzaffar.weatherapp.util;

import android.content.Context;
import android.util.Log;

import com.alimuzaffar.weatherapp.Constants;
import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.WeatherApplication;
import com.alimuzaffar.weatherapp.model.current.CurrentWeather;
import com.alimuzaffar.weatherapp.model.forecast.WeatherForecasts;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.net.URLEncoder;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class WeatherHelper implements Constants {

    public static void getWeatherForLocationByName(Context context, String location, FutureCallback<WeatherForecasts> callback) {
        try {
            String url = buildUrl(FORECAST_BY_NAME_URL, location);
            getWeatherForUrl(context, url, callback);
        } catch (Exception e) {
            Log.e("WeatherHelper", e.getMessage(), e);
        }
    }

    public static void getWeatherForLocationById(Context context, long id, FutureCallback<WeatherForecasts> callback) {
        String url = buildUrl(FORECAST_BY_ID_URL, id);
        getWeatherForUrl(context, url, callback);
    }

    public static WeatherForecasts getWeatherForLocationById(Context context, long id) {
        String url = buildUrl(FORECAST_BY_ID_URL, id);
        return getWeatherForUrl(context, url, null);
    }

    public static void getWeatherForLocationByGeo(Context context, String lat, String lon, FutureCallback<WeatherForecasts> callback) {
        String url = buildUrl(FORECAST_BY_GEO_URL, lat, lon);
        getWeatherForUrl(context, url, callback);
    }

    private static WeatherForecasts getWeatherForUrl(Context context, String url, FutureCallback<WeatherForecasts> callback) {
        //Do this in the background, so we need a callback to return the result
        //when it is available
        try {
            if (callback != null) {
                Ion.with(context)
                        .load(url).as(new TypeToken<WeatherForecasts>() {
                }).setCallback(callback);
            } else {
                return Ion.with(context)
                        .load(url).as(new TypeToken<WeatherForecasts>() {
                }).get();
            }
        } catch (Exception e) {
            Log.e("WeatherHelper", e.getMessage(), e);
        }

        return null;
    }

    public static CurrentWeather getCurrentWeatherForLocationById(Context context, long id, FutureCallback<CurrentWeather> callback) {
        StringBuilder builder = new StringBuilder(String.format(CURRENT_WEATHER_BY_ID_URL, String.valueOf(id)));
        addUnitParam(builder);
        String url = builder.toString();
        try {
            if (callback != null) {
                Ion.with(context)
                        .load(url).as(new TypeToken<CurrentWeather>() {
                }).setCallback(callback);
            } else {
                return Ion.with(context)
                        .load(url).as(new TypeToken<CurrentWeather>() {
                }).get();
            }

        } catch (Exception e) {
            Log.e("WeatherHelper", e.getMessage(), e);
        }
        return null;
    }

    public static CurrentWeather getCurrentWeatherForLocationById(Context context, long id) {
        return getCurrentWeatherForLocationById(context, id, null);
    }

    public static String buildUrl(String urlTemplate, String location) {
        try {
            StringBuilder builder = new StringBuilder(String.format(urlTemplate, URLEncoder.encode(location, "UTF-8")));
            addUnitParam(builder);
            return builder.toString();
        } catch (Exception e) {
            return FORECAST_BY_NAME_URL;
        }
    }

    public static String buildUrl(String urlTemplate, long location) {
        return buildUrl(urlTemplate, String.valueOf(location));
    }

    public static String buildUrl(String urlTemplate, String lat, String lon) {
        try {
            StringBuilder builder = new StringBuilder(String.format(urlTemplate,
                    URLEncoder.encode(lat, "UTF-8"),
                    URLEncoder.encode(lon, "UTF-8")));
            addUnitParam(builder);
            return builder.toString();
        } catch (Exception e) {
            return FORECAST_BY_NAME_URL;
        }
    }

    private static void addUnitParam(StringBuilder builder) {
        builder.append(Constants.OPEN_WEATHER_API_KEY).append(WeatherApplication.getAppContext().getString(R.string.openweather_api_key));
        boolean useF = AppSettings.getInstance(WeatherApplication.getAppContext()).getBoolean(AppSettings.Key.IS_FAHRENHEIT);
        if (useF) {
            builder.append(Constants.OPEN_WEATHER_API_UNITS).append("imperial");
        } else {
            builder.append(Constants.OPEN_WEATHER_API_UNITS).append("metric");
        }
    }

    public static String getFormattedTemperature(float temp) {
        boolean useF = AppSettings.getInstance(WeatherApplication.getAppContext()).getBoolean(AppSettings.Key.IS_FAHRENHEIT);
        if (useF) {
            return String.format(Constants.FORMAT_TEMP_F, temp);
        } else {
            return String.format(Constants.FORMAT_TEMP_C, temp);
        }
    }

    public static String getFormattedTemperatureAccessibility(float temp) {
        boolean useF = AppSettings.getInstance(WeatherApplication.getAppContext()).getBoolean(AppSettings.Key.IS_FAHRENHEIT);
        if (useF) {
            return String.format(Constants.FORMAT_TEMP_F_ACCESSIBILITY, temp);
        } else {
            return String.format(Constants.FORMAT_TEMP_C_ACCESSIBILITY, temp);
        }
    }

}
