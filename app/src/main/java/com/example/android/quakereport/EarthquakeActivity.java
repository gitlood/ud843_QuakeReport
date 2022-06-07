/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2022-01-01&endtime=2022-06-08&minfelt=50&minmagnitude=0";

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private EarthquakeAdapter earthquakeAdapters;

    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
// Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);
        emptyView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);
        // Create a new adapter that takes an empty list of earthquakes as input
        earthquakeAdapters = new EarthquakeAdapter(this, new ArrayList<>());
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(earthquakeAdapters);
        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener((adapterView, view, position, l) -> {
            // Find the current earthquake that was clicked on
     //       Earthquake currentEarthquake = earthquakeAdapters.getItem(position);
            // Convert the String URL into a URI object (to pass into the Intent constructor)
//                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
//                // Create a new intent to view the earthquake URI
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
//                // Send the intent to launch a new activity
//                startActivity(websiteIntent);
        });        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        // Set empty state text to display "No earthquakes found."
        emptyView.setText(R.string.no_earthquakes);
        if (earthquakes != null && !earthquakes.isEmpty()) {
            // Clear the adapter of previous earthquake data
            earthquakeAdapters.clear();
            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            earthquakeAdapters.addAll(earthquakes);
            earthquakeAdapters.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Loader reset, so we can clear out our existing data.
        earthquakeAdapters.clear();
    }

    /**
     * Loads a list of earthquakes by using an AsyncTaskLoader to perform the
     * network request to the given URL.
     */
    public static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

        /**
         * Tag for log messages
         */
        private final String LOG_TAG = EarthquakeLoader.class.getName();

        /**
         * Query URL
         */
        private final String mUrl;

        /**
         * Constructs a new {@link EarthquakeLoader}.
         *
         * @param context of the activity
         * @param url     to load data from
         */
        public EarthquakeLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        /**
         * This is on a background thread.
         */
        @Override
        public List<Earthquake> loadInBackground() {
            if (mUrl == null) {
                return null;
            }

            // Perform the network request, parse the response, and extract a list of earthquakes.
            return QueryUtils.fetchEarthquakeData(mUrl);
        }
    }
}