package com.alimuzaffar.weatherapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.alimuzaffar.weatherapp.Constants;
import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.WeatherApplication;
import com.alimuzaffar.weatherapp.model.forecast.Prediction;
import com.alimuzaffar.weatherapp.model.forecast.Predictions;
import com.alimuzaffar.weatherapp.util.StringHelper;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.ion.Ion;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchAutoCompleteAdapter extends BaseAdapter implements Filterable, Constants {

    private Context mContext;
    private List<Prediction> resultList = new ArrayList<>();
    private static String URL = null;

    public SearchAutoCompleteAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Prediction getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_dropdown_item, parent, false);
        }
        ((TextView) convertView.findViewById(R.id.text1)).setText(getItem(position).getDescription());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    Predictions predictions = findLocation(mContext, constraint.toString());

                    // Assign the data to the FilterResults
                    filterResults.values = predictions.getPredictions();
                    filterResults.count = predictions.getCount();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List<Prediction>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    private Predictions findLocation(Context context, String queryText) {
        try {
            String url = getLocationSearchUrl(queryText);
            if (url == null) {
                return Predictions.ERROR;
            }
            //blocking call, since perform filtering is called on a helper thread,
            //we don't need to do this asynchronously.
            if (StringHelper.isGpsCoordinates(queryText)) {
                String[] geo = StringHelper.convertToGpsCoordinates(queryText);
                Prediction p = new Prediction();
                p.setDescription(String.format("Search Lat: %s, Lon: %s", geo));
                Predictions res = new Predictions();
                res.setStatus(Predictions.Status.OK.name());
                res.setPredictions(Arrays.asList(p));
                return res;

            } else {
                Predictions json = Ion.with(context)
                        .load(url).as(new TypeToken<Predictions>() {
                        }).get();

                if (json != null) {
                    return json;
                } else {
                    return Predictions.ERROR; //Avoid returning nulls.
                }
            }
        } catch (Exception e) {
            Log.e("AutoCompleteAdapater", e.getMessage(), e);
            return Predictions.ERROR;
        }
    }

    public static String getLocationSearchUrl(String queryText) {
        if (URL == null) {
            URL = AUTOCOMPLETE_API_URL + WeatherApplication.getAppContext().getString(R.string.google_api_key);
        }
        try {
            return String.format(URL, URLEncoder.encode(queryText, "UTF-8"));
        } catch (Exception e) {
            return null;
        }
    }
}
