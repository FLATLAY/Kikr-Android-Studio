package com.kikr.post_upload.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.utility.PictureUtils;
import com.kikrlib.utils.Syso;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;


public class GalleryImage extends BaseFragment implements LoaderCallbacks<Cursor> {

    /**
     * SimpleCursorAdapter, holds images and layout for the gridview
     */
    OnItemClickListener itemClickListener;
    SimpleCursorAdapter mAdapter;
    private static final int CROP_PIC = 1007;
    private Uri picUri;
    View view;
    GridView gridView;
    ImageView uploadedImageView;

    @Override
    public void onStart() {
        super.onStart();

        /** Initializes the Loader */
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        gridView = (GridView) view.findViewById(R.id.gridview);
        uploadedImageView = (ImageView) view.findViewById(R.id.uploadImageView);
    }

    @Override
    public void setData(Bundle bundle) {


        mAdapter = new SimpleCursorAdapter(
                getActivity().getBaseContext(),
                R.layout.gridview,
                null,
                new String[]{"_data", "_id"},
                new int[]{R.id.img},
                0
        );
        itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                /** Getting the cursor object corresponds to the clicked item */
                Cursor c1 = (Cursor) arg0.getItemAtPosition(position);

                /** Getting the image_id from the cursor */
                /** image_id of the thumbnail is same as the original image id */
                String id = c1.getString(c1.getColumnIndex("image_id"));
                String data = c1.getString(c1.getColumnIndex("_data"));
                picUri = Uri.fromFile(new File(data));


                if (Build.VERSION.SDK_INT <= 23) {
                    Crop crop = new Crop(picUri);
                    Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
                    crop.output(destination);
                    crop.asSquare().start(mContext);
                }
                	//performCrop();
                else {
                    Bitmap thePic = PictureUtils.getBitmap(mContext, new File(data));
                    if (thePic != null) {


                        // uploadImageView.setImageBitmap(thePic);
                    }
                    return;
                }

            }


        };
        gridView.setAdapter(mAdapter);

        /** Loader to get images from the SD Card */
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        /** Defining item click listener for the grid view */

        /** Setting itemclicklistener for the grid view */

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        gridView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.galleryimage, container, false);

        /** Getting a reference to gridview of the GalleryImage layout */


        /** Setting adapter for the gridview */

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Crop.REQUEST_CROP) {
            // get the returned data
            Syso.info("abcd:   ");

            Bundle extras = data.getExtras();
            // get the cropped bitmap
            Bitmap thePic = extras.getParcelable("data");


            String filePath = Environment.getExternalStorageDirectory()
                    + "/temporary_holder.jpg";

            File file = new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg");

            Bitmap thumbnail = BitmapFactory.decodeFile(filePath);
            thumbnail = createScaledImage(file);

            thumbnail = getResizedBitmap(thumbnail, 640, 640);
            if (thumbnail != null) {

                // isImage = YES;
                gridView.setVisibility(View.GONE);
                uploadedImageView.setVisibility(View.VISIBLE);
                uploadedImageView.setImageBitmap(thumbnail);
            }


        }
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

    /**
     * A callback method invoked by the loader when initLoader() is called
     */
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        /** Getting uri to the Thumbnail images stored in the external storage */
        Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;

        /** Invoking the uri */
        return new CursorLoader(getActivity(), uri, null, null, null, null);
    }

    /**
     * A callback method, invoked after the requested content provider returned all the data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
        mAdapter.swapCursor(arg1);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
    }
}