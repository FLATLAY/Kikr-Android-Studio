package com.flatlay;

//import com.crashlytics.android.Crashlytics;

import io.branch.referral.BranchApp;
//import io.fabric.sdk.android.Fabric;

import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.AppContext;
import com.flatlaylib.db.DatabaseHelper;

//test
public class KikrApp extends BranchApp {

	private static DatabaseHelper mDatabaseHelper;
	private boolean mAddressValidForPromo;
    private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    
    private static final String PROPERTY_ID = "UA-59209406-2";
    
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

	@Override
	public void onCreate() {
		super.onCreate();
		//Fabric.with(this, new Crashlytics());
		Log.w("Activity","KikrApp");
		AppContext context = AppContext.getInstance();
		context.setContext(this);
		mDatabaseHelper = DatabaseHelper.getIntance(this);
		FontUtility.overrideFont(getApplicationContext(), "SERIF"); // font from assets
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.exit(0);
	}

	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static SQLiteDatabase getDatabase() {
		return mDatabaseHelper.getWritableDatabase();
	}

	
	public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(
                            R.xml.global_tracker)
                            : analytics.newTracker(R.xml.ecommerce_tracker);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

	public String getAccountName() {
		return null;
	}

	public boolean isAddressValidForPromo() {
		return mAddressValidForPromo;
	}

	public void setAddressValidForPromo(boolean addressValidForPromo) {
		this.mAddressValidForPromo = addressValidForPromo;
	}
}
