package com.alimuzaffar.weatherapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.Log;

/**
 * Created by Ali Muzaffar on 16/11/2015.
 */
public class WidgetDarkUpdateProvider extends WidgetUpdateProvider {
    @ColorInt
    public int getTextColor() {
        return Color.BLACK;
    }
}
