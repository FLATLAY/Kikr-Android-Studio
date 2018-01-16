package com.flatlay.utility;

import java.lang.reflect.Field;
import java.util.Hashtable;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;

public class FontUtility {

	private static final Hashtable<String, Typeface> proximanovaLight = new Hashtable<String, Typeface>();
	private static final Hashtable<String, Typeface> proximanovaRegular = new Hashtable<String, Typeface>();
	private static final Hashtable<String, Typeface> proximanovaSemiBold = new Hashtable<String, Typeface>();

	private static final Hashtable<String, Typeface> montserratLight = new Hashtable<String, Typeface>();
	private static final Hashtable<String, Typeface> montserratRegular = new Hashtable<String, Typeface>();
	private static final Hashtable<String, Typeface> montserratSemiBold = new Hashtable<String, Typeface>();


	public static Typeface setProximanovaLight(Activity context) {

		String name = "proximanova-light.otf";
		synchronized (proximanovaLight) {
			if (!proximanovaLight.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(context.getAssets(), name);
				proximanovaLight.put(name, t);
			}
			return proximanovaLight.get(name);
		}
	}

	public static Typeface setMontserratLight(Activity context) {

		String name = "Montserrat-Light.otf";
		synchronized (montserratLight) {
			if (!montserratLight.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(context.getAssets(), name);
				montserratLight.put(name, t);
			}
			return montserratLight.get(name);
		}
	}

	public static Typeface setProximanovaRegular(Activity context) {

		String name = "proximanova-regular.otf";
		synchronized (proximanovaRegular) {
			if (!proximanovaRegular.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(context.getAssets(), name);
				proximanovaRegular.put(name, t);
			}
			return proximanovaRegular.get(name);
		}
	}

	public static Typeface setMontserratRegular(Activity context) {

		String name = "Montserrat-Regular.otf";
		synchronized (montserratRegular) {
			if (!montserratRegular.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(context.getAssets(), name);
				montserratRegular.put(name, t);
			}
			return montserratRegular.get(name);
		}
	}


	public static Typeface setProximanovaSemibold(Activity context) {

		String name = "proximanova-semibold.ttf";
		synchronized (proximanovaSemiBold) {
			if (!proximanovaSemiBold.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(context.getAssets(), name);
				proximanovaSemiBold.put(name, t);
			}
			return proximanovaSemiBold.get(name);
		}
	}
	
	 public static void overrideFont(Context context, String defaultFontNameToOverride) {
	        try {
	            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), "proximanova-light.otf");
	 
	            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
	            defaultFontTypefaceField.setAccessible(true);
	            defaultFontTypefaceField.set(null, customFontTypeface);
	        } catch (Exception e) {
//	            Log.e("Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
	        }
	    }

}
