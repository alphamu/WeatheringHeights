package com.alimuzaffar.weatherapp.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;

public class NetworkHelper extends Fragment {
  public static final String TAG = "NetworkHelper";
  public static final String CHECK_INTERNET = "network_connection";

  private Activity mActivity;
  AlertDialog mAlertDialog = null;

  private BroadcastReceiver onNotice = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.hasExtra(CHECK_INTERNET) && !intent.getBooleanExtra(CHECK_INTERNET, true)) {
        showAlertDialog(mActivity, "Internet Connection",
            "No internet connection available.\n\n" +
                "Please check your internet connection and try again.");
      } else {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
          mAlertDialog.dismiss();
          mAlertDialog = null;
        }
      }
    }
  };


  public static NetworkHelper newInstance() {
    return new NetworkHelper();
  }

  public NetworkHelper() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mActivity = activity;
    //throw new IllegalArgumentException("activity must extend BaseActivity and implement LocationHelper.LocationCallback");

  }

  @Override
  public void onResume() {
    super.onResume();
    IntentFilter iff = new IntentFilter(CHECK_INTERNET);
    LocalBroadcastManager.getInstance(mActivity).registerReceiver(onNotice, iff);
    if (!isInternetConnected(mActivity)) {
      showAlertDialog(mActivity, "Internet Connection",
          "No internet connection available.\n\n" +
              "Please check your internet connection and try again.");
    }

  }

  @Override
  public void onPause() {
    super.onPause();
    LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(onNotice);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mActivity = null;
  }

  public void showAlertDialog(Context context, String title, String message) {
    if (mAlertDialog != null && mAlertDialog.isShowing()) {
      return; //already showing
    } else if (mAlertDialog != null) {
      mAlertDialog.dismiss();
      mAlertDialog = null;
    }
    mAlertDialog = new AlertDialog.Builder(context).create();

    // Setting Dialog Title
    mAlertDialog.setTitle(title);

    // Setting Dialog Message
    mAlertDialog.setMessage(message);

    // Setting alert dialog icon
    //mAlertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

    // Setting OK Button
    mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
        getString(android.R.string.ok),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            mAlertDialog = null;
          }
        });

    // Showing Alert Message
    mAlertDialog.show();
  }

  /*
    private void initAppAfterCheckingForGooglePlayerServices() {
      // Showing status
      int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

      if (status == ConnectionResult.SUCCESS) {
        if (mGoogleApiClient == null) {
          buildGoogleApiClient();
        }
      } else {
        int requestCode = 10;
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, mActivity, requestCode);
        dialog.show();
      }

    }

  private synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();

    mGoogleApiClient.connect();
  }

  @Override
  public void onConnected(Bundle bundle) {

  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

  }
*/

  public static boolean isInternetConnected(Context context) {
    ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

    // Here if condition check for wifi and mobile network is available or
    // not.
    // If anyone of them is available or connected then it will return true,
    // otherwise false;

    if (wifi != null && wifi.isConnected()) {
      return true;
    } else if (mobile != null && mobile.isConnected()) {
      return true;
    }
    return false;
  }

  public static boolean isWifiConnected(Context context) {
    ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

    if (wifi != null && wifi.isConnected()) {
      return true;
    }

    return false;
  }

  public static boolean isMobileDataConnected(Context context) {
    ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

    if (mobile != null && mobile.isConnected()) {
      return true;
    }

    return false;
  }

}
