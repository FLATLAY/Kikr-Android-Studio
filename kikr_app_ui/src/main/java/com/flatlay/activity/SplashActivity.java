package com.flatlay.activity;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.flatlay.AlarmReceiver;
import com.flatlay.R;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.api.GetServerIpApi;
import com.kikrlib.bean.LikeInfo;
import com.kikrlib.bean.Product;
import com.kikrlib.db.AppPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ServerIpRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class SplashActivity extends Activity {
	
	private Activity context;
	private static final int SPLASH_DISPLAY_TIME = 2000;
	private final Handler handler = new Handler();
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context=(SplashActivity)this;
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_splash);
		Syso.info("Device Token>> "+CommonUtility.getDeviceTocken(context));
//		if(TextUtils.isEmpty(AppPreference.getInstance().getServerIp())&&CommonUtility.isOnline(context)){
//			getServerIp();
//		}else{
		//	handler.postDelayed(runnable, SPLASH_DISPLAY_TIME);
//		}
		setNotification();
//		facebookKey();
	}

	private void facebookKey() {
		  // Add code to print out the key hash
		  try {
		   PackageInfo info = getPackageManager().getPackageInfo(
		     getPackageName(), PackageManager.GET_SIGNATURES);
		   for (Signature signature : info.signatures) {
		    MessageDigest md = MessageDigest.getInstance("SHA");
		    md.update(signature.toByteArray());
		    Log.d("KeyHash:",
		      Base64.encodeToString(md.digest(), Base64.DEFAULT));
		    Log.e("KeyHash:",
		      Base64.encodeToString(md.digest(), Base64.DEFAULT));
		   }
		  } catch (NameNotFoundException e) {
		   e.printStackTrace();
		  } catch (NoSuchAlgorithmException e) {
		   e.printStackTrace();
		  }		
	}


	private Runnable runnable = new Runnable() {
		public void run() {
			handler.removeCallbacks(runnable);
			Syso.info("Logged in user id>>>"+UserPreference.getInstance().getUserID());
			goToNext();
		}
	};

	private void goToNext(){
		if(UserPreference.getInstance().getUserID().equals("")){
			showLoginHome();
		}else{
			showHome();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		handler.removeCallbacks(runnable);
		finish();
	}

	private void showLoginHome() {
//		Intent i = new Intent(context,LandingActivity.class);
		Intent i = new Intent(context,IntroductionPagerActivity.class).putExtra("from","splash");
		startActivity(i);
		finish();
	}
	
	private void showHome() {
		Intent i=null;
		String currentScreen=UserPreference.getInstance().getCurrentScreen();
		if(currentScreen.equals(Screen.HomeScreen)){
			i = new Intent(context,HomeActivity.class);
		}else if(currentScreen.equals(Screen.EmailScreen)){
			i = new Intent(context,EmailActivity.class);
		}else if(currentScreen.equals(Screen.UserNameScreen)){
			i = new Intent(context,EditProfileActivity.class);
		}else if(currentScreen.equals(Screen.CategoryScreen)){
			i = new Intent(context,FollowCategoriesNewActivity.class);
		}else if(currentScreen.equals(Screen.BrandScreen)){
			i = new Intent(context,FollowBrandsActivity.class);
		}else if(currentScreen.equals(Screen.StoreScreen)){
			i = new Intent(context,FollowStoreActivity.class);
		}else if(currentScreen.equals(Screen.CardScreen)){
			i = new Intent(context,KikrTutorialActivity.class);
		}else{
			i = new Intent(context,HomeActivity.class);
		}
		startActivity(i);
		finish();
	}
	
	private void getServerIp() {
		progressBar=(ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		GetServerIpApi getServerIpApi=new GetServerIpApi(new ServiceCallback() {
				@Override
				public void handleOnSuccess(Object object) {
					ServerIpRes ipRes=(ServerIpRes) object;
					String address=ipRes.getServer_address();
					Syso.info("Server IP: "+address);
					if(!TextUtils.isEmpty(address))
						AppPreference.getInstance().setServerIp(address);
					goToNext();
				}
				
				@Override
				public void handleOnFailure(ServiceException exception, Object object) {
						AlertUtils.showToast(context,R.string.invalid_response);
					goToNext();
				}
			});
			getServerIpApi.getServerIp(CommonUtility.getIpAddress(context));
			getServerIpApi.execute();
	}
	
	  private void setNotification(){
	        /* Set the alarm to start at 08:00 AM */
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(System.currentTimeMillis());
	        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
	        calendar.set(Calendar.HOUR_OF_DAY, 8);
	        calendar.set(Calendar.MINUTE, 00);

	    	AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

	    	Intent _myIntent = new Intent(context, AlarmReceiver.class);
	    	PendingIntent _myPendingIntent = PendingIntent.getBroadcast(context, 123, _myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//	    	alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),_myPendingIntent);
	    	/* Repeating on every 1 day interval */
	    	alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
	    			24*60*60*1000, _myPendingIntent);  
	  }
	  
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Branch branch = Branch.getInstance();
		branch.initSession(new Branch.BranchReferralInitListener() {

			@Override
			public void onInitFinished(JSONObject referringParams, BranchError error) {
				if (error == null) {
					String productid = referringParams.optString("id", "");
					String userprofilepic = referringParams.optString("userprofilepic", "");
					String username = referringParams.optString("username", "");
					String userid = referringParams.optString("userid", "");
					String usermail = referringParams.optString("usermail", "");
					
					String sharecollection = referringParams.optString("sharecollection", "");
					String user_id = referringParams.optString("user_id", "");
					
					if(!userid.equals("") && UserPreference.getInstance().getUserID().equals("")) {
						Log.e("referred by usermail", "referred by usermail");
						Intent i = new Intent(context,LandingActivity.class);
						i.putExtra("referred_usermail", usermail);
						i.putExtra("referred_userid", userid);
						i.putExtra("referred_username", username);
						i.putExtra("referred_userprofilepic", userprofilepic);
						startActivity(i);
						finish();
					}
					Log.e("share collection value", sharecollection);
					if(sharecollection.equals("1") && !UserPreference.getInstance().getUserID().equals("")) {
		                	Log.e("share collection", "collection not empty");
		                	Intent i = new Intent(context, HomeActivity.class);
		                    i.putExtra("profile_collection", user_id);
		                    
		                    startActivity(i);
		            } else if (productid.equals("") || UserPreference.getInstance().getUserID().equals("")) {
	                	Log.e("Product empty or not logged in", "product empty or not logged in");
	                	handler.postDelayed(runnable, SPLASH_DISPLAY_TIME);
	                }
	                else {
	                	Log.e("product not empty", "not empty");
	                	Product prod = new Product();
	                	prod.setId(referringParams.optString("id", ""));
	                	prod.setProductname(referringParams.optString("productname", ""));
	                	prod.setSkunumber(referringParams.optString("skunumber", ""));
	                	prod.setPrimarycategory(referringParams.optString("primarycategory", ""));
	                	prod.setSecondarycategory(referringParams.optString("secondarycategory", ""));
	                	prod.setProducturl(referringParams.optString("producturl", ""));
	                	prod.setProductimageurl(referringParams.optString("productimageurl", ""));
	                	prod.setShortproductdesc(referringParams.optString("shortproductdesc", ""));
	                	prod.setLongproductdesc(referringParams.optString("longproductdesc", ""));
	                	prod.setDiscount(referringParams.optString("discount", ""));
	                	prod.setDiscounttype(referringParams.optString("discounttype", ""));
	                	prod.setSaleprice(referringParams.optString("saleprice", ""));
	                	prod.setRetailprice(referringParams.optString("retailprice", ""));
	                	//prod.setBrand(referringParams.optString("brand", ""));
	                	prod.setShippingcost(referringParams.optString("shippingcost", ""));
	                	prod.setKeywords(referringParams.optString("keywords", ""));
	                	prod.setManufacturename(referringParams.optString("manufacturename", ""));
	                	prod.setAvailability(referringParams.optString("availability", ""));
	                	prod.setShippinginfo(referringParams.optString("shippinginfo", ""));
	                	prod.setPixel(referringParams.optString("pixel", ""));
	                	prod.setMerchantid(referringParams.optString("merchantid", ""));
	                	prod.setMerchantname(referringParams.optString("merchantname", ""));
	                	prod.setQuantity(referringParams.optString("quantity", ""));
	                	prod.setColor(referringParams.optString("color", ""));
	                	prod.setCart_id(referringParams.optString("cart_id", ""));
	                	prod.setFrom_user_id(referringParams.optString("from_user_id", ""));
	                	prod.setFrom_collection_id(referringParams.optString("from_collection_id", ""));
	                	prod.setProductcart_id(referringParams.optString("productcart_id", ""));
	                	prod.setTbl_product_id(referringParams.optString("tbl_product_id", ""));
	                	prod.setSize(referringParams.optString("size", ""));
	                	prod.setSelected_size(referringParams.optString("selected_size", ""));
	                	prod.setSelected_color(referringParams.optString("selected_color", ""));
	                	prod.setBrand_image(referringParams.optString("brand_image", ""));
	                	LikeInfo likeinfo = new LikeInfo();
	                	likeinfo.setLike_count(referringParams.optString("like_count", ""));
	                	likeinfo.setLike_id(referringParams.optString("like_id", ""));
	                	prod.setLike_info(likeinfo);
	                	prod.setAffiliateurl(referringParams.optString("affiliateurl", ""));
	                	prod.setOption(referringParams.optString("option", ""));
	                	prod.setFit(referringParams.optString("fit", ""));
	                	prod.setSiteId(referringParams.optString("siteId", ""));
	                	
	                	
	                    Intent i = new Intent(context, HomeActivity.class);
	                    i.putExtra("productobj", prod);
	                    
	                    startActivity(i);
	                }
					// params are the deep linked params associated with the link that the user clicked before showing up
					Log.e("BranchConfigTest", "deep link data: " + referringParams.toString());
				}
				else {
					Log.e("MyApp", error.getMessage());
					handler.postDelayed(runnable, SPLASH_DISPLAY_TIME);
				}
			}
		}, this.getIntent().getData(), this);
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		this.setIntent(intent);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    if (Branch.isAutoDeepLinkLaunch(this)) {
	        try {
	            String autoDeeplinkedValue = Branch.getInstance().getLatestReferringParams().getString("auto_deeplink_key_1");
	            Log.e("Resume deep", "Launched by Branch on auto deep linking! " +  autoDeeplinkedValue);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    } else {
	    	Log.e("else resume", "Launched by normal application flow");
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		//Checking if the previous activity is launched on branch Auto deep link.
		if(requestCode == getResources().getInteger(R.integer.deeplinkrequestcode)){
			//Decide here where  to navigate  when an auto deep linked activity finishes.
			//For e.g. Go to HomeActivity or a  SignUp Activity.
			Log.e("entered activity result", "moving to home");
			Intent i = new Intent(getApplicationContext(), HomeActivity.class);
			startActivity(i);
		}
	}
}
