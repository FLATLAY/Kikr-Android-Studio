package com.kikr.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.ChangeEmailActivity;
import com.kikr.activity.ChangePasswordActivity;
import com.kikr.activity.ChangeWalletPinActivity;
import com.kikr.activity.EditProfileActivity;
import com.kikr.dialog.RemoveSocialAccountDialog;
import com.kikr.dialog.WelcomeDialog;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.ui.SettingsAddressList;
import com.kikr.ui.SettingsCardList;
import com.kikr.utility.AppConstants;
import com.kikrlib.api.AddressApi;
import com.kikrlib.api.CardInfoApi;
import com.kikrlib.api.ChangePasswordApi;
import com.kikrlib.api.NotificationSettingsApi;
import com.kikrlib.api.WalletPinApi;
import com.kikrlib.bean.NotificationSetting;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.AddressRes;
import com.kikrlib.service.res.CardInfoRes;
import com.kikrlib.service.res.ChangePasswordRes;
import com.kikrlib.service.res.NotificationSettingRes;
import com.kikrlib.service.res.WalletPinRes;
import com.kikrlib.utils.AlertUtils;

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
		getAddressList(true);

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
				if(checkInternet()){
					if (settingStatus.containsKey(AppConstants.Options.FOLLOWERS)&&settingStatus.get(AppConstants.Options.FOLLOWERS)) {
						setNotificationStatus(AppConstants.Options.FOLLOWERS, "off",progressBar_settings_followers);
					} else {
						setNotificationStatus(AppConstants.Options.FOLLOWERS, "on",progressBar_settings_followers);
					}
				}
				break;
			case R.id.switchcomment:
				if(checkInternet()){
					if (settingStatus.containsKey(AppConstants.Options.COMMENTS)&&settingStatus.get(AppConstants.Options.COMMENTS)) {
						setNotificationStatus(AppConstants.Options.COMMENTS, "off",progressBar_settings_comments);
					} else {
						setNotificationStatus(AppConstants.Options.COMMENTS, "on",progressBar_settings_comments);
					}
				}
				break;
			case R.id.switchfavoritesImage:
				if(checkInternet()){
					if (settingStatus.containsKey(AppConstants.Options.FAVOURITES)&&settingStatus.get(AppConstants.Options.FAVOURITES)) {
						setNotificationStatus(AppConstants.Options.FAVOURITES, "off",progressBar_settings_favorites);
					} else {
						setNotificationStatus(AppConstants.Options.FAVOURITES, "on",progressBar_settings_favorites);
					}
				}
				break;
			case R.id.switchitemsPurchasedImage:
				if(checkInternet()){
					if (settingStatus.containsKey(AppConstants.Options.PURCHASES)&&settingStatus.get(AppConstants.Options.PURCHASES)) {
						setNotificationStatus(AppConstants.Options.PURCHASES, "off",progressBar_settings_purchases);
					} else {
						setNotificationStatus(AppConstants.Options.PURCHASES, "on",progressBar_settings_purchases);
					}
				}
				break;
			default:
				break;
		}
	}

	public void getCardList(final boolean isLoadOther) {
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
					cardListLayout.addView(new SettingsCardList(mContext, cardInfoRes.getData(),fragmentSettings).getView());
					if(cardInfoRes.getData().size()>0)
						noCardFound.setVisibility(View.GONE);
					else
						noCardFound.setVisibility(View.VISIBLE);

					if(isLoadOther)
						getNotificationStatus();
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

	public void getAddressList(final boolean loadOther) {
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
					addressListLayout.addView(new SettingsAddressList(mContext, addressRes.getData(),fragmentSettings).getView());
					if(addressRes.getData().size()>0)
						noAddressFound.setVisibility(View.GONE);
					else
						noAddressFound.setVisibility(View.VISIBLE);
					if(loadOther)
						getCardList(true);
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

					if (settingStatus.get(AppConstants.Options.COMMENTS))
						//commentsImage.setImageDrawable(getResources().getDrawable(R.drawable.hdpi_on));
					switchcomment.setChecked(true);
					else
						switchcomment.setChecked(false);
					//	commentsImage.setImageDrawable(getResources().getDrawable(R.drawable.hdpi_off));


					if (settingStatus.get(AppConstants.Options.FOLLOWERS))
						switchfollowers.setChecked(true);
						//followersImage.setImageDrawable(getResources().getDrawable(R.drawable.hdpi_on));
					else
						switchfollowers.setChecked(false);
					//followersImage.setImageDrawable(getResources().getDrawable(R.drawable.hdpi_off));

					if (settingStatus.get(AppConstants.Options.PURCHASES))
						switchitemsPurchasedImage.setChecked(true);
						//itemsPurchasedImage.setImageDrawable(getResources().getDrawable(R.drawable.hdpi_on));
					else
						switchitemsPurchasedImage.setChecked(false);
						//itemsPurchasedImage.setImageDrawable(getResources().getDrawable(R.drawable.hdpi_off));
					if (settingStatus.get(AppConstants.Options.FAVOURITES))
						switchfavoritesImage.setChecked(true);
						//favoritesImage.setImageDrawable(getResources().getDrawable(R.drawable.hdpi_on));
					else
						switchfavoritesImage.setChecked(false);
						//favoritesImage.setImageDrawable(getResources().getDrawable(R.drawable.hdpi_off));
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
