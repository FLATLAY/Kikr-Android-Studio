package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;

public class DeleteCommentDialog extends Dialog{
    private TextView messageTextView,cancelTextView,okTextView;
    private FragmentActivity mContext;
    private FragmentInspirationDetail fragmentInspirationDetail;
    private String commentId;

    public DeleteCommentDialog(FragmentActivity mContext,FragmentInspirationDetail fragmentInspirationDetail, String commentId) {
        super(mContext, R.style.AdvanceDialogTheme);
        this.mContext=mContext;
        this.commentId = commentId;
        this.fragmentInspirationDetail = fragmentInspirationDetail;
        init();
    }

    public DeleteCommentDialog(Context context, int theme) {
        super(context, R.style.AdvanceDialogTheme);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_delete_comment);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        messageTextView= (TextView) findViewById(R.id.messageTextView);
        messageTextView.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        cancelTextView= (TextView) findViewById(R.id.cancelTextView);
        cancelTextView.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        okTextView = (TextView) findViewById(R.id.okTextView);
        okTextView.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternet()){
                    dismiss();
                    fragmentInspirationDetail.removeComment(commentId);
                }
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


}