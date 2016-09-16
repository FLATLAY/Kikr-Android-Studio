package com.kikr.ibeacon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kikr.R;
import com.radiusnetworks.ibeacon.IBeaconManager;

public class BeaconMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_main);
		verifyBluetooth();
	}

	public void onMonitoringClicked(View view) {
		Intent myIntent = new Intent(this, MonitoringActivity.class);
		this.startActivity(myIntent);
	}

	private void verifyBluetooth() {
		try {
			if (!IBeaconManager.getInstanceForApplication(this).checkAvailability()) {
				
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intentOpenBluetoothSettings = new Intent();
						intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS); 
						startActivity(intentOpenBluetoothSettings); 
					}
				});
				builder.show();

			}
		} catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
			builder.show();

		}

	}

}
