package com.tc.roboservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "From Alarm...", Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context, CountingService.class));
    }
}
