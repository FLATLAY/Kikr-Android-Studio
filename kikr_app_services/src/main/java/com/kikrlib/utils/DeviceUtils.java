package com.kikrlib.utils;

import android.os.Build;

public class DeviceUtils {

	public static String getPhoneModel() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalizeFirst(model);
		} else {
			return capitalizeFirst(manufacturer) + " " + model;
		}
	}

	public static  String capitalizeFirst(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}
}
