package com.kikr.fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.activity.IntroductionPagerActivity;
import com.kikrlib.utils.Syso;

import java.util.HashMap;
import java.util.Map;


public class SettingFragmentTab extends BaseFragment implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    private HomeActivity homeActivity;
    String[] titalName = new String[]{"setting",  "support"};
    private ActionBar actionBar;
  //  TextView cards, credits, orders, cartitem;
    TextView setting,guide,support,logout;
    private boolean isInspiration = false;
    TextView[] optionArray;// = new TextView[]{option1TextView,option2TextVie,option3TextView};
    int currentTab = 0;
    public static boolean isCreateCollection;
    HashMap<Integer, String> mFragmentTags = new HashMap<>();


    public SettingFragmentTab() {
        currentTab = 0;
        isCreateCollection = false;
    }


    public SettingFragmentTab(int currenttab) {
        this.currentTab = currenttab;
        if (currenttab == 2)
            isCreateCollection = true;
        else
            isCreateCollection = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Syso.info("uuuuuuuuuuu in onCreateView");
        View view = inflater.inflate(R.layout.fragment_kikr_setting_tab, null);
        mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), (Context) getActivity());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        // mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        setting = (TextView) view.findViewById(R.id.setting);
        guide = (TextView) view.findViewById(R.id.guide);
        support = (TextView) view.findViewById(R.id.support);
        logout = (TextView) view.findViewById(R.id.logout);

        optionArray = new TextView[]{setting, support};
        initData();
        return view;
    }
    public void initData() {
        mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), (Context) getActivity());
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setCurrentItem(currentTab);
        changeIndicator(currentTab);
        setClickListner();
    }

    private void setClickListner() {
        for (int i = 0; i < optionArray.length; i++) {
            optionArray[i].setTag(i);
            optionArray[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int tag = (Integer) v.getTag();
                    changeIndicator((Integer) v.getTag());
                    mViewPager.setCurrentItem((Integer) v.getTag());
                    //      setFragmentData(tag);

                }
            });
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mContext).logoutscreen();
            }
        });

        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, IntroductionPagerActivity.class);
                i.putExtra("from","inside");
                startActivity(i);
            }
        });
    }

    private void setFragmentData(int tag) {
        try {
            if (tag == 0)
                ((FragmentFeatured) mCustomPagerAdapter.getFragment(tag)).initData();
            if (tag == 1)
                ((FragmentDiscover) mCustomPagerAdapter.getFragment(tag)).initData();
            if (tag == 2) {
                ((FragmentInspirationSection) mCustomPagerAdapter.getFragment(tag)).initData();

                // FragmentInspirationSection.inspirationSection.initData();
            }
            if (tag == 3)
                ((FragmentDiscover) mCustomPagerAdapter.getFragment(tag)).initData();
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }

    protected void changeIndicator(int tag) {
        for (int i = 0; i < optionArray.length; i++) {
            if (tag == i) {

                //    HomeActivity.homeImageView.setVisibility(View.INVISIBLE);
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    optionArray[i].setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                } else {
                    optionArray[i].setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                }

                // optionArray[i].setTextColor(getResources().getColor(R.color.btn_green));
            } else {
                //     HomeActivity.homeImageView.setVisibility(View.INVISIBLE);
                if (android.os.Build.VERSION.SDK_INT >= 21) {

                    optionArray[i].setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                } else {
                    optionArray[i].setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
                }
                //  optionArray[i].setCompoundDrawablesWithIntrinsicBounds(null, null, null, mContext.getResources().getDrawable(R.drawable.tab_indicater_unselected));
                //optionArray[i].setTextColor(getResources().getColor(R.color.btn_gray));
            }

        }


    }

    @Override
    public void initUI(Bundle savedInstanceState) {
    }

    @Override
    public void setData(Bundle bundle) {
    }

    @Override
    public void refreshData(Bundle bundle) {
    }

    @Override
    public void setClickListener() {
    }

    class CustomPagerAdapter extends FragmentPagerAdapter {

        Context mContext;
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }


        @Override
        public Fragment getItem(int position) {
            Syso.info("uuuuuuuuuuu in getItem");
            switch (position) {
                case 0:
//                    LogoutDialog logoutDialog = new LogoutDialog(mContext, homeActivity);
//                    logoutDialog.show();
                    return Fragment.instantiate(mContext, FragmentSettings.class.getName(), null);


                // return new FragmentFeatured();

                case 1:
                    return Fragment.instantiate(mContext, FragmentSupport.class.getName(), null);


                //return new FragmentDiscover();
                case 2:
                    return Fragment.instantiate(mContext, FragmentKikrGuide.class.getName(), null);

                default:
                    return Fragment.instantiate(mContext, FragmentSettings.class.getName(), null);
                // return new FragmentInspirationSection(true, UserPreference.getInstance().getUserID());
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
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
    public void onResume() {
        super.onResume();
        //HomeActivity.photouploadnext.setVisibility(View.GONE);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onTabReselected(ActionBar.Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        mViewPager.setCurrentItem(tab.getPosition());
        // setFragmentData(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

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
        setFragmentData(position);
//		actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//		getAuthTocken();
//		validateCard();
    }
}
