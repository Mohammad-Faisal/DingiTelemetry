package com.dingi.sdk.dingitelemetry;

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
        stoptimertask();
    }


    public void startLocationService(){
        final Context context= getApplicationContext();
        locationTracker=new LocationTracker("my.action")
                .setInterval(5000)
                .setGps(true)
                .setNetWork(false)
                //IF YOU WANT JUST CURRENT LOCATION
                .currentLocation(new CurrentLocationReceiver(new CurrentLocationListener() {
                    @Override
                    public void onCurrentLocation(Location location) {
                        Log.d("Dingi", ":onCurrentLocation" + location.getLongitude());


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
                }))
                .start(context, activity);
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("Dingi", "in timer ++++  "+ (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*protected void createLocationRequest() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Dingi" , "Found a new locaion which is "+location.getLatitude());
    }*/
}
