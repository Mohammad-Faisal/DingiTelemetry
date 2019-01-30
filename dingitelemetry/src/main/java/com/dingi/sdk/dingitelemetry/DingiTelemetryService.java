package com.dingi.sdk.dingitelemetry;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import br.com.safety.locationlistenerhelper.core.CurrentLocationListener;
import br.com.safety.locationlistenerhelper.core.CurrentLocationReceiver;
import br.com.safety.locationlistenerhelper.core.LocationTracker;
import timber.log.Timber;

public class DingiTelemetryService extends Service {


    private LocationTracker locationTracker;
    private Context context;
    private AppCompatActivity activity;
    private FusedLocationProviderClient mFusedLocationClient;


    public int counter=0;
    public DingiTelemetryService(Context applicationContext , AppCompatActivity appCompatActivity) {

        /*this.context = applicationContext;
        this.activity = appCompatActivity;*/


        super();
        Log.i("Dingi", "here I am!");



    }

    public DingiTelemetryService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        Log.i("Dingi", "here I am in onCreate of Telemetry Service");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        //startLocationService();
        /*createLocationReceiver(context);
        createTelemetryReceiver(context);
        createServiceTaskCallbacks();*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startLocationService();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Dingi", "ondestroy!");
        Intent broadcastIntent = new Intent(this, LocationReceiver.class);
        sendBroadcast(broadcastIntent);
    }


    public void startLocationService(){
        final Context context= getApplicationContext();
        locationTracker=new LocationTracker("my.action")
                .setInterval(500)
                .setGps(true)
                .setNetWork(false)
                //IF YOU WANT JUST CURRENT LOCATION
                /*.currentLocation(new CurrentLocationReceiver(new CurrentLocationListener() {
                    @Override
                    public void onCurrentLocation(Location location) {
                        Log.d("Dingi", ":onCurrentLocationn" + location.getLongitude());


                        LocationMapper obtainLocationEvent = new LocationMapper();
                        LocationEvent locationEvent = obtainLocationEvent.createLocationEvent(context.getApplicationContext() , location, "Foreground");


                        ArrayList<Event> batch = new ArrayList<>();
                        batch.add(locationEvent);


                        //sendEvents(batch);
                        //locationTracker.stopLocationService(context);
                    }

                    @Override
                    public void onPermissionDiened() {
                        Log.d("Dingi", ":onPermissionDiened");
                        //locationTracker.stopLocationService(context);
                    }
                }))*/
                .start(context, activity);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
