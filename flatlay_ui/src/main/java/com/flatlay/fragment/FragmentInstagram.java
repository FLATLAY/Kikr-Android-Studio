package com.flatlay.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.LifestyleImageAdapter;
import com.flatlay.model.InstagramImage;
import com.flatlay.utility.InstagramCallBack;
import com.flatlay.utility.InstagramUtility;
import com.flatlaylib.bean.BgImage;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.EditProfileRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by RachelDi on 4/17/18.
 */

public class FragmentInstagram extends ParentFragment implements AdapterView.OnItemClickListener, ServiceCallback {

    GridView imagesList;
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


//    public static FragmentInstagram newInstance(String param1, String param2) {
//
//        FragmentInstagram fragment = new FragmentInstagram();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.e("FragmentInstagram","1");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_default_lifestyle_images, container, false);
        Log.e("FragmentInstagram","2");

        return view;
    }


    public void getInstagramList(int count, boolean isShowLoader) {
        Log.e("FragmentInstagram","3");

        super.getInstagramList(count, isShowLoader);

    }

    public void loginToInstagram(int count, boolean isShowLoader) {

        isLoading = true;
        postImages.clear();
        bgImageList.clear();

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
                    bgImageList.add(image.getHigh_resolution_url());
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
    }

    boolean isLoading = false;

    public void handleOnSuccess(Object object) {
        Syso.info("In handleOnSuccess>>" + object);
        EditProfileRes editProfileRes = (EditProfileRes) object;
        List<BgImage> bgImages = editProfileRes.getData();
        if (bgImages.size() > 0) {
            bgImageList = getImageList(bgImages);
            lifestyleImageAdapter = new LifestyleImageAdapter(mContext, bgImageList);
            imagesList.setAdapter(lifestyleImageAdapter);
        }
    }

    private List<String> getImageList(List<BgImage> bgImages2) {
        Log.e("FragmentInstagram","4");

        List<String> bgImages = new ArrayList<String>();
        for (BgImage bgImage : bgImages2) {
            bgImages.add(bgImage.getBackground_pic());
        }
        return bgImages;
    }


    public void handleOnFailure(ServiceException exception, Object object) {
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
        Log.e("FragmentInstagram","5");
        loginToInstagram(120, true);
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
                Uri url = bitmapToUriConverter(bitmap);
                ((HomeActivity) mContext).startCropActivity(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bitmapImg = result;
        }
    }

    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Log.e("FragmentInstagram","6");

        Uri uri = null;
        try {

            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), mBitmap.getHeight(),
                    true);
            File file = new File(getActivity().getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_PRIVATE);
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