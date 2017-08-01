package com.flatlay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.UpdateCurrentScreenApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.utils.Syso;

public abstract class BaseActivity extends FragmentActivity {

    protected FragmentActivity context;
    protected DisplayMetrics mDisplayMetrics;

    public abstract void initLayout();

    public abstract void setupData();

    public abstract void headerView();

    public abstract void setUpTextType();

    public abstract void setClickListener();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.w("Activity","BaseActivity");
        context = (BaseActivity) this;
        mDisplayMetrics = new DisplayMetrics();
        addActivity();
    }

    @Override
    public void setContentView(int layoutResID) {

        super.setContentView(R.layout.activity_base);
        System.out.println("In setcontent view");
        FrameLayout layout = (FrameLayout) findViewById(R.id.baseFrameLayout);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(layoutResID, null);
        layout.addView(v);
        initLayout();
        setupData();
        headerView();
        setUpTextType();
        setClickListener();
        setParantClickListener();
        hideFutter();
        hideIcons();
//		setupFontStyle(layout);
    }

    private void hideIcons() {
        ImageView imageView = (ImageView) findViewById(R.id.menuSearchImageView);
        imageView.setVisibility(View.GONE);
        ImageView menuRightImageView = (ImageView) findViewById(R.id.menuRightImageView);
        menuRightImageView.setVisibility(View.GONE);
    }


    public void setContentView(LinearLayout layout) {
        super.setContentView(layout);
        initLayout();
        headerView();
        setupData();
        setUpTextType();
        setClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startActivity(Class activity) {
        Intent i = new Intent(context, activity);
        startActivity(i);
    }

    public void startActivity(Class activity, Bundle bundle) {
        Intent i = new Intent(context, activity);
        if (bundle != null)
            i.putExtras(bundle);
        startActivity(i);
    }

    public void hideHeader() {
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.baseHeader);
        layout.setVisibility(View.GONE);
    }

    public void setTextHeader() {
        RelativeLayout baseLayout = (RelativeLayout) findViewById(R.id.baseHeader);
        RelativeLayout textLayout = (RelativeLayout) findViewById(R.id.textHeader);
        baseLayout.setVisibility(View.GONE);
        textLayout.setVisibility(View.VISIBLE);
    }

    public void setBackHeader() {

        getLeftTextView().setVisibility(View.VISIBLE);
        getRightButton().setVisibility(View.GONE);
        getCartText().setVisibility(View.GONE);
    }

    public TextView getHeaderTextView() {
        return (TextView) findViewById(R.id.headerNameTextView);
    }

    public TextView getCartText() {
        return (TextView) findViewById(R.id.txtCartCount);
    }

    public ImageView getHomeImageView() {
        return (ImageView) findViewById(R.id.homeImageView);
    }

    public ImageView getHeaderImageView() {
        return (ImageView) findViewById(R.id.headerImageView);
    }

    public ImageView getRightButton() {
        return (ImageView) findViewById(R.id.menuRightImageView);
    }

    public TextView getRightTextView() {
        return (TextView) findViewById(R.id.menuRightTextView);
    }

    public TextView getRightText() {
        return (TextView) findViewById(R.id.menuRightText);
    }


    public ImageView getLeftImageView() {
        return (ImageView) findViewById(R.id.backIcon);
    }

    public TextView getLeftTextView() {
        return (TextView) findViewById(R.id.leftTextView);
    }


    protected int getScreenWidth() {
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }


    private void setParantClickListener() {
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        parentLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtility.hideSoftKeyboard(context);
            }
        });
    }

    private void addActivity() {
        try {
            if (HomeActivity.activities != null)
                HomeActivity.activities.add(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToHome() {
        clearAllActivity();
    }

    private void clearAllActivity() {
        CommonUtility.hideSoftKeyboard(BaseActivity.this);
        if (HomeActivity.activities != null) {
            for (int i = 0; i < HomeActivity.activities.size(); i++) {
                if (!HomeActivity.activities.get(i).isFinishing()) {
                    HomeActivity.activities.get(i).finish();
                }
            }
        }
    }

    public void showDataNotFound() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.itemNotFound);
        layout.setVisibility(View.VISIBLE);
        TextView textView = (TextView) findViewById(R.id.noDataFoundTextView);
        textView.setText(getResources().getString(R.string.no_data_found));
    }

    public TextView getDataNotFound() {
        TextView textView = (TextView) findViewById(R.id.noDataFoundTextView);
        textView.setText(Html.fromHtml(getResources().getString(R.string.no_internet)));
        return textView;
    }

    public void hideDataNotFound() {
        try {
            LinearLayout layout = (LinearLayout) findViewById(R.id.itemNotFound);
            if (layout != null)
                layout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextView getReloadFotter() {
        return ((HomeActivity) context).getReloadFotter();
    }

    public void showFooter() {
        LinearLayout footerLayout = (LinearLayout) findViewById(R.id.footer);
        footerLayout.setVisibility(View.VISIBLE);
    }

    public void hideFutter() {
        LinearLayout footerLayout = (LinearLayout) findViewById(R.id.footer);
        footerLayout.setVisibility(View.GONE);
    }

    public void updateScreen(String current_screen) {
        final UpdateCurrentScreenApi updateCurrentScreenApi = new UpdateCurrentScreenApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("response  " + object);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("response  " + object);
            }
        });
        updateCurrentScreenApi.updateScreen(UserPreference.getInstance().getUserID(), current_screen);
        updateCurrentScreenApi.execute();
    }

    public boolean checkInternet() {
        if (CommonUtility.isOnline(context)) {
            return true;
        } else {
            CommonUtility.showNoInternetAlert(context);
            return false;
        }
    }

    public void setGoToHome() {
        getHomeImageView().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDiscover();
            }
        });
    }

    public void showDiscover() {
        finish();
        Intent intent = new Intent(AppConstants.ACTION_GO_TO_HOME);
        sendBroadcast(intent);
    }

    public void setupFontStyle(View view) {
        if (view instanceof EditText) {
            EditText editText = (EditText) view;
            editText.setTypeface(FontUtility.setProximanovaLight(context));
        } else if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTypeface(FontUtility.setProximanovaLight(context));
        } else if (view instanceof Button) {
            Button button = (Button) view;
            button.setTypeface(FontUtility.setProximanovaLight(context));
        }
        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupFontStyle(innerView);
            }
        }
    }


}
