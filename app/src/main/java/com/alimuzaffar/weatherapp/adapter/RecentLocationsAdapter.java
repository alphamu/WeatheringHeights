package com.alimuzaffar.weatherapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alimuzaffar.weatherapp.R;
import com.alimuzaffar.weatherapp.model.RecentLocation;

import java.util.List;

/**
 * Created by Ali Muzaffar on 6/11/2015.
 */
public class RecentLocationsAdapter extends ArrayAdapter<RecentLocation> {

    private OnDeleteListener mListener;

    private static class ViewHolder {
        public final FrameLayout rootView;
        public final TextView text1;
        public final ImageView delete;

        private ViewHolder(FrameLayout rootView, TextView text1, ImageView delete) {
            this.rootView = rootView;
            this.text1 = text1;
            this.delete = delete;
        }

        public static ViewHolder create(FrameLayout rootView) {
            TextView text1 = (TextView)rootView.findViewById( R.id.text1 );
            ImageView delete = (ImageView)rootView.findViewById( R.id.delete );
            return new ViewHolder( rootView, text1, delete );
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if ( convertView == null ) {
            View view = mInflater.inflate(R.layout.simple_list_item, parent, false);
            vh = ViewHolder.create( (FrameLayout)view );
            view.setTag( vh );
        } else {
            vh = (ViewHolder)convertView.getTag();
        }

        RecentLocation item = getItem( position );
        vh.text1.setText(item.getName());
        vh.delete.setOnClickListener(new View.OnClickListener() {
            long locId;
            int position;
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.delete(locId, position);
                }
            }

            public View.OnClickListener init(long locId, int pos) {
                this.locId = locId;
                this.position = pos;
                return this;
            }

        }.init(item.getId(), position));

        return vh.rootView;
    }

    private LayoutInflater mInflater;

    // Constructors
    public RecentLocationsAdapter(Context context, OnDeleteListener listener, List<RecentLocation> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from( context );
        this.mListener = listener;
    }

    public interface OnDeleteListener {
        void delete(long locId, int position);
    }
}
