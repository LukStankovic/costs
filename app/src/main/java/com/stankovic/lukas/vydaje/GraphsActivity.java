package com.stankovic.lukas.vydaje;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.stankovic.lukas.vydaje.Model.Entry;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GraphsActivity extends Activity {

    private ArrayList<Entry> entries = new ArrayList<>();

    private GraphView lineGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        entries = MainActivity.entries;

        lineGraph = (GraphView) findViewById(R.id.graph);

        if (!entries.isEmpty()) {

            DataPoint[] dataPoints = new DataPoint[12];
            ArrayList<Double> groupedEntries = new ArrayList<>();

            for (int i = 0; i < 12; i++) {
                groupedEntries.add(0.0);
            }

            for (Entry entry : entries) {
                DateFormat format = new SimpleDateFormat("d. M. y", Locale.FRANCE);
                try {
                    Date date = format.parse(entry.datetime);
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.FRANCE);
                    int month = Integer.parseInt(monthFormat.format(date));

                    Double amountByMonth = groupedEntries.get(month-1);
                    amountByMonth += entry.amount;
                    groupedEntries.set(month-1, amountByMonth);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            int index = 0;
            for (Double amount : groupedEntries) {
                int month = index;
                dataPoints[index] = new DataPoint(month+1, amount);
                index++;
            }
            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);
            lineGraph.getViewport().setXAxisBoundsManual(true);
            lineGraph.getViewport().setMaxX(12);
            lineGraph.addSeries(series);
        }

    }

}
