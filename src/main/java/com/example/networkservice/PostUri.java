package com.example.networkservice;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PostUri extends AsyncTask<Void, Void, String> {

    //private static final String POST_URL = "http://10.0.2.2:5000/postresults";
    private static final String LOG_TAG = PostUri.class.getSimpleName();

    public PostUri() {
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            Thread.sleep(600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return NetworkUtils.postUriInfo(Service.pingResult);
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON result and updates the views.
     *
     * @param s Result from the doInBackground() method containing the raw
     *          JSON response, or null if it failed.
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            Log.i("Viktor","POST RESULT = "+s);
            Log.d(LOG_TAG,"POST Result: " + s);
            Service.responseString += s;

        } catch (Exception e) {
            Log.d(LOG_TAG, "onPostExecute: Failed URI");
            e.printStackTrace();
        }
    }
}
