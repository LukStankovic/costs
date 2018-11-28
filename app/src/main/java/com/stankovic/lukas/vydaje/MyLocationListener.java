package com.stankovic.lukas.vydaje;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MyLocationListener implements LocationListener {

    TextView longitude;
    TextView latitude;

    public MyLocationListener(TextView longitude, TextView latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public void onLocationChanged(Location loc) {
        Double loadedLongitude = loc.getLongitude();
        Double loadedLatitude = loc.getLatitude();
        longitude.setText(Double.toString(loadedLongitude));
        latitude.setText(Double.toString(loadedLatitude));
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}