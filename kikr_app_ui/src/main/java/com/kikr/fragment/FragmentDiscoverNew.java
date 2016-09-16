package com.kikr.fragment;

import android.app.ActionBar;
import android.content.Context;
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
import com.kikr.R;
import com.kikr.utility.AppConstants;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.Syso;

import java.util.HashMap;
import java.util.Map;

import static com.kikr.R.id.cart_tab;

public class FragmentDiscoverNew extends BaseFragment implements OnPageChangeListener {

    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    String[] titalName = new String[]{"Trending", "Featured", "Collections"};
    private ActionBar actionBar;
    LinearLayout option1_LinearLayout, option2_LinearLayout, option3_LinearLayout, cart_tab_LinearLayout;
    private boolean isInspiration = false;
    LinearLayout[] optionArray;// = new TextView[]{option1TextView,option2TextVie,option3TextView};
    public static int currentTab = 0;
    public static boolean isCreateCollection;
    HashMap<Integer, String> mFragmentTags = new HashMap<>();
    LinearLayout tabLayout;

    public FragmentDiscoverNew() {
        currentTab = 0;
        isCreateCollection = false;
    }

    public FragmentDiscoverNew(int currenttab, boolean isCreateCollection) {
        this.currentTab = currenttab;

        this.isCreateCollection = isCreateCollection;


    }

    public FragmentDiscoverNew(int currenttab) {
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
        View view = inflater.inflate(R.layout.discover_fragment_new, null);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);


        option1_LinearLayout = (LinearLayout) view.findViewById(R.id.option1);
        option2_LinearLayout = (LinearLayout) view.findViewById(R.id.option2);
        option3_LinearLayout = (LinearLayout) view.findViewById(R.id.option3);
        cart_tab_LinearLayout = (LinearLayout) view.findViewById(cart_tab);
        optionArray = new LinearLayout[]{option1_LinearLayout, option2_LinearLayout, option3_LinearLayout};

        initData();

        return view;
    }

    public void initData() {
        currentTab = AppConstants.fragmentFeedTabPosition;
        mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), (Context) getActivity());
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(currentTab);
        changeIndicator(currentTab);
        setClickListner();

    }

    private void setClickListner() {
        for (int i = 0; i < optionArray.length; i++) {
            optionArray[i].setTag(i);
            optionArray[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    int tag = (Integer) v.getTag();
                    changeIndicator((Integer) v.getTag());
                    mViewPager.setCurrentItem((Integer) v.getTag());
                    // setFragmentData(tag);

                }
            });
        }

        cart_tab_LinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new CartFragmentTab());
            }
        });
    }

    private void setFragmentData(int tag) {
        try {
            if (tag == 0) {
                ((FragmentInspirationSection) mCustomPagerAdapter.getFragment(tag)).initData();

            }
            if (tag == 1) {
                ((FragmentDiscover) mCustomPagerAdapter.getFragment(tag)).initData();

            }
            if (tag == 2) {
                ((FragmentFeatured) mCustomPagerAdapter.getFragment(tag)).initData();

            }

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

                    return Fragment.instantiate(mContext, new FragmentInspirationSection(true, UserPreference.getInstance().getUserID(), true).getClass().getName(), null);


                case 1:
                    return Fragment.instantiate(mContext, new FragmentDiscover().getClass().getName(), null);

                case 2:
                    return Fragment.instantiate(mContext, new FragmentFeatured().getClass().getName(), null);


                default:
                    return Fragment.instantiate(mContext, new FragmentFeatured().getClass().getName(), null);

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
