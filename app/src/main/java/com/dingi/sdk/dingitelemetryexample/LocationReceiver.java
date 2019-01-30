package com.dingi.sdk.dingitelemetryexample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

public class LocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Dingi", "I am now at the event Receiever of the app");
    }
}
