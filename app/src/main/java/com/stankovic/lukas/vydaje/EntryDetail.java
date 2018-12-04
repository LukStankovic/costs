package com.stankovic.lukas.vydaje;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.stankovic.lukas.vydaje.Model.Entry;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EntryDetail extends Activity {

    private TextView category;
    private TextView name;
    private TextView dateTime;
    private TextView amount;
    private ImageView imgBill;

    private Entry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);

        amount = (TextView) findViewById(R.id.amount);
        name = (TextView) findViewById(R.id.name);
        category = (TextView) findViewById(R.id.category);
        dateTime = (TextView) findViewById(R.id.dateTime);
        imgBill = (ImageView) findViewById(R.id.imgBill);

        String entryString = getIntent().getExtras().getString("entry");
        if (!entryString.equals("")) {
            Gson gson = new Gson();
            entry = gson.fromJson(entryString, Entry.class);
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        amount.setText("-" + Double.toString(entry.amount) + " Kč");
        name.setText(entry.name);
        category.setText(entry.categoryName);
        dateTime.setText(entry.datetime);

        if (!entry.imgPath.equals("")) {
            Bitmap img = BitmapFactory.decodeFile(entry.imgPath);
            imgBill.setImageBitmap(img);

            final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(img);
            dialog.setContentView(imageView);
            imgBill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
        } else {
            imgBill.setClickable(false);
        }


    }

}
