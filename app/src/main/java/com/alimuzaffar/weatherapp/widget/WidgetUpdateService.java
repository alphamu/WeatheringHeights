package com.alimuzaffar.weatherapp.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.alimuzaffar.weatherapp.BuildConfig;
import com.alimuzaffar.weatherapp.Constants;
import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.activity.MainActivity;
import com.alimuzaffar.weatherapp.model.current.CurrentWeather;
import com.alimuzaffar.weatherapp.model.current.Main;
import com.alimuzaffar.weatherapp.model.current.Weather;
import com.alimuzaffar.weatherapp.model.forecast.Forecast;
import com.alimuzaffar.weatherapp.model.forecast.WeatherForecasts;
import com.alimuzaffar.weatherapp.util.AppSettings;
import com.alimuzaffar.weatherapp.util.WeatherHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Ali Muzaffar on 16/11/2015.
 */
public class WidgetUpdateService extends IntentService implements Constants {
    private static final String LOG = "WidgetUpdateService";

    int[] mDay = new int[]{R.id.day_one, R.id.day_two, R.id.day_three, R.id.day_four, R.id.day_five};
    int[] mMin = new int[]{R.id.day_one_min, R.id.day_two_min, R.id.day_three_min, R.id.day_four_min, R.id.day_five_min};
    int[] mMax = new int[]{R.id.day_one_max, R.id.day_two_max, R.id.day_three_max, R.id.day_four_max, R.id.day_five_max};
    int[] mForecast = new int[]{R.id.day_one_forecast, R.id.day_two_forecast, R.id.day_three_forecast, R.id.day_four_forecast, R.id.day_five_forecast};

    int mColor = 0;

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WidgetUpdateService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
                .getApplicationContext());

        int[] allWidgetIds = intent
                .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        mColor = intent.getIntExtra("color", -1);

        for (int widgetId : allWidgetIds) {
            // create some random data

            RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(),
                    R.layout.view_weather_layout);

            long locId = AppSettings.getInstance(getApplicationContext()).getLong(AppSettings.Key.CURRENT_LOCATION_ID);
            if (locId == 0) {
                //remoteViews.setViewVisibility(R.id.empty, View.VISIBLE);
                remoteViews.setTextViewText(R.id.temp_city, getString(R.string.help_text));
            } else {
                //remoteViews.setViewVisibility(R.id.empty, View.VISIBLE);
                remoteViews.setTextViewText(R.id.temp_city, getString(R.string.loading_please_wait));

                WeatherForecasts forecasts = WeatherHelper.getWeatherForLocationById(getApplicationContext(), locId);
                if (forecasts != null) {
                    //remoteViews.setViewVisibility(R.id.empty, View.GONE);
                    updateForecastViews(remoteViews, forecasts);
                    // Push update for this widget to the home screen
                }

                CurrentWeather weather = WeatherHelper.getCurrentWeatherForLocationById(getApplicationContext(), locId);
                if (weather != null) {
                    updateCurrentViews(remoteViews, weather);
                }

                if (forecasts != null || weather != null) {
                    Bundle appWidgetOptions = null;
                    AppWidgetProviderInfo appWidgetProviderInfo = appWidgetManager.getAppWidgetInfo(widgetId);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && appWidgetProviderInfo != null) {
                        appWidgetOptions = appWidgetManager.getAppWidgetOptions(widgetId);
                        getWidgetDimensions(appWidgetOptions,appWidgetProviderInfo);
                    }
                    setOnClickListenerOpenApp(remoteViews);
                    appWidgetManager.updateAppWidget(widgetId, remoteViews);
                }
            }

        }

    }


    private void updateCurrentViews(RemoteViews remoteViews, CurrentWeather result) {
        Weather weather = result.getWeather().get(0);
        Main main = result.getMain();
        String cityName = AppSettings.getInstance(getApplicationContext()).getString(AppSettings.Key.CURRENT_LOCATION_NAME);

        remoteViews.setTextViewText(R.id.temp_city, getString(R.string.temp_in_city, WeatherHelper.getFormattedTemperature(main.getTemp()), cityName));
        remoteViews.setTextViewText(R.id.today_forecast, weather.getMain());
        if (mColor != -1) {
            remoteViews.setTextColor(R.id.temp_city, mColor);
            remoteViews.setTextColor(R.id.today_forecast, mColor);
        }
        try {
            Bitmap bmp = Ion.with(getApplicationContext())
                    .load(weather.getIcon())
                    .withBitmap()
                    .asBitmap()
                    .get();
            remoteViews.setImageViewBitmap(R.id.temp_city_icon, bmp);
        } catch (Exception e) {
            Log.e(LOG, e.getMessage(), e);
        }
    }

    private void updateForecastViews(RemoteViews remoteViews, WeatherForecasts forecasts) {
        // Set the text
        for (int i = 0; i < mDay.length; i++) {
            Forecast f = forecasts.getForecasts().get(i);
            remoteViews.setTextViewText(mDay[i], f.getDateString(true));
            remoteViews.setTextViewText(mMin[i], WeatherHelper.getFormattedTemperature(f.getTemp().getMinTemp()));
            remoteViews.setTextViewText(mMax[i], WeatherHelper.getFormattedTemperature(f.getTemp().getMaxTemp()));
            remoteViews.setContentDescription(mForecast[i], f.getWeather().get(0).getMain());
            if (mColor != -1) {
                remoteViews.setTextColor(mDay[i], mColor);
                remoteViews.setTextColor(mMin[i], mColor);
                remoteViews.setTextColor(mMax[i], mColor);
            }
        }
        for (int i = 0; i < mForecast.length; i++) {
            try {
                Forecast f = forecasts.getForecasts().get(i);
                Bitmap bmp = Ion.with(getApplicationContext())
                        .load(f.getWeather().get(0).getIcon()).withBitmap().asBitmap().get();
                remoteViews.setImageViewBitmap(mForecast[i], bmp);
            } catch (Exception e) {
                Log.e(LOG, e.getMessage(), e);
            }
        }
    }

    private int mWidgetLandWidth, mWidgetLandHeight;
    private int mWidgetPortWidth, mWidgetPortHeight;
    private int mWidgetWidthPerOrientation, mWidgetHeightPerOrientation;
    private boolean mIsPortraitOrientation = true;
    private boolean mIsKeyguard;

    public int[] getWidgetDimensions(Bundle mAppWidgetOptions, AppWidgetProviderInfo providerInfo) {
         /* Get Device and Widget orientation.
           This is done by adding a boolean value to
           a port resource directory like values-port/bools.xml */

        mIsPortraitOrientation = getResources().getBoolean(R.bool.isPort);

        // Since min and max is usually the same, just take min
        mWidgetLandWidth = providerInfo.minWidth;
        mWidgetPortHeight = providerInfo.minHeight;
        mWidgetPortWidth = providerInfo.minWidth;
        mWidgetLandHeight = providerInfo.minHeight;

        if (mAppWidgetOptions != null
                && mAppWidgetOptions
                .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH) > 0) {
            if (BuildConfig.DEBUG)
                Log.d(LOG,
                        "appWidgetOptions not null, getting widget sizes...");
            // Reduce width by a margin of 8dp (automatically added by
            // Android, can vary with third party launchers)

            /* Actually Min and Max is a bit irritating,
               because it depends on the homescreen orientation
               whether Min or Max should be used: */

            mWidgetPortWidth = mAppWidgetOptions
                    .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            mWidgetLandWidth = mAppWidgetOptions
                    .getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
            mWidgetLandHeight = mAppWidgetOptions
                    .getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
            mWidgetPortHeight = mAppWidgetOptions
                    .getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

            // Get the value of OPTION_APPWIDGET_HOST_CATEGORY
            int category = mAppWidgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_HOST_CATEGORY, -1);

            // If the value is WIDGET_CATEGORY_KEYGUARD, it's a lockscreen
            // widget (dumped with Android-L preview :-( ).
            mIsKeyguard = category == AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD;

        } else {
            if (BuildConfig.DEBUG)
                Log.d(LOG,
                        "No AppWidgetOptions for this widget, using minimal dimensions from provider info!");
            // For some reason I had to set this again here, may be obsolete
            mWidgetLandWidth = providerInfo.minWidth;
            mWidgetPortHeight = providerInfo.minHeight;
            mWidgetPortWidth = providerInfo.minWidth;
            mWidgetLandHeight = providerInfo.minHeight;
        }

        if (BuildConfig.DEBUG)
            Log.d(LOG, "Dimensions of the Widget in DIP: portWidth =  "
                    + mWidgetPortWidth + ", landWidth = " + mWidgetLandWidth
                    + "; landHeight = " + mWidgetLandHeight
                    + ", portHeight = " + mWidgetPortHeight);

        // If device is in port oriantation, use port sizes
        mWidgetWidthPerOrientation = mWidgetPortWidth;
        mWidgetHeightPerOrientation = mWidgetPortHeight;

        if (!mIsPortraitOrientation)
        {
            // Not Portrait, so use landscape sizes
            mWidgetWidthPerOrientation = mWidgetLandWidth;
            mWidgetHeightPerOrientation = mWidgetLandHeight;
        }

        return new int[] {mWidgetWidthPerOrientation, mWidgetHeightPerOrientation};

    }


    private void setOnClickListenerOpenApp(RemoteViews remoteViews) {
        // Register an onClickListener
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent settingsPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1011, intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.layout_container, settingsPendingIntent);

    }


}
