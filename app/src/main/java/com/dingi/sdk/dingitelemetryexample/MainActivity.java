package com.dingi.sdk.dingitelemetryexample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.dingi.dingisdk.Dingi;
import com.dingi.dingisdk.constants.Style;
import com.dingi.dingisdk.maps.DingiMap;
import com.dingi.dingisdk.maps.MapView;
import com.dingi.sdk.dingitelemetry.DingiTelemetry;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {


    MapView mapView;
    DingiMap map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Dingi.getInstance(getApplicationContext(),this,"EjFUMTUMKFcnJ2VzRnL39Cd2ixtHScJ2p0C1vhP2");
        setContentView(R.layout.activity_main);


        DingiTelemetry telemetry = new DingiTelemetry(this , "EjFUMTUMKFcnJ2VzRnL39Cd2ixtHScJ2p0C1vhP2" );

       /* mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(dingiMap -> {
            dingiMap.setStyleUrl(Style.DINGI_ENGLISH);
            Timber.tag("Dingi").d("set style is called !!!!!!");
            //dingiMap.setStyleUrl(Style.DINGI_BANGLA);
            map = dingiMap;
        });


        mapView.setStyleUrl(Style.DINGI_BANGLA);*/
        //scheduleAlarm();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /*@Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }*/
}
