package com.flatlaylib.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.flatlaylib.AppContext;
import com.flatlaylib.utils.Syso;

public class HelpPreference {

	private String TAG = this.getClass().getSimpleName();
	private SharedPreferences mPrefs;
	private Editor mPrefsEditor;

	private final String mHelpInspiration = "mHelpInspiration";
	private final String mHelpKikrCards = "mHelpKikrCards";
	private final String mHelpPinMenu = "mHelpPinMenu";
	private final String mHelpSideMenu = "mHelpSideMenu";
	private final String mHelpCollection = "mHelpCollection";
	private final String mHelpCart = "mHelpCart";
	private final String mHelpFriendsSideMenu = "mHelpFriendsSideMenu";
	private static HelpPreference INSTANCE;

	public static HelpPreference getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new HelpPreference(AppContext.getInstance().getContext());
		}
		return INSTANCE;
	}
	
	public void clearAllData() {
		mPrefsEditor.clear();
		mPrefsEditor.commit();
	}

	private HelpPreference(Context context) {
		this.mPrefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		this.mPrefsEditor = mPrefs.edit();
	}

	public void setHelpInspiration(String value) {
		Syso.debug("setUserID = ", value);
		if(value!=null){
			mPrefsEditor.putString(mHelpInspiration, value);
			mPrefsEditor.commit();
		}
	}

	public String getHelpInspiration() {
		return mPrefs.getString(mHelpInspiration, "");
	}
	
	public void setHelpKikrCards(String value) {
		Syso.debug("setUserID = ", value);
		if(value!=null){
			mPrefsEditor.putString(mHelpKikrCards, value);
			mPrefsEditor.commit();
		}
	}

	public String getHelpKikrCards() {
		return mPrefs.getString(mHelpKikrCards, "");
	}
	
	public void setHelpPinMenu(String value) {
		if(value!=null){
			mPrefsEditor.putString(mHelpPinMenu, value);
			mPrefsEditor.commit();
		}
	}

	public String getHelpPinMenu() {
		return mPrefs.getString(mHelpPinMenu, "");
	}
	
	public void setHelpSideMenu(String value) {
		if(value!=null){
			mPrefsEditor.putString(mHelpSideMenu, value);
			mPrefsEditor.commit();
		}
	}

	public String getHelpSideMenu() {
		return mPrefs.getString(mHelpSideMenu, "");
	}
	
	public void setHelpCollection(String value) {
		if(value!=null){
			mPrefsEditor.putString(mHelpCollection, value);
			mPrefsEditor.commit();
		}
	}

	public String getHelpCollection() {
		return mPrefs.getString(mHelpCollection, "");
	}
	
	public void setHelpCart(String value) {
		if(value!=null){
			mPrefsEditor.putString(mHelpCart, value);
			mPrefsEditor.commit();
		}
	}

	public String getHelpCart() {
		return mPrefs.getString(mHelpCart, "");
	}
	
	public void setHelpFriendsSideMenu(String value) {
		if(value!=null){
			mPrefsEditor.putString(mHelpFriendsSideMenu, value);
			mPrefsEditor.commit();
		}
	}

	public String getHelpFriendsSideMenu() {
		return mPrefs.getString(mHelpFriendsSideMenu, "");
	}
	
}
