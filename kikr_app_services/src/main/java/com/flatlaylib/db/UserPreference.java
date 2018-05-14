package com.flatlaylib.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.flatlaylib.AppContext;
import com.flatlaylib.utils.Syso;

public class UserPreference {

    public static String FACEBOOK = "facebook";
    private static String PIC_FILENAME = "Kikr.file_name";

    private String TAG = this.getClass().getSimpleName();
    private SharedPreferences mPrefs;
    private Editor mPrefsEditor;

    private final String mUserId = "mUserId";
    private final String mCartId = "mCartId";
    private final String mTwotapCartId = "mTwotapCartId";
    private final String mUserName = "mUserName";

    private final String mUserFb = "mUserFb";
    private final String mUserIns = "mUserIns";

    private final String mUserTube = "mUserTube";
    private final String mUserTwi = "mUserTwi";
    private final String mUserPin = "mUserPin";

    private final String mProfilePic = "mProfilePic";
    private final String mBgImage = "mBgImage";
    private final String mEmailId = "mEmailId";
    private final String mPassword = "mPassword";

    private final String mIsCreateWalletPin = "isCreateWalletPin";
    private final String mConnectedByFB = "mConnectedByFB";
    private final String mConnectedByTwitter = "mConnectedByTwitter";
    private final String mCheckedIsConnected = "mCheckedIsConnected";
    private final String mCurrentScreen = "mCurrentScreen";
    private final String mIsCheckBluetooth = "mIsCheckBluetooth";
    private final String mIsRefreshProfile = "mIsRefreshProfile";
    private final String cartCount = "cartCount";
    private final String mFinalPrice = "mFinalPrice";
    private final String mPurchaseId = "mPurchaseId";
    private final String mNotificationCliked = "mNotificationCliked";

    private final String mAuthTimeStamp = "mAuthTimeStamp";
    private final String mAuthExpireTime = "mAuthExpireTime";
    private final String mAccessToken = "mAccessToken";
    private final String mIsFacebookSignedIn = "facebooksign";
    private final String mIsInstagramSignedIn = "instagramsign";
    private final String mIsTwitterSignedIn = "twittesign";
    private final String mIsPinterestSignedIn = "pinterestsign";
    private final String mPinterestBoardId = "boardid";

    private static UserPreference INSTANCE;
    private String mPaypalAccessToken = "paypal_access_token";

    public static UserPreference getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new UserPreference(AppContext.getInstance().getContext());
        }
        return INSTANCE;
    }

    private UserPreference(Context context) {
        this.mPrefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        this.mPrefsEditor = mPrefs.edit();
    }

    public void setCartCount(String value) {
        Syso.debug("setUserID = ", value);
        if (value != null) {
            mPrefsEditor.putString(cartCount, value);
            mPrefsEditor.commit();
        }
    }

    public void incCartCount() {
        int count = Integer.parseInt(TextUtils.isEmpty(getCartCount()) ? "0" : getCartCount()) + 1;
        setCartCount(String.valueOf(count));
    }

    public void decCartCount() {
        int count = Integer.parseInt(TextUtils.isEmpty(getCartCount()) ? "0" : getCartCount()) - 1;
        setCartCount(String.valueOf(count > 0 ? count : 0));
    }

    public String getCartCount() {
        return mPrefs.getString(cartCount, "");
    }

    public void setUserID(String value) {
        Syso.debug("setUserID = ", value);
        if (value != null) {
            mPrefsEditor.putString(mUserId, value);
            mPrefsEditor.commit();
        }
    }

    public String getCartID() {
        return mPrefs.getString(mCartId, "");
    }

    public void setCartID(String value) {
        Syso.debug("setUserID = ", value);
        if (value != null) {
            mPrefsEditor.putString(mCartId, value);
            mPrefsEditor.commit();
        }
    }

    public String getUserID() {
        return mPrefs.getString(mUserId, "");
    }

    public void setCurrentScreen(String value) {
        Syso.debug("mCurrentScreen = ", value);
        if (value != null) {
            mPrefsEditor.putString(mCurrentScreen, value);
            mPrefsEditor.commit();
        }
    }

    public String getCurrentScreen() {
        return mPrefs.getString(mCurrentScreen, "");
    }

    public void setIsCreateWalletPin(boolean value) {
        Syso.debug("mIsCreateWalletPin = ", value);
        mPrefsEditor.putBoolean(mIsCreateWalletPin, value);
        mPrefsEditor.commit();
    }

    public boolean getIsCreateWalletPin() {
        return mPrefs.getBoolean(mIsCreateWalletPin, true);
    }

    public void setIsTwitterConnected(boolean value) {
        Syso.debug("mConnectedByTwitter = ", value);
        mPrefsEditor.putBoolean(mConnectedByTwitter, value);
        mPrefsEditor.commit();
    }

    public boolean getCheckedIsConnected() {
        return mPrefs.getBoolean(mCheckedIsConnected, false);
    }

    public void setIsFbConnected(boolean value) {
        Syso.debug("mConnectedByFB = ", value);
        mPrefsEditor.putBoolean(mConnectedByFB, value);
        mPrefsEditor.commit();
    }

    public void setcheckedIsConnected(boolean value) {
        Syso.debug("mCheckedIsConnected = ", value);
        mPrefsEditor.putBoolean(mCheckedIsConnected, value);
        mPrefsEditor.commit();
    }

    public boolean getmIsFacebookSignedIn() {
        return mPrefs.getBoolean(mIsFacebookSignedIn, false);
    }

    public boolean getmIsInstagramSignedIn() {
        boolean result = mPrefs.getBoolean(mIsInstagramSignedIn, false);
        return result;
    }

    public boolean getmIsTwitterSignedIn() {
        return mPrefs.getBoolean(mIsTwitterSignedIn, false);
    }

    public boolean getmIsPinterestSignedIn() {
        return mPrefs.getBoolean(mIsPinterestSignedIn, false);
    }

    public String getmPinterestBoardId() {
        return mPrefs.getString(mPinterestBoardId, "");
    }

    public void setmIsFacebookSignedIn(boolean isFacebookSignedIn) {
        Syso.debug("mIsFacebookSignedIn = ", isFacebookSignedIn);
        mPrefsEditor.putBoolean(mIsFacebookSignedIn, isFacebookSignedIn);
        mPrefsEditor.commit();
    }

    public void setmIsInstagramSignedIn(boolean isInstagramSignedIn) {
        Syso.debug("isInstagramSignedIn = ", isInstagramSignedIn);
        mPrefsEditor.putBoolean(mIsInstagramSignedIn, isInstagramSignedIn);
        mPrefsEditor.commit();
    }

    public void setmIsTwitterSignedIn(boolean isTwitterSignedIn) {
        Syso.debug("isTwitterSignedIn = ", isTwitterSignedIn);
        mPrefsEditor.putBoolean(mIsTwitterSignedIn, isTwitterSignedIn);
        mPrefsEditor.commit();
    }

    public void setmIsPinterestSignedIn(boolean isPinterestSignedIn) {
        Syso.debug("isPinterestSignedIn = ", isPinterestSignedIn);
        mPrefsEditor.putBoolean(mIsPinterestSignedIn, isPinterestSignedIn);
        mPrefsEditor.commit();
    }

    public void setmPinterestBoardId(String boardId) {
        Syso.debug("setmPinterestBoardId = ", boardId);
        mPrefsEditor.putString(mPinterestBoardId, boardId);
        mPrefsEditor.commit();
    }

    public boolean getIsRefreshProfile() {
        return mPrefs.getBoolean(mIsRefreshProfile, false);
    }

    public void setIsRefreshProfile(boolean value) {
        Syso.debug("mCheckedIsConnected = ", value);
        mPrefsEditor.putBoolean(mIsRefreshProfile, value);
        mPrefsEditor.commit();
    }

    public boolean getIsNotificationClicked() {
        return mPrefs.getBoolean(mNotificationCliked, false);
    }

    public void setIsNotificationClicked(boolean value) {
        mPrefsEditor.putBoolean(mNotificationCliked, value);
        mPrefsEditor.commit();
    }


    public boolean getIsCheckBluetooth() {
        return mPrefs.getBoolean(mIsCheckBluetooth, true);
    }

    public void clearAllData() {
        mPrefsEditor.clear();
        mPrefsEditor.commit();
    }

    public void saveChatImage(String fileName) {
        mPrefsEditor.putString(PIC_FILENAME, fileName);
        mPrefsEditor.commit();
    }

    public String getChatImage() {
        return mPrefs.getString(PIC_FILENAME, "");
    }


    public String getPassword() {
        return mPrefs.getString(mPassword, "");
    }

    public void setPassword() {
        mPrefsEditor.putString(mPassword, "haspassword");
        mPrefsEditor.commit();
    }

    public String getEmail() {
        return mPrefs.getString(mEmailId, "");
    }

    public void setEmail(String value) {
        if (value != null) {
            mPrefsEditor.putString(mEmailId, value);
            mPrefsEditor.commit();
        }
    }

    public void setBgImage(String value) {
        if (value != null) {
            mPrefsEditor.putString(mBgImage, value);
            mPrefsEditor.commit();
        }
    }

    public void setProfilePic(String value) {
        Log.e("Profile", "lalaa1001:" + UserPreference.getInstance().getProfilePic());

        if (value != null) {
            mPrefsEditor.putString(mProfilePic, value);
            Log.e("Profile", "lalaa44" + UserPreference.getInstance().getProfilePic());
            mPrefsEditor.commit();
        }
    }

    public String getProfilePic() {
        return mPrefs.getString(mProfilePic, "");
    }

    public void setUserName(String value) {
        if (value != null) {
            mPrefsEditor.putString(mUserName, value);
            mPrefsEditor.commit();
        }
    }

    public String getUserName() {
        return mPrefs.getString(mUserName, "");
    }


    public void setUserFb(String value) {
        if (value != null) {
            mPrefsEditor.putString(mUserFb, value);
            mPrefsEditor.commit();
        }
    }

    public String getUserFb() {
        return mPrefs.getString(mUserFb, "");
    }

    public void setUserIns(String value) {
        if (value != null) {
            mPrefsEditor.putString(mUserIns, value);
            mPrefsEditor.commit();
        }
    }

    public String getUserIns() {
        return mPrefs.getString(mUserIns, "");
    }

    public void setUserTube(String value) {
        if (value != null) {
            mPrefsEditor.putString(mUserTube, value);
            mPrefsEditor.commit();
        }
    }

    public String getUserTube() {
        return mPrefs.getString(mUserTube, "");
    }

    public void setUserPin(String value) {
        if (value != null) {
            mPrefsEditor.putString(mUserPin, value);
            mPrefsEditor.commit();
        }
    }

    public String getUserPin() {
        return mPrefs.getString(mUserPin, "");
    }

    public void setUserTwi(String value) {
        if (value != null) {
            mPrefsEditor.putString(mUserTwi, value);
            mPrefsEditor.commit();
        }
    }

    public String getUserTwi() {
        return mPrefs.getString(mUserTwi, "");
    }

    public void setFinalPrice(String value) {
        mPrefsEditor.putString(mFinalPrice, value);
        mPrefsEditor.commit();
    }

    public String getFinalPrice() {
        return mPrefs.getString(mFinalPrice, "");
    }

    public void setPurchaseId(String value) {
        mPrefsEditor.putString(mPurchaseId, value);
        mPrefsEditor.commit();
    }

    public String getPurchaseId() {
        return mPrefs.getString(mPurchaseId, "");
    }

    public long getAuthTimeStamp() {
        return mPrefs.getLong(mAuthTimeStamp, 0);
    }

    public void setTimeStamp(long value) {
        mPrefsEditor.putLong(mAuthTimeStamp, value);
        mPrefsEditor.commit();
    }

    public int getAuthExpireTime() {
        return mPrefs.getInt(mAuthExpireTime, 0);
    }

    public void setExpireTime(int value) {
        mPrefsEditor.putInt(mAuthExpireTime, value);
        mPrefsEditor.commit();
    }

    public void setAccessToken(String value) {
        Syso.debug("mAccesstoken = ", value);
        if (value != null) {
            mPrefsEditor.putString(mAccessToken, value);
            mPrefsEditor.commit();
        }
    }

    public String getAccessToken() {
        return mPrefs.getString(mAccessToken, "");
    }

    public void setPaypalAccessToken(String value) {
        Syso.debug("mPaypalAccessToken = ", value);
        if (value != null) {
            mPrefsEditor.putString(mPaypalAccessToken, value);
            mPrefsEditor.commit();
        }
    }

    public String getPaypalAccessToken() {

        return mPrefs.getString(mPaypalAccessToken, "");
    }

    public void setSearchIndex(int searchIndex) {
        this.searchIndex = searchIndex;
    }

    private int searchIndex = 0;

    public int getSearchIndex() {
        return searchIndex;
    }

    public void setPpsIndex(int ppsIndex) {
        this.ppsIndex = ppsIndex;
    }

    private int ppsIndex = 0;

    public int getPpsIndex() {
        return ppsIndex;
    }
}
