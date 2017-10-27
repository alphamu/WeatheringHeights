package com.alimuzaffar.weatherapp.model.forecast;

import com.alimuzaffar.weatherapp.Constants;
import com.alimuzaffar.weatherapp.util.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class Forecast implements Serializable {

    @SerializedName("dt")
    long epoc;
    private Temp temp;
    private List<Weather> weather;
    private float pressure;
    private float speed;
    private float raisn;
    private int deg;
    private int clouds;
    private int humidity;

    transient Date date;

    public Date getDate() {
        if (date == null) {
            date = DateUtils.epocSecondsToDate(epoc);
        }
        return date;
    }

    public String getDateString(boolean useShortFormat) {
        if (date == null) {
            date = DateUtils.epocSecondsToDate(epoc);
        }
        return DateUtils.dateToDayDateString(date, useShortFormat);
    }

    public String getDay() {
        return DateUtils.getDay(date);
    }

    public long getEpoc() {
        return epoc;
    }

    public void setEpoc(long epoc) {
        this.epoc = epoc;
    }

    public Temp getMain() {
        return temp;
    }

    public void setMain(Temp temp) {
        this.temp = temp;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getRaisn() {
        return raisn;
    }

    public void setRaisn(float raisn) {
        this.raisn = raisn;
    }

    public int getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public static class Temp implements Serializable {
        @SerializedName("min")
        private float minTemp;
        @SerializedName("max")
        private float maxTemp;

        public float getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }

        public float getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }

    }

    public static class Weather implements Serializable {
        String main;
        String description;
        String icon;

        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return String.format(Constants.OPEN_WEATHER_ICON_URL, icon);
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

}
