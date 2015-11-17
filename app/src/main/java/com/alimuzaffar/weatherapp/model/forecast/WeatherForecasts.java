package com.alimuzaffar.weatherapp.model.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class WeatherForecasts {

    City city;
    @SerializedName("cnt")
    int count;
    @SerializedName("list")
    List<Forecast> forecasts;
    @SerializedName("cod")
    String code;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        this.forecasts = forecasts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean hasError() {
        return !code.equals("200") || this == ERROR;
    }

    public static final WeatherForecasts ERROR = new WeatherForecasts();
}
