package com.alimuzaffar.weatherapp.model.current;

import com.alimuzaffar.weatherapp.Constants;

public class Weather
{
    private String icon;

    private String description;

    private String main;

    public String getIcon ()
    {
        return String.format(Constants.OPEN_WEATHER_ICON_URL, icon);
    }

    public void setIcon (String icon)
    {
        this.icon = icon;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getMain ()
    {
        return main;
    }

    public void setMain (String main)
    {
        this.main = main;
    }
}

			
