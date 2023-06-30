package com.example.tcpui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadFileActivity extends AppCompatActivity {

    private static final int PICKFILE_REQUEST_CODE = 1;
    private Button btnChooseFile;
    private Button btnUpload;
    public byte[] fileData;
    private String username = null;
    private Double speed = 0.0;
    private Double elevation = 0.0;
    private Double time = 0.0;
    private Double distance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_file);
        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnUpload = findViewById(R.id.btnUpload);
        setTitle("Add route");

        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
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
                        intent = new Intent(UploadFileActivity.this, UploadFileActivity.class);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.navigation_statistics:
                        intent = new Intent(UploadFileActivity.this, StatisticsActivity.class);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case R.id.navigation_my_results:
                        intent = new Intent(UploadFileActivity.this, MyResultsActivity.class);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });

    }

    public void onClickChooseFile(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            if(data == null){
                return;
            }
            Uri fileUri = data.getData();
            try {
                InputStream is = getContentResolver().openInputStream(fileUri);
                fileData = readBytes(is);
                Toast.makeText(getApplicationContext(), "File loaded", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void onClickUpload(View view) throws IOException {
        try {
            if (fileData == null){
                Toast.makeText(getApplicationContext(), "Please Choose a file first", Toast.LENGTH_SHORT).show();
            }
            else if (fileData != null) {
                if (Client.getInstance() != null) {
                    new UploadTask().execute(fileData);
//                    Intent intent = new Intent(UploadFileActivity.this, StatisticsActivity.class);
//                    startActivity(intent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
    private class UploadTask extends AsyncTask<byte[], Void, Void> {
        @Override
        protected Void doInBackground(byte[]... params) {
            try {
                Client.getInstance().upload(params[0]);
                Log.i("yo", "estilen ta");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("yo", e.getMessage());
                // Handle error gracefully
            }
            return null;
        }
    }
}




