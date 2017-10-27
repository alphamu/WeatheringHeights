package com.alimuzaffar.weatherapp.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alimuzaffar.weatherapp.BuildConfig;
import com.alimuzaffar.weatherapp.Constants;
import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.db.RecentLocationsHelper;
import com.alimuzaffar.weatherapp.model.current.CurrentWeather;
import com.alimuzaffar.weatherapp.model.current.Main;
import com.alimuzaffar.weatherapp.model.current.Weather;
import com.alimuzaffar.weatherapp.model.forecast.City;
import com.alimuzaffar.weatherapp.model.forecast.Forecast;
import com.alimuzaffar.weatherapp.model.forecast.WeatherForecasts;
import com.alimuzaffar.weatherapp.util.AppSettings;
import com.alimuzaffar.weatherapp.util.StringHelper;
import com.alimuzaffar.weatherapp.util.WeatherHelper;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class ShowForecastFragment extends Fragment implements Constants {

    private OnShowForecastListener mListener;
    TextView mTempCity;
    ImageView mImgTempCity;
    TextView mForecastToday;
    TextView[] mDay = new TextView[5];
    ImageView[] mForecast = new ImageView[5];
    TextView[] mMin = new TextView[5];
    TextView[] mMax = new TextView[5];
    TextView mEmpty;

    View mTempCityContainer;
    View [] mDayContainer = new View[5];

    public ShowForecastFragment() {
        // Required empty public constructor
    }

    public static ShowForecastFragment newInstance() {
        return new ShowForecastFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_show_forecast, container, false);
        mTempCity = (TextView) v.findViewById(R.id.temp_city);
        mImgTempCity = (ImageView) v.findViewById(R.id.temp_city_icon);
        mTempCityContainer = v.findViewById(R.id.temp_city_container);
        mForecastToday = (TextView) v.findViewById(R.id.today_forecast);
        if (isNotMock()) {
            mTempCityContainer.setFocusable(true);
            mTempCityContainer.setFocusableInTouchMode(true);
            mTempCity.setFocusable(false);
            mImgTempCity.setFocusable(false);
        }

        mDay[0] = (TextView) v.findViewById(R.id.day_one);
        mDay[1] = (TextView) v.findViewById(R.id.day_two);
        mDay[2] = (TextView) v.findViewById(R.id.day_three);
        mDay[3] = (TextView) v.findViewById(R.id.day_four);
        mDay[4] = (TextView) v.findViewById(R.id.day_five);

        mMin[0] = (TextView) v.findViewById(R.id.day_one_min);
        mMin[1] = (TextView) v.findViewById(R.id.day_two_min);
        mMin[2] = (TextView) v.findViewById(R.id.day_three_min);
        mMin[3] = (TextView) v.findViewById(R.id.day_four_min);
        mMin[4] = (TextView) v.findViewById(R.id.day_five_min);

        mMax[0] = (TextView) v.findViewById(R.id.day_one_max);
        mMax[1] = (TextView) v.findViewById(R.id.day_two_max);
        mMax[2] = (TextView) v.findViewById(R.id.day_three_max);
        mMax[3] = (TextView) v.findViewById(R.id.day_four_max);
        mMax[4] = (TextView) v.findViewById(R.id.day_five_max);

        mForecast[0] = (ImageView) v.findViewById(R.id.day_one_forecast);
        mForecast[1] = (ImageView) v.findViewById(R.id.day_two_forecast);
        mForecast[2] = (ImageView) v.findViewById(R.id.day_three_forecast);
        mForecast[3] = (ImageView) v.findViewById(R.id.day_four_forecast);
        mForecast[4] = (ImageView) v.findViewById(R.id.day_five_forecast);

        mDayContainer[0] = v.findViewById(R.id.day1_container);
        mDayContainer[1] = v.findViewById(R.id.day2_container);
        mDayContainer[2] = v.findViewById(R.id.day3_container);
        mDayContainer[3] = v.findViewById(R.id.day4_container);
        mDayContainer[4] = v.findViewById(R.id.day5_container);

        if (isNotMock()) {
            for (View c : mDayContainer) {
                c.setFocusable(true);
                c.setFocusableInTouchMode(true);
            }
        }

        mEmpty = (TextView) v.findViewById(R.id.empty);

        getWeatherForLocation();

        try {
            mListener = (OnShowForecastListener) getActivity();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(getActivity().getClass().getSimpleName() + " should be an instance of OnShowForecastListener.");
        }

        return v;
    }

    public void getWeatherForLocation() {
        long locId = AppSettings.getInstance(getActivity()).getLong(AppSettings.Key.CURRENT_LOCATION_ID);
        if (locId == 0) {
            mEmpty.setVisibility(View.VISIBLE);
            mEmpty.setText(R.string.help_text);
        } else {
            mEmpty.setVisibility(View.VISIBLE);
            mEmpty.setText(R.string.loading_please_wait);
            getWeatherForLocationId(locId);
        }
    }

    public void getWeatherForLocationId(long locId) {
        FutureCallback<WeatherForecasts> callback = new FutureCallback<WeatherForecasts>() {
            @Override
            public void onCompleted(Exception e, WeatherForecasts result) {
                if (e == null && !result.hasError()) {
                    showWeatherForecasts(result);
                } else {
                    //handle error?
                }
            }
        };
        WeatherHelper.getWeatherForLocationById(getActivity(), locId, callback);

    }

    public void getWeatherForLocation(String location) {
        mListener.onStartWeatherUpdate();
        FutureCallback<WeatherForecasts> callback = new FutureCallback<WeatherForecasts>() {
            @Override
            public void onCompleted(Exception e, WeatherForecasts result) {
                if (e == null && !result.hasError()) {
                    showWeatherForecasts(result);
                } else {
                    //handle error?
                }
            }
        };
        if (!StringHelper.isGpsCoordinates(location)) {
            WeatherHelper.getWeatherForLocationByName(getActivity(), location, callback);
        } else {
            String[] geo = StringHelper.convertToGpsCoordinates(location);
            WeatherHelper.getWeatherForLocationByGeo(getActivity(), geo[0], geo[1], callback);
        }
    }

    private void showWeatherForecasts(WeatherForecasts forecasts) {
        City city = forecasts.getCity();
        AppSettings.getInstance(getActivity()).put(AppSettings.Key.CURRENT_LOCATION_NAME, city.getDisplayString());
        AppSettings.getInstance(getActivity()).put(AppSettings.Key.CURRENT_LOCATION_ID, city.getId());
        showCurrentWeather();
        RecentLocationsHelper.addLocationToDB(city.getId(), city.getDisplayString());
        for (int i = 0; i < mDay.length; i++) {
            Forecast f = forecasts.getForecasts().get(i);
            mDay[i].setText(f.getDateString(true));
            mMin[i].setText(WeatherHelper.getFormattedTemperature(f.getTemp().getMinTemp()));
            mMax[i].setText(WeatherHelper.getFormattedTemperature(f.getTemp().getMaxTemp()));
            if (f.getWeather().size() > 0) {
                mForecast[i].setContentDescription(f.getWeather().get(0).getMain());
                Ion.with(mForecast[i])
                        .load(f.getWeather().get(0).getIcon());
            }
            if (isNotMock()) {
                mDayContainer[i].setContentDescription(
                        String.format("In %s, on %s, it's %s with a low of %s and a high of %s.",
                                city.getName(),
                                f.getDay(),
                                mForecast[i].getContentDescription(),
                                f.getMain().getMinTemp(),
                                f.getMain().getMaxTemp()
                        ));
            }
        }
    }

    private void showCurrentWeather() {
        long cityId = AppSettings.getInstance(getActivity()).getLong(AppSettings.Key.CURRENT_LOCATION_ID);
        if (cityId != 0) {
            WeatherHelper.getCurrentWeatherForLocationById(getActivity(), cityId, new FutureCallback<CurrentWeather>() {
                @Override
                public void onCompleted(Exception e, CurrentWeather result) {
                    mListener.onWeatherUpdateFinished(result);
                    if (e == null && result.getCod() == 200 && result.getWeather().size() > 0) {
                        mEmpty.setVisibility(View.GONE);
                        Weather weather = result.getWeather().get(0);
                        Main main = result.getMain();
                        String cityName = AppSettings.getInstance(getActivity()).getString(AppSettings.Key.CURRENT_LOCATION_NAME);
                        mTempCity.setText(getString(R.string.temp_in_city, WeatherHelper.getFormattedTemperature(main.getTemp()), cityName));
                        mForecastToday.setText(weather.getMain());
                        Ion.with(mImgTempCity)
                                .load(weather.getIcon());
                        if (isNotMock()) {
                            String cityNameDescription = cityName.replace("AU", "Australia");
                            mTempCityContainer.setContentDescription(
                                    String.format("In %s, it's %s and %s",
                                            cityNameDescription,
                                            weather.getDescription(),
                                            WeatherHelper.getFormattedTemperatureAccessibility(main.getTemp()))
                            );
                            mForecastToday.setContentDescription(String.format("It's %s today", weather.getDescription()));
                        }
                    }
                }
            });
        }
    }

    public boolean isNotMock() {
        return !BuildConfig.FLAVOR.equals("mock");
    }

    public interface OnShowForecastListener {
        void onStartWeatherUpdate();

        void onWeatherUpdateFinished(CurrentWeather weather);
    }
}
