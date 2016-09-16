package com.kikr.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidZipcode {

	public static boolean isValidPostalUSCode(String zipCode) {
		String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(zipCode);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	public static boolean isValidPostalCanadaCode(String zipCode) {
		String regex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(zipCode);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

}
