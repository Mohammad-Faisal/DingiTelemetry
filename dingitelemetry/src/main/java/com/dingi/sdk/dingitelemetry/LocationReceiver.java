package com.dingi.sdk.dingitelemetry;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.util.ArrayList;

import br.com.safety.locationlistenerhelper.core.CurrentLocationListener;
import br.com.safety.locationlistenerhelper.core.CurrentLocationReceiver;
import br.com.safety.locationlistenerhelper.core.LocationTracker;
import br.com.safety.locationlistenerhelper.core.SettingsLocationTracker;

public class LocationReceiver extends BroadcastReceiver {


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationTracker locationTracker;
    private Context context;
    private AppCompatActivity activity;


    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d("Dingi", "I am now in the broadcaster!!!!!");
        //context.startService(new Intent(context, DingiTelemetryService.class));;

        if (null != intent && intent.getAction().equals("my.action")) {
            Location locationData = (Location) intent.getParcelableExtra(SettingsLocationTracker.LOCATION_MESSAGE);
            Log.d("Location: ", "Latitude: " + locationData.getLatitude() + "Longitude:" + locationData.getLongitude());
            //send your call to api or do any things with the of location data
        }



        //if anyone want to get data from google
/*

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.d("Dingi", "I am now in the broadcaster and the location is "+location.getLatitude());
                Log.d("Dingi", "I am now in the broadcaster and the location is "+location.getLongitude());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Dingi", "I am now in the broadcaster and the location is failed "+e.getMessage());
            }
        });
*/








    }
}
