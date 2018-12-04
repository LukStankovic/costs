package com.stankovic.lukas.vydaje.Core;

import android.app.AlertDialog;
import android.content.Context;

public class ConnectivityDialogs {

    public static void offlineDialog(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle("Ajajaj");
        alertDialog.setMessage("Bez internetu to nepůjde... \uD83D\uDE1E");
        alertDialog.show();
    }
}
