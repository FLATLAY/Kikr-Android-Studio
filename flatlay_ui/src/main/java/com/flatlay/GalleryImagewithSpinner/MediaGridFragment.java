package com.flatlay.GalleryImagewithSpinner;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.flatlay.BaseFragment;
import com.flatlay.GallerychacheKikr.ImageCache;
import com.flatlay.GallerychacheKikr.ImageWorker;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ProgressBarDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaGridFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private Uri picUri;
    AdapterView.OnItemClickListener itemClickListener;
    List<String> list = new ArrayList<String>();
    Spinner mstatus = HomeActivity.mstatus;
    private static final String LOG_TAG = "MediaGridFragment";
    List<String> categories = new ArrayList<String>();
    // Variables related to Media items
    private int imgCount; // number of images
    private int vidCount; // number of videos
    private Bitmap[] thumbnails;
    private String[] imgDisplayNames;
    private String[] imgSize;
    private long[] thumbnailIds;
    private String[] arrPath;
    static int folderposition;
    private ImageWorker imageWorker;

    // Content Resolver

    private ContentResolver mContentResolver;

    private static final String IMAGE_CACHE_DIR = "thumbs";
    // Views

    private GridView gridView;

    // Map and Hash initializations

    Map<Integer, String> mFolderBucket = new HashMap<Integer, String>();
    Map<Integer, Integer> mFolderBucketCount = new HashMap<Integer, Integer>() {
        @Override
        public Integer get(Object key) {
            Integer result = super.get(key);

            if (result == null)
                return 0;
            else
                return result;
        }
    };

    private Integer[] folderBucketIds;

    private FolderAdapter folderAdapter;

    private ImagesInFolderAdapter imagesAdapter;
    private ProgressBarDialog mProgressBarDialog;

    public MediaGridFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setRetainInstance(true);
        Log.d(LOG_TAG, "onCreate");
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_TAG, "onCreateView");

        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_media_grid, container, false);

        gridView = (GridView) v.findViewById(R.id.gridview);

        //  mstatus= (Spinner) v.findViewById(R.id.mstatus);
        mContentResolver = getActivity().getContentResolver();
        if (Build.VERSION.SDK_INT >= 23) {

            if (!checkPermission()) {
                requestPermission();
            } else {
                getRootFolders();
            }
        } else {
            getRootFolders();
        }


        // categories.add("ALL");
        for (Map.Entry m : mFolderBucket.entrySet()) {

            String abhi = m.getValue().toString();
            categories.add(abhi);
            // System.out.println(m.getKey()+" "+m.getValue());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        mstatus.setAdapter(dataAdapter);
        mstatus.setOnItemSelectedListener(new GridViewListener());


        //   folderAdapter = new FolderAdapter(getActivity(),getActivity().getContentResolver(),1,1,1);

        //  gridView.setAdapter(folderAdapter);

        //gridView.setOnItemSelectedListener(new GridViewListener());
        return v;

    }

    @Override
    public void initUI(Bundle savedInstanceState) {

    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {

    }


    private boolean checkPermission() {

        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }
    }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            Toast.makeText(mContext, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class GridViewListener implements AdapterView.OnItemSelectedListener {


        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // folderposition=position;

            getMediaInFolder(position);
            //    Cursor c1 = (Cursor) parent.getItemAtPosition(position);

            imagesAdapter = new ImagesInFolderAdapter(getActivity(), getActivity().getContentResolver(), imgCount, vidCount, thumbnailIds, arrPath);
            gridView.setAdapter(imagesAdapter);
            // gridView.setOnItemClickListener(null);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
                                        long arg3) {

                    mProgressBarDialog = new ProgressBarDialog(mContext);
                    mProgressBarDialog.show();
                    // Cursor  c1 = (Cursor ) arg0.getItemAtPosition(position);
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Bitmap bmp=BitmapFactory.decodeFile(String.valueOf(arrPath[position]));
                            picUri=resizeBitmapFitXY(bmp.getWidth(),bmp.getHeight(),bmp);
                            //picUri = bitmapToUriConverter(BitmapFactory.decodeFile(String.valueOf(arrPath[position])));
                            //picUri = Uri.fromFile(new File(String.valueOf(arrPath[position])));
                            ((HomeActivity) mContext).startCropActivityForMedia(picUri);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            if (mProgressBarDialog.isShowing())
                                mProgressBarDialog.dismiss();
                        }
                    }.execute();


                }
            });


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }


    public Uri resizeBitmapFitXY(int width, int height, Bitmap bitmap) {
        try {
            Bitmap background = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            float originalWidth = bitmap.getWidth(), originalHeight = bitmap.getHeight();
            Canvas canvas = new Canvas(background);
            float scale, xTranslation = 0.0f, yTranslation = 0.0f;
            if (originalWidth > originalHeight) {
                scale = height / originalHeight;
                xTranslation = (width - originalWidth * scale) / 2.0f;
            } else {
                scale = width / originalWidth;
                yTranslation = (height - originalHeight * scale) / 2.0f;
            }
            Matrix transformation = new Matrix();
            transformation.postTranslate(xTranslation, yTranslation);
            transformation.preScale(scale, scale);
            Paint paint = new Paint();
            paint.setFilterBitmap(true);
            canvas.drawBitmap(bitmap, transformation, paint);
            Uri uri = null;
            File file = new File(getActivity().getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = mContext.openFileOutput(file.getName(),
                    Context.MODE_PRIVATE);
            background.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);
            return uri;
        } catch (Exception ex) {
            return null;
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


    // finding folder name by abhishek
    private void getfolders() {


        for (Map.Entry m : mFolderBucket.entrySet()) {
            System.out.println(m.getKey() + " " + m.getValue());
        }
    }


    //TODO   :  Start from here

    private void getRootFolders() {


        Log.d(LOG_TAG, "getRootFolders is called");

        String[] IMAGE_PROJECTION = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME};


        String[] VIDEO_PROJECTION = {MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.VideoColumns.BUCKET_ID,
                MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME};


        /*
        * uri :  The URI, using the content:// scheme
        * projection :  a list of which columns to return, Passing null will return all columns, which is inefficient
        * selection:  Filter declaring which rows to return, formateed as an SQL WHERE clause
        * selectionArgs:  You may include ?s in selection, which will be replaced by values from selectionArgs, in the order that they appear in the selection,
        * sortOrder: How to order the rows, formatted as an SQL ORDER BY clause
        * */

        Cursor imgCsr = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                null, null, MediaStore.Images.ImageColumns.DATE_ADDED);


        Cursor vidCsr = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                null, null, MediaStore.Video.VideoColumns.DATE_ADDED);


        getMediaIdnName(imgCsr, vidCsr);


    }


    private void getMediaIdnName(Cursor imageCsr, Cursor videoCsr) {


        // getting Bucket ID and Display Name Column number for both Images and Videos

        int imgBucketIdCol = imageCsr.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID);
        int imgBucketNameCol = imageCsr.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);

        int vidBucketIdCol = videoCsr.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_ID);
        int vidBucketNameCol = videoCsr.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME);


        // Image and Video count as returned by Cursor

        imgCount = imageCsr.getCount();
        vidCount = videoCsr.getCount();


        for (int i = 0; i < imgCount + vidCount; i++) {

            if (i < imgCount) {

                imageCsr.moveToPosition(i);

                // get bucket id and bucket display names from MediaStore db

                int imgBucketId = imageCsr.getInt(imgBucketIdCol);
                String imgBucketName = imageCsr.getString(imgBucketNameCol);
                mFolderBucket.put(imgBucketId, imgBucketName);
                mFolderBucketCount.put(imgBucketId, mFolderBucketCount.get(imgBucketId) + 1);

                Log.d(LOG_TAG, "Folder name : " + imgBucketName + " for id : " + imgBucketId);

            } else {

                videoCsr.moveToPosition(i - imgCount);

                int vidBucketId = videoCsr.getInt(vidBucketIdCol);
                String vidBucketName = videoCsr.getString(vidBucketNameCol);

                mFolderBucket.put(vidBucketId, vidBucketName);
                mFolderBucketCount.put(vidBucketId, mFolderBucketCount.get(vidBucketId) + 1);

                Log.d(LOG_TAG, "Folder name : " + vidBucketName + " for id : " + vidBucketId);

            }

        }


        folderBucketIds = mFolderBucket.keySet().toArray(new Integer[mFolderBucket.size()]);

        // closing the cusror!!!!
        imageCsr.close();
        videoCsr.close();


    }


    //TODO   :  Step 2

    private void getMediaInFolder(int bucketFolderId) {


        final String[] IMAGE_PROJECTION = {MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};

        final String[] VIDEO_PROJECTION = {MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        final String vorderBy = MediaStore.Video.Media.DATE_TAKEN;


        // selection and Selection Args for Folders
        final String selection = MediaStore.Images.Media.BUCKET_ID + "=?";
        final String[] selectionArgs = {folderBucketIds[bucketFolderId].toString()};
        for (int i = 0; i <= folderBucketIds.length; i++) {
            System.out.print(folderBucketIds[bucketFolderId]);
        }

        final String vselection = MediaStore.Video.Media.BUCKET_ID + "=?";
        final String[] vselectionArgs = {folderBucketIds[bucketFolderId].toString()};


        Cursor imagecursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                selection, selectionArgs, orderBy);
        Cursor videocursor = getActivity().getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                vselection, vselectionArgs, vorderBy);


        int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
        int video_column_index = videocursor.getColumnIndex(MediaStore.Video.Media._ID);
        int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
        int vdataColumnIndex = videocursor.getColumnIndex(MediaStore.Video.Media.DATA);
        int imgdisplay_name_index = imagecursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
        int vdisplay_name_index = videocursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME);
        int imgsize_index = imagecursor.getColumnIndex(MediaStore.Images.Media.SIZE);
        int vduration_index = videocursor.getColumnIndex(MediaStore.Video.Media.DURATION);


        this.imgCount = imagecursor.getCount();
        this.vidCount = videocursor.getCount();
        this.thumbnails = new Bitmap[this.imgCount + this.vidCount];
        this.arrPath = new String[this.imgCount + this.vidCount];
        this.thumbnailIds = new long[this.imgCount + this.vidCount];
        this.imgDisplayNames = new String[this.imgCount
                + this.vidCount];
        this.imgSize = new String[this.imgCount + this.vidCount];

        if (imagecursor != null)
            imagecursor.moveToFirst();

        int i = 0;

        while (true) {
            if (this.imgCount != 0) {

                thumbnailIds[i] = imagecursor.getInt(image_column_index);
                arrPath[i] = imagecursor.getString(dataColumnIndex);
                imgDisplayNames[i] = imagecursor.getString(imgdisplay_name_index);
                imgSize[i] = imagecursor.getString(imgsize_index);

                if (imagecursor.isLast())
                    break;
                imagecursor.moveToNext();
                i++;
            } else
                break;
        }

        if (videocursor != null)
            videocursor.moveToFirst();
        if (this.imgCount != 0)
            i++;

        while (true) {

            if (this.vidCount != 0) {

                thumbnailIds[i] = videocursor.getInt(video_column_index);
                arrPath[i] = videocursor.getString(vdataColumnIndex);
                imgDisplayNames[i] = videocursor
                        .getString(vdisplay_name_index);
                imgSize[i] = videocursor.getString(vduration_index);
                if (videocursor.isLast())
                    break;

                videocursor.moveToNext();
                i++;
            } else
                break;
        }
        if (imagecursor != null)
            imagecursor.close();
        if (videocursor != null)
            videocursor.close();


    }

//  TODO   :  Step 3

    public class FolderAdapter extends BaseAdapter {

        private static final String LOG = "FolderAdapter";
        private Context mContext;
        private ContentResolver mContentResolver;


        public FolderAdapter(Context context, ContentResolver contentResolver,
                             int nCountImage, int nCountVideo, int FolderClicked) {

            Log.d(LOG, "FolderAdapter Constructor");

            mContext = context;
            mContentResolver = contentResolver;

        }

        @Override
        public int getCount() {
            return folderBucketIds.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewGroup view = (ViewGroup) convertView;

            if (convertView == null) {

                view = (ViewGroup) getActivity().getLayoutInflater().inflate(
                        R.layout.folder_view, null);

//                GridView.LayoutParams layoutParams = new GridView.LayoutParams(
//                        dipToPx(117), dipToPx(113));
//                view.setLayoutParams(layoutParams);
//                view.setPadding(dipToPx(1), dipToPx(1), dipToPx(1),
//                        dipToPx(1));


            }
            //  TextView tview1 = (TextView) view.findViewById(R.id.foldertxt);
            ViewGroup frame = (ViewGroup) view.findViewById(R.id.folderimageholder);
            // ImageView view1 = (ImageView) view.findViewById(R.id.folderimage);


            try {
                //   view1.setImageDrawable(null);
                //  frame.setBackgroundResource(R.drawable.android_folder);
                //  tview1.setText(mFolderBucket.get(folderBucketIds[position]));
                Log.v(LOG_TAG, "Folder Name =" + mFolderBucket.get(folderBucketIds[position]));
            } catch (Exception e) {
                e.printStackTrace();
            }


            return view;
        }
    }


//TODO   :  Step 4


    public class ImagesInFolderAdapter extends BaseAdapter {

        private Context mContext;
        private ContentResolver mContentResolver;

        public ImagesInFolderAdapter(Context context, ContentResolver contentResolver,
                                     int nCountImage, int nCountVideo, long[] arrayId,
                                     String[] arrayPath) {

            mContext = context;
            mContentResolver = contentResolver;
            imageWorker = new ImageWorker(mContext, mContentResolver,
                    nCountImage, nCountVideo, arrayId, arrayPath,
                    getResources().getDisplayMetrics());

            ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(mContext,
                    IMAGE_CACHE_DIR);

            // Set memory cache to
            // 25% of app memory
            cacheParams.setMemCacheSizePercent(mContext, 0.79f);

            // The ImageFetcher takes care of loading images into our ImageView
            // children asynchronously
            imageWorker.setLoadingImage(R.drawable.empty_photo);
            imageWorker.addImageCache(getFragmentManager(),
                    cacheParams);
            // mImageWorker.clearCache();
        }

        @Override
        public int getCount() {
            return thumbnailIds.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewGroup v = (ViewGroup) convertView;

            if (convertView == null) {
                v = (ViewGroup) getActivity().getLayoutInflater()
                        .inflate(R.layout.single_folder_view, null);

//                v.setLayoutParams(new GridView.LayoutParams(
//                        dipToPx(105), dipToPx(68)));
//                v.setPadding(dipToPx(5), dipToPx(5), dipToPx(5),
//                        dipToPx(5));

            }

            ImageView view1 = (ImageView) v.findViewById(R.id.thumbnail);
            view1.setScaleType(ImageView.ScaleType.CENTER_CROP);


            if (position < imgCount && imgCount != 0) {
                imageWorker.loadImage(position, view1, thumbnailIds[position], arrPath[position], v, true);

            } else if (vidCount != 0) {

                imageWorker.loadImage(position, view1, thumbnailIds[position], arrPath[position], v, false);


            }
            return v;
        }

    }

    private int dipToPx(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sp, getResources().getDisplayMetrics());
    }
}