package com.dingi.sdk.dingitelemetry;


import android.content.Context;
import android.location.Location;

import java.math.BigDecimal;

public class LocationMapper {
    private static final int SEVEN_DIGITS_AFTER_DECIMAL = 7;
    private static final double MIN_LONGITUDE = -180;
    private static final double MAX_LONGITUDE = 180;
    private SessionIdentifier sessionIdentifier;

    public LocationMapper() {
        sessionIdentifier = new SessionIdentifier();
    }

    public LocationEvent from(Context context , Location location, String applicationState) {
        return createLocationEvent(context , location, applicationState);
    }

    void updateSessionIdentifier( SessionIdentifier sessionIdentifier) {
        this.sessionIdentifier = sessionIdentifier;
    }

    public LocationEvent createLocationEvent(Context context , Location location, String applicationState) {
        String sessionId = sessionIdentifier.getSessionId();
        double latitudeScaled = round(location.getLatitude());
        double longitudeScaled = round(location.getLongitude());
        double longitudeWrapped = wrapLongitude(longitudeScaled);
        String userId = TelemetryUtils.retrieveVendorId(context);
        LocationEvent locationEvent = new LocationEvent(sessionId, latitudeScaled, longitudeWrapped, applicationState ,userId);
        addAltitudeIfPresent(location, locationEvent);
        addAccuracyIfPresent(location, locationEvent);
        return locationEvent;
    }

    private double round(double value) {
        return new BigDecimal(value).setScale(SEVEN_DIGITS_AFTER_DECIMAL, BigDecimal.ROUND_DOWN).doubleValue();
    }

    private double wrapLongitude(double longitude) {
        double wrapped = longitude;
        if ((longitude < MIN_LONGITUDE) || (longitude > MAX_LONGITUDE)) {
            wrapped = wrap(longitude, MIN_LONGITUDE, MAX_LONGITUDE);
        }
        return wrapped;
    }

    private double wrap(double value, double min, double max) {
        double delta = max - min;

        double firstMod = (value - min) % delta;
        double secondMod = (firstMod + delta) % delta;

        return secondMod + min;
    }

    private void addAltitudeIfPresent(Location location, LocationEvent locationEvent) {
        if (location.hasAltitude()) {
            double altitudeRounded = Math.round(location.getAltitude());
            locationEvent.setAltitude(altitudeRounded);
        }
    }

    private void addAccuracyIfPresent(Location location, LocationEvent locationEvent) {
        if (location.hasAccuracy()) {
            float accuracyRounded = Math.round(location.getAccuracy());
            locationEvent.setAccuracy(accuracyRounded);
        }
    }
}
