package com.alimuzaffar.weatherapp.model;

import java.io.Serializable;

/**
 * Created by Ali Muzaffar on 6/11/2015.
 */
public class RecentLocation implements Serializable {
    public static final RecentLocation ERROR = new RecentLocation(0,"Something went wrong.");
    long id;
    String name;

    public RecentLocation(long id, String name) {
        this.id = id;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return name;
    }
}
