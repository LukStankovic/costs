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

    public ApiNewEntryPostAsyncRequest(Context context) {
        super(context);
        dialog.setTitle("Ukládání");
        dialog.setMessage("Ukládám výdaj");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String status = ApiReader.parseStatus(s);
        if (status.equals("ok")) {
            Toast.makeText(context, "Uloženo", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, MainActivity.class));
        } else {
            Toast.makeText(context, "Chyba", Toast.LENGTH_SHORT).show();
        }
    }

}
