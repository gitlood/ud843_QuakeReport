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

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }


    private void updateUi(List<Earthquake> earthquakes) {
        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = findViewById(R.id.list);
        ArrayList<EarthquakeAdapter> earthquakeAdapters = new ArrayList<>();
        for (int i = 0; i < earthquakes.size(); i++) {
            earthquakeAdapters.add(new EarthquakeAdapter(this, earthquakes));
            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(earthquakeAdapters.get(i));
        }
    }

    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            // Don't perform the request if there are no URLs, or the first URL is null.
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            return QueryUtils.fetchEarthquakeData(urls[0]);
        }

        /**
         * This method is invoked on the main UI thread after the background work has been
         * completed.
         * <p>
         * It IS okay to modify the UI within this method. We take the {@link List<Earthquake>} object
         * (which was returned from the doInBackground() method) and update the views on the screen.
         */
        protected void onPostExecute(List<Earthquake> result) {
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }

            updateUi(result);
        }
    }
}
