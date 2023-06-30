package com.example.tcpui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcpui.Client;
import com.example.tcpui.R;
import com.example.tcpui.Results;
import com.example.tcpui.ResultsAdapter;
import com.example.tcpui.StatisticsActivity;
import com.example.tcpui.UploadFileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyResultsActivity  extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ResultsAdapter resultsAdapter;
    private List<Results> resultsList = new ArrayList<>();
    private String username = null;
    private Double speed = 0.0;
    private Double elevation = 0.0;
    private Double time = 0.0;
    private Double distance = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_results);
        setTitle("Results");

        new ReceiveTask().execute();

        recyclerView = findViewById(R.id.results_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        resultsAdapter = new ResultsAdapter(resultsList);
        recyclerView.setAdapter(resultsAdapter);
        try{
            Bundle bundle = getIntent().getExtras();
            String username = bundle.getString("username");
            Double speed = bundle.getDouble("speed");
            Double elevation = bundle.getDouble("elevation");
            Double time = bundle.getDouble("time");
            Double distance = bundle.getDouble("distance");
            if(username != null){
                Results results = new Results(username,distance,elevation,time,speed);
                resultsList.add(results);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                Bundle bundle = new Bundle();
                bundle.putDouble("speed", resultsList.get(0).getAverageSpeed());
                bundle.putDouble("elevation", resultsList.get(0).getTotalElevationGain());
                bundle.putDouble("distance", resultsList.get(0).getTotalDistance());
                bundle.putDouble("time", resultsList.get(0).getTotalTime());
                bundle.putString("username", resultsList.get(0).getName());

                switch (item.getItemId()) {
                    case R.id.navigation_add_route:
                        intent = new Intent(MyResultsActivity.this, UploadFileActivity.class);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.navigation_statistics:
                        intent = new Intent(MyResultsActivity.this, StatisticsActivity.class);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.navigation_my_results:
                        intent = new Intent(MyResultsActivity.this, MyResultsActivity.class);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });


    }

    private class ReceiveTask extends AsyncTask<Void, Void, Results> {
        @Override
        protected Results doInBackground(Void... params) {
            try {
                Client.getInstance().receive();
                Results results = Client.getInstance().getResults();
                Log.d("results",results.toString());
                return results;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                Log.i("ReceiveTask", e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Results results) {
            if (results != null) {
                Log.i("ReceiveTask", "Received results: " + results.toString());
                resultsList.add(results);
                resultsAdapter.notifyDataSetChanged();
            }
        }
    }
}