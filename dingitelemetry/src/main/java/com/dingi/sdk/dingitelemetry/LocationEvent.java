package com.dingi.sdk.dingitelemetry;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class LocationEvent extends Event implements Parcelable {
    private static final String LOCATION = "location";
    private static final String SOURCE_DINGI = "dingi";
    private static final String OPERATING_SYSTEM = "Android - " + Build.VERSION.RELEASE;

    @SerializedName("event")
    private final String event;
    @SerializedName("created")
    private final String created;
    @SerializedName("source")
    private String source;
    @SerializedName("sessionId")
    private final String sessionId;
    @SerializedName("lat")
    private final double latitude;
    @SerializedName("lng")
    private final double longitude;
    @SerializedName("altitude")
    private Double altitude = null;
    @SerializedName("operatingSystem")
    private String operatingSystem;
    @SerializedName("applicationState")
    private String applicationState;
    @SerializedName("horizontalAccuracy")
    private Float accuracy = null;
    @SerializedName("userId")
    private String userId;


    LocationEvent(String sessionId, double latitude, double longitude, String applicationState  , String userId) {
        this.event = LOCATION;
        this.created = TelemetryUtils.obtainCurrentDate();
        this.source = SOURCE_DINGI;
        this.sessionId = sessionId;
        Double p = 0.0;
        this.altitude =p;
        this.latitude = latitude;
        this.longitude = longitude;
        this.operatingSystem = OPERATING_SYSTEM;
        this.applicationState = applicationState;
        this.userId = userId;
        Float pp = 0F;
        this.accuracy = pp;
    }

    @Override
    Type obtainType() {
        return Type.LOCATION;
    }

    String getEvent() {
        return event;
    }

    String getSource() {
        return source;
    }

    double getLatitude() {
        return latitude;
    }

    double getLongitude() {
        return longitude;
    }

    Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    String getOperatingSystem() {
        return operatingSystem;
    }

    Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    private LocationEvent(Parcel in) {
        event = in.readString();
        created = in.readString();
        source = in.readString();
        sessionId = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        altitude = in.readByte() == 0x00 ? null : in.readDouble();
        operatingSystem = in.readString();
        applicationState = in.readString();
        accuracy = in.readByte() == 0x00 ? null : in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(event);
        dest.writeString(created);
        dest.writeString(source);
        dest.writeString(sessionId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        if (altitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(altitude);
        }
        dest.writeString(operatingSystem);
        dest.writeString(applicationState);
        if (accuracy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(accuracy);
        }
    }

    @SuppressWarnings("unused")
    public static final Creator<LocationEvent> CREATOR = new Creator<LocationEvent>() {
        @Override
        public LocationEvent createFromParcel(Parcel in) {
            return new LocationEvent(in);
        }

        @Override
        public LocationEvent[] newArray(int size) {
            return new LocationEvent[size];
        }
    };
}