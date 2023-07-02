package com.example.tcpui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.Map;

public class ResultService extends Service {

    // Unique id for the notification
    private static final int NOTIFICATION_ID = 1234;
    // Notification channel id
    private static final String CHANNEL_ID = "channel_id";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    public void onCreate() {
        super.onCreate();

        sharedPref = getSharedPreferences("results", MODE_PRIVATE);
        editor = sharedPref.edit();

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        CharSequence name = "My Channel";
        String description = "Channel for My Results notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification) // replace with your own icon
                .setContentTitle("Result Service Running")
                .setContentText("The service is running in the foreground.");
        startForeground(NOTIFICATION_ID, builder.build());
        // Start a separate thread and run the receiving task there
        new Thread(() -> {
            try {
                Client.getInstance().receive();
                Results results = Client.getInstance().getResults();
                Results averages = Client.getInstance().getAverages();
                Map<String,Double> percDiffCalc = (Map<String, Double>) Client.getInstance().getPercDiffCalculator();

                if (results != null && averages != null && percDiffCalc != null) {
                    // If new results received, show notification
                    showNotification(results);
                    editor.putString("username", results.getName());
                    editor.putFloat("speed", results.getAverageSpeed().floatValue());
                    editor.putFloat("elevation", results.getTotalElevationGain().floatValue());
                    editor.putFloat("distance", results.getTotalDistance().floatValue());
                    editor.putFloat("time", results.getTotalTime().floatValue());

                    editor.putFloat("avgSpeed", averages.getAverageSpeed().floatValue());
                    editor.putFloat("avgElevation", averages.getTotalElevationGain().floatValue());
                    editor.putFloat("avgDistance", averages.getTotalDistance().floatValue());
                    editor.putFloat("avgTime", averages.getTotalTime().floatValue());

                    editor.putFloat("% distance", percDiffCalc.get("% distance").floatValue());
                    editor.putFloat("% elevation", percDiffCalc.get("% elevation").floatValue());
                    editor.putFloat("% time", percDiffCalc.get("% time").floatValue());
                    editor.putFloat("% speed", percDiffCalc.get("% speed").floatValue());


                    editor.apply();
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                Log.i("ReceiveTask", e.getMessage());
            }
        }).start();

        // If service gets killed, after returning from here, restart
        return START_STICKY;
    }

    private void showNotification(Results results) {
        // Create a simple notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification) // replace with your own icon
                .setContentTitle("New Results")
                .setContentText("New results have been received.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}

