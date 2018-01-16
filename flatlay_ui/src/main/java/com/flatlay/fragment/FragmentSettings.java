package com.flatlay.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.ChangeEmailActivity;
import com.flatlay.activity.ChangePasswordActivity;
import com.flatlay.activity.ChangeWalletPinActivity;
import com.flatlay.activity.EditProfileActivity;
import com.flatlay.dialog.RemoveSocialAccountDialog;
import com.flatlay.dialog.WelcomeDialog;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants;
import com.flatlaylib.api.AddressApi;
import com.flatlaylib.api.CardInfoApi;
import com.flatlaylib.api.ChangePasswordApi;
import com.flatlaylib.api.NotificationSettingsApi;
import com.flatlaylib.api.WalletPinApi;
import com.flatlaylib.bean.NotificationSetting;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.AddressRes;
import com.flatlaylib.service.res.CardInfoRes;
import com.flatlaylib.service.res.ChangePasswordRes;
import com.flatlaylib.service.res.NotificationSettingRes;
import com.flatlaylib.service.res.WalletPinRes;
import com.flatlaylib.utils.AlertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//isCreatePassword,isCreatePin
public class FragmentSettings extends BaseFragment implements OnClickListener {
	private View mainView;
	private LinearLayout passwordTextLayout,walletPinChangeLayout,cardListLayout,addressListLayout,emailTextLayout, usernameTextLayout;
	private ImageView socialFbImage,socialTwitterImage,socialPinterestImage,socialInstagramImage;
//	private ImageView followersImage,commentsImage,favoritesImage,itemsPurchasedImage;
	SwitchCompat switchfollowers,switchcomment,switchfavoritesImage,switchitemsPurchasedImage;
	private static TextView emailText;
	private TextView noAddressFound;
	private TextView noCardFound;
	private TextView usernameText;
	private LinearLayout fbAccountLayout,twitterAccountLayout,instagramAccountLayout,pinterestAccountLayout;
	private ProgressBarDialog progressBarDialog;
	private FragmentSettings fragmentSettings;
	private List<NotificationSetting> data = new ArrayList<NotificationSetting>();
	private HashMap<String, Boolean> settingStatus = new HashMap<String, Boolean>();
	private ProgressBar progressBar_settings_followers,progressBar_settings_comments,progressBar_settings_purchases,progressBar_settings_favorites;
	public static int cameFromPassword = -1;
	SharedPreferences userSettings;
	public FragmentSettings() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_settings, null);
		fragmentSettings=this;
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		passwordTextLayout = (LinearLayout) mainView.findViewById(R.id.passwordTextLayout);
		walletPinChangeLayout = (LinearLayout) mainView.findViewById(R.id.walletPinChangeLayout);
		cardListLayout = (LinearLayout) mainView.findViewById(R.id.cardListLayout);
		addressListLayout = (LinearLayout) mainView.findViewById(R.id.addressListLayout);
		emailTextLayout = (LinearLayout) mainView.findViewById(R.id.emailTextLayout);
		emailText = (TextView) mainView.findViewById(R.id.emailText);
		usernameTextLayout = (LinearLayout) mainView.findViewById(R.id.usernameTextLayout);
		usernameText = (TextView) mainView.findViewById(R.id.usernameText);
		socialFbImage = (ImageView) mainView.findViewById(R.id.socialFbImage);
		socialTwitterImage = (ImageView) mainView.findViewById(R.id.socialTwitterImage);
		socialPinterestImage = (ImageView) mainView.findViewById(R.id.socialPinterestImage);
		socialInstagramImage = (ImageView) mainView.findViewById(R.id.socialInstagramImage);
		fbAccountLayout = (LinearLayout) mainView.findViewById(R.id.fbAccountLayout);
		twitterAccountLayout = (LinearLayout) mainView.findViewById(R.id.twitterAccountLayout);
		instagramAccountLayout = (LinearLayout) mainView.findViewById(R.id.instagramAccountLayout);
		pinterestAccountLayout = (LinearLayout) mainView.findViewById(R.id.pinterestAccountLayout);
	//	followersImage = (ImageView) mainView.findViewById(followersImage);
		switchfollowers = (SwitchCompat) mainView.findViewById(R.id.switchfollowers);
		switchcomment=(SwitchCompat) mainView.findViewById(R.id.switchcomment);
		switchfavoritesImage=(SwitchCompat) mainView.findViewById(R.id.switchfavoritesImage);
		switchitemsPurchasedImage=(SwitchCompat) mainView.findViewById(R.id.switchitemsPurchasedImage);
//		commentsImage = (ImageView) mainView.findViewById(commentsImage);
//		favoritesImage = (ImageView) mainView.findViewById(favoritesImage);
//		itemsPurchasedImage = (ImageView) mainView.findViewById(itemsPurchasedImage);
		noAddressFound= (TextView) mainView.findViewById(R.id.noAddressFound);
		noCardFound= (TextView) mainView.findViewById(R.id.noCardFound);
		progressBar_settings_followers = (ProgressBar) mainView.findViewById(R.id.progressBar_settings_followers);
		progressBar_settings_comments = (ProgressBar) mainView.findViewById(R.id.progressBar_settings_comments);
		progressBar_settings_purchases = (ProgressBar) mainView.findViewById(R.id.progressBar_settings_purchases);
		progressBar_settings_favorites = (ProgressBar) mainView.findViewById(R.id.progressBar_settings_favorites);
		userSettings = mContext.getSharedPreferences("UserSettings", 0);
	}

	@Override
	public void refreshData(Bundle bundle) {
	}

	@Override
	public void setClickListener() {
		passwordTextLayout.setOnClickListener(this);
		walletPinChangeLayout.setOnClickListener(this);
		//commentsImage.setOnClickListener(this);
		emailTextLayout.setOnClickListener(this);
		usernameTextLayout.setOnClickListener(this);
		fbAccountLayout.setOnClickListener(this);
		twitterAccountLayout.setOnClickListener(this);
		pinterestAccountLayout.setOnClickListener(this);
		instagramAccountLayout.setOnClickListener(this);
		//followersImage.setOnClickListener(this);
		switchfollowers.setOnClickListener(this);
		//itemsPurchasedImage.setOnClickListener(this);
		//favoritesImage.setOnClickListener(this);

		switchitemsPurchasedImage.setOnClickListener(this);
		switchfavoritesImage.setOnClickListener(this);
		switchcomment.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {

		if(userSettings.getString("isSet", "").equals("True"))
		{
			Log.w("isSet","false!!!!!");
			emailText.setText(UserPreference.getInstance().getEmail());
			setNotifications();
			getAddressList(false);
		}
		else
		{
			Log.w("isSet","true!!!!!");
			getAddressList(true);
		}

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(UserPreference.getInstance().getUserName() != "") {
			usernameText.setText(UserPreference.getInstance().getUserName());
			usernameTextLayout.setBackground(null);
		}else {
			usernameText.setText("Username");
			usernameTextLayout.setBackgroundColor(mContext.getResources().getColor(R.color.aquamarine2));
		}
		if( UserPreference.getInstance().getEmail() == "")
			emailTextLayout.setBackgroundColor(mContext.getResources().getColor(R.color.aquamarine2));
		else
			emailTextLayout.setBackground(null);

		if( UserPreference.getInstance().getPassword() == "")
			passwordTextLayout.setBackgroundColor(mContext.getResources().getColor(R.color.aquamarine2));
		else
			passwordTextLayout.setBackground(null);

		if(cameFromPassword == 1) {
			WelcomeDialog welcome = new WelcomeDialog(mContext);
			welcome.show();
			cameFromPassword = -1;
			mContext.getSupportFragmentManager().popBackStack();
		}

	}

	@Override
	public void onClick(View v) {
		SharedPreferences.Editor editor = userSettings.edit();
		switch (v.getId()) {
			case R.id.passwordTextLayout:
//			startActivity(ChangePasswordActivity.class);
				if(checkInternet()){
					checkPassword();
				}
				break;
			case R.id.walletPinChangeLayout:
				if(checkInternet()){
					if (UserPreference.getInstance().getIsCreateWalletPin()) {
						checkKikrWalletPin();
					} else {
						Intent i = new Intent(mContext, ChangeWalletPinActivity.class);
						i.putExtra("isCreatePin", false);
						startActivity(i);
					}
				}
				break;
			case R.id.emailTextLayout:
				String email = emailText.getText().toString();
				Bundle bundle = new Bundle();
				if (!email.equals("Email")) {
					bundle.putString("email", email);
				} else {
					bundle.putString("email", "");
				}
				Intent intent=new Intent(mContext,ChangeEmailActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, AppConstants.REQUEST_CODE_EMAIL_CHANGE);
//			startActivity(ChangeEmailActivity.class, bundle);
				break;
			case R.id.usernameTextLayout:
				Bundle bundle1=new Bundle();
				bundle1.putString("username", UserPreference.getInstance().getUserName());
				bundle1.putBoolean("is_edit_profile", true);
				bundle1.putString("from", AppConstants.FROM_PROFILE);
				startActivityForResult(EditProfileActivity.class,bundle1, 1000);
				break;
			case R.id.fbAccountLayout:
				RemoveSocialAccountDialog socialAccountDialog = new RemoveSocialAccountDialog(mContext);
				socialAccountDialog.show();
				break;
			case R.id.twitterAccountLayout:
				RemoveSocialAccountDialog socialAccountDialog1 = new RemoveSocialAccountDialog(mContext);
				socialAccountDialog1.show();
				break;
			case R.id.pinterestAccountLayout:
				RemoveSocialAccountDialog socialAccountDialog2 = new RemoveSocialAccountDialog(mContext);
				socialAccountDialog2.show();
				break;
			case R.id.instagramAccountLayout:
				RemoveSocialAccountDialog socialAccountDialog3 = new RemoveSocialAccountDialog(mContext);
				socialAccountDialog3.show();
				break;
			//case followersImage:
//				if(checkInternet()){
//					if (settingStatus.containsKey(AppConstants.Options.FOLLOWERS)&&settingStatus.get(AppConstants.Options.FOLLOWERS)) {
//						setNotificationStatus(AppConstants.Options.FOLLOWERS, "off",progressBar_settings_followers,followersImage);
//					} else {
//						setNotificationStatus(AppConstants.Options.FOLLOWERS, "on",progressBar_settings_followers,followersImage);
//					}
//				}
				//break;
			case R.id.switchfollowers:
				Log.w("Updating followers","Clicked");
				if(checkInternet()){
					if (userSettings.getString("settingFollowers", null).equals("true")) {
						Log.w("followers","false/off");
						editor.putString("settingFollowers", "false");
						editor.apply();
						Log.w("followers","now is "+userSettings.getString("settingFollowers", null));
						setNotificationStatus(AppConstants.Options.FOLLOWERS, "off",progressBar_settings_followers);
					} else {
						Log.w("followers","true/on");
						editor.putString("settingFollowers", "true");
						editor.apply();
						Log.w("followers","now is "+userSettings.getString("settingFollowers", null));
						setNotificationStatus(AppConstants.Options.FOLLOWERS, "on",progressBar_settings_followers);
					}
				}

				break;
			case R.id.switchcomment:
				Log.w("Updating comments","Clicked");
				if(checkInternet()){
					if (userSettings.getString("settingComments", null).equals("true")) {
						Log.w("comments","false/off");
						editor.putString("settingComments", "false");
						editor.apply();
						Log.w("comments","now is "+userSettings.getString("settingComments", null));
						setNotificationStatus(AppConstants.Options.COMMENTS, "off",progressBar_settings_comments);
					} else {
						Log.w("comments","true/on");
						editor.putString("settingComments", "true");
						editor.apply();
						Log.w("comments","now is "+userSettings.getString("settingComments", null));
						setNotificationStatus(AppConstants.Options.COMMENTS, "on",progressBar_settings_comments);
					}
				}

				break;
			case R.id.switchfavoritesImage:
				if(checkInternet()){
					Log.w("Updating favorites","Clicked");
					if (userSettings.getString("settingFavourites", null).equals("true")) {
						Log.w("favorites","false/off");
						editor.putString("settingFavourites", "false");
						editor.apply();
						Log.w("favorites","now is "+userSettings.getString("settingFavourites", null));
						setNotificationStatus(AppConstants.Options.FAVOURITES, "off",progressBar_settings_favorites);
					} else {
						Log.w("favorites","true/on");
						editor.putString("settingFavourites", "true");
						editor.apply();
						Log.w("favorites","now is "+userSettings.getString("settingFavourites", null));
						setNotificationStatus(AppConstants.Options.FAVOURITES, "on",progressBar_settings_favorites);
					}
				}

				break;
			case R.id.switchitemsPurchasedImage:
				if(checkInternet()){
					Log.w("Updating purchases","Clicked");
					if (userSettings.getString("settingPurchases", null).equals("true")) {
						Log.w("purchases","false/off");
						editor.putString("settingPurchases", "false");
						editor.apply();
						Log.w("purchases","now is "+userSettings.getString("settingPurchases", null));
						setNotificationStatus(AppConstants.Options.PURCHASES, "off",progressBar_settings_purchases);
					} else {
						Log.w("purchases","true/on");
						editor.putString("settingPurchases", "true");
						editor.apply();
						Log.w("purchases","now is "+userSettings.getString("settingPurchases", null));
						setNotificationStatus(AppConstants.Options.PURCHASES, "on",progressBar_settings_purchases);
					}
				}

				break;
			default:
				break;
		}
	}

	public void getCardList(final boolean isLoadNot) {
		progressBarDialog = new ProgressBarDialog(mContext);
		progressBarDialog.show();
		final CardInfoApi cardInfoApi = new CardInfoApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				if (object != null) {
					CardInfoRes cardInfoRes = (CardInfoRes) object;
					cardListLayout.setVisibility(View.VISIBLE);
					cardListLayout.removeAllViews();
					//cardListLayout.addView(new SettingsCardList(mContext, cardInfoRes.getData(),fragmentSettings).getView());
					if(cardInfoRes.getData().size()>0)
						noCardFound.setVisibility(View.GONE);
					else
						noCardFound.setVisibility(View.VISIBLE);

					if (isLoadNot) {
						getNotificationStatus();
					}


				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					CardInfoRes cardInfoRes=(CardInfoRes) object;
					AlertUtils.showToast(mContext, cardInfoRes.getMessage());
				}else{
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		cardInfoApi.getCardList(UserPreference.getInstance().getUserID());
		cardInfoApi.execute();
	}

	public void getAddressList(final boolean isLoadNot) {
		progressBarDialog = new ProgressBarDialog(mContext);
		progressBarDialog.show();
		final AddressApi addressApi = new AddressApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				if (object != null) {
					AddressRes addressRes = (AddressRes) object;
					addressListLayout.setVisibility(View.VISIBLE);
					addressListLayout.removeAllViews();
					//addressListLayout.addView(new SettingsAddressList(mContext, addressRes.getData(),fragmentSettings).getView());

					//Log.w("getAddressList","Address stuff"+addressRes.getData().toString());
					//SharedPreferences.Editor editor = userSettings.edit();
					//editor.putString("isSet", addressRes.getData().toString());
					//editor.putString("settingAddress", null);
					//editor.apply();

					if(addressRes.getData().size()>0)
						noAddressFound.setVisibility(View.GONE);
					else
						noAddressFound.setVisibility(View.VISIBLE);



					getCardList(isLoadNot);


				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					AddressRes addressRes=(AddressRes) object;
					AlertUtils.showToast(mContext, addressRes.getMessage());
				}else{
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		addressApi.getAddressList(UserPreference.getInstance().getUserID());
		addressApi.execute();
	}




	public void setNotifications()
	{

		Log.w("FragmentSettings","setNotifications()");
		Log.w("Comments",":"+userSettings.getString("settingComments", ""));
		Log.w("Followers",":"+userSettings.getString("settingFollowers", ""));
		Log.w("Purcahses",":"+userSettings.getString("settingPurchases", ""));
		Log.w("Favourites",":"+userSettings.getString("settingFavourites", ""));

		if (userSettings.getString("settingComments", "").equals("true")) {
			Log.w("c","true");
			switchcomment.setChecked(true);
		}
		else {
			Log.w("c","false");
			switchcomment.setChecked(false);
		}

		//FOLLOWERS
		if (userSettings.getString("settingFollowers", "").equals("true")) {
			Log.w("fol","true");
			switchfollowers.setChecked(true);
		}
		else {
			Log.w("fol","false");
			switchfollowers.setChecked(false);
		}

		//PURCHASES
		if (userSettings.getString("settingPurchases", "").equals("true")) {
			Log.w("p","true");
			switchitemsPurchasedImage.setChecked(true);
		}
		else {
			Log.w("p","false");
			switchitemsPurchasedImage.setChecked(false);
		}

		//FAVOURITES
		if (userSettings.getString("settingFavourites", "").equals("true")) {
			Log.w("fav","true");
			switchfavoritesImage.setChecked(true);
		}
		else {
			Log.w("fav","false");
			switchfavoritesImage.setChecked(false);
		}
	}


	public void getNotificationStatus() {
		progressBarDialog = new ProgressBarDialog(mContext);
		progressBarDialog.show();
		final NotificationSettingsApi notificationSettingsApi = new NotificationSettingsApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				if (object != null) {
					NotificationSettingRes notificationSettingRes = (NotificationSettingRes) object;
					if (!TextUtils.isEmpty(notificationSettingRes.getEmail())) {
						emailText.setText(notificationSettingRes.getEmail());
					} else {
						emailText.setText("Email");
					}
					data.clear();
					data = notificationSettingRes.getData();
					settingStatus.clear();
					for (int i = 0; i <data.size(); i++) {
						settingStatus.put(data.get(i).getOption(), data.get(i).getStatus().equals("on") ? true: false);
					}

					//SharedPreferences userSettings = mContext.getSharedPreferences("UserSettings", 0);


					SharedPreferences.Editor editor = userSettings.edit();
					editor.putString("isSet", "True");
					editor.putString("settingComments", settingStatus.get(AppConstants.Options.COMMENTS).toString());
					editor.putString("settingFollowers", settingStatus.get(AppConstants.Options.FOLLOWERS).toString());
					editor.putString("settingPurchases", settingStatus.get(AppConstants.Options.PURCHASES).toString());
					editor.putString("settingFavourites", settingStatus.get(AppConstants.Options.FAVOURITES).toString());
					editor.apply();

					// txtUname.setText(settings.getString("Username", "").toString());
					// txtPWD.setText(settings.getString("Password", "").toString());

					// COMMENTS
					Log.w("Comments",":"+settingStatus.get(AppConstants.Options.COMMENTS));
					Log.w("Followers",":"+settingStatus.get(AppConstants.Options.FOLLOWERS));
					Log.w("Purcahses",":"+settingStatus.get(AppConstants.Options.PURCHASES));
					Log.w("Favourites",":"+settingStatus.get(AppConstants.Options.FAVOURITES));

					if (settingStatus.get(AppConstants.Options.COMMENTS)) {
						switchcomment.setChecked(true);
					}
					else {
						switchcomment.setChecked(false);
					}

					//FOLLOWERS
					if (settingStatus.get(AppConstants.Options.FOLLOWERS)) {
						switchfollowers.setChecked(true);
					}
					else {
						switchfollowers.setChecked(false);
					}

					//PURCHASES
					if (settingStatus.get(AppConstants.Options.PURCHASES)) {
						switchitemsPurchasedImage.setChecked(true);
					}
					else {
						switchitemsPurchasedImage.setChecked(false);
					}

					//FAVOURITES
					if (settingStatus.get(AppConstants.Options.FAVOURITES)) {
						switchfavoritesImage.setChecked(true);
					}
					else {
						switchfavoritesImage.setChecked(false);
					}

				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					NotificationSettingRes notificationSettingRes = (NotificationSettingRes) object;
					AlertUtils.showToast(mContext, notificationSettingRes.getMessage());
				}else{
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		notificationSettingsApi.getNotificationStatus(UserPreference.getInstance().getUserID());
		notificationSettingsApi.execute();
	}

	public void setNotificationStatus(final String option,final String status,final ProgressBar progressBar) {
	//	progressBar.setVisibility(View.VISIBLE);
		//imageView.setVisibility(View.GONE);
		final NotificationSettingsApi notificationSettingsApi = new NotificationSettingsApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				if (object != null) {
					NotificationSettingRes notificationSettingRes = (NotificationSettingRes) object;
					AlertUtils.showToast(mContext, notificationSettingRes.getMessage());
					settingStatus.put(option, status.equals("on")?true:false);
//					if(settingStatus.get(option))
//						//switchfollowers.setChecked(true);
//					else
//						//switchfollowers.setChecked(false);
		}
               else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
				//progressBar.setVisibility(View.GONE);
				//imageView.setVisibility(View.VISIBLE);
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				progressBar.setVisibility(View.GONE);
				//imageView.setVisibility(View.VISIBLE);
				if(object!=null){
					NotificationSettingRes notificationSettingRes=(NotificationSettingRes) object;
					AlertUtils.showToast(mContext, notificationSettingRes.getMessage());
				}else{
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		notificationSettingsApi.setNotificationStatus(UserPreference.getInstance().getUserID(),status,option);
		notificationSettingsApi.execute();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null&&data.hasExtra("email")){
			String email=data.getStringExtra("email");
			emailText.setText(email);
		}
	}

	private void checkKikrWalletPin() {

		progressBarDialog = new ProgressBarDialog(mContext);
		progressBarDialog.show();

		final WalletPinApi service = new WalletPinApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					WalletPinRes response=(WalletPinRes) object;
					if(!TextUtils.isEmpty(response.getPin_created())&&response.getPin_created().equals("yes")){
						UserPreference.getInstance().setIsCreateWalletPin(false);
						Intent i = new Intent(mContext, ChangeWalletPinActivity.class);
						i.putExtra("isCreatePin", false);
						startActivity(i);
					}else{
						Intent i = new Intent(mContext, ChangeWalletPinActivity.class);
						i.putExtra("isCreatePin", true);
						startActivity(i);
					}
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					WalletPinRes response=(WalletPinRes) object;
					AlertUtils.showToast(mContext,response.getMessage());
				}else{
					AlertUtils.showToast(mContext,R.string.invalid_response);
				}
			}
		});
		service.checkKikrWalletPin(UserPreference.getInstance().getUserID());
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}


	private void checkPassword() {

		progressBarDialog = new ProgressBarDialog(mContext);
		progressBarDialog.show();

		final ChangePasswordApi service = new ChangePasswordApi(new ServiceCallback() {

			@Override
			public void handleOnSuccess(Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					ChangePasswordRes response=(ChangePasswordRes) object;
					if(!TextUtils.isEmpty(response.getPassword_created())&&response.getPassword_created().equals("yes")){
						Intent i = new Intent(mContext, ChangePasswordActivity.class);
						i.putExtra("isCreatePassword", false);
						startActivity(i);
					}else{
						Intent i = new Intent(mContext, ChangePasswordActivity.class);
						i.putExtra("isCreatePassword", true);
						startActivity(i);
					}
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				progressBarDialog.dismiss();
				if(object!=null){
					WalletPinRes response=(WalletPinRes) object;
					AlertUtils.showToast(mContext,response.getMessage());
				}else{
					AlertUtils.showToast(mContext,R.string.invalid_response);
				}
			}
		});
		service.checkPasswordCreated(UserPreference.getInstance().getUserID());
		service.execute();

		progressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				service.cancel();
			}
		});
	}
}