package com.alimuzaffar.weatherapp.model.forecast;

import java.io.Serializable;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class City implements Serializable {
    private long id;
    private String name;
    private String country;
    private Coord coord;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getDisplayString() {
        return String.format("%s, %s", name, country);
    }

    @Override
    public String toString() {
        return String.format("%s, %s", name, country);
    }
}
