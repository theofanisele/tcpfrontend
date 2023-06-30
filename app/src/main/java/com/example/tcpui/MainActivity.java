package com.example.tcpui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btnClientConnect;
    private byte[] fileData;
    private Client client;

    private static final int PICKFILE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnClientConnect = findViewById(R.id.btnClientConnect);
        setTitle("Login");
    }

    public void onClickConnect(View view) {
        String host = "10.0.2.2";
        int portNumber = 8000;

        new Thread(() -> {
            try {
                client = Client.getClient(host, portNumber);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            runOnUiThread(() -> {
                Intent intent = new Intent(MainActivity.this, UploadFileActivity.class);
                startActivity(intent);
            });
        }).start();

    }

}