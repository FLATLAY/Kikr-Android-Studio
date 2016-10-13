package com.kikr.dialog;

import android.app.Dialog;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;


public class RedirectToPlayStore extends Dialog {
    private TextView cancelTextView, okTextView,messageTextView;

    private FragmentActivity mContext;
    String ACTION;

    public RedirectToPlayStore(FragmentActivity homeActivity, String action) {
        super(homeActivity, R.style.AdvanceDialogTheme);
        this.ACTION = action;
        this.mContext = homeActivity;
        init();
    }


    private void init() {
        setContentView(R.layout.redirect_to_play_store);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        cancelTextView = (TextView) findViewById(R.id.cancelTextView);
        okTextView = (TextView) findViewById(R.id.okTextView);
        messageTextView=(TextView)findViewById(R.id.messageTextView);
        if(ACTION.equals("instagram"))
            messageTextView.setText("Instagram is not installed on your device. Would you like to install it now in order to post?");
        if(ACTION.equals("pinterest"))
            messageTextView.setText("Pinterest is not installed on your device. Would you like to install it now in order to post?");
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((HomeActivity) mContext).checkInternet()) {
                    if(ACTION.equals("instagram")) {
                        final String appPackageName = "com.instagram.android"; // getPackageName() from Context or Activity object
                        try {
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                    if(ACTION.equals("pinterest")) {
                        final String appPackageName = "com.pinterest"; // getPackageName() from Context or Activity object
                        try {
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }

                }
                    dismiss();
            }
        });
    }


}

