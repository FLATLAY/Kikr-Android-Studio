package com.flatlay.utility;

import com.flatlay.bubble.ChipBubbleText;

public class AppConstants {


    public static int fragmentFeedTabPosition;
    public static int sameLevelCount=0;
    public static ChipBubbleText chipBubbleText;
    public static boolean isBubbleApiCall = true;
    public static int REQUEST_CODE_INSTAGRAM = 5555;
    public static final String APP_ID = "1";
    public static final String PERSONAGRAPH_API_KEY = "1231:p61AxhMNtyHkIma6";
    public static final String MESSAGE = "Come check out Kikr - The shopping wallet that gives you exclusive access to deals, discounts & rewards you for sharing items you love. Pay less, earn more. Download at ";
    public static final String ANDROID_APP_URL = "http://goo.gl/fcEVWa";
    public static final String IOS_APP_URL = "http://goo.gl/VO6O6A";
    public static final String SHARE_MESSAGE = "Checkout what I found on Flatlay";
    public static final String APP_LINK = "\nDL for Android " + ANDROID_APP_URL + "\nDL for iOS " + IOS_APP_URL;
    //	public static final String ANDROID_APP_URL="https://play.google.com/store/apps/details?id=com.kikr";
//	public static final String IOS_APP_URL="https://itunes.apple.com/us/app/kikr-personalized-shopping/id941013374?ls=1&mt=8";
    public static final String INVITE_FRIEND_MESSAGE = MESSAGE + " \nAndroid " + ANDROID_APP_URL + "\niOS " + IOS_APP_URL;
    public static final String INVITE_FRIEND_MESSAGE2 = "Come check out Flatlay - Download at :" + " \nAndroid " + ANDROID_APP_URL + "\niOS " + IOS_APP_URL;
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


    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int USERNAME_MAX_LENGTH = 10;
    public static final int COLLECTION_NAME_MAX_LENGTH = 16;
    public static boolean isFromTutorial = false;

    //	public static final String[] SCREEN_NAME_LIST = new String[] {
//			"UserNameScreen", "EmailScreen", "PhoneNumberScreen", "PinScreen",
//			"CategoryScreen", "BrandScreen", "CardScreen", "HomeScreen" };
    public interface Screen {
        String UserNameScreen = "UserNameScreen";
        String EmailScreen = "EmailScreen";
        String PhoneNumberScreen = "PhoneNumberScreen";
        String PinScreen = "PinScreen";
        String CategoryScreen = "CategoryScreen";
        String BrandScreen = "BrandScreen";
        String StoreScreen = "StoreScreen";
        String CardScreen = "CardScreen";
        String HomeScreen = "HomeScreen";
    }

    public interface Options {
        String FOLLOWERS = "followers";
        String COMMENTS = "comments";
        String FAVOURITES = "favourites";
        String PURCHASES = "purchases";
    }


    public static final String TERMS_OF_USE_URL =  "https://flat-lay.com/#/terms";
    public static final String PRIVACY_POLICY_URL = "https://flat-lay.com/#/privacy";

  //  String uriPath = "android.resource://com.kikr/"+ R.raw.flatlaylegal;
    public static final String LEGAL_URL = "file:///android_asset/flatlaylegalfile.html";


    public static final String NAME_SEPRATER = "<!!>";

    public final static String CREDIT_CARD_NO_ENCRYPT_DECRYPT_KEY = "KikrSuperSekritMobi1eKe!";

    public static final String INSTAGRAM_CLIENT_ID = "0c38cad2d6124a22bf1025ef88f6d72a";
    public static final String INSTAGRAM_CLIENT_SECRET = "9160d43968fd459185dc384c6de83214";
    public static final String INSTAGRAM_REDIRECT_URI = "http://www.flat-lay.com";

    public static final String PINTEREST_APP_ID = "4841772085915168467";

}


