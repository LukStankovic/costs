package com.stankovic.lukas.vydaje;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stankovic.lukas.vydaje.Api.ApiRequest.ApiPostAsyncRequest;
import com.stankovic.lukas.vydaje.Api.ApiRequest.ApiParamsBuilder;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class NewEntry extends Activity {

    TextView txtCoords;
    EditText etName;
    EditText etAmount;
    EditText etDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);


        Log.d("lukstan", "cl: " + getString(R.string.clientId));

        txtCoords = (TextView) findViewById(R.id.tvCoords);
        loadLocation();

        etName = (EditText)findViewById(R.id.etEntryName);
        etDateTime = (EditText)findViewById(R.id.etDateTime);
        etAmount = (EditText)findViewById(R.id.etAmount);
        Button btnSave = (Button)findViewById(R.id.btnEntrySave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save()) {
                    Toast.makeText(NewEntry.this, "Uloženo", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewEntry.this, MainActivity.class));
                } else {
                    Toast.makeText(NewEntry.this, "Chyba", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean save() {
        HashMap<String, String> params = new HashMap<>();
        params.put("txtCoords", txtCoords.getText().toString());

        ApiPostAsyncRequest apiAsyncRequest = new ApiPostAsyncRequest();
        String status = "error";
        try {
            status = apiAsyncRequest.execute("entry/save/", ApiParamsBuilder.buildParams(params)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return status.equals("ok");
    }


    private void loadLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener(txtCoords);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 50, locationListener);
    }
}
