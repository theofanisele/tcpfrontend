package com.example.tcpui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {
    private GraphView graphView;
    private String username = null;
    private Double speed = 0.0;
    private Double elevation = 0.0;
    private Double time = 0.0;
    private Double distance = 0.0;
    private Double avgSpeed = 0.0;
    private Double avgElevation = 0.0;
    private Double avgTime = 0.0;
    private Double avgDistance = 0.0;
    Button elevationButton;
    Button speedButton;
    Button distanceButton;
    Button timeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // Initialize buttons
        elevationButton = findViewById(R.id.elevationButton);
        speedButton = findViewById(R.id.speedButton);
        distanceButton = findViewById(R.id.distanceButton);
        timeButton = findViewById(R.id.timeButton);

        graphView = findViewById(R.id.idGraphView);
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        setTitle("Statistics");

        // get the saved data from shared preferences
        SharedPreferences sharedPref = getSharedPreferences("results", MODE_PRIVATE);
        username = sharedPref.getString("username", "DefaultName");
        speed = Double.valueOf(sharedPref.getFloat("speed", 0.0f));
        elevation = Double.valueOf(sharedPref.getFloat("elevation", 0.0f));
        time = Double.valueOf(sharedPref.getFloat("time", 0.0f));
        distance = Double.valueOf(sharedPref.getFloat("distance", 0.0f));

        avgSpeed = Double.valueOf(sharedPref.getFloat("avgSpeed", 0.0f));
        avgElevation = Double.valueOf(sharedPref.getFloat("avgElevation", 0.0f));
        avgDistance = Double.valueOf(sharedPref.getFloat("avgDistance", 0.0f));
        avgTime = Double.valueOf(sharedPref.getFloat("avgTime", 0.0f));



        // Rest of your code...

        elevationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGraphData(elevation, avgElevation);
            }
        });

        speedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGraphData(speed, avgSpeed);
            }
        });

        distanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGraphData(distance, avgDistance);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadGraphData(time, avgTime);
            }
        });
    }

    private void loadGraphData(double dataValue, double average) {
        // Clear any existing data
        graphView.removeAllSeries();

        // Create an array of DataPoints for your data
        DataPoint[] dataPoints = new DataPoint[] { new DataPoint(0, dataValue) };

        // Create a BarGraphSeries
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(dataPoints);

        // Add series to graph
        graphView.addSeries(series);

        // Draw average line
        LineGraphSeries<DataPoint> avgSeries = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, average), new DataPoint(dataPoints.length - 1, average)
        });
        avgSeries.setColor(Color.RED);
        graphView.addSeries(avgSeries);


    }
}