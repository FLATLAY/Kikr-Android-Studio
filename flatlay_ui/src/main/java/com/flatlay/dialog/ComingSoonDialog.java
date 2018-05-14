package com.flatlay.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.utility.FontUtility;

/**
 * Created by RachelDi on 4/8/18.
 */

public class ComingSoonDialog extends Dialog {
    private TextView sure_text, ok_text;
    private FragmentActivity mContext;

    public ComingSoonDialog(FragmentActivity mContext) {
        super(mContext, R.style.AdvanceDialogTheme);
        this.mContext = mContext;
        init();
    }

    public ComingSoonDialog(FragmentActivity context, int theme) {
        super(context, R.style.AdvanceDialogTheme);
        init();
    }

    private void init() {
        setContentView(R.layout.coming_soon_dialog);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        sure_text = (TextView) findViewById(R.id.sure_text);
        sure_text.setTypeface(FontUtility.setMontserratLight(mContext));
        ok_text = (TextView) findViewById(R.id.ok_text);
        ok_text.setTypeface(FontUtility.setMontserratLight(mContext));
        ok_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }
}


