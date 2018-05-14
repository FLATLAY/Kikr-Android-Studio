package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.CardGridAdapter;
import com.flatlay.fragment.FragmentDiscoverNew;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.service.PlaceOrderService;
import com.flatlay.utility.FontUtility;

/**
 * Created by RachelDi on 4/6/18.
 */

public class OrderProcessingDialog extends Dialog {
    private TextView sure_text, yes_text, cancel_text;
    private FragmentActivity mContext;
    private String message;
    private String purchaseId;
    private String cartId;
    private MyListener listener;


    public OrderProcessingDialog(FragmentActivity mContext, String message, MyListener listener) {
        super(mContext, R.style.AdvanceDialogTheme);
        this.mContext = mContext;
        this.message = message;
        this.listener = listener;
        init();
    }

    public OrderProcessingDialog(FragmentActivity context, int theme) {
        super(context, R.style.AdvanceDialogTheme);
        init();
    }

    private void init() {
        setContentView(R.layout.confirm_delete_dialog);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        cancel_text = (TextView) findViewById(R.id.cancel_text);
        cancel_text.setTypeface(FontUtility.setMontserratLight(mContext));
        sure_text = (TextView) findViewById(R.id.sure_text);
        sure_text.setTypeface(FontUtility.setMontserratLight(mContext));
        sure_text.setText(message);
        yes_text = (TextView) findViewById(R.id.yes_text);
        yes_text.setTypeface(FontUtility.setMontserratLight(mContext));
        cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        yes_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickButton();
                dismiss();
            }
        });
    }

    public interface MyListener { // create an interface

        void onClickButton();
    }
}

