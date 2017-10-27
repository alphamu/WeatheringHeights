package com.alimuzaffar.weatherapp;

import com.alimuzaffar.weatherapp.adapter.SearchAutoCompleteAdapter;
import com.alimuzaffar.weatherapp.util.DateUtils;
import com.alimuzaffar.weatherapp.util.StringHelper;
import com.alimuzaffar.weatherapp.util.WeatherHelper;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class WeatherApiUnitTest implements Constants {
    @Test
    public void googleAutocompleteApi_isCorrect() throws Exception {
        String url = SearchAutoCompleteAdapter.getLocationSearchUrl("Hello World");
        assertNotNull("Autocomplete Api URL was null", url);
        String expected = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=Hello+World&types=(regions)&sensor=false";
        assertTrue("Auto Complete Url Is Wrong", url.startsWith(expected));
    }

    @Test
    public void currentWeatherApi_isCorrect() throws Exception {
        String url = WeatherHelper.buildUrl(CURRENT_WEATHER_BY_ID_URL, "12345");
        String expected = "http://api.openweathermap.org/data/2.5/weather?id=12345";
        assertTrue("Current Weather Url Is Wrong", url.startsWith(expected));
    }

    @Test
    public void forecastWeatherApi_isCorrect() throws Exception {
        String url = WeatherHelper.buildUrl(FORECAST_BY_NAME_URL, "Sydney, AU");
        String expected = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Sydney%2C+AU";

        //use startsWith incase we change the units or api key. our tests won't break
        assertTrue("Forecast Weather By Name Url Is Wrong", url.startsWith(expected));

        url = WeatherHelper.buildUrl(FORECAST_BY_ID_URL, "12345");
        expected = "http://api.openweathermap.org/data/2.5/forecast/daily?id=12345";
        assertTrue("Forecast Weather By Id Url Is Wrong", url.startsWith(expected));

        url = WeatherHelper.buildUrl(FORECAST_BY_GEO_URL, "33.1", "181.1");
        expected = "http://api.openweathermap.org/data/2.5/forecast/daily?lat=33.1&lon=181.1";
        assertTrue("Forecast Weather By Id Url Is Wrong", url.startsWith(expected));
    }

    @Test
    public void dateUtilsFormat_isCorrect() throws Exception {
        long epoc = 1446885450; //7th Nov 2015
        Date date = DateUtils.epocSecondsToDate(epoc);
        assertEquals("Date time in millis is wrong", epoc * 1000, date.getTime());

        String day = DateUtils.dateToDayDateString(date, true);
        assertEquals("Day is wrong", "SAT", day);
    }

    @Test
    public void testInput_isGpsCoord() throws Exception {
        String input1 = "-33.867, 151.207";
        String input2 = "Hello World";
        String input3 = "-33.867, Hello";

        assertTrue("GPS Coords were not recognised", StringHelper.isGpsCoordinates(input1));
        assertFalse("GPS Coords were found where none exist", StringHelper.isGpsCoordinates(input2));
        assertFalse("GPS Coords were found where none exist", StringHelper.isGpsCoordinates(input3));

    }
}
