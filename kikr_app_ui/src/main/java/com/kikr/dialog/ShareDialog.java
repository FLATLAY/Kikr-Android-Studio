package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Session;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.sessionstore.SessionStore;
import com.kikr.utility.AppConstants;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.LogoutApi;
import com.kikrlib.bean.Product;
import com.kikrlib.db.DatabaseHelper;
import com.kikrlib.db.HelpPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.db.dao.FavoriteDealsDAO;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;

import net.londatiga.android.instagram.Instagram;
import net.londatiga.android.instagram.InstagramSession;

import io.branch.referral.Branch;

public class ShareDialog extends Dialog{
	private Button pinItButton, otherButton;
	private HomeActivity homeActivity;
	private Context mContext;
	private Product product;

	public ShareDialog(Context mContext, HomeActivity homeActivity,Product product) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.homeActivity = homeActivity;
		this.mContext=mContext;
		this.product = product;
		init();
	}

	public ShareDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_share);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		pinItButton = (Button) findViewById(R.id.pinItButton);
		otherButton = (Button) findViewById(R.id.otherButton);
		pinItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeActivity.checkInternet()) {
                    homeActivity.shareProduct(product, false);
                    dismiss();
                }
            }
        });
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeActivity.checkInternet()) {
                    homeActivity.shareProduct(product,true);
                    dismiss();
                }
            }
        });
    }

	
}
