package com.alimuzaffar.weatherapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.alimuzaffar.weatherapp.Constants;
import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.fragment.LocationSearchFragment;
import com.alimuzaffar.weatherapp.fragment.NetworkHelper;

public class SearchActivity extends AppCompatActivity implements LocationSearchFragment.OnLocationSelectedListener, Constants {


    NetworkHelper mNetworkHelper;
    LocationSearchFragment mLocationSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //Check for network connectivity
        mNetworkHelper = (NetworkHelper) getSupportFragmentManager().findFragmentByTag(NetworkHelper.TAG);
        if (mNetworkHelper == null) {
            mNetworkHelper = NetworkHelper.newInstance();
            getSupportFragmentManager().beginTransaction().add(mNetworkHelper, NetworkHelper.TAG).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_to_right_animation);
    }


    @Override
    public void onLocationSelected(String location) {
        Intent i = new Intent();
        i.putExtra(EXTRA_SELECTED_LOCATION_NAME, location);
        setResult(Activity.RESULT_OK, i);
        overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_to_right_animation);
        finish();
    }

    @Override
    public void onLocationSelected(long locId) {
        Intent i = new Intent();
        i.putExtra(EXTRA_SELECTED_LOCATION_ID, locId);
        setResult(Activity.RESULT_OK, i);
        overridePendingTransition(R.anim.slide_in_from_left_animation, R.anim.slide_out_to_right_animation);
        finish();
    }
}
