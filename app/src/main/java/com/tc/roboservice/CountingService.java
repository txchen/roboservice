package com.tc.roboservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CountingService extends Service {

    private int mCurrentScore = 0;

    private final String TAG = "CS";

    @Override
    public void onCreate() {
        mCurrentScore = 0;
        Log.i(TAG, "onCreate CountingService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand CountingService");
        Toast.makeText(this, "CountingService is starting", Toast.LENGTH_SHORT).show();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {

        Log.w(TAG, "onDestroy countingService");
        Toast.makeText(this, "CountingService is destroyed", Toast.LENGTH_SHORT).show();
    }
}
