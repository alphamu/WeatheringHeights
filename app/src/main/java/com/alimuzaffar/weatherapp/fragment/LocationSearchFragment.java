package com.alimuzaffar.weatherapp.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.alimuzaffar.weatherapp.Constants;
import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.adapter.RecentLocationsAdapter;
import com.alimuzaffar.weatherapp.adapter.SearchAutoCompleteAdapter;
import com.alimuzaffar.weatherapp.db.RecentLocationsHelper;
import com.alimuzaffar.weatherapp.model.RecentLocation;
import com.alimuzaffar.weatherapp.model.forecast.Prediction;
import com.alimuzaffar.weatherapp.widget.DelayAutoCompleteTextView;

import java.util.List;


public class LocationSearchFragment extends Fragment implements Constants, RecentLocationsAdapter.OnDeleteListener {

    private OnLocationSelectedListener mListener;
    DelayAutoCompleteTextView mSearchView;
    private static final int THRESHOLD = 3; //minimum chars before search

    ListView mRecentLocs;
    List<RecentLocation> mData;
    RecentLocationsAdapter mAdapter;
    View mEmpty;

    public static LocationSearchFragment newInstance() {
        LocationSearchFragment fragment = new LocationSearchFragment();
        return fragment;
    }

    public LocationSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_location_search, container, false);
        mEmpty = v.findViewById(R.id.empty);

        if (getActivity() != null) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setCustomView(R.layout.view_search_field);
                mSearchView = (DelayAutoCompleteTextView) actionBar.getCustomView().findViewById(R.id.txt_search);
                mSearchView.setThreshold(THRESHOLD); //min chars before search
                mSearchView.setAdapter(new SearchAutoCompleteAdapter(getActivity()));
                mSearchView.setLoadingIndicator((ProgressBar) getActivity().findViewById(R.id.pb_loading_indicator));
                mSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Prediction p = (Prediction) parent.getItemAtPosition(position);
                        setSelectedLocation(p.getDescription());
                    }
                });

                View clear = actionBar.getCustomView().findViewById(R.id.btn_clear);
                clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSearchView.setText("");
                    }
                });
            }
        }

        mRecentLocs = (ListView) v.findViewById(R.id.recent_locations);
        mRecentLocs.setEmptyView(mEmpty);
        mAdapter = new RecentLocationsAdapter(getActivity(), this, mData = RecentLocationsHelper.getAllLocations());
        mRecentLocs.setAdapter(mAdapter);
        mRecentLocs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecentLocation r = (RecentLocation) parent.getItemAtPosition(position);
                if (r.getId() != 0) {
                    setSelectedLocation(r.getId());
                }
            }
        });

        return v;
    }

    public void setSelectedLocation(String location) {
        checkIfListenerAttached();
        mListener.onLocationSelected(location);
    }

    public void setSelectedLocation(long locId) {
        checkIfListenerAttached();
        mListener.onLocationSelected(locId);
    }

    private void checkIfListenerAttached() {
        if (mListener == null) {
            try {
                mListener = (OnLocationSelectedListener) getActivity();
            } catch (ClassCastException e) {
                throw new ClassCastException(getActivity().toString()
                        + " must implement OnLocationSelectedListener");
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void delete(long locId, int position) {
        RecentLocation l = mData.remove(position);
        mAdapter.notifyDataSetChanged();
        RecentLocationsHelper.deleteLocation(locId);
        Snackbar.make(getView(), getString(R.string.undo_long, l.getName()), Snackbar.LENGTH_LONG).setAction(R.string.undo, new View.OnClickListener() {
            RecentLocation recentLocation;
            int position;

            @Override
            public void onClick(View v) {
                mData.add(position, recentLocation);
                mAdapter.notifyDataSetChanged();
                RecentLocationsHelper.addLocationToDB(recentLocation.getId(), recentLocation.getName());
            }

            public View.OnClickListener init(RecentLocation l, int pos) {
                this.recentLocation = l;
                this.position = pos;
                return this;
            }

        }.init(l, position)).show();
    }

    public interface OnLocationSelectedListener {
        void onLocationSelected(String location);
        void onLocationSelected(long locId);
    }

}
