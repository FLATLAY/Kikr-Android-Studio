package com.kikr.utility;

import com.google.android.gms.wallet.WalletConstants;
import com.kikrlib.utils.Constants;

public class AppConstants {
	public static final String APP_ID = "1";
	public static final String PERSONAGRAPH_API_KEY = "1231:p61AxhMNtyHkIma6";
	public static final String MESSAGE="Come check out Kikr - The shopping wallet that gives you exclusive access to deals, discounts & rewards you for sharing items you love. Pay less, earn more. Download at ";
	public static final String ANDROID_APP_URL="http://goo.gl/fcEVWa";
	public static final String IOS_APP_URL="http://goo.gl/VO6O6A";
	public static final String SHARE_MESSAGE = "Checkout what I found on Kikr";
	public static final String APP_LINK = "\nDL for Android "+ANDROID_APP_URL+"\nDL for iOS "+IOS_APP_URL;
//	public static final String ANDROID_APP_URL="https://play.google.com/store/apps/details?id=com.kikr";
//	public static final String IOS_APP_URL="https://itunes.apple.com/us/app/kikr-personalized-shopping/id941013374?ls=1&mt=8";
	public static final String INVITE_FRIEND_MESSAGE = MESSAGE+" \nAndroid "+ANDROID_APP_URL+"\niOS "+IOS_APP_URL;
	public static final String INVITE_FRIEND_MESSAGE2 = "Come check out Kikr - Download at :"+" \nAndroid "+ANDROID_APP_URL+"\niOS "+IOS_APP_URL;
	public static final int WALLETLIST = 1000;
	public static final int REQUEST_CODE_FB_LOGIN = 1001;
	public static final int REQUEST_CODE_TWIT_LOGIN = 1002;
	public static final int REQUEST_CODE_FB_FRIEND_LIST = 1003;
	public static final int REQUEST_CODE_TWIT_FRIEND_LIST = 1004;
	public static final int REQUEST_CODE_MASKED_WALLET = 1005;
	public static final int REQUEST_CODE_TWIT_TO_FRIEND = 1006;
	public static final int REQUEST_CODE_EMAIL_CHANGE = 1007;

	public static final String ACTION_GO_TO_HOME = "kikr_action_go_to_home";
	public static final String FROM_PROFILE = "FROM_PROFILE";


	public static final int PASSWORD_MIN_LENGTH=5;
	public static final int USERNAME_MAX_LENGTH=10;
	public static final int COLLECTION_NAME_MAX_LENGTH=16;
	public static boolean isFromTutorial = false;
	
//	public static final String[] SCREEN_NAME_LIST = new String[] {
//			"UserNameScreen", "EmailScreen", "PhoneNumberScreen", "PinScreen",
//			"CategoryScreen", "BrandScreen", "CardScreen", "HomeScreen" };
	public interface Screen {
		String UserNameScreen="UserNameScreen";
		String EmailScreen="EmailScreen";
		String PhoneNumberScreen="PhoneNumberScreen";
		String PinScreen="PinScreen";
		String CategoryScreen="CategoryScreen";
		String BrandScreen="BrandScreen";
		String StoreScreen="StoreScreen";
		String CardScreen="CardScreen";
		String HomeScreen="HomeScreen";
	}
	public interface Options {
		String FOLLOWERS="followers";
		String COMMENTS="comments";
		String FAVOURITES="favourites";
		String PURCHASES="purchases";
	}
	
	public static final int GOOGLE_WALLET_ENVIRONMENT= WalletConstants.ENVIRONMENT_SANDBOX;
	
	public static final String TERMS_OF_USE_URL = Constants.getServerIp() + "terms/terms_of_use/";
	public static final String PRIVACY_POLICY_URL = Constants.getServerIp() + "terms/privacy/";
	public static final String LEGAL_URL = Constants.getServerIp() + "terms/legal/";
	
	public static final  String NAME_SEPRATER="<!!>";
	
	public final static String CREDIT_CARD_NO_ENCRYPT_DECRYPT_KEY = "KikrSuperSekritMobi1eKe!";
	
	public static final String INSTAGRAM_CLIENT_ID = "779186c61e7a40f5aa58dffcef56c0b0";
	public static final String INSTAGRAM_CLIENT_SECRET = "3e4d893da24848de9480d264f73ed41d";
	public static final String INSTAGRAM_REDIRECT_URI = "http://www.mykikr.com";

	public static final String PINTEREST_APP_ID = "4803833610914245485";

}


