package com.stankovic.lukas.vydaje;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stankovic.lukas.vydaje.Api.ApiReader.ApiReader;
import com.stankovic.lukas.vydaje.Api.ApiRequest.ApiParamsBuilder;
import com.stankovic.lukas.vydaje.Api.ApiRequest.ApiPostAsyncRequest;
import com.stankovic.lukas.vydaje.DataAdapter.EntryAdapter;
import com.stankovic.lukas.vydaje.Model.Entry;
import com.stankovic.lukas.vydaje.Model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    ListView lvEntries;
    SharedPreferences settings;
    User user;
    private ArrayList<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings = getApplicationContext().getSharedPreferences("settings", Context.MODE_PRIVATE);

        String userString = settings.getString("user", "");
        Gson gson = new Gson();
        user = gson.fromJson(userString, User.class);


        Button btnNewEntry = (Button)findViewById(R.id.btnNewEntry);
        lvEntries = (ListView)findViewById(R.id.lvEntries);
        loadEntries();
        EntryAdapter adapter = new EntryAdapter(this, R.layout.entry_layout, entries);
        lvEntries.setAdapter(adapter);

        btnNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewEntry.class));
            }
        });
    }

    private void loadEntries() {
        ApiPostAsyncRequest apiAsyncRequest = new ApiPostAsyncRequest();
        String response = "";
        try {
            response = apiAsyncRequest.execute("entry/user/" + user.id, ApiParamsBuilder.buildParams()).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String entriesString = ApiReader.parseElement(response, "output");
        if (!entriesString.equals("")) {
            Type listType = new TypeToken<ArrayList<Entry>>(){}.getType();
            entries = new Gson().fromJson(entriesString, listType);
        } else {
            entries = new ArrayList<>();
        }
    }
}
