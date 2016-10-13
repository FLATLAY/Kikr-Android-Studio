package com.flatlay.post_upload;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desmond.squarecamera.CameraSettingPreferences;
import com.desmond.squarecamera.ImageParameters;
import com.desmond.squarecamera.ResizeAnimation;
import com.desmond.squarecamera.SquareCameraPreview;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback, Camera.PictureCallback {

    public static final String TAG = CameraFragment.class.getSimpleName();
    public static final String CAMERA_ID_KEY = "camera_id";
    public static final String CAMERA_FLASH_KEY = "flash_mode";
    public static final String IMAGE_INFO = "image_info";

    private static final int PICTURE_SIZE_MAX_WIDTH = 1280;
    private static final int PREVIEW_SIZE_MAX_WIDTH = 640;

    private int mCameraID;
    private String mFlashMode;
    RelativeLayout camera_tools_view;
    private Camera mCamera;
    private SquareCameraPreview mPreviewView;
    private SurfaceHolder mSurfaceHolder;
    ImageView flash_icon;

    private boolean mIsSafeToTakePhoto = false;

    private ImageParameters mImageParameters;

    private CameraOrientationListener mOrientationListener;
    private boolean mHasDoubleClicked = false;

    public static Fragment newInstance() {
        return new CameraFragment();
    }

    public CameraFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOrientationListener = new CameraOrientationListener(context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mCameraID = getBackCameraID();
            mFlashMode = CameraSettingPreferences.getCameraFlashMode(getActivity());
            mImageParameters = new ImageParameters();
        } else {
            mCameraID = savedInstanceState.getInt(CAMERA_ID_KEY);
            mFlashMode = savedInstanceState.getString(CAMERA_FLASH_KEY);
            mImageParameters = savedInstanceState.getParcelable(IMAGE_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.squarecamera__fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mOrientationListener.enable();
        try {
            mPreviewView = (SquareCameraPreview) view.findViewById(R.id.camera_preview_view);
            flash_icon = (ImageView) view.findViewById(R.id.flash_icon);
        } catch (Exception ex) {
            Syso.info(ex.getMessage());
        }
        mPreviewView.getHolder().addCallback(CameraFragment.this);

        mImageParameters.mIsPortrait =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        if (savedInstanceState == null) {
            ViewTreeObserver observer = mPreviewView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mImageParameters.mPreviewWidth = mPreviewView.getWidth();
                    mImageParameters.mPreviewHeight = mPreviewView.getHeight();

                    mImageParameters.mCoverWidth = mImageParameters.mCoverHeight
                            = mImageParameters.calculateCoverWidthHeight();


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mPreviewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        mPreviewView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
        final ImageView swapCameraBtn = (ImageView) view.findViewById(R.id.change_camera);
        swapCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCameraID == CameraInfo.CAMERA_FACING_FRONT) {
                    mCameraID = getBackCameraID();
                } else {
                    mCameraID = getFrontCameraID();
                }
                restartPreview();
            }
        });

        final View changeCameraFlashModeBtn = view.findViewById(R.id.flash);
        changeCameraFlashModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlashMode();
            }
        });
        setupFlashMode();

        final ImageView takePhotoBtn = (ImageView) view.findViewById(R.id.capture_image_button);
        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        camera_tools_view = (RelativeLayout) view.findViewById(R.id.camera_tools_view);


        camera_tools_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoubleClick();

                if (mHasDoubleClicked) {
                    setFlashMode();
                    mHasDoubleClicked = false;

                }


            }
        });

    }


    private void setFlashMode() {
        if (mFlashMode.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_AUTO)) {
            mFlashMode = Camera.Parameters.FLASH_MODE_ON;
            Toast.makeText(getActivity(), "Flash Mode On", Toast.LENGTH_SHORT).show();

        } else if (mFlashMode.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_ON)) {
            mFlashMode = Camera.Parameters.FLASH_MODE_OFF;
            Toast.makeText(getActivity(), "Flash Mode Off", Toast.LENGTH_SHORT).show();
        } else if (mFlashMode.equalsIgnoreCase(Camera.Parameters.FLASH_MODE_OFF)) {
            mFlashMode = Camera.Parameters.FLASH_MODE_AUTO;
            Toast.makeText(getActivity(), "Flash Mode Auto", Toast.LENGTH_SHORT).show();
        }

        setupFlashMode();
        setupCamera();
    }

    private void setupFlashMode() {
        View view = getView();
        if (view == null) return;

        final TextView autoFlashIcon = (TextView) view.findViewById(R.id.auto_flash_icon);
        if (Camera.Parameters.FLASH_MODE_AUTO.equalsIgnoreCase(mFlashMode) || Camera.Parameters.FLASH_MODE_ON.equalsIgnoreCase(mFlashMode)) {
            autoFlashIcon.setText("Auto");
            flash_icon.setImageResource(R.drawable.flash);
        } else if (Camera.Parameters.FLASH_MODE_OFF.equalsIgnoreCase(mFlashMode)) {
            autoFlashIcon.setText("Off");
            flash_icon.setImageResource(R.drawable.flash_icon_gray);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(CAMERA_ID_KEY, mCameraID);
        outState.putString(CAMERA_FLASH_KEY, mFlashMode);
        outState.putParcelable(IMAGE_INFO, mImageParameters);
        super.onSaveInstanceState(outState);
    }


    long lastPressTime = 0;

    private boolean findDoubleClick() {
        // Get current time in nano seconds.
        long pressTime = System.currentTimeMillis();
        // If double click...
        long pressgap = pressTime - lastPressTime;
        if (pressgap <= 800) {
            mHasDoubleClicked = true;

            // double click event....
        }
        lastPressTime = pressTime;
        return mHasDoubleClicked;
    }


    private void resizeTopAndBtmCover(final View topCover, final View bottomCover) {
        ResizeAnimation resizeTopAnimation
                = new ResizeAnimation(topCover, mImageParameters);
        resizeTopAnimation.setDuration(800);
        resizeTopAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        topCover.startAnimation(resizeTopAnimation);

        ResizeAnimation resizeBtmAnimation
                = new ResizeAnimation(bottomCover, mImageParameters);
        resizeBtmAnimation.setDuration(800);
        resizeBtmAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        bottomCover.startAnimation(resizeBtmAnimation);
    }

    private void getCamera(int cameraID) {
        try {

            mCamera = Camera.open(cameraID);
            mPreviewView.setCamera(mCamera);

        } catch (Exception e) {
            Log.d(TAG, "Can't open camera with id " + cameraID);
            e.printStackTrace();
        }
    }

    /**
     * Restart the camera preview
     */
    private void restartPreview() {
        if (mCamera != null) {
            stopCameraPreview();
            mCamera.release();
            mCamera = null;
        }

        getCamera(mCameraID);
        startCameraPreview();
    }

    /**
     * Start the camera preview
     */
    private void startCameraPreview() {
        determineDisplayOrientation();
        setupCamera();

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();

            setSafeToTakePhoto(true);
            setCameraFocusReady(true);
        } catch (IOException e) {
            Log.d(TAG, "Can't start camera preview due to IOException " + e);
            e.printStackTrace();
        }
    }

    /**
     * Stop the camera preview
     */
    private void stopCameraPreview() {
        setSafeToTakePhoto(false);
        setCameraFocusReady(false);

        // Nulls out callbacks, stops face detection
        mCamera.stopPreview();
        mPreviewView.setCamera(null);
    }

    private void setSafeToTakePhoto(final boolean isSafeToTakePhoto) {
        mIsSafeToTakePhoto = isSafeToTakePhoto;
    }

    private void setCameraFocusReady(final boolean isFocusReady) {
        if (this.mPreviewView != null) {
            mPreviewView.setIsFocusReady(isFocusReady);
        }
    }

    /**
     * Determine the current display orientation and rotate the camera preview
     * accordingly
     */
    private void determineDisplayOrientation() {
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(mCameraID, cameraInfo);

        // Clockwise rotation needed to align the window display to the natural position
        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: {
                degrees = 0;
                break;
            }
            case Surface.ROTATION_90: {
                degrees = 90;
                break;
            }
            case Surface.ROTATION_180: {
                degrees = 180;
                break;
            }
            case Surface.ROTATION_270: {
                degrees = 270;
                break;
            }
        }

        int displayOrientation;

        // CameraInfo.Orientation is the angle relative to the natural position of the device
        // in clockwise rotation (angle that is rotated clockwise from the natural position)
        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
            // Orientation is angle of rotation when facing the camera for
            // the camera image to match the natural orientation of the device
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }

        mImageParameters.mDisplayOrientation = displayOrientation;
        mImageParameters.mLayoutOrientation = degrees;
        try {
            mCamera.setDisplayOrientation(mImageParameters.mDisplayOrientation);
        } catch (Exception ex) {
            AlertUtils.showToast(getActivity(), "Something went wrong, please try again.");
        }
    }

    /**
     * Setup the camera parameters
     */
    private void setupCamera() {
        // Never keep a global parameters

        Camera.Parameters parameters = mCamera.getParameters();

        Size bestPreviewSize = determineBestPreviewSize(parameters);
        Size bestPictureSize = determineBestPictureSize(parameters);

        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);


        // Set continuous picture focus, if it's supported
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        final View changeCameraFlashModeBtn = getView().findViewById(R.id.flash);
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes != null && flashModes.contains(mFlashMode)) {
            parameters.setFlashMode(mFlashMode);
            changeCameraFlashModeBtn.setVisibility(View.VISIBLE);
        } else {
            changeCameraFlashModeBtn.setVisibility(View.INVISIBLE);
        }

        // Lock in the changes
        mCamera.setParameters(parameters);
    }

    private Size determineBestPreviewSize(Camera.Parameters parameters) {
        return determineBestSize(parameters.getSupportedPreviewSizes(), PREVIEW_SIZE_MAX_WIDTH);
    }

    private Size determineBestPictureSize(Camera.Parameters parameters) {
        return determineBestSize(parameters.getSupportedPictureSizes(), PICTURE_SIZE_MAX_WIDTH);
    }

    private Size determineBestSize(List<Size> sizes, int widthThreshold) {
        Size bestSize = null;
        Size size;
        int numOfSizes = sizes.size();
        for (int i = 0; i < numOfSizes; i++) {
            size = sizes.get(i);
            boolean isDesireRatio = (size.width / 4) == (size.height / 3);
            boolean isBetterSize = (bestSize == null) || size.width > bestSize.width;

            if (isDesireRatio && isBetterSize) {
                bestSize = size;
            }
        }

        if (bestSize == null) {
            Log.d(TAG, "cannot find the best camera size");
            return sizes.get(sizes.size() - 1);
        }

        return bestSize;
    }

    private int getFrontCameraID() {
        PackageManager pm = getActivity().getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            return CameraInfo.CAMERA_FACING_FRONT;
        }

        return getBackCameraID();
    }

    private int getBackCameraID() {
        return CameraInfo.CAMERA_FACING_BACK;
    }

    /**
     * Take a picture
     */
    private void takePicture() {

        if (mIsSafeToTakePhoto) {
            setSafeToTakePhoto(false);

            mOrientationListener.rememberOrientation();

            // Shutter callback occurs after the image is captured. This can
            // be used to trigger a sound to let the user know that image is taken
            Camera.ShutterCallback shutterCallback = null;

            // Raw callback occurs when the raw image data is available
            Camera.PictureCallback raw = null;

            // postView callback occurs when a scaled, fully processed
            // postView image is available.
            Camera.PictureCallback postView = null;

            // jpeg callback occurs when the compressed image is available
            mCamera.takePicture(shutterCallback, raw, postView, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mCamera == null) {
            restartPreview();
        }
    }

    @Override
    public void onStop() {
        mOrientationListener.disable();

        // stop the preview
        if (mCamera != null) {
            stopCameraPreview();
            mCamera.release();
            mCamera = null;
        }

        CameraSettingPreferences.saveCameraFlashMode(getActivity(), mFlashMode);

        super.onStop();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;

        getCamera(mCameraID);
        startCameraPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // The surface is destroyed with the visibility of the SurfaceView is set to View.Invisible
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case 1:
                Uri imageUri = data.getData();
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private Bitmap rotatePicture(int rotation, Bitmap bitmap) {

        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            bitmap = Bitmap.createBitmap(
                    oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
            );

            oldBitmap.recycle();
        }

        return bitmap;
    }

    @Override
    public void onPictureTaken(final byte[] data, Camera camera) {
        final int rotation = getPhotoRotation();
        final ProgressBarDialog mProgressBarDialog = new ProgressBarDialog(getActivity());
        mProgressBarDialog.show();

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                image = rotatePicture(rotation, image);
                //                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                //                image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                //                String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), image, "Title", null);
                Uri url = resizeBitmapFitXY(image.getWidth(), image.getHeight(), image);
                ((HomeActivity) getActivity()).startCropActivity(url);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //takePhotoBtn.setEnabled(true);
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                onStop();
            }
        };
        task.execute((Void) null);


        setSafeToTakePhoto(true);
    }


    private Bitmap scaleBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int maxWidth = 600;
        int maxHeight = 450;
        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
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
            FileOutputStream out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
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

//            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), mBitmap.getHeight(),
//                    true);
            File file = new File(getActivity().getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = getActivity().openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);
//            if(mBitmap!=null&&!mBitmap.isRecycled())
//            {
//                mBitmap.recycle();
//                mBitmap=null;
//            }

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }

        return uri;
    }

    private int getPhotoRotation() {
        int rotation;
        int orientation = mOrientationListener.getRememberedNormalOrientation();
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(mCameraID, info);

        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation - orientation + 360) % 360;
        } else {
            rotation = (info.orientation + orientation) % 360;
        }

        return rotation;
    }

    /**
     * When orientation changes, onOrientationChanged(int) of the listener will be called
     */
    private static class CameraOrientationListener extends OrientationEventListener {

        private int mCurrentNormalizedOrientation;
        private int mRememberedNormalOrientation;

        public CameraOrientationListener(Context context) {
            super(context, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation != ORIENTATION_UNKNOWN) {
                mCurrentNormalizedOrientation = normalize(orientation);
            }
        }

        /**
         * @param degrees Amount of clockwise rotation from the device's natural position
         * @return Normalized degrees to just 0, 90, 180, 270
         */
        private int normalize(int degrees) {
            if (degrees > 315 || degrees <= 45) {
                return 0;
            }

            if (degrees > 45 && degrees <= 135) {
                return 90;
            }

            if (degrees > 135 && degrees <= 225) {
                return 180;
            }

            if (degrees > 225 && degrees <= 315) {
                return 270;
            }

            throw new RuntimeException("The physics as we know them are no more. Watch out for anomalies.");
        }

        public void rememberOrientation() {
            mRememberedNormalOrientation = mCurrentNormalizedOrientation;
        }

        public int getRememberedNormalOrientation() {
            rememberOrientation();
            return mRememberedNormalOrientation;
        }
    }
}
