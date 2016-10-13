package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;

/**
 * Created by anshumaan on 3/17/2016.
 */
public class AddressVerificationDialog extends Dialog {
    private TextView cancelTextView,okTextView,messageTextView;
    private Context mContext;
    private String msg="";

    public AddressVerificationDialog(Context mContex,String msg) {
        super(mContex, R.style.AdvanceDialogTheme);
        this.mContext=mContext;
        this.msg=msg;
        init();
    }

    public AddressVerificationDialog(Context context, int theme) {
        super(context, R.style.AdvanceDialogTheme);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_logout);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        cancelTextView= (TextView) findViewById(R.id.cancelTextView);
        messageTextView= (TextView) findViewById(R.id.messageTextView);
        messageTextView.setText(msg);
        okTextView = (TextView) findViewById(R.id.okTextView);
        cancelTextView.setVisibility(View.GONE);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
