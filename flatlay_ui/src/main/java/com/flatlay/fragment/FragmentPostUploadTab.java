package com.flatlay.fragment;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.GalleryImagewithSpinner.MediaGridFragment;
import com.flatlay.R;
import com.flatlay.post_upload.*;
import com.flatlay.post_upload.CameraFragment;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentBottom;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.nineoldandroids.view.ViewHelper;
import com.soundcloud.android.crop.Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by RachelDi on 3/30/18.
 */

public class FragmentPostUploadTab extends BaseFragment implements View.OnClickListener {

    final static String TAG = "PostUploadTab";
    private TextView text;
    private ImageView gallery_icon, ins_icon;
    private RelativeLayout relativeLayout, gallery_layout;
    private View mainView;
    private LinearLayout camera_layout;
    String inspirationImageUrl;
    private boolean isImage = false;
    private CameraFragment cameraFragment = new CameraFragment();
    private MediaGridFragment mediaGridFragment = new MediaGridFragment();
    private FragmentInstagram fragmentInstagram;
    private FrameLayout frame_container4;
    private GestureDetector mDetector;


    public FragmentPostUploadTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e(TAG, "PostUploadTab");
        mainView = inflater.inflate(R.layout.fragment_post_upload_main, null);
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        text = (TextView) mainView.findViewById(R.id.text);
        camera_layout = (LinearLayout) mainView.findViewById(R.id.camera_layout);
        text.setTypeface(FontUtility.setMontserratLight(mContext));
        gallery_icon = (ImageView) mainView.findViewById(R.id.gallery_icon);
        ins_icon = (ImageView) mainView.findViewById(R.id.ins_icon);
        relativeLayout = (RelativeLayout) mainView.findViewById(R.id.relativeLayout);
        gallery_layout = (RelativeLayout) mainView.findViewById(R.id.gallery_layout);
        mDetector = new GestureDetector(mContext, new MyListener());
        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // pass the events to the gesture detector
                // a return value of true means the detector is handling it
                // a return value of false means the detector didn't
                // recognize the event
                mDetector.onTouchEvent(event);
                return true;
            }
        };
        gallery_layout.setOnTouchListener(touchListener);
        frame_container4 = (FrameLayout) mainView.findViewById(R.id.frame_container4);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (CommonUtility.getDeviceHeight(mContext) * 37 / 100));
        layoutParams.setMargins(0, 90, 0, 40);
        frame_container4.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (CommonUtility.getDeviceHeight(mContext) / 2));
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        gallery_layout.setLayoutParams(layoutParams2);
        myAddFragment(mediaGridFragment);
    }

    public void myAddFragment(Fragment fragment) {
        FragmentTransaction transaction = mContext.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container4, fragment, null);
        transaction.commit();

    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        relativeLayout.setOnClickListener(this);
        gallery_icon.setOnClickListener(this);
        ins_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relativeLayout:
                break;
            case R.id.gallery_icon:
                myAddFragment(mediaGridFragment);

                break;
            case R.id.ins_icon:
                fragmentInstagram = new FragmentInstagram();
                myAddFragment(fragmentInstagram);

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public int calculateInSampleSize2(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Syso.info("12345678 inSampleSize:" + inSampleSize + ", height:" + height + ", width:" + width);
        return inSampleSize;
    }

    public void goToNext(Bitmap thumbnail) {
        Log.w("FragmentPUTag", "gotoNext");
        if (thumbnail != null || inspirationImageUrl != null) {
            addFragment(new FragmentPostUploadTag(thumbnail, inspirationImageUrl, String.valueOf(isImage)));
        } else {
            AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("resu and req code", requestCode + " -- " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resu and req code", requestCode + " -- " + resultCode);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.w("onActivityResultFPUT", "1");
                String filePath = Environment.getExternalStorageDirectory()
                        + "/temporary_holder.jpg";
                Bitmap thumbnail = BitmapFactory.decodeFile(filePath);
                inspirationImageUrl = filePath;
                goToNext(thumbnail);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (resultCode == RESULT_OK) {

            Log.e("req code", requestCode + "");
            if (requestCode == UCrop.REQUEST_CROP) {
                String filePath = Environment.getExternalStorageDirectory()
                        + "/temporary_holder.jpg";

                Log.w("onActivityResultFPUT", "****FPUT: " + filePath);

                Bitmap thumbnail = BitmapFactory.decodeFile(filePath);

                inspirationImageUrl = filePath;
                goToNext(thumbnail);


            } else if (requestCode == Crop.REQUEST_PICK) {
                Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
                Crop crop = new Crop(data.getData());
                crop.output(destination).asSquare().start(mContext); //Check the Max Size
            }
        }
    }

    private class MyListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float distanceX, float distanceY) {

            ObjectAnimator objectAnimator;
            if (motionEvent.getRawY() < motionEvent2.getRawY()) {
                objectAnimator = ObjectAnimator.ofFloat(gallery_layout, "translationY", (CommonUtility.getDeviceHeight(mContext)));
                objectAnimator.setInterpolator(new LinearOutSlowInInterpolator());
                objectAnimator.setDuration(800);
                objectAnimator.start();
            } else {

            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float velocityX, float velocityY) {

            ObjectAnimator objectAnimator;
            if (motionEvent.getRawY() < motionEvent2.getRawY()) {
                objectAnimator = ObjectAnimator.ofFloat(gallery_layout, "translationY", (CommonUtility.getDeviceHeight(mContext)));
                objectAnimator.setInterpolator(new LinearOutSlowInInterpolator());
                objectAnimator.setDuration(800);
                objectAnimator.start();
            } else {
            }
            return true;
        }
    }


}
