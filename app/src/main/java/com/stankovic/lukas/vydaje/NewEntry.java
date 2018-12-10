package com.stankovic.lukas.vydaje;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stankovic.lukas.vydaje.Api.ApiReader.ApiReader;
import com.stankovic.lukas.vydaje.Api.ApiRequest.Base.ApiPostAsyncRequest;
import com.stankovic.lukas.vydaje.Api.ApiRequest.Base.ApiParamsBuilder;
import com.stankovic.lukas.vydaje.Core.ConnectivityDialogs;
import com.stankovic.lukas.vydaje.Model.User;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class NewEntry extends Activity {

    TextView tvLatitude;
    TextView tvLongitude;
    EditText etName;
    EditText etAmount;
    EditText etDateTime;
    Button btnSave;
    Spinner category;
    Button btnCamera;
    ImageView imgBill;

    User loggedUser;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CONTENT_REQUEST=1337;
    private File output;
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    private static boolean onlineMode = false;

    private boolean isImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        tvLatitude = (TextView) findViewById(R.id.tvLatitude);
        tvLongitude = (TextView) findViewById(R.id.tvLongitude);
        loadLocation();

        etName = (EditText)findViewById(R.id.etEntryName);
        etDateTime = (EditText)findViewById(R.id.etDateTime);
        etAmount = (EditText)findViewById(R.id.etAmount);
        btnSave = (Button)findViewById(R.id.btnEntrySave);
        category = (Spinner) findViewById(R.id.category);
        btnCamera = (Button)findViewById(R.id.btnCamera);
        imgBill = (ImageView) findViewById(R.id.imgBill);

        Date currentDateTime = Calendar.getInstance().getTime();
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("d. M. yyyy HH:mm");
        String formattedCurrentDate = simpleDateFormat.format(currentDateTime);

        etDateTime.setText(formattedCurrentDate);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String userJson = settings.getString("user", "");

        Gson gson = new Gson();
        loggedUser = gson.fromJson(userJson, User.class);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                output = new File(dir, ts + ".jpeg");
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));

                startActivityForResult(i, CONTENT_REQUEST);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = save();
                if (status == 1) {
                    Toast.makeText(NewEntry.this, "Uloženo", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                    startActivity(new Intent(NewEntry.this, MainActivity.class));
                } else if (status == -1) {
                    Toast.makeText(NewEntry.this, "Vyplň veškeré údaje", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true);
                } else {
                    btnSave.setEnabled(true);
                    Toast.makeText(NewEntry.this, "Chyba", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap img = BitmapFactory.decodeFile(output.getAbsolutePath());
                imgBill.setImageBitmap(img);
                isImage = true;
            }
        }
    }


    private int save() {
        btnSave.setEnabled(false);
        HashMap<String, String> params = new HashMap<>();

        String name = etName.getText().toString();
        String amount = etAmount.getText().toString();
        String dateTime = etDateTime.getText().toString();
        String longitude = tvLongitude.getText().toString();
        String latitude = tvLatitude.getText().toString();
        String categoryString = Long.toString(category.getSelectedItemId());

        updateConnectedFlags();
        if (!onlineMode) {
            ConnectivityDialogs.offlineDialog(NewEntry.this);
            return 0;
        }

        if (name.equals("") || amount.equals("") || dateTime.equals("") || longitude.equals("Zaměřuji šířku") || latitude.equals("Zaměřuji délku")) {
            Toast.makeText(NewEntry.this, "Vyplň veškeré údaje", Toast.LENGTH_SHORT).show();
            return 0;
        }

        params.put("name", name);
        params.put("amount", amount);
        params.put("dateTime", dateTime);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("userId", Integer.toString(loggedUser.id));
        params.put("categoryId", categoryString);
        params.put("imgPath", isImage ? output.getAbsolutePath() : "");

        ApiPostAsyncRequest apiAsyncRequest = new ApiPostAsyncRequest(this);
        String status = "error";
        String response = "";
        try {
            response = apiAsyncRequest.execute("entry/save/", ApiParamsBuilder.buildParams(params)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        status = ApiReader.parseStatus(response);
        return status.equals("ok") ? 1 : 0;
    }


    private void loadLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener(tvLongitude, tvLatitude);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 50, locationListener);
    }

    private void updateConnectedFlags() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
        } else {
            wifiConnected = false;
            mobileConnected = false;
        }

        onlineMode = wifiConnected || mobileConnected;
    }
}
