package com.kikr.sessionstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionStore {

	private static final String TWITTER_ACCESS_TOKEN = "twitter_access_token";
	private static final String ACCESS_TOKEN = "twitter_accesstoken";
    private static final String TWITTER_SECRET_TOKEN = "twitter_secret_token";
    private static final String SECRET_TOKEN = "secret_token";
    private static final String TWITTER_SCREEN_NAME = "twitter_screen_name";
    private static final String SCREEN_NAME = "screen_name";
	private static final String TWITTER_LOGIN_VALUE = "twitter_login";
	private static final String LOGIN_VALUE = "twitter_value";
	


	public static boolean saveTwitterAccessToken(String accessToken, Context context) {
		Editor editor = context.getSharedPreferences(TWITTER_ACCESS_TOKEN, Context.MODE_PRIVATE).edit();
		editor.putString(ACCESS_TOKEN, accessToken);
		return editor.commit();
	}

	public static String getTwitterAccessToken(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(TWITTER_ACCESS_TOKEN, Context.MODE_PRIVATE);
		return savedSession.getString(ACCESS_TOKEN, "");
	}
	
	
	
	public static boolean saveTwitterSecretToken(String secretKey, Context context) {
        Editor editor = context.getSharedPreferences(TWITTER_SECRET_TOKEN, Context.MODE_PRIVATE).edit();
        editor.putString(SECRET_TOKEN, secretKey);
        return editor.commit();
    }

    public static String getTwitterSecretToken(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(TWITTER_SECRET_TOKEN, Context.MODE_PRIVATE);
        return savedSession.getString(SECRET_TOKEN, "");
    }
    
    
    public static boolean saveScreenName(String screenName, Context context) {
        Editor editor = context.getSharedPreferences(TWITTER_SCREEN_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(SCREEN_NAME, screenName);
        return editor.commit();
    }

    public static String getScreenName(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(TWITTER_SCREEN_NAME, Context.MODE_PRIVATE);
        return savedSession.getString(SCREEN_NAME, "");
    }


    public static boolean saveTwitterLogedIn(boolean loginValue, Context context) {
		Editor editor = context.getSharedPreferences(TWITTER_LOGIN_VALUE, Context.MODE_PRIVATE).edit();
		editor.putBoolean(LOGIN_VALUE, loginValue);
		return editor.commit();
	}

	public static boolean isTwitterLogedIn(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(TWITTER_LOGIN_VALUE, Context.MODE_PRIVATE);
		return savedSession.getBoolean(LOGIN_VALUE, false);
	}
 
	public static void resetTwitterLogin(Context context){
		context.getSharedPreferences(TWITTER_LOGIN_VALUE, Context.MODE_PRIVATE).edit().clear().commit();
		context.getSharedPreferences(TWITTER_SCREEN_NAME, Context.MODE_PRIVATE).edit().clear().commit();
		context.getSharedPreferences(TWITTER_SECRET_TOKEN, Context.MODE_PRIVATE).edit().clear().commit();
		context.getSharedPreferences(TWITTER_ACCESS_TOKEN, Context.MODE_PRIVATE).edit().clear().commit();
	}
    
}
