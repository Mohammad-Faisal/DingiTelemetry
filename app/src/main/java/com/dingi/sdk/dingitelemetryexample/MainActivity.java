package com.dingi.sdk.dingitelemetryexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dingi.sdk.dingitelemetry.AppUserTurnstile;
import com.dingi.sdk.dingitelemetry.DingiTelemetry;
import com.dingi.sdk.dingitelemetry.Event;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private DingiTelemetry telemetry;
    //private LocationTracker locationTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        telemetry = new DingiTelemetry(this , "EjFUMTUMKFcnJ2VzRnL39Cd2ixtHScJ2p0C1vhP2" , this);


        ArrayList<Event> batch = new ArrayList<>();


        AppUserTurnstile turnstileEvent = new AppUserTurnstile("Dingi",
                "1.0");
        AppUserTurnstile appUserTurnstile = new AppUserTurnstile("Dingi" , "1.0");
        batch.add(turnstileEvent);
        telemetry.sendEvents(batch);

    }
}
