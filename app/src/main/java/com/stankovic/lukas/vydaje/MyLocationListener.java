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

    TextView textView;

    public MyLocationListener(TextView textView) {
        this.textView = textView;
    }

    @Override
    public void onLocationChanged(Location loc) {
        Double longitude = loc.getLongitude();
        Double latitude = loc.getLatitude();
        textView.setText(longitude + " " + latitude);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}