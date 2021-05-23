package com.example.networkservice;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL = "http://10.0.2.2:5000/getjobs/emulator";
    private static final String POST_URL = "http://10.0.2.2:5000/postresults";

    static String getUriInfo() {

        // Set up variables for the try block that need to be closed in the
        // finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jobJSONString = null;

        try {

            Uri builtURI = Uri.parse(BASE_URL).buildUpon().build();

            Log.d(LOG_TAG, "Connecting to backend ");

            // Convert the URI to a URL,
            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            Log.d(LOG_TAG, "Connected to backend ");

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                // Add the current line to the string.
                builder.append(line);

                // Since this is JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  Exit without parsing.
                return null;
            }

            jobJSONString = builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the connection and the buffered reader.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Write the final JSON response to the log

        Log.d(LOG_TAG, "JSON downloaded");

        return jobJSONString;
    }

    static String postUriInfo(String result) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jobJSONString = null;

        try {

            Log.d(LOG_TAG, "TRYING TO POST ");


            URL url = new URL (POST_URL);


            // Open the network connection.
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type","application/json; utf-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoOutput(true);

            String jsonInputString = "{\"result\":\"" + result + "\"}";
            Log.d(LOG_TAG, "PRATEN STRING: " + jsonInputString);

            OutputStream os = urlConnection.getOutputStream();
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input,0,input.length);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));

            StringBuilder response = new StringBuilder();
            String responseLine = null;

            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            jobJSONString = response.toString();

            Log.d(LOG_TAG, "POST SUCCESSFUL ");

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jobJSONString;
    }

}
