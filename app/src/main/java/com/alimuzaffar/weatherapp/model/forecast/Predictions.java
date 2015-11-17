package com.alimuzaffar.weatherapp.model.forecast;

import android.text.TextUtils;

import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.WeatherApplication;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali Muzaffar on 5/11/2015.
 */
public class Predictions {
    public static final Predictions ERROR = new Predictions();
    static  {
        ERROR.status = Status.ERROR.name();
        ERROR.errorMessage = WeatherApplication.getAppContext().getString(R.string.error_unknown);
    }

    private List<Prediction> predictions;
    private String status;
    @SerializedName("error_message")
    private String errorMessage;

    public List<Prediction> getPredictions() {
        //Avoid returning nulls where possible.
        if (predictions == null) {
            return new ArrayList<>();
        }
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public Status getStatus() {
        if (TextUtils.isEmpty(status)) {
            return Status.ERROR;
        } else {
            if (status.equals(Status.OK.name())) {
                return Status.OK;
            } else if (status.equals(Status.REQUEST_DENIED.name())) {
                return Status.REQUEST_DENIED;
            } else if (status.equals(Status.ZERO_RESULTS.name())) {
                return Status.ZERO_RESULTS;
            } else {
                return Status.ERROR;
            }
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCount() {
        if (predictions != null) {
            return predictions.size();
        }
        return 0;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null && errorMessage.length() > 0;
    }

    public enum Status {
        ZERO_RESULTS, OK, REQUEST_DENIED, ERROR
    }
}
