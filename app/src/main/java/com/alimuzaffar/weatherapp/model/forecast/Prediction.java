package com.alimuzaffar.weatherapp.model.forecast;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class Prediction {
    String id;
    String description;
    @SerializedName("structured_formatting")
    PredictionStructuredFormatting structuredFormatting;

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

    public PredictionStructuredFormatting getStructuredFormatting() {
        return structuredFormatting;
    }

    public void setStructuredFormatting(PredictionStructuredFormatting structuredFormatting) {
        this.structuredFormatting = structuredFormatting;
    }

    @Override
    public String toString() {
        if (structuredFormatting == null || TextUtils.isEmpty(structuredFormatting.mainText) || TextUtils.isEmpty(structuredFormatting.secondaryText)) {
            return description;
        } else {
            return structuredFormatting.mainText + ", " + structuredFormatting.secondaryText;
        }

    }
}
