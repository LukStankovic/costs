package com.stankovic.lukas.vydaje;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.stankovic.lukas.vydaje.Api.ApiRequest.ApiEntryPostAsyncRequest;
import com.stankovic.lukas.vydaje.Api.ApiRequest.Base.ApiParamsBuilder;
import com.stankovic.lukas.vydaje.Core.ConnectivityDialogs;
import com.stankovic.lukas.vydaje.Model.Entry;
import com.stankovic.lukas.vydaje.Model.User;

import java.util.ArrayList;

public class MainActivity extends Activity {

    ListView lvEntries;
    SharedPreferences settings;
    User user;
    private ArrayList<Entry> entries = new ArrayList<>();
    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;
    private static boolean onlineMode = false;
    private ConnectivityManager connectivityManager;
    private Button btnNewEntry;

    @Override
    protected void onResume() {
        super.onResume();
        renderEntries();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

        String userString = settings.getString("user", "");
        final Gson gson = new Gson();
        user = gson.fromJson(userString, User.class);


        btnNewEntry = (Button) findViewById(R.id.btnNewEntry);
        lvEntries = (ListView) findViewById(R.id.lvEntries);
        renderEntries();

        btnNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewEntry.class));
            }
        });

        lvEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EntryDetail.class);
                intent.putExtra("entry", gson.toJson(entries.get(position)));
                startActivity(intent);
            }

        });
    }

    private void renderEntries() {
        if (onlineMode) {
            loadEntries();
        }
    }

    private void loadEntries() {
        ApiEntryPostAsyncRequest apiAsyncRequest = new ApiEntryPostAsyncRequest(this, entries, lvEntries);
        apiAsyncRequest.execute("entry/user/" + user.id, ApiParamsBuilder.buildParams());
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateConnectedFlags();

        if (!onlineMode) {
            ConnectivityDialogs.offlineDialog(MainActivity.this);
        }
    }


    // Checks the network connection and sets the wifiConnected and mobileConnected
    // variables accordingly.
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

    // Populates the activity's options menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    // Handles the user's menu selection.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                settings = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("logged", false);
                editor.apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            case R.id.refresh:
                renderEntries();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
