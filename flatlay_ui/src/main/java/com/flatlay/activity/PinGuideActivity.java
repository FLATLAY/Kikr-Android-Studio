package com.flatlay.activity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.BaseActivityWithVideo;
import com.flatlay.R;
import com.flatlay.dialog.HelpPressMenuDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;

/**
 * Created by RachelDi on 1/17/18.
 */

public class PinGuideActivity extends BaseActivity implements View.OnClickListener {
    //ImageView circleImages;
    private Button next;
    private TextView pinDescription,title;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        CommonUtility.noTitleActivity(context);
        setContentView(R.layout.pinguide);
        hideHeader();
        HelpPressMenuDialog helpPressMenuDialog = new HelpPressMenuDialog(this);
        //helpPressMenuDialog.setContentView(R.layout.helpscreen_presshere_menu);
        Window dialogWindow = helpPressMenuDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        //lp.alpha = 0.7f;
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels);
       // dialogWindow.setAttributes(lp);
        helpPressMenuDialog.show();
    }


    @Override
    public void initLayout() {
       // circleImages = (ImageView) findViewById(R.id.circleImages);
        title = (TextView) findViewById(R.id.pintitle);
        next = (Button) findViewById(R.id.nextButton);
        pinDescription = (TextView) findViewById(R.id.pinDescription);
    }

    @Override
    public void setupData() {

    }

    @Override
    public void headerView() {

    }

    @Override
    public void setUpTextType() {
        title.setTypeface(FontUtility.setMontserratRegular(this));
        next.setTypeface(FontUtility.setMontserratLight(this));
        pinDescription.setTypeface(FontUtility.setMontserratLight(this));
    }

    @Override
    public void setClickListener() {
        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.nextButton)
            startActivity(BaseActivityWithVideo.class);
    }
}