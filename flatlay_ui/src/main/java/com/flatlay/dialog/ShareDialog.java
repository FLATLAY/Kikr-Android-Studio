package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.utils.AlertUtils;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.BranchShortLinkBuilder;


public class ShareDialog extends Dialog {
    private Button otherButton;
    private HomeActivity homeActivity;
    private Context mContext;
    private Product product;
    private String imageUrl;
    private TextView cancelicon;
    private Inspiration inspiration;
    private String shareimagename;
    public static final String TAG = "ShareDialog";

    public ShareDialog(Context mContext, HomeActivity homeActivity, String imageUrl, String shareimagename) {
        super(mContext, R.style.AdvanceDialogTheme);
        this.homeActivity = homeActivity;
        this.mContext = mContext;
        this.imageUrl = imageUrl;
        this.shareimagename = shareimagename;
        share();
    }

    public ShareDialog(Context mContext, String imageUrl, String shareimagename) {
        super(mContext, R.style.AdvanceDialogTheme);
        this.mContext = mContext;
        this.imageUrl = imageUrl;
        this.shareimagename = shareimagename;
        share();
    }

    private void share() {
        setContentView(R.layout.dialog_share);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        otherButton = (Button) findViewById(R.id.otherButton);
        otherButton.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        cancelicon = (TextView) findViewById(R.id.cancellayout);
        cancelicon.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternet()) {
                    shareImage(imageUrl, true, shareimagename);
                    dismiss();
                }
            }
        });

        cancelicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public boolean checkInternet() {
        if (CommonUtility.isOnline(mContext)) {
            return true;
        } else {
            CommonUtility.showNoInternetAlert(mContext);
            return false;
        }
    }

    public void shareImage(final String imageUrl, final boolean isOther, final String shareimagename) {
        AlertUtils.showToast(mContext, "Please wait...");
        BranchShortLinkBuilder shortUrlBuilder = new BranchShortLinkBuilder(mContext)

                .addParameters("product_url", imageUrl);
        Log.w(TAG, "CREATING BRANCH LINK!" + imageUrl);
        Log.w(TAG, "CREATING BRANCH LINK!" + shareimagename);

        // Get URL Asynchronously
        shortUrlBuilder.generateShortUrl(new Branch.BranchLinkCreateListener() {

            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error != null) {
                    Log.e("Branch Error", "Branch create short url failed. Caused by -" + error.getMessage());
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    String link = AppConstants.SHARE_MESSAGE + " " + AppConstants.APP_LINK + "\nItem: ";
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    if (isOther)
                        mContext.startActivity(Intent.createChooser(intent, "Share"));
                    else {
                    }
                } else {
                    Log.i("Branch", "Got a Branch URL 3 " + url);
                    Log.w("shareimagename", "" + shareimagename);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String link = shareimagename;
                    intent.putExtra(Intent.EXTRA_TEXT, link);
                    if (isOther) {
                        Log.w("inside if", "inside if" + link);
                        mContext.startActivity(Intent.createChooser(intent, "Share"));
                    } else {
                        Log.w("inside else", "inside else");
                        Log.w("inside else", "" + link);
                        Log.w("inside else", "" + url);
                        Log.w("inside else", "" + imageUrl);
                    }
                }
            }
        });
    }

}
