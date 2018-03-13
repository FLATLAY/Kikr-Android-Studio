package com.flatlay;

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

import com.flatlay.activity.HomeActivity;
import com.flatlaylib.db.AppPreference;
import com.flatlaylib.utils.Syso;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String message = "";

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            message = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        Log.i(TAG, "Received message"+message);
        Intent intent = new Intent(this, HomeActivity.class);
        dumpIntent(intent);
        Log.w("FirebaseMsgService","intent"+intent.toString());

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
            //message = intent.getStringExtra("message");
            section = intent.getStringExtra("section");
            if (section != null && section.equalsIgnoreCase("follow")) {
                inspiration_id = intent.getStringExtra("user_idsend");
            }
            if (section != null && section.equalsIgnoreCase("twotap")) {
                AppPreference.getInstance().setIsShowNotification(purchase_id, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //message = "You received new message";
        }
        boolean isShowNotification = true;
        if (section != null && section.equalsIgnoreCase("placeorder")) {
            isShowNotification = false;
        }
        if (isShowNotification) {
            Log.w("FirebaseMsgService","message"+message);
            generateCustomNotification(getBaseContext(), message, inspiration_id, section, otherdata);
        }

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */



    /**
     * Method called on Receiving a new message
     */



//    public double getFinalPrice(String string) {
//        double price = 0;
//        try {
//            JSONObject jsonObject = new JSONObject(string);
//            price = jsonObject.getDouble("finalprice");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return price;
//    }

    private void generateNotification(Context context, String message, String inspiration_id, String section, String otherdata) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.imgpsh_smallsize);
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
        if (!TextUtils.isEmpty(section)) {
            intent.putExtra("section", section);
            intent.setData(Uri.parse(section));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.imgpsh_smallsize)
                // Set Ticker Message
                .setTicker("Flatlay")
                // Dismiss Notification
                .setAutoCancel(true)
                // Set PendingIntent into Notification
                .setContentIntent(pendingIntent)
                // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.image, R.drawable.imgpsh_smallsize);
        Date date = new Date();
        SimpleDateFormat format=new SimpleDateFormat("hh:mm aa");

        String hey = "Hey!";

        Log.w("FirebaseMsgService","Notification Text: "+hey+message);
        Log.w("FirebaseMsgService","Time: "+format.format(date));

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title, "Flatlay");
        remoteViews.setTextViewText(R.id.notificationText, message);
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
