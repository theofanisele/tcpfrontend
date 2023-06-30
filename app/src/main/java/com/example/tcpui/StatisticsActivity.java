package com.example.tcpui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class StatisticsActivity extends AppCompatActivity {
    private GraphView graphView;
    private String username = null;
    private Double speed = 0.0;
    private Double elevation = 0.0;
    private Double time = 0.0;
    private Double distance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        graphView = findViewById(R.id.idGraphView);
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        setTitle("Statistics");


        try{
            Bundle bundle = getIntent().getExtras();
            username = bundle.getString("username");
            speed = bundle.getDouble("speed");
            elevation = bundle.getDouble("elevation");
            time = bundle.getDouble("time");
            distance = bundle.getDouble("distance");

        }catch(Exception e){
            e.printStackTrace();
        }


        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                Bundle bundle = new Bundle();
                bundle.putDouble("speed", speed);
                bundle.putDouble("elevation", elevation);
                bundle.putDouble("distance", distance);
                bundle.putDouble("time", time);
                bundle.putString("username", username);

                switch (item.getItemId()) {
                    case R.id.navigation_add_route:
                        intent = new Intent(StatisticsActivity.this, UploadFileActivity.class);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.navigation_statistics:
                        intent = new Intent(StatisticsActivity.this, StatisticsActivity.class);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.navigation_my_results:
                        intent = new Intent(StatisticsActivity.this, MyResultsActivity.class);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });

        // on below line we are adding data to our graph view.
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[]{
                // on below line we are adding.
                // each point on our x and y axis.
                new DataPoint(0, 1),
                new DataPoint(1, 3),
                new DataPoint(2, 4),
                new DataPoint(3, 9),
                new DataPoint(4, 6),
                new DataPoint(5, 3),
                new DataPoint(6, 6),
                new DataPoint(7, 1),
                new DataPoint(8, 2)
        });

        // after adding data to our line graph series.
        // on below line we are setting
        // title for our graph view.
        graphView.setTitle("My Graph View");
        // on below line we are setting
        // text color to our graph view.
        graphView.setTitleColor(R.color.purple_200);

        // on below line we are setting
        // our title text size.
        graphView.setTitleTextSize(18);

        // on below line we are adding
        // data series to our graph view.
        graphView.addSeries(series);
    }
}
