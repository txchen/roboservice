package com.tc.roboservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class CountingService extends Service {

    private int mCurrentScore = 0;
    private final Handler myHandler = new Handler();
    CountThread mCT;

    private final String TAG = "CS";

    @Override
    public void onCreate() {
        super.onCreate();
        mCurrentScore = 0;
        Log.i(TAG, "onCreate CountingService");
        mCT = new CountThread();
        mCT.start();
    }

    private Notification buildNotification(String text) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);

        return new NotificationCompat.Builder(this)
                .setContentTitle("RoboService")
                .setTicker("RoboService started!!!")
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand CountingService");
        Toast.makeText(this, "CountingService is starting", Toast.LENGTH_SHORT).show();
        startForeground(101, buildNotification("My Text"));

        if (intent != null && intent.getBooleanExtra("crash", false)) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(3000);
                        int i = Integer.parseInt("aaa");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        // if return START_STICKY, system will restart this service by passing intent = null after the
        //   service is killed or crashed
        //   If kill process, service will always be restarted by system.
        // if return START_REDELIVER_INTENT, system will restart this service by passing same intent after the
        //   service is killed or crashed
        // if the service is crashed, system will only restart the service once.
        // but if we handle the unhandledException and exit(1), system will not popup a crash dialog, and it
        //   will always restart the service.
        return START_STICKY;
    }

    private class CountThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    sleep(3000);
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    mCurrentScore++;
                    myHandler.post(updateRunnable);
                } catch (InterruptedException e) {
                    Log.w(TAG, "Thread is ending itself");
                    return;
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

    final Runnable updateRunnable = new Runnable() {
        public void run() {
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            Log.w(TAG, "Increase count to: " + Integer.toString(mCurrentScore));
            mNotificationManager.notify(101, buildNotification("Count: " + Integer.toString(mCurrentScore)));
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.w(TAG, "onLowMemory countingService");
        Toast.makeText(this, "CountingService low memory", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy countingService");
        Toast.makeText(this, "CountingService is destroyed", Toast.LENGTH_SHORT).show();
        if (mCT != null) {
            Log.w(TAG, "kill the repeating thread");
            mCT.interrupt();
        }
        stopSelf();
    }
}
