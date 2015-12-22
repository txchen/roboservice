package com.tc.roboservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btnStart = (Button) findViewById(R.id.btnStartService);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, CountingService.class));
            }
        });

        final Button btnStop = (Button) findViewById(R.id.btnStopService);
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, CountingService.class));
            }
        });

        final Button btnSetAlarm = (Button) findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(MainActivity.this, AlarmReceiver.class);
                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10, pi); // Millisec * Second
            }
        });

        final Button btnCancelAlarm = (Button) findViewById(R.id.btnCancelAlarm);
        btnCancelAlarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(MainActivity.this, AlarmReceiver.class);
                PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, i, 0);
                manager.cancel(sender);
            }
        });
    }
}
