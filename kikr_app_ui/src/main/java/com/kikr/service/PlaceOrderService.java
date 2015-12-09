package com.kikr.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.kikr.GCMAlarmReceiver;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentDiscoverNew;
import com.kikr.fragment.FragmentPlaceMyOrder;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.CartApi;
import com.kikrlib.api.TwoTapApi;
import com.kikrlib.api.UpdateCartApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CartRes;
import com.kikrlib.utils.StringUtils;
import com.kikrlib.utils.Syso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Iterator;


/**
 * Created by Ujjwal on 12/6/2015.
 */
public class PlaceOrderService extends IntentService{

    private static int PURCHASE_STATUS_TIME = 15*1000;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public PlaceOrderService(String name) {
        super(name);
    }

//    'still_processing', 'has_failures', or 'done'.

    public PlaceOrderService() {
        super("PlaceOrderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Syso.info("UUUUUUUUUUUU >>>>>> in onHandleIntent : " + intent);
        if(intent!=null){
            if(intent.hasExtra("purchase_id")&&intent.hasExtra("cartId"))
                purchaseStatus(intent.getStringExtra("purchase_id"),intent.getStringExtra("cartId"));
        }
    }

    private void setNextHandler(final String purchase_id,final String cartId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                purchaseStatus(purchase_id,cartId);
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, PURCHASE_STATUS_TIME);
    }

    public void purchaseStatus(final String purchase_id,final String cartId) {
        Syso.info("UUUUUUUUUUUU >>>>>> in purchaseStatus : "+purchase_id);
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("purchaseStatus response : " + object);
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    String purchaseId=jsonObject.getString("purchase_id");
                    String message=jsonObject.getString("message");
                    if(message.equals("still_processing")){
                        setNextHandler(purchase_id,cartId);
                    }else {
                          boolean isOrderSuccess = jsonObject.optBoolean("pending_confirm",false);
                          if(isOrderSuccess){
                              double savedValue = StringUtils.getDoubleValue(UserPreference.getInstance().getFinalPrice());
                              double finalPrice = getFinalPrice(jsonObject);
                              double difference = finalPrice-savedValue;
                              if(difference<=1)
                                  confirmPuchase(purchaseId,cartId);
                              else {
                                  String otherdata = "{\"confirm_with_user\":false,\"purchase_id\":\""+purchaseId+"\",\"message\":\""+message+"\",\"finalprice\":"+finalPrice+"}";
                                  String lMessage = "Your cart is ready to confirm.";
                                  String section = "placeorder";
                                  generateNotification(otherdata, lMessage, section);
                              }
                          }else{
                              String otherdata = "{\"message\":\"Your order has been failed\"}";
                              String lMessage = "Your order has been failed";
                              String section = "twotap";
                              generateNotification(otherdata,lMessage,section);
                          }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                setNextHandler(purchase_id,cartId);
            }
        });
        twoTapApi.purchaseStatus(purchase_id);
        twoTapApi.execute();
    }

    private double getFinalPrice(JSONObject jsonObject){
        double finalPrice = 0;
        try {
            JSONObject sites = jsonObject.getJSONObject("sites");
            Iterator keys = sites.keys();
            while(keys.hasNext()) {
                String currentDynamicKey = (String) keys.next();
                JSONObject currentDynamicValue = sites.getJSONObject(currentDynamicKey);
                JSONObject  prices = currentDynamicValue.getJSONObject("prices");
                finalPrice+= Double.parseDouble(prices.optString("final_price").replace("$", ""));
            }
            return finalPrice;
        }catch(Exception e){
            e.printStackTrace();
            return  finalPrice;
        }
    }

    public void confirmPuchase(final String purchase_id,final String cartId) {
        Syso.info("UUUUUUUUUUUU >>>>>> in confirmPuchase : "+purchase_id);
        TwoTapApi twoTapApi = new TwoTapApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   "+ object);
                Syso.info("UUUUUUUUUUUU >>>>>> in handleOnSuccess of confirmPuchase");
                try {
                    JSONObject jsonObject = new JSONObject(object.toString());
                    Syso.info("result:  "+jsonObject);
                    callServerForConfirmation(purchase_id,"confirmed",cartId);
                    GCMAlarmReceiver.setAlarmForOrderNotification(purchase_id,getApplicationContext());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        twoTapApi.confirmPurchase(purchase_id);
        twoTapApi.execute();
    }

    public void callServerForConfirmation(final String purchase_id, final String status, String cartId) {
        UpdateCartApi twoTapApi = new UpdateCartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("success:   "+ object);
                Syso.info("uuuuuuuuuuuuu >>>>> in handleOnSuccess of callServerForConfirmation");
                    if (status.equals("confirmed")) {
                        createCart();
                        UserPreference.getInstance().setCartCount("0");
                        UserPreference.getInstance().setPurchaseId("");
                        UserPreference.getInstance().setFinalPrice("");
                        UserPreference.getInstance().setIsNotificationClicked(false);
                        Syso.info("uuuuuuuuuuuuu >>>>> final done");
                    }
                }
            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (status.equals("confirmed")) {
                    createCart();
                    UserPreference.getInstance().setCartCount("0");
                    UserPreference.getInstance().setPurchaseId("");
                    UserPreference.getInstance().setFinalPrice("");
                    UserPreference.getInstance().setIsNotificationClicked(false);
                    Syso.info("uuuuuuuuuuuuu >>>>> final done");
                }

            }
        });
        twoTapApi.updatecarttwotapstatus(UserPreference.getInstance().getUserID(), purchase_id, cartId, "", status);
        twoTapApi.execute();
    }

    public void createCart() {
        final CartApi cartApi = new CartApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                CartRes cartRes = (CartRes) object;
                UserPreference.getInstance().setCartID(cartRes.getCart_id());
            }
            @Override
            public void handleOnFailure(ServiceException exception,Object object) {
            }
        });
        cartApi.createCart(UserPreference.getInstance().getUserID());
        cartApi.execute();

    }


    private void generateNotification(String otherdata,String message,String section) {
        Context context = getApplicationContext();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setSmallIcon(R.drawable.ic_app_logo);
        notificationBuilder.setTicker(message);
        notificationBuilder.setContentTitle("Kikr");
        notificationBuilder.setContentText(message);
        notificationBuilder.setAutoCancel(true);

        Intent intent = new Intent(context, HomeActivity.class);
        if (!TextUtils.isEmpty(otherdata)) {
            intent.putExtra("otherdata", otherdata);
        }
        intent.putExtra("section", section);
        intent.setData(Uri.parse(section));
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1, intent,PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);

        if(!TextUtils.isEmpty(section)&&section.equalsIgnoreCase("placeorder")){
            setAlarmForNotification();
        }
    }

    private void setAlarmForNotification() {
        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + 2);
        PendingIntent intent = PendingIntent.getBroadcast(getApplicationContext(),0, new Intent(getApplicationContext(), GCMAlarmReceiver.class), 0);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Syso.info("============= in service onDestroy");
    }
}
