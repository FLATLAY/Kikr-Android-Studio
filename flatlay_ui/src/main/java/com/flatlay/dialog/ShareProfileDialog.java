package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.utility.FontUtility;

/**
 * Created by RachelDi on 3/8/18.
 */

public class ShareProfileDialog extends Dialog {
    private Context mContext;
    private TextView otherButton;
    private TextView title;
    private ImageView cancelIcon;


    public ShareProfileDialog(@NonNull Context mContext) {
        super(mContext, R.style.AdvanceDialogTheme);
        setContentView(R.layout.dialog_share_profile);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        otherButton = (TextView) findViewById(R.id.otherButton);
        otherButton.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        title = (TextView) findViewById(R.id.title_text);
        title.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        cancelIcon=(ImageView) findViewById(R.id.cancel_icon);
        cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
