package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.post_upload.FragmentPostUploadTag;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CallBack;
import com.flatlaylib.bean.Inspiration;

public class RemoveProduct extends Dialog {
    private TextView cancelTextView, okTextView, editcollection;
    private String inspiration_id;
    private String user_id;
    private ProgressBarDialog mProgressBarDialog;
    private Context mContext;
    CallBack callBack;
    String products;
    Inspiration inspiration;
    private FragmentInspirationSection fragmentInspirationSection;
    private FragmentInspirationDetail fragmentInspirationDetail;

    public RemoveProduct(Context context, Inspiration inspiration, FragmentInspirationSection fragmentInspirationSection, CallBack callBack) {
        super(context, R.style.AdvanceDialogTheme);
        this.inspiration = inspiration;
        this.mContext = context;
        this.callBack = callBack;
        this.fragmentInspirationSection = fragmentInspirationSection;
        init();
    }

    public RemoveProduct(Context context, Inspiration inspiration, FragmentInspirationDetail fragmentInspirationDetail, CallBack callBack) {
        super(context, R.style.AdvanceDialogTheme);
        this.inspiration = inspiration;
        this.mContext = context;
        this.callBack = callBack;
        this.fragmentInspirationDetail = fragmentInspirationDetail;
        removeFragmentInspirationDetail();
    }

    private void removeFragmentInspirationDetail() {

        setContentView(R.layout.remove_post_item);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.4f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        cancelTextView = (TextView) findViewById(R.id.cancelTextView);
        okTextView = (TextView) findViewById(R.id.okTextView);
        editcollection = (TextView) findViewById(R.id.editcollection);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        editcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mContext).addFragment(new FragmentPostUploadTag(inspiration));
                dismiss();
            }
        });
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((HomeActivity) mContext).checkInternet()) {
                    fragmentInspirationDetail.removePost(inspiration.getInspiration_id(), inspiration.getUser_id());
                    dismiss();
                } else
                    System.out.print("error");
            }


        });
    }


    protected RemoveProduct(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init() {


        setContentView(R.layout.remove_post_item);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.4f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        cancelTextView = (TextView) findViewById(R.id.cancelTextView);
        okTextView = (TextView) findViewById(R.id.okTextView);
        editcollection = (TextView) findViewById(R.id.editcollection);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        editcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) mContext).addFragment(new FragmentPostUploadTag(inspiration));
                dismiss();
            }
        });
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((HomeActivity) mContext).checkInternet()) {
                    fragmentInspirationSection.removePost(inspiration.getInspiration_id(), inspiration.getUser_id());
                    dismiss();
                } else
                    System.out.print("error");
            }


        });
    }
}