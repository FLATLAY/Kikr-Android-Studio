package com.kikr.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.LifestyleImageAdapter;
import com.kikr.model.InstagramImage;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.InstagramCallBack;
import com.kikr.utility.InstagramUtility;
import com.kikrlib.api.EditProfileApi;
import com.kikrlib.bean.BgImage;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.EditProfileRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FragmentInstagram extends ParentFragment implements AdapterView.OnItemClickListener, ServiceCallback {
    GridView imagesList;

    private ProgressBarDialog mProgressBarDialog;
    private LifestyleImageAdapter lifestyleImageAdapter;
    InstagramUtility instagramUtility;
    SharedPreferences sharedPreferences;
    //	List<BgImage> bgImages = new ArrayList<BgImage>();
    List<String> bgImageList = new ArrayList<String>();
    ArrayList<InstagramImage> postImages = new ArrayList<>();
    int count = 120;
    View view;
    Button intstagramlogin;
    boolean isLoadDefault = true;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Bitmap bitmapImg;
    public static int login = 0;
    //Image_Layout_Quiz image_layout_quiz;

    public FragmentInstagram() {
        // Required empty public constructor
    }


    public static FragmentInstagram newInstance(String param1, String param2) {

        FragmentInstagram fragment = new FragmentInstagram();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //CommonUtility.noTitleActivity(mContext);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.activity_default_lifestyle_images, container, false);
        return view;
    }


    public void getInstagramList(int count, boolean isShowLoader) {
        super.getInstagramList(count, isShowLoader);
//        isLoading = true;
//      instagramUtility = new InstagramUtility(mContext, false, count + "", isShowLoader, new InstagramCallBack() {
//            @Override
//            public void setProfilePic(String url) {
//
//            }
//
//            @Override
//            public void setPictureList(ArrayList<String> photoList) {
//                if (photoList.size() == 10)
//                    isLoading = false;
//                bgImageList.addAll(photoList);
//                if (lifestyleImageAdapter == null) {
//                    lifestyleImageAdapter = new LifestyleImageAdapter(mContext, bgImageList);
//                    imagesList.setAdapter(lifestyleImageAdapter);
//                } else {
//                    lifestyleImageAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//       instagramUtility.inItInstgram();
    }

    public void loginToInstagram(int count, boolean isShowLoader) {

        isLoading = true;
        postImages.clear();
        bgImageList.clear();

        // if(instagramUtility == null) {
        instagramUtility = new InstagramUtility(mContext, false, count + "", isShowLoader, new InstagramCallBack() {
            @Override
            public void setProfilePic(String url) {
                userLoggedIn = true;
                isLoading = false;
            }

            @Override
            public void setPictureList(ArrayList<String> photoList) {

            }

            @Override
            public void setPictureListPost(ArrayList<InstagramImage> photoList) {
                if (photoList.size() == 10)
                    isLoading = false;

                postImages.addAll(photoList);
                for (InstagramImage image : postImages)
                    bgImageList.add(image.getThumbnail_url());
                if (lifestyleImageAdapter == null) {
                    lifestyleImageAdapter = new LifestyleImageAdapter(mContext, bgImageList);
                    imagesList.setAdapter(lifestyleImageAdapter);
                } else {
                    lifestyleImageAdapter.notifyDataSetChanged();
                }

                userLoggedIn = true;
            }
        });

        instagramUtility.inItInstgram();
        //instagramUtility.login();
        //}


    }


    private void getBgList() {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        final EditProfileApi listApi = new EditProfileApi(this);
        listApi.getBgImageUrlList(UserPreference.getInstance().getUserID());
        listApi.execute();
        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                listApi.cancel();
            }
        });
    }

    boolean isLoading = false;


    public void handleOnSuccess(Object object) {
        mProgressBarDialog.dismiss();
        Syso.info("In handleOnSuccess>>" + object);
        EditProfileRes editProfileRes = (EditProfileRes) object;
        List<BgImage> bgImages = editProfileRes.getData();
//		List<String> bgImages = new ArrayList<String>();
        if (bgImages.size() > 0) {
            bgImageList = getImageList(bgImages);
            lifestyleImageAdapter = new LifestyleImageAdapter(mContext, bgImageList);
            imagesList.setAdapter(lifestyleImageAdapter);
        }
    }

    private List<String> getImageList(List<BgImage> bgImages2) {
        List<String> bgImages = new ArrayList<String>();
        for (BgImage bgImage : bgImages2) {
            bgImages.add(bgImage.getBackground_pic());
        }
        return bgImages;
    }


    public void handleOnFailure(ServiceException exception, Object object) {
        mProgressBarDialog.dismiss();
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            EditProfileRes response = (EditProfileRes) object;
            AlertUtils.showToast(mContext, response.getMessage());
        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }


    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        new DownloadImage().execute(postImages.get(arg2).getHigh_resolution_url());
//        Uri picUri = Uri.fromFile(new File(bgImageList.get(arg2)));
//
//
//        if (Build.VERSION.SDK_INT <= 23) {
//            Crop crop = new Crop(picUri);
//            Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
//            crop.output(destination);
//            crop.asSquare().start(mContext);
//        }
//        Intent i = new Intent();
//        i.putExtra("url", bgImageList.get(arg2));
//        mContext.setResult(AppConstants.REQUEST_CODE_INSTAGRAM, i);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        imagesList = (GridView) view.findViewById(R.id.imagesList);
    }


    //    public void goToNext(Bitmap thumbnail) {
//
//        if (thumbnail != null || inspirationImageUrl != null) {
//            addFragment(new FragmentPostUploadTag(thumbnail, inspirationImageUrl, String.valueOf(isImage)));
//        } else {
//            AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
//        }
//
////            if (byteArray!=null) {
////                addFragment(new FragmentInspirationPost(byteArray,isImage,filePath));
////            } else {
////                AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
////            }
//    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressBarDialog = new ProgressBarDialog(mContext);
            mProgressBarDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                //  String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "Title", null);
                Uri url = bitmapToUriConverter(bitmap);
//                if(bitmap!=null&&!bitmap.isRecycled()) {
//                    bitmap.recycle();
//                    bitmap = null;
//                }
                ((HomeActivity) mContext).startCropActivity(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            bitmapImg = result;
            // Close progressdialog
            mProgressBarDialog.dismiss();
        }
    }

    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            // Calculate inSampleSize
//            options.inSampleSize = calculateInSampleSize2(options, 300, 300);
//
//            // Decode bitmap with inSampleSize set
//            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), mBitmap.getHeight(),
                    true);
            File file = new File(getActivity().getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }


    @Override
    public void setData(Bundle sa) {

//            if (checkInternet()) {
//                if (true) {
//                   getInstagramList(count, true);
//                    isLoadDefault = false;
//                } else
//
//                   getBgList();
//            }

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        imagesList.setOnItemClickListener(this);
    }


}
