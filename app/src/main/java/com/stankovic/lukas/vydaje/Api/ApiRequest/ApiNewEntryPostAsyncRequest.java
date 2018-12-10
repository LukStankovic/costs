package com.stankovic.lukas.vydaje.Api.ApiRequest;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stankovic.lukas.vydaje.Api.ApiReader.ApiReader;
import com.stankovic.lukas.vydaje.Api.ApiRequest.Base.ApiPostAsyncRequest;
import com.stankovic.lukas.vydaje.DataAdapter.EntryAdapter;
import com.stankovic.lukas.vydaje.MainActivity;
import com.stankovic.lukas.vydaje.Model.Entry;
import com.stankovic.lukas.vydaje.NewEntry;
import com.stankovic.lukas.vydaje.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ApiNewEntryPostAsyncRequest extends ApiPostAsyncRequest {
    private ArrayList<Entry> entries;

    private ListView lvEntries;

    public ApiNewEntryPostAsyncRequest(Context context, ArrayList<Entry> entries, ListView lvEntries) {
        super(context);
        this.entries = entries;
        this.lvEntries = lvEntries;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String status = ApiReader.parseStatus(s);
    /*    if (status.equals("ok")) {
            Toast.makeText(NewEntry.this, "Uloženo", Toast.LENGTH_SHORT).show();
            btnSave.setEnabled(true);
            startActivity(new Intent(NewEntry.this, MainActivity.class));
        } else if (status == -1) {
            Toast.makeText(NewEntry.this, "Vyplň veškeré údaje", Toast.LENGTH_SHORT).show();
            btnSave.setEnabled(true);
        } else {
            btnSave.setEnabled(true);
            Toast.makeText(NewEntry.this, "Chyba", Toast.LENGTH_SHORT).show();
        }*/
    }


}
