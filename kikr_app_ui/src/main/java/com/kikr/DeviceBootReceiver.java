package com.kikr;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
	        /* Set the alarm to start at 08:00 AM */
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(System.currentTimeMillis());
	        calendar.set(Calendar.HOUR_OF_DAY, 8);
	        calendar.set(Calendar.MINUTE, 00);
	    	AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	    	Intent _myIntent = new Intent(context, AlarmReceiver.class);
	    	PendingIntent _myPendingIntent = PendingIntent.getBroadcast(context, 123, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//	    	alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),_myPendingIntent);
	    	/* Repeating on every 1 day interval */
	    	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
	    			24*60*60*1000, _myPendingIntent);  
        }
    }
}
