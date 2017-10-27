package com.alimuzaffar.weatherapp;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public interface Constants {
    //API key left in for convenience
    String AUTOCOMPLETE_API_URL="https://maps.googleapis.com/maps/api/place/autocomplete/json?input=%s&types=(regions)&sensor=false&key=";

    String OPEN_WEATHER_API = "http://api.openweathermap.org/data/2.5";
    String OPEN_WEATHER_API_UNITS = "&units=";
    String OPEN_WEATHER_API_KEY = "&appid=";
    String OPEN_WEATHER_API_MODE = "&mode=json";
    String OPEN_WEATHER_COMMON_PARAMS = OPEN_WEATHER_API_MODE;

    String OPEN_WEATHER_ICON_URL = "http://openweathermap.org/img/w/%s.png";

    String CURRENT_WEATHER_BY_ID_URL = OPEN_WEATHER_API + "/weather?id=%s" + OPEN_WEATHER_COMMON_PARAMS;
    String FORECAST_BY_NAME_URL = OPEN_WEATHER_API + "/forecast/daily?q=%s&cnt=5" + OPEN_WEATHER_COMMON_PARAMS;
    String FORECAST_BY_ID_URL = OPEN_WEATHER_API + "/forecast/daily?id=%s&cnt=5" + OPEN_WEATHER_COMMON_PARAMS;
    String FORECAST_BY_GEO_URL = OPEN_WEATHER_API + "/forecast/daily?lat=%s&lon=%s" + OPEN_WEATHER_COMMON_PARAMS;

    String EXTRA_SELECTED_LOCATION_NAME = "extra_selected_location";
    String EXTRA_SELECTED_LOCATION_ID = "extra_selected_location_id";

    String FORMAT_TEMP_C = "%.1f\u00b0C";
    String FORMAT_TEMP_F = "%.1f\u00b0F";
    String FORMAT_TEMP_C_ACCESSIBILITY = "%.1f degrees celsius";
    String FORMAT_TEMP_F_ACCESSIBILITY = "%.1f degrees fahrenheit";


}
