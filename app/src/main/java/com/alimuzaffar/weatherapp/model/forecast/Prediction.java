package com.alimuzaffar.weatherapp.model.forecast;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class Prediction {
    String id;
    String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
