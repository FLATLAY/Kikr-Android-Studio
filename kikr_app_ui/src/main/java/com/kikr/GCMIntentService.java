package com.kikr;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.flatlay.R;
import com.google.android.gcm.GCMBaseIntentService;
import com.kikr.activity.HomeActivity;
import com.kikr.utility.CommonUtility;
import com.kikrlib.db.AppPreference;
import com.kikrlib.utils.Syso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(CommonUtility.SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered" + registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        dumpIntent(intent);
        String message;
        String otherdata = "";
        String inspiration_id = null;
        String purchase_id = "";
        String section = "";
        if (intent.hasExtra("otherdata")) {
            try {
                otherdata = intent.getStringExtra("otherdata");
                JSONObject jsonobj = new JSONObject(otherdata);
                if (jsonobj.has("inspiration_id")) {
                    inspiration_id = jsonobj.getString("inspiration_id");
                }
                if (jsonobj.has("purchase_id")) {
                    purchase_id = jsonobj.getString("purchase_id");
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        try {
            message = intent.getStringExtra("message");
            section = intent.getStringExtra("section");
            if (section.equalsIgnoreCase("follow"))
                inspiration_id = intent.getStringExtra("user_idsend");
            if (section.equalsIgnoreCase("twotap")) {
                AppPreference.getInstance().setIsShowNotification(purchase_id, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "You received new message";
        }
        boolean isShowNotification = true;
        if (section.equalsIgnoreCase("placeorder")) {
//			double finalValue = StringUtils.getDoubleValue(getFinalPrice(otherdata.toString()));
            isShowNotification = false;

//			double finalValue = getFinalPrice(otherdata.toString());
//			double savedValue = StringUtils.getDoubleValue(UserPreference.getInstance().getFinalPrice());
//			double diffrence = finalValue-savedValue;
//			Syso.info("uuuuuuuuuuuuu >>>>> Final value : "+finalValue+", Saved Final value>>"+savedValue+" Diff>>>"+diffrence);
//			if(diffrence<=5){
//				if(!TextUtils.isEmpty(UserPreference.getInstance().getPurchaseId())){
//					isShowNotification = false;
//					FragmentPlaceMyOrder myOrder = new FragmentPlaceMyOrder(otherdata, false);
//					myOrder.confirmPuchase(UserPreference.getInstance().getPurchaseId());
//				}
//			}
        }
        if (isShowNotification)
            generateCustomNotification(context, message, inspiration_id, section, otherdata);
    }


    public double getFinalPrice(String string) {
        double price = 0;
        try {
            JSONObject jsonObject = new JSONObject(string);
            price = jsonObject.getDouble("finalprice");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return price;
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");

    }

    /**
     * Method called on Error
     */

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        // CommonUtility.displayMessage(context, "GCM error occured!");
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

    private void generateNotification(Context context, String message, String inspiration_id, String section, String otherdata) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.small_logo);
        notificationBuilder.setTicker(message);
        notificationBuilder.setContentTitle(context.getString(R.string.app_name));
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);

        Intent intent = new Intent(context, HomeActivity.class);
        if (!TextUtils.isEmpty(otherdata)) {
            intent.putExtra("otherdata", otherdata);
        }
        if (!TextUtils.isEmpty(inspiration_id)) {
            intent.putExtra("inspiration_id", inspiration_id);
        }
        intent.putExtra("section", section);
        intent.setData(Uri.parse(section));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!TextUtils.isEmpty(section) && section.equalsIgnoreCase("commission"))
            notificationManager.notify(100, notification);
        else
            notificationManager.notify(1, notification);

        if (!TextUtils.isEmpty(section) && section.equalsIgnoreCase("placeorder")) {
            setAlarmForNotification();
        }
    }

    private void setAlarmForNotification() {
        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 2);
        PendingIntent intent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(getApplicationContext(), GCMAlarmReceiver.class), 0);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent);
    }

    public static String dumpIntent(Intent i) {
        String value = "";
        if (i != null) {
            Bundle bundle = i.getExtras();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    value += key + " : " + bundle.get(key) + "\n";
                    Syso.info("Intent Data", "[" + key + "=" + bundle.get(key) + "]");
                }
            }
        }
        return value;
    }


    private void generateCustomNotification(Context context, String message, String inspiration_id, String section, String otherdata) {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),
                R.layout.customnotification);

        Intent intent = new Intent(context, HomeActivity.class);
        if (!TextUtils.isEmpty(otherdata)) {
            intent.putExtra("otherdata", otherdata);
        }
        if (!TextUtils.isEmpty(inspiration_id)) {
            intent.putExtra("inspiration_id", inspiration_id);
        }
        intent.putExtra("section", section);
        intent.setData(Uri.parse(section));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.round_logo)
                // Set Ticker Message
                .setTicker("Flatlay")
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pendingIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.image, R.drawable.round_logo);
        Date date = new Date();
        SimpleDateFormat format=new SimpleDateFormat("hh:mm aa");


        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title, "Flatlay");
        remoteViews.setTextViewText(R.id.text, message);
        remoteViews.setTextViewText(R.id.time, format.format(date));


        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (!TextUtils.isEmpty(section) && section.equalsIgnoreCase("commission"))
            notificationManager.notify(100, notification);
        else
            notificationManager.notify(1, notification);

        if (!TextUtils.isEmpty(section) && section.equalsIgnoreCase("placeorder")) {
            setAlarmForNotification();
        }


    }


}
