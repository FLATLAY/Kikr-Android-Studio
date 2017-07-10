package com.flatlay.fragment;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlaylib.utils.Syso;

import java.util.HashMap;
import java.util.Map;


public class CartFragmentTab extends BaseFragment implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    String[] titalName = new String[]{"cart", "cards", "credits", "orders"};
    private ActionBar actionBar;
    LinearLayout cart, cards, credits, orders;
    private boolean isInspiration = false;
    LinearLayout[] optionArray;// = new TextView[]{option1TextView,option2TextVie,option3TextView};
    int currentTab = 0;
    public static boolean isCreateCollection;
    HashMap<Integer, String> mFragmentTags = new HashMap<>();
boolean isFromOutside;
   public static boolean isordered =false;

    public CartFragmentTab() {
        currentTab = 0;
        isCreateCollection = false;
        this.isFromOutside=false;
        isordered=false;
    }

    public CartFragmentTab(int currenttab, boolean isCreateCollection) {
        this.currentTab = currenttab;

        this.isCreateCollection = isCreateCollection;


    }

    public CartFragmentTab(int currenttab) {
        this.currentTab = currenttab;
        if (currenttab == 2)
            isCreateCollection = true;
        else
            isCreateCollection = false;
    }

    public CartFragmentTab(boolean b) {
       this.isordered=b;
        currentTab=3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Syso.info("uuuuuuuuuuu in onCreateView");
        View view = inflater.inflate(R.layout.fragment_kikr_cart_tab, null);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        // mViewPager.setOffscreenPageLimit(1);

        mViewPager.setOnPageChangeListener(this);
        cards = (LinearLayout) view.findViewById(R.id.cards);
        credits = (LinearLayout) view.findViewById(R.id.credits);
        orders = (LinearLayout) view.findViewById(R.id.orders);
        cart = (LinearLayout) view.findViewById(R.id.cart);

        optionArray = new LinearLayout[]{cart, cards, credits, orders};
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
    }

    private void setFragmentData(int tag) {
        try {
            if (tag == 0)
                ((FragmentUserCart) mCustomPagerAdapter.getFragment(tag)).initData();
            if (tag == 1)
                ((FragmentKikrWalletCard) mCustomPagerAdapter.getFragment(tag)).initData();
            if (tag == 2) {
                ((FragmentKikrCreditsScreen) mCustomPagerAdapter.getFragment(tag)).initData();
                // FragmentInspirationSection.inspirationSection.initData();
            }
            if (tag == 3)
                ((FragmentAllOrders) mCustomPagerAdapter.getFragment(tag)).initData();
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }

    protected void changeIndicator(int tag) {
        for (int i = 0; i < optionArray.length; i++) {
            if (tag == i) {
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    optionArray[i].setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                } else {
                    optionArray[i].setBackgroundColor(getResources().getColor(R.color.tab_selected_new));
                }

                // optionArray[i].setTextColor(getResources().getColor(R.color.btn_green));
            } else {
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
         //   HomeActivity.menuTextCartCount.setVisibility(View.INVISIBLE);
            Syso.info("uuuuuuuuuuu in getItem");
            switch (position) {
                case 0:
                    return Fragment.instantiate(mContext, new FragmentUserCart( true).getClass().getName(), null);

                // return new FragmentFeatured();

                case 1:
                    return Fragment.instantiate(mContext, FragmentKikrWalletCard.class.getName(), null);
                //return new FragmentDiscover();
                case 2:
                    return Fragment.instantiate(mContext, FragmentKikrCreditsScreen.class.getName(), null);
                case 3:
                    return Fragment.instantiate(mContext, FragmentAllOrders.class.getName(), null);
                  //  return Fragment.instantiate(mContext, FragmentAllOrders.class.getName(), null);

                default:
                    return Fragment.instantiate(mContext, FragmentAllOrders.class.getName(), null);
//                    if(isordered==true)
//                        return Fragment.instantiate(mContext, new FragmentAllOrders(true).getClass().getName(), null);
//                        else
//                    return Fragment.instantiate(mContext, FragmentUserCart.class.getName(), null);
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
         setFragmentData(tab.getPosition());
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