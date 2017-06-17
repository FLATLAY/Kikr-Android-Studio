package com.flatlay.fragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.flatlay.BaseFragment;
import com.flatlay.GalleryImagewithSpinner.MediaGridFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.post_upload.CameraFragment;
import com.flatlay.post_upload.FragmentPostUploadTag;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.bean.ProfileCollectionList;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.soundcloud.android.crop.Crop;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentPostUploadTab extends BaseFragment implements TabListener, OnPageChangeListener {
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    String[] titalName = new String[]{"Gallery", "Camera", "Instagram"};
    public static int tabposition;
    private ActionBar actionBar;
    LinearLayout galleryOption, cameraOption, instagramOption;
    private boolean isImage = false;

    LinearLayout[] optionArray;// = new TextView[]{option1TextView,option2TextVie,option3TextView};
    String inspirationImageUrl;

    private List<ProfileCollectionList> collectionLists = new ArrayList<ProfileCollectionList>();
    FragmentInstagram instagramFragment = null;
    private ProgressBarDialog mProgressBarDialog;
    FrameLayout createCollectionAlert;
    private Button create_my_collection;

    public FragmentPostUploadTab(boolean isInspiration) {
        // this.isInspiration = isInspiration;
    }

    public FragmentPostUploadTab() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Syso.info("uuuuuuuuuuu in onCreateView");
        View view = inflater.inflate(R.layout.fragment_post_upload_main, null);
        mCustomPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), (Context) getActivity());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        createCollectionAlert = (FrameLayout) view.findViewById(R.id.createcollection_alert);
        create_my_collection = (Button) view.findViewById(R.id.btnCreateMyCollection);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mCustomPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
        galleryOption = (LinearLayout) view.findViewById(R.id.galleryOption);
        cameraOption = (LinearLayout) view.findViewById(R.id.cameraOption);
        instagramOption = (LinearLayout) view.findViewById(R.id.instagramOption);
        optionArray = new LinearLayout[]{galleryOption, cameraOption, instagramOption};

        mViewPager.setCurrentItem(0);

        changeIndicator(0);
        setClickListner();



        return view;
    }

    private void setClickListner() {
        Log.w("my-App","setClickListner");
        for (int i = 0; i < optionArray.length; i++) {
            optionArray[i].setTag(i);
            // mViewPager.setCurrentItem(optionArray.(2));
            mViewPager.setCurrentItem(1);
            optionArray[i].setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    changeIndicator((Integer) v.getTag());

                    mViewPager.setCurrentItem((Integer) v.getTag());
                    //setFragmentData((Integer) v.getTag());
                }
            });
        }
        create_my_collection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new MainFragmentContainer(1, true));
            }
        });
    }

    protected void changeIndicator(int tag) {
//        for (int i = 0; i < optionArray.length; i++)
//        {
//
        Log.w("my-App","changeIndicator()");
        tabposition = tag;
        if (tag == 0) {
            optionArray[0].setBackgroundColor(getResources().getColor(R.color.tab_color));
            optionArray[1].setBackgroundColor(getResources().getColor(R.color.white));
            optionArray[2].setBackgroundColor(getResources().getColor(R.color.white));
            HomeActivity.menuTextCartCount.setVisibility(View.INVISIBLE);
            HomeActivity.menuRightImageView.setVisibility(View.INVISIBLE);
            HomeActivity.homeImageView.setVisibility(View.GONE);
            HomeActivity.photouploadnext.setVisibility(View.GONE);
            HomeActivity.uploadphoto.setVisibility(View.GONE);
            HomeActivity.camera.setVisibility(View.GONE);
            HomeActivity.mstatus.setVisibility(View.VISIBLE);
            // HomeActivity.gallery.setVisibility(View.VISIBLE);
            HomeActivity.menuBackTextView.setVisibility(View.GONE);
            HomeActivity.instagram.setVisibility(View.GONE);
            HomeActivity.crossarrow.setVisibility(View.VISIBLE);

        } else if ((tag == 1)) {
            optionArray[0].setBackgroundColor(getResources().getColor(R.color.white));
            optionArray[1].setBackgroundColor(getResources().getColor(R.color.tab_color));
            optionArray[2].setBackgroundColor(getResources().getColor(R.color.white));
            HomeActivity.menuTextCartCount.setVisibility(View.INVISIBLE);
            HomeActivity.menuRightImageView.setVisibility(View.INVISIBLE);
            HomeActivity.photouploadnext.setVisibility(View.GONE);
            HomeActivity.uploadphoto.setVisibility(View.GONE);
            HomeActivity.camera.setVisibility(View.VISIBLE);
            HomeActivity.crossarrow.setVisibility(View.VISIBLE);
            HomeActivity.homeImageView.setVisibility(View.GONE);
            HomeActivity.mstatus.setVisibility(View.GONE);
            HomeActivity.gallery.setVisibility(View.GONE);
            HomeActivity.instagram.setVisibility(View.GONE);
            HomeActivity.menuBackTextView.setVisibility(View.GONE);
        } else if ((tag == 2)) {
            int k = 60;
            // optionArray[0].setImageResource(R.drawable.galleryselect);


            optionArray[0].setBackgroundColor(getResources().getColor(R.color.white));
            optionArray[1].setBackgroundColor(getResources().getColor(R.color.white));
            optionArray[2].setBackgroundColor(getResources().getColor(R.color.tab_color));

//                optionArray[0].getLayoutParams().height = 80;
//                optionArray[1].setImageResource(R.drawable.cameratab);
//                optionArray[2].setImageResource(R.drawable.instagrampic);

            HomeActivity.menuTextCartCount.setVisibility(View.GONE);
            HomeActivity.menuRightImageView.setVisibility(View.GONE);
            HomeActivity.photouploadnext.setVisibility(View.GONE);
            HomeActivity.uploadphoto.setVisibility(View.GONE);
            HomeActivity.gallery.setVisibility(View.GONE);
            HomeActivity.mstatus.setVisibility(View.GONE);
            HomeActivity.crossarrow.setVisibility(View.VISIBLE);
            HomeActivity.camera.setVisibility(View.GONE);
            HomeActivity.menuBackTextView.setVisibility(View.GONE);
            HomeActivity.homeImageView.setVisibility(View.GONE);
            HomeActivity.instagram.setVisibility(View.VISIBLE);

//            if((instagramFragment.userLoggedIn == false) &&(instagramFragment.isLoading == false))
//            {
//                instagramFragment.loginToInstagram(120, true);
//            }
            // ((FragmentInstagram)mCustomPagerAdapter.getItem(tag)).setData1(null);
        }
        // }
    }

    private void setFragmentData(int tag) {
        try {

            if (tag == 2) {
                //if((instagramFragment.userLoggedIn == false) &&(instagramFragment.isLoading == false))
                ((FragmentInstagram) mCustomPagerAdapter.getFragment(tag)).loginToInstagram(120, true);
                // FragmentInspirationSection.inspirationSection.initData();
            }
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        HomeActivity.menuTextCartCount.setVisibility(View.GONE);
        HomeActivity.menuRightImageView.setVisibility(View.GONE);
        // setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.menuTextCartCount.setVisibility(View.GONE);
        HomeActivity.menuRightImageView.setVisibility(View.GONE);
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
    }

    @Override
    public void setData(Bundle bundle) {

        //getCollectionCount();

    }

    private int getCollectionCount() {
        try {
            mProgressBarDialog = new ProgressBarDialog(mContext);
            mProgressBarDialog.show();
            final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {

                @Override
                public void handleOnSuccess(Object object) {
                    if (mProgressBarDialog.isShowing())
                        mProgressBarDialog.dismiss();
                    Syso.info("In handleOnSuccess>>" + object);
                    MyProfileRes myProfileRes = (MyProfileRes) object;
                    collectionLists = myProfileRes.getCollection_list();
                    if (collectionLists.size() > 0)
                        createCollectionAlert.setVisibility(View.GONE);
                    else {
                        createCollectionAlert.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {

                    mProgressBarDialog = new ProgressBarDialog(mContext);
                    mProgressBarDialog.show();
                    Syso.info("In handleOnFailure>>" + object);
                    if (object != null) {
                        MyProfileRes response = (MyProfileRes) object;
                        AlertUtils.showToast(mContext, response.getMessage());
                    } else {
                        AlertUtils.showToast(mContext, R.string.invalid_response);
                    }
                }
            });
            myProfileApi.getUserProfileDetail(UserPreference.getInstance().getUserID(), UserPreference.getInstance().getUserID());
            myProfileApi.execute();
        } catch (Exception ex) {
            return 0;
        }
        return collectionLists.size();
    }

    @Override
    public void refreshData(Bundle bundle) {
    }

    @Override
    public void setClickListener() {
    }

    class CustomPagerAdapter extends FragmentPagerAdapter {

        Context mContext;
        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }


        @Override
        public Fragment getItem(int position) {
            Syso.info("uuuuuuuuuuu in getItem");

            switch (position) {
                case 0: {
                    // return new MediaGridFragment();
                    return Fragment.instantiate(mContext, MediaGridFragment.class.getName(), null);
                }


                case 1: {
                    // return new CameraFragment();
                    //Log.w("test","testing3");
                    return Fragment.instantiate(mContext, CameraFragment.class.getName(), null);

                }


                case 2: {

//                    if (instagramFragment == null) {
//                        instagramFragment = FragmentInstagram.newInstance("param1", "param2");
//
//                    } else {
//                        instagramFragment.loginToInstagram(120,true);
//                        //getInstagramList(120, true);
//                        System.out.print("test");
//                    }
//
//                    return instagramFragment;

                    return Fragment.instantiate(mContext, FragmentInstagram.class.getName(), null);
                }


                default: {
                    return new MediaGridFragment();
                }
            }

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                // record the fragment tag here.
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null)
                return null;
            return mFragmentManager.findFragmentByTag(tag);
        }

        @Override
        public int getCount() {
            return titalName.length;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titalName[position];
//        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Syso.info("uuuuuuuuuuu in onDestroy");

    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        //setFragmentData(tab.getPosition());
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        setFragmentData(position);
        changeIndicator(position);
//		actionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//		getAuthTocken();
//		validateCard();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("resu and req code", requestCode + " -- " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resu and req code", requestCode + " -- " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            Log.e("req code", requestCode + "");
            if (requestCode == UCrop.REQUEST_CROP) {

                //Log.w("onActivityResultFPUT","REQUEST_CROP");

                String filePath = Environment.getExternalStorageDirectory()
                        + "/temporary_holder.jpg";

                //Log.w("onActivityResultFPUT","****1"+filePath);
                Bitmap thumbnail = BitmapFactory.decodeFile(filePath);
                //Log.w("onActivityResultFPUT","****2");

                inspirationImageUrl = filePath;
                goToNext(thumbnail);
                //Log.w("onActivityResultFPUT","****3");


            } else if (requestCode == Crop.REQUEST_PICK) {
                //Log.w("onActivityResultFPUT","REQUEST_PICK");
                Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
                Crop crop = new Crop(data.getData());
                crop.output(destination).asSquare().start(mContext);
            }
        }
    }

    public void goToNext(Bitmap thumbnail) {

        if (thumbnail != null || inspirationImageUrl != null) {
            addFragment(new FragmentPostUploadTag(thumbnail, inspirationImageUrl, String.valueOf(isImage)));
        } else {
            AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
        }

//            if (byteArray!=null) {
//                addFragment(new FragmentInspirationPost(byteArray,isImage,filePath));
//            } else {
//                AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
//            }
    }

    public Bitmap createScaledImage(File mFile) {

        Bitmap bitmap = null;
        ExifInterface exif;
        try {
            Syso.info("1111====" + mFile.getPath());
            exif = new ExifInterface(mFile.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix mat = new Matrix();
            mat.postRotate(angle);
            BitmapFactory.Options options = new BitmapFactory.Options();
//			options.inJustDecodeBounds=false;
            options.inSampleSize = calculateInSampleSize2(options, 640, 640);
            Syso.info("2222====" + mFile.getPath());
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(mFile), null, options);

            Syso.info("123456789  2222==== Width:" + bmp.getWidth() + ", Height:" + bmp.getHeight() + ", ww:" + options.outWidth + ", hh:" + options.outHeight);
            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), mat, true);
            Syso.info("123456789  2222==== Width:" + bitmap.getWidth() + ", Height:" + bitmap.getHeight());
            Syso.info("====" + bitmap);
        } catch (Exception e) {
            Syso.error(e);
        }
        if (bitmap != null)
            return bitmap;
        else
            return null;
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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Log.w("test","testing1");
        ((CameraFragment) (mCustomPagerAdapter.getFragment(mViewPager.getCurrentItem()))).onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}