package com.dingi.sdk.dingitelemetry;

import android.app.ActivityManager;
import android.app.AlarmManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.safety.locationlistenerhelper.core.CurrentLocationListener;
import br.com.safety.locationlistenerhelper.core.CurrentLocationReceiver;
import br.com.safety.locationlistenerhelper.core.LocationTracker;
import okhttp3.OkHttpClient;

public class DingiTelemetry {

    private String accessToken;
    private Context context;
    private TelemetryClient telemetryClient;
    private CertificateBlacklist certificateBlacklist;
    private ConfigurationClient configurationClient;
    private AppCompatActivity activity;

    private DingiTelemetryService dingiTelemetryService;
    private LocationTracker locationTracker;
    Intent mServiceIntent;

    ArrayList<Event> batch = new ArrayList<>();
    public DingiTelemetry(Context context  , String accessToken ){
        this.context = context;
        this.accessToken = accessToken;
        telemetryClient = createTelemetryClient(accessToken , "userAgent");
        this.configurationClient = new ConfigurationClient(context, TelemetryUtils.createFullUserAgent("user-agent",
                context), accessToken, new OkHttpClient());
        this.certificateBlacklist = new CertificateBlacklist(context, configurationClient);

        //startTelemetryService(context,activity);
        sendTurnstileEvent();
        sendLocationEvent();
       // scheduleAlarm();
    }

    public void startTelemetryService(Context  context , AppCompatActivity appCompatActivity){
        dingiTelemetryService = new DingiTelemetryService(context , appCompatActivity);

        mServiceIntent = new Intent(context, DingiTelemetryService.class);
        if (!isMyServiceRunning(DingiTelemetryService.class)) {
            Log.d("Dingi", "service is started");
            context.startService(mServiceIntent);
        }
    }


    private TelemetryClient createTelemetryClient(String dingiMapAccessToken, String userAgent) {
        String fullUserAgent = "userAgent";
        TelemetryClientFactory telemetryClientFactory = new TelemetryClientFactory(dingiMapAccessToken, fullUserAgent,
                new Logger(), certificateBlacklist);
        telemetryClient = telemetryClientFactory.obtainTelemetryClient(context);
        return telemetryClient;
    }

    private void sendTurnstileEvent(){
        ArrayList<Event> batch = new ArrayList<>();
        AppUserTurnstile turnstileEvent = new AppUserTurnstile(context , "Dingi", "1.0");
        batch.add(turnstileEvent);
        sendEvents(batch);
    }

    private void sendLocationEvent(){
        locationTracker=new LocationTracker("my.action")
                .setInterval(30000)
                .setGps(true)
                .setNetWork(false)
                .currentLocation(new CurrentLocationReceiver(new CurrentLocationListener() {
                    @Override
                    public void onCurrentLocation(Location location) {
                        LocationMapper obtainLocationEvent = new LocationMapper();
                        LocationEvent locationEvent = obtainLocationEvent.createLocationEvent(context.getApplicationContext() , location, "Foreground");
                        batch.add(locationEvent);
                        if(batch.size() == 20){
                            sendEvents(batch);
                            batch.clear();
                        }
                        //locationTracker.stopLocationService(context);
                    }

                    @Override
                    public void onPermissionDiened() {
                        //Log.d("Dingi", ":onPermissionDiened");
                        locationTracker.stopLocationService(context);
                    }
                }))
                .start(context);
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
    public void sendEvents(List<Event> events) {
        telemetryClient.sendEvents(events);
    }


    public void scheduleAlarm() {
        PendingIntent pendingIntent;
        Intent alarmIntent = new Intent(activity, LocationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        int interval = 300;
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
    }

}
