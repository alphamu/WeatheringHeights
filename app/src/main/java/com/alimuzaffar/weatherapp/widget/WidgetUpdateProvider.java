package com.alimuzaffar.weatherapp.widget;

import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.widget.RemoteViews;

import com.alimuzaffar.weatherapp.R;

import java.util.List;

/**
 * Created by Ali Muzaffar on 16/11/2015.
 */
public class WidgetUpdateProvider extends AppWidgetProvider {
    private static final String ACTION_CLICK = "ACTION_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        Log.w("WeatherUpdateProvider", "onUpdate method called");

        // Get all ids
        if (appWidgetIds == null || appWidgetIds.length == 0) {
            ComponentName thisWidget = new ComponentName(context, WidgetUpdateProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            appWidgetIds = allWidgetIds;
        }

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(), WidgetUpdateService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        intent.putExtra("color", getTextColor());

        // Update the widgets via the service
        context.startService(intent);
    }

    @ColorInt
    public int getTextColor() {
        return Color.argb(254, 255, 255, 255);
    }
}
