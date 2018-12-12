package com.stankovic.lukas.vydaje.Api.ApiRequest;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stankovic.lukas.vydaje.Api.ApiReader.ApiReader;
import com.stankovic.lukas.vydaje.Api.ApiRequest.Base.ApiPostAsyncRequest;
import com.stankovic.lukas.vydaje.DataAdapter.EntryAdapter;
import com.stankovic.lukas.vydaje.Model.Entry;
import com.stankovic.lukas.vydaje.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ApiEntryPostAsyncRequest extends ApiPostAsyncRequest {
    private ArrayList<Entry> entries;

    private ListView lvEntries;

    public ApiEntryPostAsyncRequest(Context context, ArrayList<Entry> entries, ListView lvEntries) {
        super(context);
        this.entries = entries;
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
            entries = new Gson().fromJson(entriesString, listType);
        } else {
            entries = new ArrayList<>();
        }

        EntryAdapter adapter = new EntryAdapter(context, R.layout.entry_layout, entries);
        lvEntries.setAdapter(adapter);
    }


}
