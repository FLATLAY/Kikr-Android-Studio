package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikrlib.bean.Product;


public class ShareDialog extends Dialog{
	private Button pinItButton, otherButton;
	private HomeActivity homeActivity;
	private Context mContext;
	private Product product;
	private String imageUrl;
	private String shareimagename;

	public ShareDialog(Context mContext, HomeActivity homeActivity,Product product) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.homeActivity = homeActivity;
		this.mContext=mContext;
		this.product = product;
		init();
	}

	public ShareDialog(Context mContext, HomeActivity homeActivity,String imageUrl,String shareimagename) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.homeActivity = homeActivity;
		this.mContext=mContext;
		this.imageUrl = imageUrl;
		this.shareimagename=shareimagename;
		share();
	}
	private void share() {
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
					homeActivity.shareImage(imageUrl, false,shareimagename);
					dismiss();
				}
			}
		});
		otherButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (homeActivity.checkInternet()) {
					homeActivity.shareImage(imageUrl,true,shareimagename);
					dismiss();
				}
			}
		});
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
