package com.kikrlib.utils;

import android.text.TextUtils;

import com.kikrlib.db.AppPreference;

public class Constants {

	public interface WebConstants {

		int NETWORK_ERROR = 10000;
		int FAILED_ERROR = 10001;
		String SUCCESS_CODE = "0000";
		String FAIL_CODE = "0001";
//		String NETWORK_MESSAGE = "Could not establish connection. Please check network settings or contact your mobile operator.";
		String NETWORK_MESSAGE = "No internet connection found";
		String INVALID_RESPONSE_MESSAGE = "Invalid response from server. Please try again later.";


		int CONNECTIONTIMEOUT = 30 * 1000;
		int READTIMEOUT = 30 * 1000;

		String HTTP_METHOD_POST = "POST";
		String HTTP_METHOD_GET = "GET";
		String HTTP_METHOD_PUT = "PUT";

//		String HOST_API = "http://23.227.167.180/api/"; 
//		String DEFAULT_IP = "23.227.167.180"; //104.131.58.147
		String DEFAULT_IP = "http://api.kikr.io/";
//		String DEFAULT_IP = "http://v1.kikr.io/";
//		String HOST_API = "http://104.131.58.147/api/"; 

		String HOST_FILE = ""; 
//		String HOST_FILE = "mobile.php/"; 
//		String HOST_IMAGE = "http://eyetea.my/ice/";

		boolean isLive = false;
		String PAYPAL_URL = isLive ? "https://api.paypal.com" : "https://api.sandbox.paypal.com";
		String PAYPAL_AUTH_URL = PAYPAL_URL+"/v1/oauth2/token";
		String PAYPAL_PAYMENT_URL = PAYPAL_URL+"/v1/payments/payment";
		String PAYPAL_VOID_URL = PAYPAL_URL+"/v1/payments/authorization/";

		String PAYPAL_CLIENT = "ASyMq4Qwa4gDwb_eESSSO5H0x2lJmE6TVOuXbEPbsnCU0cNxOocsO7EINHqOn_kfWdpV_2SB8qaCQh01";
		String PAYPAL_SECRET = "ENDkPsr014T2sOkMd74TfuwySrBVWGrzbk0UGsmpnC8CZMVWqKzCfXBATz_ARjhj0PuIVeHiAOvkhpQv";
	}
	
	public static String getServerIp(){
		String serverIp;
		String savedIp=AppPreference.getInstance().getServerIp();
		if(TextUtils.isEmpty(savedIp)){
			serverIp="http://"+WebConstants.DEFAULT_IP+"/api/";
		}else{
			serverIp="http://"+savedIp+"/api/";
		}
		return WebConstants.DEFAULT_IP;
	}


}
