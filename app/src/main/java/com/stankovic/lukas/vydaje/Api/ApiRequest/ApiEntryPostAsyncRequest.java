package com.stankovic.lukas.vydaje.Api.ApiRequest;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stankovic.lukas.vydaje.Api.ApiReader.ApiReader;
import com.stankovic.lukas.vydaje.Api.ApiRequest.Base.ApiPostAsyncRequest;
import com.stankovic.lukas.vydaje.DataAdapter.EntryAdapter;
import com.stankovic.lukas.vydaje.MainActivity;
import com.stankovic.lukas.vydaje.Model.Entry;
import com.stankovic.lukas.vydaje.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ApiEntryPostAsyncRequest extends ApiPostAsyncRequest {
    private ListView lvEntries;

    public ApiEntryPostAsyncRequest(Context context, ListView lvEntries) {
        super(context);
        this.lvEntries = lvEntries;
        dialog.setTitle("Načítání");
        dialog.setMessage("Načítám výdaje");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String entriesString = ApiReader.parseElement(s, "output");
        if (!entriesString.equals("error")) {
            Type listType = new TypeToken<ArrayList<Entry>>(){}.getType();
            MainActivity.entries = new Gson().fromJson(entriesString, listType);
        } else {
            MainActivity.entries = new ArrayList<>();
        }

        EntryAdapter adapter = new EntryAdapter(context, R.layout.entry_layout, MainActivity.entries);
        lvEntries.setAdapter(adapter);
    }


}
