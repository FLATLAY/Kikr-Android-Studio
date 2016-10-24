package com.flatlaylib;

import android.app.Application;

public class AppContext {
	
	private static AppContext INSTANCE = null;
	private Application mApplication;
	
	public static AppContext getInstance (){
		
		if(INSTANCE == null){
			INSTANCE = new AppContext();
		}
		return INSTANCE;
	}
	
	public void setContext(Application application){
		this.mApplication = application;
	}
	
	public Application getContext(){
		return this.mApplication;
	}
}
