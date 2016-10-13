package com.kikr.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kikr.BaseFragment;
import com.flatlay.R;
import com.kikr.utility.AppConstants;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.Syso;
import com.yalantis.ucrop.UCrop;

import java.util.HashMap;
import java.util.Map;

public class MainFragmentContainer extends BaseFragment implements OnPageChangeListener {

    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    String[] titalName = new String[]{"Trending", "Featured", "Collections", "Following"};
    private ActionBar actionBar;
    LinearLayout option1_LinearLayout, option2_LinearLayout, option3_LinearLayout, option4_LinearLayout, camera_LinearLayout;
    private boolean isInspiration = false;
    LinearLayout[] optionArray;// = new TextView[]{option1TextView,option2TextVie,option3TextView};
    int currentTab = 0;
    public static boolean isCreateCollection;
    HashMap<Integer, String> mFragmentTags = new HashMap<>();


    public MainFragmentContainer() {
        currentTab = 0;
        isCreateCollection = false;
    }

    public MainFragmentContainer(int currenttab, boolean isCreateCollection) {
        this.currentTab = currenttab;

        this.isCreateCollection = isCreateCollection;


    }

    public MainFragmentContainer(int currenttab) {
        this.currentTab = currenttab;
        if (currenttab == 2)
            isCreateCollection = true;
        else
            isCreateCollection = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Syso.info("uuuuuuuuuuu in onCreateView");
        View view = inflater.inflate(R.layout.fragment_main_new, null);
        mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), (Context) getActivity());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);

        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        option1_LinearLayout = (LinearLayout) view.findViewById(R.id.cart_tab);
        option2_LinearLayout = (LinearLayout) view.findViewById(R.id.profile_tab);
        camera_LinearLayout = (LinearLayout) view.findViewById(R.id.upload_post_tab);
        option4_LinearLayout = (LinearLayout) view.findViewById(R.id.message_tab);
        option3_LinearLayout = (LinearLayout) view.findViewById(R.id.search_tab);
        optionArray = new LinearLayout[]{option1_LinearLayout, option3_LinearLayout, option2_LinearLayout, option4_LinearLayout};
        return view;
    }


    private void setClickListner() {
        for (int i = 0; i < optionArray.length; i++) {
            optionArray[i].setTag(i);
            optionArray[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int tag = (Integer) v.getTag();
                    changeIndicator(tag);
                    mViewPager.setCurrentItem(tag);
                    //setFragmentData(tag);

                }
            });
        }

        camera_LinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new FragmentPostUploadTab());
            }
        });
    }

    private void setFragmentData(int tag) {
        try {


            if (tag == 1)
                ((FragmentSearchProduct) mCustomPagerAdapter.getFragment(tag)).initData();
            if (tag == 2)
                ((FragmentProfileView) mCustomPagerAdapter.getFragment(tag)).initData();
            if (tag == 3)
                ((FollowingkikrTab) mCustomPagerAdapter.getFragment(tag)).initData();


        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }

    protected void changeIndicator(int tag) {
        for (int i = 0; i < optionArray.length; i++) {
            if (tag == i) {
                optionArray[i].setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
            } else {
                optionArray[i].setBackgroundColor(getResources().getColor(R.color.tab_bg_new));
            }

        }


    }

    @Override
    public void initUI(Bundle savedInstanceState) {
    }

    @Override
    public void setData(Bundle bundle) {
        mViewPager.setCurrentItem(currentTab);
        changeIndicator(currentTab);
        setClickListner();
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


                    return Fragment.instantiate(mContext, new FragmentDiscoverNew().getClass().getName(), null);


                case 1:
                    return Fragment.instantiate(mContext, new FragmentSearchProduct(true).getClass().getName(), null);


                case 2:
                    return Fragment.instantiate(mContext, new FragmentProfileView(UserPreference.getInstance().getUserID(), "yes", true).getClass().getName(), null);

                case 3:
                    return Fragment.instantiate(mContext, FollowingkikrTab.class.getName());
                default:
                    return Fragment.instantiate(mContext, FollowingkikrTab.class.getName());
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
        if (position == 0)
            AppConstants.fragmentFeedTabPosition = 0;

        setFragmentData(position);
//		actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//		getAuthTocken();
//		validateCard();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((FragmentPostUploadTab) mCustomPagerAdapter.getFragment(mViewPager.getCurrentItem())).onActivityResult(UCrop.REQUEST_CROP, -1
                , data);
    }
}

