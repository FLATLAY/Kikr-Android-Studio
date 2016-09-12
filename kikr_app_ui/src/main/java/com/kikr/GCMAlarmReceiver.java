package com.kikr;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.kikr.activity.HomeActivity;
import com.kikrlib.db.AppPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.Syso;

import java.util.Calendar;

public class GCMAlarmReceiver extends BroadcastReceiver{


	public static String FROM_ORDER_NOTIFICATION = "from_order_notification";
	private static int NOTIFICATION_WAIT_TIME = 60; //in minute

	@Override
	public void onReceive(Context context, Intent intent) {
		Syso.info("uuuuuuuuuuuuuu>>>>>>>>> in GCMAlarmReceiver onReceive");
		if(intent.hasExtra("from")&&intent.getStringExtra("from").equals(FROM_ORDER_NOTIFICATION)){
			String message = "Your order has been processed, please check status in orders";
			String purchase_id = intent.getStringExtra("purchase_id");
			String otherdata = "{\"message\":\""+message+"\"}";
			if(AppPreference.getInstance().isShowNotification(purchase_id)){
				AppPreference.getInstance().setIsShowNotification(purchase_id,false);
				setNotification(context,message,"twotap",otherdata);
			}
		}else if(!UserPreference.getInstance().getIsNotificationClicked()&&!TextUtils.isEmpty(UserPreference.getInstance().getPurchaseId())){
			String message = "Your order has bean cancelled, Please try again.";
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(1);
			setNotification(context,message,"cancel",null);
			UserPreference.getInstance().setPurchaseId("");
			UserPreference.getInstance().setFinalPrice("");
		}
	}

	private void setNotification(Context context,String message,String section,String otherdata) {
		Syso.info("uuuuuuuuuuuuuu>>>>>>>>> in setNotification");
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
		notificationBuilder.setSmallIcon(R.drawable.flatlayhomeimage);
		notificationBuilder.setTicker(message);
		notificationBuilder.setContentTitle(context.getString(R.string.app_name));
		notificationBuilder.setContentText(message);
		notificationBuilder.setAutoCancel(true);

		Intent intent = new Intent(context, HomeActivity.class);
		intent.putExtra("message", message);
		intent.putExtra("section", section);
		if(otherdata!=null)
			intent.putExtra("otherdata", otherdata);
		intent.setData(Uri.parse(section));
		PendingIntent pendingIntent = PendingIntent.getActivity(context,1, intent,PendingIntent.FLAG_ONE_SHOT);
		notificationBuilder.setContentIntent(pendingIntent);
		Notification notification = notificationBuilder.build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, notification);
	}


	public static void setAlarmForOrderNotification(String purchase_id,Context context) {
		AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + NOTIFICATION_WAIT_TIME);
		Intent ii =  new Intent(context, GCMAlarmReceiver.class);
		ii.setData(Uri.parse(purchase_id));
		ii.putExtra("purchase_id",purchase_id);
		ii.putExtra("from", FROM_ORDER_NOTIFICATION);
		PendingIntent intent = PendingIntent.getBroadcast(context,0,ii, 0);
		manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent);
	}

}
