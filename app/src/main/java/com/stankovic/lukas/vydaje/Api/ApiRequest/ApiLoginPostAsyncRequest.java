package com.stankovic.lukas.vydaje.Api.ApiRequest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.stankovic.lukas.vydaje.Api.ApiReader.ApiReader;
import com.stankovic.lukas.vydaje.Api.ApiRequest.Base.ApiPostAsyncRequest;
import com.stankovic.lukas.vydaje.LoginActivity;
import com.stankovic.lukas.vydaje.MainActivity;

public class ApiLoginPostAsyncRequest extends ApiPostAsyncRequest {

    private SharedPreferences settings;

    public ApiLoginPostAsyncRequest(Context context, SharedPreferences settings) {
        super(context);
        dialog.setTitle("Přihlašuji");
        dialog.setMessage("Chroustám jméno a heslo");
        this.settings = settings;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String status = ApiReader.parseStatus(s);
        SharedPreferences.Editor editor = settings.edit();
        if (status.equals("ok")) {
            editor.putBoolean("logged", true);
            String user = ApiReader.parseOutput(s);
            editor.putString("user", user);
            editor.apply();
            context.startActivity(new Intent(context, MainActivity.class));
        } else {
            editor.putBoolean("logged", false);
            editor.putString("user", "");
            editor.apply();
            Toast.makeText(context, "Nesprávné jméno nebo heslo", Toast.LENGTH_SHORT).show();
        }
    }

}
