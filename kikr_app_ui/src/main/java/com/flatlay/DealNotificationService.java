package com.flatlay;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.flatlay.activity.DealWebViewActivity;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.bean.TopDeals;
import com.kikrlib.db.DatabaseHelper;
import com.kikrlib.db.dao.FavoriteDealsDAO;

import java.util.ArrayList;
import java.util.List;

public class DealNotificationService extends Service{
	
	private List<TopDeals> list = new ArrayList<TopDeals>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		FavoriteDealsDAO dao = new FavoriteDealsDAO(DatabaseHelper.getDatabase());
		list = dao.getList();
		if (list.size()>0) 
			checkDealsForExpiry();
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void checkDealsForExpiry() {
		for (int i = 0; i < list.size(); i++) {
		if ((int)CommonUtility.setExpiryDateNotification(list.get(i).getExpiryTime())== 1)
		generateNotification(getApplicationContext(), "Deal expiring tomorrow", list.get(i),i);
		else if((int)CommonUtility.setExpiryDateNotification(list.get(i).getExpiryTime()) < 0){
			FavoriteDealsDAO dao = new FavoriteDealsDAO(DatabaseHelper.getDatabase());
			dao.delete(list.get(i).getDealid());
		}
		}
	}

	private static void generateNotification(Context context, String message, TopDeals topDeals, int i) {
		Intent launchIntent = new Intent(context, DealWebViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		int notificationId=i;
			launchIntent.putExtra("data", topDeals.getLink());
			NotificationManager notificationManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
			notificationManager.notify(i, new NotificationCompat.Builder(context).setWhen(System.currentTimeMillis())
						.setSmallIcon(R.drawable.flatlayhomeimage).setTicker(message)
						.setContentTitle(context.getString(R.string.app_name)).setContentText(message)
						.setContentIntent(PendingIntent.getActivity(context, notificationId, launchIntent, 0)).setAutoCancel(true)
						.build());
	}
}
