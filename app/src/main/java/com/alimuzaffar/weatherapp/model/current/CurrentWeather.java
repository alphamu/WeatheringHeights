package com.alimuzaffar.weatherapp.model.current;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CurrentWeather implements Serializable {
    private long id;
    @SerializedName("dt")
    private long epoc;
    private Clouds clouds;

    private Wind wind;

    private int cod;

    private List<Weather> weather;
    private Main main;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEpoc() {
        return epoc;
    }

    public void setEpoc(long epoc) {
        this.epoc = epoc;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public List<Weather> getWeather() {
        if (weather == null) {
            weather = new ArrayList<>();
        }
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public static class Clouds implements Serializable {
        private String all;

        public String getAll() {
            return all;
        }

        public void setAll(String all) {
            this.all = all;
        }

    }
}

			
