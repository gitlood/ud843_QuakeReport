package com.example.android.quakereport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.listitem, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        TextView magnitudeTV = (TextView) listItemView.findViewById(R.id.magnitudeTextView);
        TextView placeTV = listItemView.findViewById(R.id.placeTextView);
        TextView dateTV = listItemView.findViewById(R.id.dateTextView);

        magnitudeTV.setText(String.valueOf(currentEarthquake.getMagnitude()));
        placeTV.setText(currentEarthquake.getWhere());
        dateTV.setText(currentEarthquake.getDate());

        return listItemView;
    }
}
