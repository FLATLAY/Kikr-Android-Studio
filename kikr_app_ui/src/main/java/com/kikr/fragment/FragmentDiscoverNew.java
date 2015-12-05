package com.kikr.fragment;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.Syso;

public class FragmentDiscoverNew extends BaseFragment implements TabListener, OnPageChangeListener{

	 CustomPagerAdapter mCustomPagerAdapter;
	 ViewPager mViewPager;
	 String[] titalName = new String[]{"Trending", "Featured","Collections"};
	 private ActionBar actionBar;
	 TextView option1TextView,option2TextVie,option3TextView;
	 private boolean isInspiration=false;
	 TextView[] optionArray;// = new TextView[]{option1TextView,option2TextVie,option3TextView}; 
	 
	 public FragmentDiscoverNew(boolean isInspiration){
		 this.isInspiration = isInspiration;
	 }
	 
	 public FragmentDiscoverNew(){

	 }
	 
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 Syso.info("uuuuuuuuuuu in onCreateView");
		 View view = inflater.inflate(R.layout.discover_fragment_new, null);
		 mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), (Context)getActivity());
	     mViewPager = (ViewPager) view.findViewById(R.id.pager);
	     mViewPager.setOffscreenPageLimit(3);
	     mViewPager.setAdapter(mCustomPagerAdapter);
	     mViewPager.setOnPageChangeListener(this);
	     option1TextView = (TextView) view.findViewById(R.id.option1);
	     option2TextVie = (TextView) view.findViewById(R.id.option2);
	     option3TextView = (TextView) view.findViewById(R.id.option3);
	     optionArray = new TextView[]{option1TextView,option2TextVie,option3TextView}; 
//		if (isInspiration) {
//			 mViewPager.setCurrentItem(0);
//			 changeIndicator(0);
//		 } else
			 mViewPager.setCurrentItem(0);
		 changeIndicator(0);
		setClickListner();
//	     actionBar = ((HomeActivity)mContext).getActionBar();
//	     actionBar.setHomeButtonEnabled(false);
//	     if(actionBar.getTabCount()==-1){
//		     actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 
////		     actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
//		     actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
//		     actionBar.setSplitBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.btn_green)));
////		     actionBar.set
//		     for (String tab_name : titalName) {
//		    	 Tab tab = actionBar.newTab();
//		    	 TextView tv = new TextView(mContext);
//		    	 tv.setText(tab_name);
//		    	 tv.setGravity(Gravity.CENTER);
//		    	 tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
//		    	 tv.setTextSize(getResources().getDimension(R.dimen.tab_text_size));
//		    	 tv.setTextColor(getResources().getColor(R.color.btn_green));
////		    	 tab.setText(tab_name).setTabListener(this);
//		    	 tab.setCustomView(tv).setTabListener(this);
//		         actionBar.addTab(tab);
//		      }
//	     }
//	     actionBar.setSelectedNavigationItem(1);
		return view;
	}
	
	private void setClickListner() {
		for(int i=0;i<optionArray.length;i++){
			optionArray[i].setTag(i);
			optionArray[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					changeIndicator((Integer) v.getTag());
					mViewPager.setCurrentItem((Integer) v.getTag());
				}
			});
		}
	}

	protected void changeIndicator(int tag) {
		for(int i=0;i<optionArray.length;i++){
			if(tag==i)
				optionArray[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.tab_indicater_selected));
			else
				optionArray[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, getResources().getDrawable(R.drawable.tab_indicater_unselected));

		}
	}

	@Override
	public void initUI(Bundle savedInstanceState) {}

	@Override
	public void setData(Bundle bundle) {}

	@Override
	public void refreshData(Bundle bundle) {}

	@Override
	public void setClickListener() {}
	
	class CustomPagerAdapter extends FragmentPagerAdapter {
		 
        Context mContext;
 
        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }
 

		@Override
        public Fragment getItem(int position) {
			Syso.info("uuuuuuuuuuu in getItem");
			switch(position){
			case 0:
				return new FragmentInspirationSection(true,UserPreference.getInstance().getUserID());
			case 1:
				return new FragmentFeatured();
			case 2:
				return new FragmentDiscover();
			default:
				return new FragmentInspirationSection(true,UserPreference.getInstance().getUserID());
			}
        }
 
        @Override
        public int getCount() {
            return titalName.length;
        }
 
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titalName[position];
//        }
    }
 
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Syso.info("uuuuuuuuuuu in onDestroy");

	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		 mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		changeIndicator(position);
//		actionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		getAuthTocken();
//		validateCard();
	}
}
