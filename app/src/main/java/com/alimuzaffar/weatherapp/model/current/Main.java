package com.alimuzaffar.weatherapp.model.current;

import com.google.gson.annotations.SerializedName;

public class Main {

    private int humidity;
    private float pressure;
    @SerializedName("temp_max")
    private float tempMax;
    @SerializedName("temp_min")
    private float tempMin;
    private float temp;

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }
}

			
