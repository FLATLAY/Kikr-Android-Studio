package com.kikrlib.utils;

import java.util.UUID;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.util.Base64;

public class StringUtils {
   
	public static boolean isEmpty(String data){
		
		Syso.info(data);
		if(TextUtils.isEmpty(data)){
			return true;
		}
		else{
			String inputData = data.trim();
			if(inputData.equalsIgnoreCase("null")){
				return true;
			}		
			else if(inputData.equalsIgnoreCase("\"\"")){
				return true;
			}
		}
		return false;
	}
	
	public static String getBase64EncodedString(){
		  // encode data on your side using BASE64
		  String authorizationString ="Basic " + Base64.encodeToString(
		        ("live_bf92e586ac52066021d03ab6ade48ce5227:").getBytes(),
		        Base64.NO_WRAP);
		  System.out.println("============ BASE64 string : " + authorizationString);
		  return authorizationString;
	}

	public static String getBase64EncodedString(String client_id,String client_secret){
		// encode data on your side using BASE64
		String authorizationString ="Basic " + Base64.encodeToString(
				(client_id + ":" + client_secret).getBytes(),
				Base64.NO_WRAP);
		System.out.println("============ BASE64 string : " + authorizationString);
		return authorizationString;
	}
	
	/**
	 * This method will Check Email Validation.</br>
	 * 
	 * @param email
	 *            - This is input email into <b>String</b> format.
	 * @return <b>true</b> if Email is valid.
	 */
	public static boolean isEmailValid(String email) {
		Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
		Boolean validation = emailPattern.matcher(email).matches();
		return validation;
	}
	
	public static String FirstLetterInUpperCase(String word){
		
		String result = "";		
		if(word.length()>0){			
		  char c = word.charAt(0);
		  String splitedString = word.substring(1, word.length());
		  result = Character.toUpperCase(c)+splitedString;
		}
		return result;
	}
	
	
	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}
	
	public static int getIntValue(String value){
		
		if(value == null){
			return 0;
		}
		
		try {
			int iValue = Integer.parseInt(value);
			return iValue;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public static Double getDoubleValue(String value){
		
		if(value == null){
			return 0.0;
		}
		
		try {
			Double iValue = Double.valueOf(value);
			return iValue;
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}
	
	public static String getIntValueFromFloat(String value){
		
		try {
			int iValue = Float.valueOf(value).intValue();
			return String.valueOf(iValue);
		} catch (NumberFormatException e) {
			return "0";
		}
	}
	
	public static String getRandomImageName() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(UUID.randomUUID().toString());
		sb.append("_a_");
		sb.append(System.currentTimeMillis());
		sb.append(".png");
		return sb.toString();
	}
	
	public static String getStringValue(boolean value){
		String sValue = (value) ? "1" : "0";
		return sValue;
	}
}
