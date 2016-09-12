package com.kikr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
        Intent  intent2=new Intent(context,DealNotificationService.class);
        context.startService(intent2);
    }

}
