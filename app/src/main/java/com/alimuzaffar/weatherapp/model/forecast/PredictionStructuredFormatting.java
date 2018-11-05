package com.alimuzaffar.weatherapp.model.forecast;

import com.google.gson.annotations.SerializedName;

public class PredictionStructuredFormatting {
    @SerializedName("main_text")
    String mainText;
    @SerializedName("secondary_text")
    String secondaryText;

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }
}
