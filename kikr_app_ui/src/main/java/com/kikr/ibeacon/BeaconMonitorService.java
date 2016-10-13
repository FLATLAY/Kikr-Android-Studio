package com.kikr.ibeacon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.utility.AppConstants;
import com.kikrlib.api.GetMessageFromUUIDApi;
import com.kikrlib.bean.IbeaconMessage;
import com.kikrlib.db.DatabaseHelper;
import com.kikrlib.db.UserPreference;
import com.kikrlib.db.dao.UuidDAO;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.GetMessageRes;
import com.kikrlib.utils.Syso;
import com.radiusnetworks.ibeacon.IBeacon;
import com.radiusnetworks.ibeacon.IBeaconConsumer;
import com.radiusnetworks.ibeacon.IBeaconManager;
import com.radiusnetworks.ibeacon.MonitorNotifier;
import com.radiusnetworks.ibeacon.RangeNotifier;
import com.radiusnetworks.ibeacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeaconMonitorService extends Service implements IBeaconConsumer{
	Context context;
	private BeaconServiceUtility beaconUtill = null;
	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Syso.info("BeaconMonitorService onCreate");
		context=this;
		beaconUtill = new BeaconServiceUtility(this);
		beaconUtill.onStart(iBeaconManager, this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Syso.info("BeaconMonitorService onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onIBeaconServiceConnect() {

		iBeaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
				checkIBeaconList((ArrayList<IBeacon>) iBeacons);
			}

		});

		iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
			@Override
			public void didEnterRegion(Region region) {
				Log.e("BeaconDetactorService", "didEnterRegion");
			}

			@Override
			public void didExitRegion(Region region) {
				Log.e("BeaconDetactorService", "didExitRegion");
			}

			@Override
			public void didDetermineStateForRegion(int state, Region region) {
				Log.e("BeaconDetactorService", "didDetermineStateForRegion");
			}

		});

		try {
			iBeaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		try {
			iBeaconManager.startMonitoringBeaconsInRegion(new Region("myMonitoringUniqueId", null, null, null));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	protected synchronized void checkIBeaconList(ArrayList<IBeacon> iBeacons) {
		// TODO Auto-generated method stub
		Syso.info("In check ibeacon list");
		ArrayList<String> uuidList=new ArrayList<String>();
		UuidDAO dao=new UuidDAO(DatabaseHelper.getDatabase());
		for(int i=0;i<iBeacons.size();i++){
			String uuid=iBeacons.get(i).getProximityUuid();
			if(dao.isUuidExist(uuid)){
				Syso.info("uuid exist in our db "+uuid);
				uuidList.add(uuid);
				if(!dao.isUuidInRange(uuid)){
					Syso.info("uuid in the range "+uuid);
					getMessageBasedOnUUID(uuid,String.valueOf(iBeacons.get(i).getMajor()),String.valueOf(iBeacons.get(i).getMinor()));
				}
			}else{
				Syso.info("uuid not match in our database");
				//this uuid not registerd with our server.
			}
		}
		dao.updateUuidStatus(uuidList);
	}
	
	private void getMessageBasedOnUUID(String uuid,String major,String minor){
		final GetMessageFromUUIDApi service = new GetMessageFromUUIDApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				GetMessageRes messageRes=(GetMessageRes) object;
				List<IbeaconMessage> ibeaconMessages=messageRes.getMessage_list();
				if(ibeaconMessages.size()>0){
					for(int i=0;i<ibeaconMessages.size();i++){
						if(!UserPreference.getInstance().getUserID().equals(""))
							generateNotification(context, ibeaconMessages.get(i).getMessage(),ibeaconMessages.get(i));
					}
				}
//				else
//					generateNotification(context, "No message found for this UUID",null);	
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				
			}
		});
		service.getMessage(AppConstants.APP_ID,uuid, major, minor,UserPreference.getInstance().getUserID());
		service.execute();
	}
	
//	private static void generateNotification(Context context, String message, IbeaconMessage ibeaconMessage) {
//
//		Intent launchIntent = new Intent(context, BeaconMessageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		int notificationId=0;
//		if(ibeaconMessage!=null){
//			launchIntent.putExtra("data", ibeaconMessage);
//			notificationId=Integer.parseInt(ibeaconMessage.getId());
//		}
//
//		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
//				0,
//				new NotificationCompat.Builder(context).setWhen(System.currentTimeMillis())
//						.setSmallIcon(R.drawable.ic_app_logo).setTicker(message)
//						.setContentTitle(context.getString(R.string.app_name)).setContentText(message)
//						.setContentIntent(PendingIntent.getActivity(context, notificationId, launchIntent, 0)).setAutoCancel(true)
//						.build());
//
//	}
	
	/**
	 * 
	 * @param context
	 * @param message
	 * @param ibeaconMessage
	 * this method used for show status bar notification
	 */
	private void generateNotification(Context context, String message,IbeaconMessage ibeaconMessage) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.flatlayhomeimage);
        notificationBuilder.setTicker(message);
        notificationBuilder.setContentTitle(context.getString(R.string.app_name));
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);

        Intent intent = null; 
        int notificationId=0;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        
        intent = new Intent(context, BeaconMessageActivity.class);
      
		if(ibeaconMessage!=null){
			intent.putExtra("data", ibeaconMessage);
			notificationId=Integer.parseInt(ibeaconMessage.getId());
			intent.setData(Uri.parse(notificationId+""));
		}
		
        stackBuilder.addParentStack(HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
        stackBuilder.addNextIntent(intent);  
        
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
       System.out.println("123456 notification>>>"+notificationId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if(beaconUtill!=null&&iBeaconManager!=null){
			beaconUtill.onStop(iBeaconManager, this);
		}
		super.onDestroy();
	}
}
