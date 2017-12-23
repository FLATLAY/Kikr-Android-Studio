package com.flatlay.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.provider.MediaStore.Images;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants.WebConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.google.android.gcm.GCMRegistrar;


public class CommonUtility {
    public static final String SENDER_ID = "331880348728";
    public static final String DISPLAY_MESSAGE_ACTION = "com.gcm.DISPLAY_MY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";

    public static void printStatement(String TAG, String message) {
        Log.v(TAG, message);
    }

    public static void showNoInternetAlert(Context context) {
        AlertUtils.showToast(context, WebConstants.NETWORK_MESSAGE);
    }

    public static void showAlert(Activity context, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_alert);
        TextView okTextView = (TextView) dialog.findViewById(R.id.okTextView);
        TextView messageTextView = (TextView) dialog
                .findViewById(R.id.messageTextView);
        messageTextView.setText(Html.fromHtml(msg));
        okTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static Dialog showAlert(Activity context, String msg, OnClickListener listener) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_alert);
        TextView okTextView = (TextView) dialog.findViewById(R.id.okTextView);
        TextView messageTextView = (TextView) dialog
                .findViewById(R.id.messageTextView);
        messageTextView.setText(msg);
        okTextView.setOnClickListener(listener);
        dialog.show();
        return dialog;
    }

    public static boolean isEmailValid(String email) {
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Boolean validation = emailPattern.matcher(email).matches();
        return validation;
    }

    public static void fullScreenActivity(Activity context) {
        context.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void noTitleActivity(Activity context) {
        context.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isNetworkAvailable(Activity context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getDeviceId(Context ctx) {
        try {
            String android_id = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
            return android_id;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void hideSoftKeyboard(Activity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            View v = context.getCurrentFocus();
            if (v != null) {
                IBinder binder = v.getWindowToken();
                if (binder != null)
                    inputMethodManager.hideSoftInputFromWindow(binder, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(FragmentActivity context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            View v = context.getCurrentFocus();
            if (v != null) {
                IBinder binder = v.getWindowToken();
                if (binder != null)
                    inputMethodManager.hideSoftInputFromWindow(binder, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {

            // getting application package name, as defined in manifest
            String packageName = context.getApplicationContext()
                    .getPackageName();

            // Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(
                    packageName, PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext()
                    .getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);

            }
        } catch (NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return key;
    }

    /**
     * @param context context of the component
     * @return registration id fromGCM server i.e device id. It will first check
     * device id in shared preference, if not found then will ask to
     * GCMRegistrar if not found then check app is registered or not, if
     * not register then register the app and again ask to GCMRegistrar
     * to get deviceid. if device id found then saved in shared
     * preference.
     */


    public static int getDeviceWidth(FragmentActivity context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getDeviceHeight(FragmentActivity context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static void setImage(Activity context, String url, ImageView imageView) {
//		AQuery aQuery = new AQuery(context);
//		Bitmap preset = BitmapFactory.decodeResource(context.getResources(), placeholder);
//		aQuery.id(imageView).image(url, true, true, 0, placeholder, preset, AQuery.FADE_IN_NETWORK, 0);
//		Glide.with(context).load(url).placeholder(placeholder).crossFade().into(imageView);
        UrlImageViewHelper.setUrlDrawable(imageView, url);
		UrlImageViewHelper.setUseBitmapScaling(true);
    }

    public static void setImage(Activity context, String url, ImageView imageView, int placeholder) {
//		AQuery aQuery = new AQuery(context);
//		Bitmap preset = BitmapFactory.decodeResource(context.getResources(), placeholder);
//		aQuery.id(imageView).image(url, true, true, 0, placeholder, preset, AQuery.FADE_IN_NETWORK, 0);
//		Glide.with(context).load(url).placeholder(placeholder).crossFade().into(imageView);
        UrlImageViewHelper.setUrlDrawable(imageView, url, placeholder);
		UrlImageViewHelper.setUseBitmapScaling(true);
    }

    public static void setImagePicasso(Activity context, String url, ImageView imageView, final ProgressBar progressBar) {
        Picasso.with(context)
                .load(url)

                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

//	public static void setImageNS(Activity context, String url,ImageView imageView, int placeholder) {
//		UrlImageViewHelper.setUrlDrawable(imageView, url, placeholder);
//		UrlImageViewHelper.setUseBitmapScaling(false);
//	}

    public static int getDeviceWidthActivity(Activity context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getDeviceHeightActivity(Activity context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static String getDateFormat(String dateStr) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MM/dd/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String startDate = null;
        try {
            date = inputFormat.parse(dateStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        startDate = outputFormat.format(date);
        return startDate;
    }

    public static String setChangeDateFormat(String dateStr) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "MMM dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String startDate = null;
        try {
            date = inputFormat.parse(dateStr);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        startDate = outputFormat.format(date);
        startDate = getDate(startDate);
        return startDate;
    }

    public static String getDate(String startDate) {
        if (startDate.endsWith("11") || startDate.endsWith("12") || startDate.endsWith("13")) {
            startDate = startDate + "th";
        } else if (startDate.endsWith("1")) {
            startDate = startDate + "st";
        } else if (startDate.endsWith("2")) {
            startDate = startDate + "nd";
        } else if (startDate.endsWith("3")) {
            startDate = startDate + "rd";
        } else
            startDate = startDate + "th";
        return startDate;
    }

    public static Uri bitmapToUri(Activity context, Bitmap inImage) {
        String path = "";
        if (inImage != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = Images.Media.insertImage(context.getContentResolver(),
                    inImage, "Title", null);
        }
        return Uri.parse(path);
    }

    public static String getFormatedNum(String price) {
        try {
            double d = Double.parseDouble(price);
            //	String value= new DecimalFormat("##.##").format(d);
            String value = String.format("%.2f", d);
            if (!value.contains(".")) {
                return value + ".00";
            } else
                return value;
        } catch (Exception exception) {
            return price;
        }
    }

    public static String getFormatedNum(double price) {
        try {
            //	String value =new DecimalFormat("##.##").format(price);
            String value = String.format("%.2f", price);
            if (!value.contains(".")) {
                return value + ".00";
            } else
                return value;
        } catch (Exception exception) {
            return String.valueOf(price);
        }
    }

    public static String getFormatedNum2(double price) {
        try {
            //	String value =new DecimalFormat("##.##").format(price);
            String value = String.format("%.2f", price);
            if (!value.contains(".")) {
                return value + ".00";
            } else
                return value;
        } catch (Exception exception) {
            return String.valueOf(price);
        }
    }

    public static void hideKeypad(FragmentActivity context, EditText editView) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editView.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String calculateTimeDiff(Calendar calServer, Calendar calLocal) {

        String lastActiveTime = "0";
        long milsecs1 = calServer.getTimeInMillis();
        long milsecs2 = calLocal.getTimeInMillis();
        long diff = milsecs2 - milsecs1-14400000-3502000;
        long dsecs = diff / 1000;
        long dminutes = diff / (60 * 1000);
        long dhours = diff / (60 * 60 * 1000);
        long ddays = diff / (24 * 60 * 60 * 1000);
        Log.e("mill 1 and 2", milsecs1 + "++" + milsecs2);

        if (diff == 0) {
            lastActiveTime = "Unknown";
        } else {
            if (dsecs <= 60) {
                lastActiveTime = dsecs + " sec ago";
            }
            if (dsecs >= 60) {
                lastActiveTime = dminutes + " min ago";
            }
            if (dminutes >= 60) {
                lastActiveTime = dhours + " hr ago";
            }
            if (dhours >= 24) {
                lastActiveTime = ddays + " days ago";
            }
        }
        return lastActiveTime;
    }

    public static String messageCenter(Calendar calServer, Calendar calLocal) {

        String lastActiveTime = "0";
        long milsecs1 = calServer.getTimeInMillis();
        long milsecs2 = calLocal.getTimeInMillis();
        long diff = milsecs2 - milsecs1-14400000-3572000;
        long dsecs = diff / 1000;
        long dminutes = diff / (60 * 1000);
        long dhours = diff / (60 * 60 * 1000);
        long ddays = diff / (24 * 60 * 60 * 1000);
        Log.e("mill 1 and 2", milsecs1 + "++" + milsecs2);

        if (diff == 0) {
            lastActiveTime = "Unknown";
        } else {
            if (dsecs <= 60) {
                lastActiveTime = dsecs + "s";
            }
            if (dsecs >= 60) {
                lastActiveTime = dminutes + "m";
            }
            if (dminutes >= 60) {
                lastActiveTime = dhours + "hr";
            }
            if (dhours >= 24) {
                lastActiveTime = ddays + "d";
            }
        }
        return lastActiveTime;
    }


    public static int getInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getIpAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        return ip;
    }

    public static String getDateDifference(String date) {
        String expirydate = "";
        SimpleDateFormat dfDate = new SimpleDateFormat("MM-dd-yyyy");
        java.util.Date d = null;
        java.util.Date d1 = null;
        Calendar cal = Calendar.getInstance();
        try {
            d = dfDate.parse(date);
            d1 = dfDate.parse(dfDate.format(cal.getTime()));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        int diffInDays = (int) ((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        if (diffInDays == 0) {
            expirydate = "Ends Today";
        } else if (diffInDays == 1) {
            expirydate = diffInDays + " Day Left";
        } else if (diffInDays == 2 || diffInDays == 3 || diffInDays == 4 || diffInDays == 5) {
            expirydate = diffInDays + " Days Left";
        } else {
            expirydate = "Ends " + setChangeDateFormat(date);
        }
        return expirydate;
    }

    public static int setExpiryDateNotification(String date) {
        SimpleDateFormat dfDate = new SimpleDateFormat("MM-dd-yyyy");
        java.util.Date d = null;
        java.util.Date d1 = null;
        Calendar cal = Calendar.getInstance();
        try {
            d = dfDate.parse(date);
            d1 = dfDate.parse(dfDate.format(cal.getTime()));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        int diffInDays = (int) ((d.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
        return diffInDays;
    }

    public static String EncryptCreditCard(String cardNumber) {
        String encrytrecard = CardCrypto.encryptCreditCard(cardNumber, AppConstants.CREDIT_CARD_NO_ENCRYPT_DECRYPT_KEY);
        return encrytrecard;
    }

    public static String DecryptCreditCard(String cardNumber) {
        String decrytrecard = CardCrypto.decryptCreditCard(cardNumber, AppConstants.CREDIT_CARD_NO_ENCRYPT_DECRYPT_KEY);
        return decrytrecard;
    }

    public static String maskCCNumberCommons(String ccnum) {
        Pattern PATTERN = Pattern.compile("[0-9]+");

        String message = ccnum;
        Matcher matcher = PATTERN.matcher(message);
        String maskingChar = "x";
        StringBuilder finalMask = new StringBuilder(maskingChar);

        while (matcher.find()) {
            String group = matcher.group();
            int groupLen = group.length();

            if (groupLen > 4) {
                for (int i = 0; i <= group.length() - 4; i++) {
                    finalMask.append(maskingChar);
                }
                finalMask.append(group.substring(groupLen - 4));
            }
            message = message.replace(group, finalMask);
        }
        return message;
    }

    public static String getDeviceTocken(Context context) {

        SharedPreferences preferences = context.getSharedPreferences("device_pref", Context.MODE_PRIVATE);
        String deviceId = preferences.getString("deviceId", "");
        if (deviceId.equals("")) {
            try{
            deviceId = FirebaseInstanceId.getInstance().getToken();}
            catch (Exception e){deviceId ="";}
             preferences.edit().putString("deviceId", deviceId).commit();
            if (TextUtils.isEmpty(deviceId)) {
                deviceId = "";
            }
        }
        return deviceId;
    }

}
