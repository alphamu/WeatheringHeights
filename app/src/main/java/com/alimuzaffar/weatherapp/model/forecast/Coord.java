package com.alimuzaffar.weatherapp.model.forecast;

import java.io.Serializable;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class Coord implements Serializable {
    private float lat;
    private float lng;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
