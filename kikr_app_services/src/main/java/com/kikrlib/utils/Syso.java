package com.kikrlib.utils;

import android.util.Log;


/**
 * @author This class is used for adding log in-line. 
 * Need to change "mIsDebug" value "false" when deploy.
 *
 */
public class Syso {
	
	private static boolean mIsDebug = true;
	private static String mTag = "Kikr_Log : ";

	
	public static void info(Object object){		
		if(mIsDebug){
			Log.i(mTag, ""+object);
		}
	}
	
	public static void info(String tag,Object object){		
		if(mIsDebug){
			Log.i(mTag + tag, ""+object);
		}
	}
	
	public static void debug(Object object){		
		if(mIsDebug){
			Log.d(mTag, ""+object);
		}
	}
	
	public static void debug(String tag,Object object){		
		if(mIsDebug){
			Log.d(mTag + tag, ""+object);
		}
	}
	
	public static void error(Object object){		
		if(mIsDebug){
			Log.e(mTag, ""+object);
			if(object instanceof Exception){
				((Exception) object).printStackTrace();
			}
		}
	}
	
	public static void error(String tag,Object object){		
		if(mIsDebug){
			Log.e(mTag + tag, ""+object);
			if(object instanceof Exception){
				((Exception) object).printStackTrace();
			}
		}
	}
	
	public static void print(Object object){		
		if(mIsDebug){
			System.out.println(mTag + object);
		}
	}
	
	public static void infoFull(Object object){
		  int maxLogSize = 2000;
		  String veryLongString=object.toString();
		  for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
		      int start = i * maxLogSize;
		      int end = (i+1) * maxLogSize;
		      end = end > veryLongString.length() ? veryLongString.length() : end;
		      Log.i("Kikr log : ", veryLongString.substring(start, end));
		  }
		 }
}
