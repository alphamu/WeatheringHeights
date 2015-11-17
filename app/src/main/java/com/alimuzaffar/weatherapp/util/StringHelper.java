package com.alimuzaffar.weatherapp.util;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class StringHelper {

    public static boolean isGpsCoordinates(String location) {
        try {
            String[] split = location.replace("Search Lat: ", "").replace("Lon: ", "").split(",");
            if (split.length == 2) {
                Double.valueOf(split[0].trim());
                Double.valueOf(split[1].trim());
                return true;
            }
            return false;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static String[] convertToGpsCoordinates(String location) {
        try {
            String[] split = location.replace("Search Lat: ", "").replace("Lon: ", "").split(",");
            if (split.length == 2) {
                split[0] = split[0].trim();
                split[1] = split[1].trim();
                return split;
            }
            return null;
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}
