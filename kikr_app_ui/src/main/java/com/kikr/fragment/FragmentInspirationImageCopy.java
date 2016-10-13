package com.kikr.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.kikr.BaseFragment;
import com.flatlay.R;
import com.kikr.activity.DefaultLifestyleImagesActivity;
import com.kikr.activity.HomeActivity;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.PhotosImageTask;
import com.kikr.utility.PictureUtils;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class FragmentInspirationImageCopy extends BaseFragment implements View.OnClickListener {

    private static final int CROP_PIC = 1007;
    private View mainView;
    private ImageView uploadImageView;
    private FragmentInspirationImageCopy fragmentUploadInspiration;
    private Uri picUri;
    private Bitmap inspirationBitmap;
    private byte[] byteArray;
    private String YES = "yes";
    private String NO = "no";
    private String isImage = YES;
    private VideoView videoView;
    private String filePath;
    private ArrayList<Bitmap> frames = new ArrayList<Bitmap>();
    private Handler handler = new Handler();
    private ProgressBarDialog mProgressBarDialog;
    private File file = null;
    private String inspirationImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.galleryimage, null);
        fragmentUploadInspiration = this;
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        uploadImageView = (ImageView) mainView.findViewById(R.id.uploadImageView);
        videoView = (VideoView) mainView.findViewById(R.id.videoView);
       // PictureUtils.showAddPictureAlert(mContext, fragmentUploadInspiration);
    }

    @Override
    public void setData(Bundle bundle) {
    }

    @Override
    public void refreshData(Bundle bundle) {
    }

    @Override
    public void setClickListener() {
        uploadImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.uploadImageView:
               // PictureUtils.showAddPictureAlert(mContext, fragmentUploadInspiration);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("resu and req code", requestCode + " -- " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resu and req code", requestCode + " -- " + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            Log.e("req code", requestCode + "");
            if (requestCode == PictureUtils.REQUEST_CODE_CAMERA) {
                File file = new File(PictureUtils.createDirectory(),
                        PictureUtils.FILE_NAME + String.valueOf(UserPreference.getInstance().getChatImage() + ".png"));
                if (file != null) {
                    picUri = Uri.fromFile(file);
                    if (Build.VERSION.SDK_INT <= 23) {
                        Crop crop = new Crop(picUri);
                        Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
                        crop.output(destination);
                        crop.asSquare().start(mContext);
                    }
                    //	performCrop();
                    else {
                        Bitmap thePic = PictureUtils.getBitmap(mContext, file);
                        if (thePic != null) {
                            inspirationBitmap = thePic;
                            videoView.setVisibility(View.GONE);
                            uploadImageView.setVisibility(View.VISIBLE);
                            uploadImageView.setImageBitmap(thePic);
                        }
                    }
                }
            } else if (requestCode == PictureUtils.REQUEST_CODE_GALLERY) {
                Uri uri = data.getData();
                isImage = YES;
                if (uri != null) {
                    picUri = uri;
                    if (Build.VERSION.SDK_INT <= 23) {
                        performCrop();
                    } else
                        showGalleryImage(mContext, uri);
                }
                // showGalleryImage(mContext, uri);
            } else if (requestCode == PictureUtils.REQUEST_CODE_VIDEO) {
                Uri uriVideo = data.getData();
                isImage = NO;
                if (uriVideo != null)
                    setVideo(mContext, uriVideo);
            } else if (requestCode == Crop.REQUEST_CROP) {
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
                    inspirationBitmap = thumbnail;
                    isImage = YES;
                    videoView.setVisibility(View.GONE);
                    uploadImageView.setVisibility(View.VISIBLE);
                    uploadImageView.setImageBitmap(thumbnail);
                }


            } else if (requestCode == Crop.REQUEST_PICK) {
                Uri destination = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
                Crop crop = new Crop(data.getData());
                crop.output(destination).asSquare().start(mContext);
            } else if (requestCode == PictureUtils.REQUEST_CODE_DEFAULT_LIST) {
                isImage = YES;
                videoView.setVisibility(View.GONE);
                uploadImageView.setVisibility(View.VISIBLE);
                String url = data.getStringExtra("url");
                if (url != null) {
                    inspirationImageUrl = url;
                    CommonUtility.setImage(mContext, url, uploadImageView, R.drawable.dum_list_item_product);
                }
            }
        }
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

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 3);
            cropIntent.putExtra("aspectY", 3);
            cropIntent.putExtra("scale", true);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 1200);
            cropIntent.putExtra("outputY", 1200);
            // retrieve data on return
            //cropIntent.putExtra("return-data", true);

            Uri uri;

            File f = new File(Environment.getExternalStorageDirectory(),
                    "/temporary_holder.jpg");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }

            uri = Uri.fromFile(f);

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(mContext,
                    "This device doesn't support the crop action!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void openCamera() {

        Boolean isSDPresent = android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            Random r = new Random();
            UserPreference.getInstance().saveChatImage(
                    String.valueOf(r.nextInt()));
            Syso.info("Random:"
                    + String.valueOf(UserPreference.getInstance()
                    .getChatImage()));
            File file = new File(createDirectory(), PictureUtils.FILE_NAME
                    + String.valueOf(UserPreference.getInstance()
                    .getChatImage()) + ".png");
            Syso.info("file name>>" + file.getName());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(intent, PictureUtils.REQUEST_CODE_CAMERA);
        } else {
            AlertUtils.showToast(mContext, "SD Card not available.");
        }
    }

    public void openGallery() {
        //	Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //	startActivityForResult(intent, PictureUtils.REQUEST_CODE_GALLERY);
        Crop.pickImage(mContext);
    }

    public void loadInstagramPic() {
        startActivityForResult(new Intent(mContext, DefaultLifestyleImagesActivity.class).putExtra("isDefault", false), PictureUtils.REQUEST_CODE_DEFAULT_LIST);
    }

    public void recordVideo() {
        Intent captureVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
        captureVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);
        captureVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        startActivityForResult(captureVideoIntent, PictureUtils.REQUEST_CODE_VIDEO);
    }

    public static File createDirectory() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);

        if (isSDPresent && isSDCardWritable()) {
            File mainDir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath(), PictureUtils.APP_DIR_MAIN);
            if (!mainDir.exists()) {
                mainDir.mkdirs();
            }
            return mainDir;
        } else {
            File mainDir = new File(Environment.getRootDirectory()
                    .getAbsolutePath(), PictureUtils.APP_DIR_MAIN);
            if (!mainDir.exists()) {
                mainDir.mkdirs();
            }
            return mainDir;
        }
    }

    // Checks if SD Card is available for read and write
    public static boolean isSDCardWritable() {
        String status = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(status)) {
            return true;
        }
        return false;
    }

    private void showGalleryImage(FragmentActivity context, Uri uri) {
        System.out.println("uri>>>>" + uri.toString());
        if (uri.toString().startsWith("content://com.google.android.apps.photos.content")) {
            PhotosImageTask photoImageTask = new PhotosImageTask(context, uri);
            photoImageTask.execute();
        } else {
            file = PictureUtils.getFileFromUri(context, uri);
            if (file != null) {
                Bitmap galleryBitmap = PictureUtils.getBitmap(mContext, file);
                Syso.info("size: " + sizeOf(galleryBitmap));
                if (sizeOf(galleryBitmap) > (1024 * 1024)) {
                    new getSampleBitmap().execute();
                    Syso.info("size: " + PictureUtils.getImageSize(sizeOf(galleryBitmap)));
                } else {
                    inspirationBitmap = galleryBitmap;
                    videoView.setVisibility(View.GONE);
                    uploadImageView.setVisibility(View.VISIBLE);
                    uploadImageView.setImageBitmap(galleryBitmap);
                }
            }
        }
    }


    private class getSampleBitmap extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            mProgressBarDialog = new ProgressBarDialog(mContext);
            mProgressBarDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return PictureUtils.getSampledBitmap(mContext, file);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mProgressBarDialog.dismiss();
            inspirationBitmap = result;
            videoView.setVisibility(View.GONE);
            uploadImageView.setVisibility(View.VISIBLE);
            uploadImageView.setImageBitmap(inspirationBitmap);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    protected int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }

    private void setVideo(FragmentActivity context, Uri uri) {
        File file = PictureUtils.getFileFromUri(context, uri);
        filePath = file.getAbsolutePath();
        Syso.info("path:    " + file.getAbsolutePath());
        if (file != null) {
            byteArray = PictureUtils.convertFileIntoByte(file);
//			videoView.setVisibility(View.VISIBLE);
            uploadImageView.setVisibility(View.GONE);
//			videoView.setVideoPath(file.getAbsolutePath());
//	        videoView.start();
            addFragment(new FragmentVideoFrames(getVideoFrame(file.getAbsolutePath())));
        }
    }

    public void setImage(Bitmap bmp) {
        videoView.setVisibility(View.GONE);
        uploadImageView.setVisibility(View.VISIBLE);
        inspirationBitmap = bmp;
        uploadImageView.setImageBitmap(bmp);
    }

    public void goToNext() {
        //put validation here for image is selected or not
        if (isImage.equals(YES)) {
            if (inspirationBitmap != null || inspirationImageUrl != null) {
                addFragment(new FragmentInspirationPost(inspirationBitmap, inspirationImageUrl, isImage));
            } else {
                AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
            }
        } else {
            if (byteArray != null) {
                addFragment(new FragmentInspirationPost(byteArray, isImage, filePath));
            } else {
                AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
            }
        }
    }

    public ArrayList<Bitmap> getVideoFrame(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            for (int i = 0; i < 15; i++) {
                frames.add(retriever.getFrameAtTime(i * 1000, MediaMetadataRetriever.OPTION_NEXT_SYNC));
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return frames;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        HomeActivity.menuTextCartCount.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        HomeActivity.menuTextCartCount.setVisibility(View.INVISIBLE);
    }

}
