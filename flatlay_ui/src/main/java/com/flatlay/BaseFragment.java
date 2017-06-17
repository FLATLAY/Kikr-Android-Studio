package com.flatlay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.utils.Syso;


/**
 * An Abstract fragment class which is responsible for Fragment Life-Cycle, Handling HeaderTitle, HeaderNext Button, HeaderPrevious Button
 * and Alerts declaration.
 * 
 */
public abstract class BaseFragment extends Fragment {

	protected FragmentActivity mContext;
	protected boolean isShowBack=false;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Syso.debug("BaseFragment - onAttach = ", this.getClass().getSimpleName());
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("Activity","BaseFragment");
		Syso.debug("BaseFragment - onCreate = ", this.getClass().getSimpleName());
		mContext = getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Syso.debug("BaseFragment - onCreateView = ", this.getClass().getSimpleName());
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Syso.debug("BaseFragment - onActivityCreated = ", this.getClass().getSimpleName());
		initUI(savedInstanceState);	
		setData(getArguments()); 
		setClickListener();
		hideFotter();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Syso.debug("BaseFragment - onStart = ", this.getClass().getSimpleName());
	}
	
	@Override
	public void onResume() {
		super.onResume();

		Syso.debug("BaseFragment - onResume = ", this.getClass().getSimpleName());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Syso.debug("BaseFragment - onPause = ", this.getClass().getSimpleName());
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Syso.debug("BaseFragment - onStop = ", this.getClass().getSimpleName());
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Syso.debug("BaseFragment - onDestroyView = ", this.getClass().getSimpleName());
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Syso.debug("BaseFragment - onDetach = ", this.getClass().getSimpleName());
	}
	
	
	/**
	 * Inside this method initialize all UI component related to Fragment.<br/>
	 * This method will call internally on {@link BaseFragment}.onStart()
	 * 
	 */
	public abstract void initUI(Bundle savedInstanceState);
	
	
	/**
	 * Inside this method set all initial data required for Fragment.<br/>
	 * It will call internally on {@link BaseFragment}.onStart() after initUI(). 
	 * 
	 */
	public abstract void setData(Bundle bundle);
	
	/**
	 * This will refresh the data for fragment.
	 */
	public abstract void refreshData(Bundle bundle);
	
	
	public abstract void setClickListener();

	public void loginInstagram(){

	}
	
	public void startActivity(Class activity){
		Intent i = new Intent(mContext,activity);
		startActivity(i);
	}
	
	public void startActivity(Class activity,Bundle bundle) {
		Intent i = new Intent(mContext, activity);
		i.putExtras(bundle);
		startActivity(i);
	}
	
	public void startActivityForResult(Class activity,Bundle bundle,int requestCode) {
		Log.w("startActivityForResult","Here");
		Intent i = new Intent(mContext, activity);
		i.putExtras(bundle);
		startActivityForResult(i, requestCode);
	}
	
	public void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}
	
	public void hideFotter(){
		((HomeActivity) mContext).hideFutter();
	}
	
	public void showFotter(){
		((HomeActivity) mContext).showFooter();
	}
	
	
	public void showDataNotFound(){
		try{
			LinearLayout layout=(LinearLayout) getView().findViewById(R.id.itemNotFound);
			layout.setVisibility(View.VISIBLE);
			TextView textView=(TextView) getView().findViewById(R.id.noDataFoundTextView);
			textView.setText(getResources().getString(R.string.no_data_found));
		}catch(NullPointerException exception){
			exception.printStackTrace();
		}
	}


	public void noFollowingFound(){
		try{
			LinearLayout layout=(LinearLayout) getView().findViewById(R.id.FollowingNotFound);
			layout.setVisibility(View.GONE);
			TextView textView=(TextView) getView().findViewById(R.id.noFollowingyet);
			textView.setText(getResources().getString(R.string.no_following));
			textView.setVisibility(View.GONE);
		}catch(NullPointerException exception){
			exception.printStackTrace();
		}
	}
	public void hideDataNotFound(){
		try{
			LinearLayout layout=(LinearLayout) getView().findViewById(R.id.itemNotFound);
			layout.setVisibility(View.GONE);
		}catch(NullPointerException e){
			e.printStackTrace();
		}
	}
	
	public TextView getDataNotFound(){
		try{
			TextView textView=(TextView) getView().findViewById(R.id.noDataFoundTextView);
			textView.setText(Html.fromHtml(getResources().getString(R.string.no_internet)));
			return textView;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean checkInternet(){
		if(CommonUtility.isOnline(mContext)){
			return true;
		}else{
			CommonUtility.showNoInternetAlert(mContext);
			return false;
		}
	}
	
	public boolean checkInternet2(){
		if(CommonUtility.isOnline(mContext)){
			return true;
		}else{
			return false;
		}
	}
	
	public TextView getReloadFotter(){
		return ((HomeActivity) mContext).getReloadFotter();
	}



}
