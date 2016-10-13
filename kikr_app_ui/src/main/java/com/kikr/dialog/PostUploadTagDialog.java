package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;

public class PostUploadTagDialog extends Dialog{
    private TextView cancelTextView,okTextView;
    private HomeActivity homeActivity;
    private Context mContext;

    public PostUploadTagDialog( HomeActivity homeActivity) {
        super(homeActivity, R.style.AdvanceDialogTheme);
        //   super(mContext, R.style.AdvanceDialogTheme);
        this.homeActivity = homeActivity;
        this.mContext=mContext;
        init();
    }

    public PostUploadTagDialog(Context context, int theme) {
        super(context, R.style.AdvanceDialogTheme);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_postuploadtag_back);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        cancelTextView= (TextView) findViewById(R.id.cancelTextView);
        okTextView = (TextView) findViewById(R.id.okTextView);
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
              homeActivity.backpostuploadtad();

            }
        });
        cancelTextView = (TextView) findViewById(R.id.cancelTextView);

        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
