package com.kikr;

import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentPlaceMyOrder;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.Syso;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

public class GCMAlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent arg1) {
		
		Syso.info("uuuuuuuuuuuuuu>>>>>>>>> in GCMAlarmReceiver onReceive");
		if(!UserPreference.getInstance().getIsNotificationClicked()&&!TextUtils.isEmpty(UserPreference.getInstance().getPurchaseId())){
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(1);
			setNotification(context);
			UserPreference.getInstance().setPurchaseId("");
			UserPreference.getInstance().setFinalPrice("");
//			FragmentPlaceMyOrder myOrder = new FragmentPlaceMyOrder(null, false);
//			myOrder.callServerForConfirmation(UserPreference.getInstance().getPurchaseId(), "cancel");
		}
	}

	private void setNotification(Context context) {
		Syso.info("uuuuuuuuuuuuuu>>>>>>>>> in setNotification");
		String message = "Your order has bean cancelled, Please try again.";
		
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
		notificationBuilder.setSmallIcon(R.drawable.ic_app_logo);
		notificationBuilder.setTicker(message);
		notificationBuilder.setContentTitle("Kikr");
		notificationBuilder.setContentText(message);
		notificationBuilder.setAutoCancel(true);

		Intent intent = new Intent(context, HomeActivity.class);
		intent.putExtra("message", message);
		intent.putExtra("section", "cancel");
		intent.setData(Uri.parse("cancel"));
		PendingIntent pendingIntent = PendingIntent.getActivity(context,1, intent,PendingIntent.FLAG_ONE_SHOT);
		notificationBuilder.setContentIntent(pendingIntent);
		Notification notification = notificationBuilder.build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(1, notification);
	}
	

}
