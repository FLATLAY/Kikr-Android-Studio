package com.kikrlib.utils;

import android.content.Context;
import android.widget.Toast;

public class AlertUtils {
	
	public static void showToast(Context context, String message){
		if(context!=null)
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
	
	public static void showToast(Context context, int resID){
		if(context!=null)
			Toast.makeText(context, resID, Toast.LENGTH_SHORT).show();
	}


}
