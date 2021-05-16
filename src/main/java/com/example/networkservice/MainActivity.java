package com.example.networkservice;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.networkservice.restarter.RestartServiceBroadcastReceiver;

public class MainActivity extends Activity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }
        finish();
    }
}
