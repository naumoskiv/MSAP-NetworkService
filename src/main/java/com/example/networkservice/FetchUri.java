package com.example.networkservice;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class FetchUri extends AsyncTask<Void, Void, String> {

    private static final String LOG_TAG = FetchUri.class.getSimpleName();


    @Override
    protected String doInBackground(Void... voids) {
        try {
            Thread.sleep(600000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return NetworkUtils.getUriInfo();
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
            Log.i("Viktor","rezultat = "+s);

            //JSONObject jsonObject = new JSONObject(s);


            JSONArray jsonArray = new JSONArray(s);

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String date = jsonObject.getString("date");
                String host = jsonObject.getString("host");
                String count = jsonObject.getString("count");
                String packetSize = jsonObject.getString("packetSize");
                String jobPeriod = jsonObject.getString("jobPeriod");
                String jobType = jsonObject.getString("jobType");

                String ping = "ping  -c  " + count;
                ping = ping + " -s " + packetSize;
                ping = ping + " " + host;
                String result = "";

                Runtime r = Runtime.getRuntime();
                Process p = r.exec(ping);
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    result += inputLine;
                }
                in.close();
                Log.d(LOG_TAG,"PING Result: " + result);
                Service.pingResult = result;


            }

        } catch (Exception e) {
            Log.d(LOG_TAG, "onPostExecute: Failed URI");
            e.printStackTrace();
        }

    }
}
