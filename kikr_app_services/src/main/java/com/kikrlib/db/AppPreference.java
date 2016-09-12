package com.kikrlib.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.kikrlib.AppContext;
import com.kikrlib.utils.Syso;

public class AppPreference {

	private String TAG = this.getClass().getSimpleName();
	private SharedPreferences mPrefs;
	private Editor mPrefsEditor;

	private final String mServerIp = "mServerIp";


	private static AppPreference INSTANCE;

	public static AppPreference getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new AppPreference(AppContext.getInstance().getContext());
		}
		return INSTANCE;
	}

	private AppPreference(Context context) {
		this.mPrefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		this.mPrefsEditor = mPrefs.edit();
	}

	public void setServerIp(String value) {
		mPrefsEditor.putString(mServerIp, value);
		mPrefsEditor.commit();
	}

	public String getServerIp() {
		return mPrefs.getString(mServerIp, "");
	}

	public void savePurchaseId(String id){
		mPrefsEditor.putBoolean(id, true);
		mPrefsEditor.commit();
	}

	public boolean isShowNotification(String id){
		return mPrefs.getBoolean(id,true);
	}

	public void setIsShowNotification(String id,boolean status){
		mPrefsEditor.putBoolean(id, status);
		mPrefsEditor.commit();
	}
}
