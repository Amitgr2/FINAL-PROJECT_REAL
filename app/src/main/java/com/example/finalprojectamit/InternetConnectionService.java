package com.example.finalprojectamit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class InternetConnectionService extends Service {
    public InternetConnectionService() {
    }

    private static final int NOTIFICATION_ID = 1; // Unique identifier for the notification
    private static final String CHANNEL_ID = "NetworkServiceChannel"; // Unique identifier for the notification channel
    private ConnectivityManager connectivityManager; // Manages network connectivity
    private NetworkChangeReceiver networkChangeReceiver; // Listens for network change events

    @Override
    public void onCreate() { // Called when the service is created
        super.onCreate();
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE); // Initialize connectivity manager
        networkChangeReceiver = new NetworkChangeReceiver(); // Initialize network change receiver
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { // Called when the service is started
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION); // Intent filter for network connectivity changes
        registerReceiver(networkChangeReceiver, intentFilter); // Register the network change receiver

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // checks if the user's phone is using android oreo API 26 and above in order for the notification to work
            NotificationChannel channel = new NotificationChannel( //making the notification channel available to use
                    CHANNEL_ID,
                    "Network Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class); // creating a notification manager
            notificationManager.createNotificationChannel(channel);
        }

        // Check initial internet connection status
        boolean isConnected = isNetworkConnected();
        if (isConnected) {
            showConnectedNotification(); // Show connected notification
        } else {
            showDisconnectedNotification(); // Show disconnected notification
        }

        // Return START_STICKY to indicate that the service should be restarted if terminated
        return START_STICKY;
    }

    @Override
    public void onDestroy() { // Called when the service is destroyed
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver); // Unregister the network change receiver
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showConnectedNotification() { // Show a notification when connected to the internet
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID) // creating the notification
                .setContentTitle("Network Service")
                .setContentText("You have internet connection")
                .setSmallIcon(R.drawable.ic_check)
                .build();

        startForeground(NOTIFICATION_ID, notification); // Start the service as a foreground service with the connected notification
    }

    private void showDisconnectedNotification() { // Show a notification when disconnected from the internet
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID) // creating the notification
                .setContentTitle("Network Service")
                .setContentText("No internet connection")
                .setSmallIcon(R.drawable.ic_baseline_warning_24)
                .build();

        startForeground(NOTIFICATION_ID, notification); // Start the service as a foreground service with the disconnected notification
    }

    private boolean isNetworkConnected() { // Check if the device is connected to the internet
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class NetworkChangeReceiver extends BroadcastReceiver { // Receiver for network change events

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isConnected = isNetworkConnected();
            if (isConnected) {
                showConnectedNotification(); // Show connected notification
            } else {
                showDisconnectedNotification(); // Show disconnected notification
            }
        }
    }
}