package com.dingi.sdk.dingitelemetry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import br.com.safety.locationlistenerhelper.core.SettingsLocationTracker;

public class LocationReceiver extends BroadcastReceiver  {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Dingi", "I am now in the broadcaster!!!!!");
        context.startService(new Intent(context, DingiTelemetryService.class));;

        /*if (null != intent && intent.getAction().equals("my.action")) {
            Location locationData = (Location) intent.getParcelableExtra(SettingsLocationTracker.LOCATION_MESSAGE);
            Log.d("Dingi", "Latitude: " + locationData.getLatitude() + "Longitude:" + locationData.getLongitude());
        }*/
    }
}
