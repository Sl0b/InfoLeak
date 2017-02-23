package com.sl0b.infoleak;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class BreachAsyncTask extends AsyncTask<String, Void, Breach[]> {

    private final String LOG_TAG = BreachAsyncTask.class.getSimpleName();
    private MainActivity activity;

    BreachAsyncTask(MainActivity activity) {
        this.activity = activity;
    }

    protected Breach[] doInBackground(String... urls) {

        if (urls.length == 0) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urls[0]);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            Breach[] breaches = new Gson().fromJson(reader, Breach[].class);
            if (breaches == null || breaches.length == 0)
                return null;
            else return breaches;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    protected void onPostExecute(Breach[] result) {
        if (result != null) {
            activity.setGoodResultVisibility(false);
            activity.onTaskCompleted(result);
        } else {
            activity.setGoodResultVisibility(true);
            activity.onTaskCompleted(null);
        }
    }
}