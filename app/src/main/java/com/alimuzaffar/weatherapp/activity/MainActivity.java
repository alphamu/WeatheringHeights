package com.alimuzaffar.weatherapp.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.alimuzaffar.weatherapp.BuildConfig;
import com.alimuzaffar.weatherapp.Constants;
import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.fragment.NetworkHelper;
import com.alimuzaffar.weatherapp.fragment.ShowForecastFragment;
import com.alimuzaffar.weatherapp.model.current.CurrentWeather;
import com.alimuzaffar.weatherapp.util.AppSettings;
import com.alimuzaffar.weatherapp.util.WeatherHelper;
import com.alimuzaffar.weatherapp.widget.TransparentProgressDialog;
import com.alimuzaffar.weatherapp.widget.WidgetDarkUpdateProvider;
import com.alimuzaffar.weatherapp.widget.WidgetFullDarkUpdateProvider;
import com.alimuzaffar.weatherapp.widget.WidgetFullUpdateProvider;
import com.alimuzaffar.weatherapp.widget.WidgetUpdateProvider;

public class MainActivity extends AppCompatActivity implements Constants, ShowForecastFragment.OnShowForecastListener {
    private static final int REQ_CODE_LOCATION = 101;

    NetworkHelper mNetworkHelper;
    private TransparentProgressDialog mProgressIndicator;
    Runnable mProgressDelay = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, REQ_CODE_LOCATION);
                overridePendingTransition(R.anim.slide_in_from_right_animation, R.anim.slide_out_to_left_animation);
            }
        });
        if (isNotMock()) {
            setTitle(R.string.accessibility_app_name_welcome);
            fab.setContentDescription("Search and select a city");
        }

        //Check for network connectivity
        mNetworkHelper = (NetworkHelper) getSupportFragmentManager().findFragmentByTag(NetworkHelper.TAG);
        if (mNetworkHelper == null) {
            mNetworkHelper = NetworkHelper.newInstance();
            getSupportFragmentManager().beginTransaction().add(mNetworkHelper, NetworkHelper.TAG).commit();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (REQ_CODE_LOCATION == requestCode) {
                String location = data.getStringExtra(EXTRA_SELECTED_LOCATION_NAME);
                long locationId = data.getLongExtra(EXTRA_SELECTED_LOCATION_ID, 0);
                ShowForecastFragment f = (ShowForecastFragment) getFragmentManager().findFragmentById(R.id.showForecastFragment);
                if (f != null) {
                    if (location != null) {
                        f.getWeatherForLocation(location);
                    } else if (locationId != 0) {
                        f.getWeatherForLocationId(locationId);
                    }
                } else {
                    Log.e("MainActivity", "ShowForecastFragment was not found!");
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showProgressIndicator(boolean delay) {
        if (mProgressIndicator == null) {
            mProgressIndicator = new TransparentProgressDialog(this);
            getWindow().getDecorView().postDelayed(mProgressDelay = new Runnable() {
                @Override
                public void run() {
                    mProgressIndicator.show();
                    mProgressDelay = null;
                }
            }, delay ? 1000 : 0);

        }
    }


    public void hideProgressIndicator() {
        if (mProgressDelay != null) {
            getWindow().getDecorView().removeCallbacks(mProgressDelay);
        }
        if (mProgressIndicator != null) {
            mProgressIndicator.dismiss();
            mProgressIndicator = null;
        }
    }

    @Override
    public void onStartWeatherUpdate() {
        showProgressIndicator(true);
    }

    @Override
    public void onWeatherUpdateFinished(CurrentWeather weather) {
        hideProgressIndicator();
        String name = AppSettings.getInstance(this).getString(AppSettings.Key.CURRENT_LOCATION_NAME);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        String formattedTemp = WeatherHelper.getFormattedTemperature(weather.getMain().getTemp());
        collapsingToolbarLayout.setTitle(formattedTemp + " " + name);
        if (isNotMock()) {
            String formattedTempAccess = WeatherHelper.getFormattedTemperatureAccessibility(weather.getMain().getTemp());
            collapsingToolbarLayout.announceForAccessibility(String.format("The temperature in %s, is %s", name.replace("AU", "Australia"), formattedTempAccess));
        }
        updateAllWidgets();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem units = menu.findItem(R.id.change_units);
        units.setChecked(AppSettings.getInstance(this).getBoolean(AppSettings.Key.IS_FAHRENHEIT, false));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.change_units:
                boolean units = AppSettings.getInstance(this).getBoolean(AppSettings.Key.IS_FAHRENHEIT, false);
                AppSettings.getInstance(this).put(AppSettings.Key.IS_FAHRENHEIT, !units);
                item.setChecked(!units);
                ShowForecastFragment f = (ShowForecastFragment) getFragmentManager().findFragmentById(R.id.showForecastFragment);
                if (f != null) {
                    f.getWeatherForLocation();
                }

                //Update all widgets
                updateAllWidgets();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateAllWidgets() {
        updateWidgetsByProviderClass(WidgetUpdateProvider.class);
        updateWidgetsByProviderClass(WidgetDarkUpdateProvider.class);
        updateWidgetsByProviderClass(WidgetFullUpdateProvider.class);
        updateWidgetsByProviderClass(WidgetFullDarkUpdateProvider.class);
    }

    private void updateWidgetsByProviderClass(Class clazz) {
        ComponentName name = new ComponentName(this, clazz);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(name);
        if (ids != null && ids.length > 0) {
            Intent intent = new Intent(this, clazz);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            sendBroadcast(intent);
        }
    }

    public boolean isNotMock() {
        return !BuildConfig.FLAVOR.equals("mock");
    }
}
