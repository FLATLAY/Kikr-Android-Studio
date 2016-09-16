package com.kikr.ibeacon;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kikr.R;
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

public class MonitoringActivity extends Activity implements IBeaconConsumer {
	private ListView list = null;
	private BeaconAdapter adapter = null;
	private ArrayList<IBeacon> arrayL = new ArrayList<IBeacon>();
	private LayoutInflater inflater;

	private BeaconServiceUtility beaconUtill = null;
	private IBeaconManager iBeaconManager = IBeaconManager.getInstanceForApplication(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitor);
		beaconUtill = new BeaconServiceUtility(this);
		list = (ListView) findViewById(R.id.list);
		adapter = new BeaconAdapter();
		list.setAdapter(adapter);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override

	protected void onStart() {
		super.onStart();
		beaconUtill.onStart(iBeaconManager, this);
	}

	@Override
	protected void onStop() {
		beaconUtill.onStop(iBeaconManager, this);
		super.onStop();
	}

	@Override
	public void onIBeaconServiceConnect() {

		iBeaconManager.setRangeNotifier(new RangeNotifier() {
			@Override
			public void didRangeBeaconsInRegion(Collection<IBeacon> iBeacons, Region region) {
				checkIBeaconList((ArrayList<IBeacon>) iBeacons);
				arrayL.clear();
				arrayL.addAll((ArrayList<IBeacon>) iBeacons);
				adapter.notifyDataSetChanged();
			}

		});

		iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
			@Override
			public void didEnterRegion(Region region) {
				Log.e("BeaconDetactorService", "didEnterRegion");
				// logStatus("I just saw an iBeacon for the first time!");
			}

			@Override
			public void didExitRegion(Region region) {
				Log.e("BeaconDetactorService", "didExitRegion");
				// logStatus("I no longer see an iBeacon");
			}

			@Override
			public void didDetermineStateForRegion(int state, Region region) {
				Log.e("BeaconDetactorService", "didDetermineStateForRegion");
				// logStatus("I have just switched from seeing/not seeing iBeacons: " + state);
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

	

	private class BeaconAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (arrayL != null && arrayL.size() > 0)
				return arrayL.size();
			else
				return 0;
		}

		@Override
		public IBeacon getItem(int arg0) {
			return arrayL.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			try {
				ViewHolder holder;

				if (convertView != null) {
					holder = (ViewHolder) convertView.getTag();
				} else {
					holder = new ViewHolder(convertView = inflater.inflate(R.layout.tupple_monitoring, null));
				}
				if (arrayL.get(position).getProximityUuid() != null)
				holder.beacon_uuid.setText("UUID: " + arrayL.get(position).getProximityUuid());

				holder.beacon_major.setText("Major: " + arrayL.get(position).getMajor());

				holder.beacon_minor.setText(", Minor: " + arrayL.get(position).getMinor());

				holder.beacon_proximity.setText("Proximity: " + arrayL.get(position).getProximity());

				holder.beacon_rssi.setText(", Rssi: " + arrayL.get(position).getRssi());

				holder.beacon_txpower.setText(", TxPower: " + arrayL.get(position).getTxPower());

				holder.beacon_range.setText("" + arrayL.get(position).getAccuracy());

			} catch (Exception e) {
				e.printStackTrace();
			}

			return convertView;
		}

		private class ViewHolder {
			private TextView beacon_uuid;
			private TextView beacon_major;
			private TextView beacon_minor;
			private TextView beacon_proximity;
			private TextView beacon_rssi;
			private TextView beacon_txpower;
			private TextView beacon_range;

			public ViewHolder(View view) {
				beacon_uuid = (TextView) view.findViewById(R.id.BEACON_uuid);
				beacon_major = (TextView) view.findViewById(R.id.BEACON_major);
				beacon_minor = (TextView) view.findViewById(R.id.BEACON_minor);
				beacon_proximity = (TextView) view.findViewById(R.id.BEACON_proximity);
				beacon_rssi = (TextView) view.findViewById(R.id.BEACON_rssi);
				beacon_txpower = (TextView) view.findViewById(R.id.BEACON_txpower);
				beacon_range = (TextView) view.findViewById(R.id.BEACON_range);

				view.setTag(this);
			}
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
					for(int i=0;i<ibeaconMessages.size();i++)
						generateNotification(MonitoringActivity.this, ibeaconMessages.get(i).getMessage(),ibeaconMessages.get(i));
				}
				else
					generateNotification(MonitoringActivity.this, "No message found for this UUID",null);	
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				
			}
		});
		service.getMessage(AppConstants.APP_ID,uuid, major, minor,UserPreference.getInstance().getUserID());
		service.execute();
	}
	
	private static void generateNotification(Context context, String message, IbeaconMessage ibeaconMessage) {

		Intent launchIntent = new Intent(context, BeaconMessageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		int notificationId=0;
		if(ibeaconMessage!=null){
			launchIntent.putExtra("data", ibeaconMessage);
			notificationId=Integer.parseInt(ibeaconMessage.getId());
		}

		((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(
				0,
				new NotificationCompat.Builder(context).setWhen(System.currentTimeMillis())
						.setSmallIcon(R.drawable.ic_app_logo).setTicker(message)
						.setContentTitle(context.getString(R.string.app_name)).setContentText(message)
						.setContentIntent(PendingIntent.getActivity(context, notificationId, launchIntent, 0)).setAutoCancel(true)
						.build());

	}

}