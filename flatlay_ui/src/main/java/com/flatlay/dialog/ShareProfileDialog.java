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
    private ImageView cancelIcon, twi_icon, pin_icon, fb_icon, ins_icon;


    public ShareProfileDialog(@NonNull Context mContext) {
        super(mContext, R.style.AdvanceDialogTheme);
        setContentView(R.layout.dialog_share_profile);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        otherButton = (TextView) findViewById(R.id.otherButton);
        otherButton.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        title = (TextView) findViewById(R.id.title_text);
        title.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        cancelIcon = (ImageView) findViewById(R.id.cancel_icon);
        cancelIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        ins_icon = (ImageView) findViewById(R.id.ins_icon);
        fb_icon = (ImageView) findViewById(R.id.fb_icon);
        pin_icon = (ImageView) findViewById(R.id.pin_icon);
        twi_icon = (ImageView) findViewById(R.id.twi_icon);
        ins_icon.setImageResource(R.drawable.instagram_black);
        fb_icon.setImageResource(R.drawable.facebooklogoo);
        pin_icon.setImageResource(R.drawable.pinterestlogoo);
        twi_icon.setImageResource(R.drawable.twitterlogoo);
        ins_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ins_icon.setImageResource(R.drawable.instagram_teal);
                fb_icon.setImageResource(R.drawable.facebooklogoo);
                pin_icon.setImageResource(R.drawable.pinterestlogoo);
                twi_icon.setImageResource(R.drawable.twitterlogoo);

            }
        });
        fb_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ins_icon.setImageResource(R.drawable.instagram_black);
                fb_icon.setImageResource(R.drawable.tealfb);
                pin_icon.setImageResource(R.drawable.pinterestlogoo);
                twi_icon.setImageResource(R.drawable.twitterlogoo);

            }
        });
        pin_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ins_icon.setImageResource(R.drawable.instagram_black);
                fb_icon.setImageResource(R.drawable.facebooklogoo);
                pin_icon.setImageResource(R.drawable.tealpinterest);
                twi_icon.setImageResource(R.drawable.twitterlogoo);

            }
        });
        twi_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ins_icon.setImageResource(R.drawable.instagram_black);
                fb_icon.setImageResource(R.drawable.facebooklogoo);
                pin_icon.setImageResource(R.drawable.pinterestlogoo);
                twi_icon.setImageResource(R.drawable.tealtwiter);
            }
        });

    }
}
