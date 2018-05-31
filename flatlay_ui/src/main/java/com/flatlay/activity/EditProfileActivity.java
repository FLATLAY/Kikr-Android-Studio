package com.flatlay.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.circle_crop.circle_crop.ImageCropActivity;
import com.flatlay.circle_crop.circle_crop.Utils;
import com.flatlay.dialog.EditUserDescriptionDialog;
import com.flatlay.model.InstagramImage;
import com.flatlay.sessionstore.SessionStore;
import com.flatlay.twitter.TwitterOAuthActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.InstagramCallBack;
import com.flatlay.utility.InstagramUtility;
import com.flatlay.utility.PictureUtils;
import com.flatlaylib.api.EditProfileApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.EditProfileRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import twitter4j.User;

public class EditProfileActivity extends BaseActivity implements OnClickListener, TextView.OnEditorActionListener {
    private final static int REQUEST_CODE_FB_PIC = 1004;
    private final static int REQUEST_CODE_TWIT_PIC = 1005;
    final int CROP_PIC = 1006;
    private ProgressBarDialog mProgressBarDialog;
    private Button mDoneButton;
    private RoundImageView user_profile_image;
    private EditText usernameEditText;
    private Bitmap mImageBitmap;
    private Bitmap mBgImageBitmap;
    private String profilePic = "";
    private String bgPic = "";
    private String oldUsername = "";
    private String newUsername = "";
    private boolean isEditProfile = false;
    private ImageView bgImageView;
    private String currentSelectedImage = "profile";
    private EditProfileActivity editProfileActivity;
    private boolean needToUpdateName = false;
    private boolean needToUpdatePic = false;
    private boolean needToUpdateBg = false;
    private ProgressBarDialog progressBarDialog;
    private String from = "";
    private Uri picUri;
    private TextView kikr_learn_more;
    private TextView textViewOutside, descriptionTextView;
    private RelativeLayout layoutPictures;

    private String blockCharacterSet = "~#^|$%&*!()%,+-?<>;:{}[]|";


    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private Uri mDestinationUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("Activity:", "EditProfileActivity");
        if (getIntent().hasExtra("is_edit_profile")) {
            isEditProfile = getIntent().getBooleanExtra("is_edit_profile", false);
        } else {
            UserPreference.getInstance().setCurrentScreen(Screen.UserNameScreen);
            updateScreen(Screen.UserNameScreen);
        }
        CommonUtility.noTitleActivity(context);
        setContentView(R.layout.activity_edit_profile);
        editProfileActivity = this;
        if (getIntent().hasExtra("from")) {
            from = getIntent().getStringExtra("from");
        }
    }

    public void gotoDefaultList() {
        startActivityForResult(new Intent(context, DefaultLifestyleImagesActivity.class).putExtra("isDefault", true), PictureUtils.REQUEST_CODE_DEFAULT_LIST);
    }

    public void gotoInstgramList() {
        startActivityForResult(new Intent(context, DefaultLifestyleImagesActivity.class).putExtra("isDefault", false), PictureUtils.REQUEST_CODE_DEFAULT_LIST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_profile_image:
                currentSelectedImage = "profile";
                PictureUtils.showAddPictureAlert(context, editProfileActivity);
                break;
            case R.id.usernameEditText:
                break;
            case R.id.mDoneButton:
                validateUserInput();
                break;
            case R.id.leftTextView:
                CommonUtility.hideSoftKeyboard(context);
                finish();
                break;
            case R.id.bgImageView:
                currentSelectedImage = "bg";
                PictureUtils.showAddPictureAlert2(context, editProfileActivity);
                break;
            case R.id.kikr_learn_more:
                Intent intent = new Intent(this, LearnMoreOutsideUSActivity.class);
                startActivity(intent);
                break;
            case R.id.descriptionTextView:
                String text = descriptionTextView.getText().toString();
                String desc = text.equalsIgnoreCase(getResources().getString(R.string.add_description)) ? "" : text;
                EditUserDescriptionDialog dialog = new EditUserDescriptionDialog(context, desc, descriptionTextView);
                dialog.show();
                break;
        }
    }

    @Override
    public void initLayout() {
        mDoneButton = (Button) findViewById(R.id.mDoneButton);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        user_profile_image = (RoundImageView) findViewById(R.id.user_profile_image);
        bgImageView = (ImageView) findViewById(R.id.bgImageView);
        oldUsername = usernameEditText.getText().toString().trim();
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        kikr_learn_more = (TextView) findViewById(R.id.kikr_learn_more);
        kikr_learn_more.setPaintFlags(kikr_learn_more.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewOutside = (TextView) findViewById(R.id.textViewOutside);
        layoutPictures = (RelativeLayout) findViewById(R.id.layoutPictures);
        usernameEditText.setFilters(new InputFilter[]{filter});
    }

    @Override
    public void setupData() {
        if (getIntent().hasExtra("profilePic")) {
            profilePic = getIntent().getStringExtra("profilePic");
            if (!TextUtils.isEmpty(profilePic)) {
                CommonUtility.setImage(context, profilePic, user_profile_image, R.drawable.profile_icon);
                if (!from.equals(AppConstants.FROM_PROFILE))
                    needToUpdatePic = true;
            }
        }
        if (getIntent().hasExtra("username")) {
            String username = getIntent().getStringExtra("username");
            if (username != null) {
                usernameEditText.setText(username);
                usernameEditText.setSelection(username.length());
                oldUsername = username;
                if (!from.equals(AppConstants.FROM_PROFILE))
                    needToUpdateName = true;
            }
        }
        if (getIntent().hasExtra("bgPic")) {
            bgPic = getIntent().getStringExtra("bgPic");
            if (!TextUtils.isEmpty(bgPic)) {
                CommonUtility.setImage(context, bgPic, bgImageView, R.drawable.dum_list_item_product);
            }
        }
        if (getIntent().hasExtra("is_edit_profile")) {
            isEditProfile = getIntent().getBooleanExtra("is_edit_profile", false);
        }
        if (getIntent().hasExtra("description")) {
            String description = getIntent().getStringExtra("description");
            if (description != null) {
                descriptionTextView.setText(description);
            }
        }
    }

    @Override
    public void headerView() {
        if (isEditProfile) {
            setBackHeader();
            getLeftTextView().setOnClickListener(this);
            setGoToHome();
        } else {
            hideHeader();
        }
    }

    @Override
    public void setUpTextType() {
        usernameEditText.setTypeface(FontUtility.setProximanovaLight(context));
        mDoneButton.setTypeface(FontUtility.setProximanovaLight(context));
        kikr_learn_more.setTypeface(FontUtility.setProximanovaLight(context));
        textViewOutside.setTypeface(FontUtility.setProximanovaLight(context));
    }

    @Override
    public void setClickListener() {
        mDoneButton.setOnClickListener(this);
        usernameEditText.setOnEditorActionListener(this);
        user_profile_image.setOnClickListener(this);
        bgImageView.setOnClickListener(this);
        kikr_learn_more.setOnClickListener(this);
        descriptionTextView.setOnClickListener(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PictureUtils.REQUEST_CODE_CAMERA) {
                File file = new File(PictureUtils.createDirectory(),
                        PictureUtils.FILE_NAME + String.valueOf(UserPreference.getInstance().getChatImage() + ".png"));
                if (file != null) {
                    if (Build.VERSION.SDK_INT < 22)
                        startCropActivity(picUri);
                    else {
                        Bitmap thePic = PictureUtils.getBitmap(context, file);
                        if (thePic != null) {
                            mImageBitmap = thePic;
                            setImage(thePic);
                        }
                    }
                }
            } else if (requestCode == PictureUtils.REQUEST_CODE_GALLERY) {
                Uri uri = data.getData();
                if (uri != null) {
                    picUri = uri;
                    if (currentSelectedImage.equalsIgnoreCase("profile")) {
                        Utils.path = picUri;
                        performNewCrop(picUri);
                    } else
                        performCropGallery(picUri);

                    //PictureUtils.showGalleryImage(context, uri);
                }
            } else if (requestCode == PictureUtils.REQUEST_CODE_DEFAULT_LIST) {
                needToUpdateBg = true;
                String url = data.getStringExtra("url");
                if (url != null) {
                    setDefaultImage(url);
                }
            } else if (requestCode == REQUEST_CODE_FB_PIC) {
                needToUpdatePic = true;
                String url = data.getStringExtra("profile_pic");
                if (url != null) {
                    profilePic = url;
                    mImageBitmap = null;
                    new DownloadImage().execute(profilePic);
                    // CommonUtility.setImage(context, url, user_profile_image, R.drawable.dum_user);
                }
            } else if (requestCode == REQUEST_CODE_TWIT_PIC) {
                needToUpdatePic = true;
                profilePic = data.getStringExtra("profile_image_url");
                mImageBitmap = null;
                if (profilePic != null) {
                    new DownloadImage().execute(profilePic);
                    // CommonUtility.setImage(context, profilePic, user_profile_image, R.drawable.dum_user);
                }
            } else if (requestCode == CROP_PIC) {
                // get the returned data
                String filePath = Environment.getExternalStorageDirectory()
                        + "/temporary_holder.jpg";


                Bitmap thumbnail = null;
                try {
                    thumbnail = BitmapFactory.decodeStream(getContentResolver().openInputStream(Utils.path));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //Bitmap thePic = extras.getParcelable("data");
                setImage(thumbnail);
                mImageBitmap = thumbnail;
            } else if (requestCode == UCrop.REQUEST_CROP) {
//                String filePath = Environment.getExternalStorageDirectory()
//                        + "/temporary_holder.jpg";
                String filePath = Environment.getExternalStorageDirectory()
                        + "/temporary_holder.jpg";

                Bitmap thumbnail = BitmapFactory.decodeFile(filePath);
                setImage(thumbnail);
            }

        }

//			if (bitmap != null) {
//				//				mImgProfilePic.setScaleType(ScaleType.CENTER_CROP);
//				user_profile_image.setImageBitmap(bitmap);
//				user_profile_image.setTag(bitmap);
//				mImageBitmap = bitmap;
//			}
//		}
    }

    private void performCropGallery(Uri picUri) {
        startCropActivity(picUri);
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
            File file = new File(getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = openFileOutput(file.getName(),
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


    private void performNewCrop(Uri imgUri) {
        Intent cropIntent = new Intent(EditProfileActivity.this, ImageCropActivity.class);
        cropIntent.putExtra("imagePath", imgUri);
        startActivityForResult(cropIntent, CROP_PIC);
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
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 200);
            cropIntent.putExtra("outputY", 200);
            // retrieve data on return
            cropIntent.putExtra("scale", false);
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(this,
                    "This device doesn't support the crop action!",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void validateUserInput() {
        boolean isValid = true;
        newUsername = usernameEditText.getText().toString().trim();
        if (TextUtils.isEmpty(newUsername)) {
            isValid = false;
            usernameEditText.requestFocus();
            AlertUtils.showToast(context, R.string.alert_blank_username);
        } else if (newUsername.length() > AppConstants.USERNAME_MAX_LENGTH) {
            isValid = false;
            usernameEditText.requestFocus();
            AlertUtils.showToast(context, R.string.alert_username_length);
        }
        if (isValid && checkInternet()) {
            if (newUsername.equals(oldUsername)) {
                needToUpdateName = false;
                upload();
            } else {
                needToUpdateName = true;
                upload();
            }
        }
    }

    private void upload() {
        mProgressBarDialog = new ProgressBarDialog(context);
        mProgressBarDialog.show();
        if (needToUpdateName) {
            setResult(RESULT_OK);
            doRegister();
            mProgressBarDialog.dismiss();
        } else if (needToUpdatePic) {
            setResult(RESULT_OK);
            uploadImage();
        } else if (needToUpdateBg) {
            setResult(RESULT_OK);
            uploadBgImage();
        } else {

            if (mProgressBarDialog.isShowing())
                mProgressBarDialog.dismiss();
            goToNextScreen();
        }
    }

    private void doRegister() {
//        mProgressBarDialog = new ProgressBarDialog(context);
//        mProgressBarDialog.show();
        final EditProfileApi uploadProfileImage = new EditProfileApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        // mProgressBarDialog.dismiss();
                        needToUpdateName = false;
                        UserPreference.getInstance().setUserName(newUsername);
                        upload();
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {
                        // mProgressBarDialog.dismiss();
                        if (object != null) {
                            EditProfileRes editProfileRes = (EditProfileRes) object;
                            AlertUtils.showToast(context, editProfileRes.getMessage());
                        }
                    }
                });
        uploadProfileImage.addUsername(UserPreference.getInstance().getUserID(), newUsername);
        uploadProfileImage.execute();
    }

    private void uploadImage() {
        final byte[] profileImage = PictureUtils.getByteArray(mImageBitmap);
//        mProgressBarDialog = new ProgressBarDialog(context);
//        mProgressBarDialog.show();
        final EditProfileApi editProfileApi = new EditProfileApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                // mProgressBarDialog.dismiss();
                needToUpdatePic = false;
                upload();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                // mProgressBarDialog.dismiss();
            }
        });
        if (profileImage != null) {
            editProfileApi.uploadImage(UserPreference.getInstance().getUserID(), profileImage);
        } else if (!TextUtils.isEmpty(profilePic)) {
            editProfileApi.updateProfileImage(UserPreference.getInstance().getUserID(), "", profilePic);
        } else {
            needToUpdatePic = false;
            upload();
        }
        editProfileApi.execute();
    }

    private void uploadBgImage() {
        final byte[] bgImage = PictureUtils.getByteArray(mBgImageBitmap);
//        mProgressBarDialog = new ProgressBarDialog(context);
//        mProgressBarDialog.show();
        final EditProfileApi editProfileApi = new EditProfileApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                //  mProgressBarDialog.dismiss();
                needToUpdateBg = false;
                upload();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                // mProgressBarDialog.dismiss();
            }
        });
        if (bgImage != null) {
            editProfileApi.uploadBgImage(UserPreference.getInstance().getUserID(), bgImage);
        } else if (!TextUtils.isEmpty(bgPic)) {
            editProfileApi.updateBgImageUrl(UserPreference.getInstance().getUserID(), bgPic);
        } else {
            needToUpdateBg = false;
            upload();
        }
        editProfileApi.execute();
    }

    private void goToNextScreen() {
        if (isEditProfile) {
            finish();
            String email = UserPreference.getInstance().getEmail();
            Bundle bundle = new Bundle();
            if (!email.equals("Email")) {
                bundle.putString("email", email);
            } else {
                bundle.putString("email", "");
            }
            if (email == "") {
                Intent intent = new Intent(this, ChangeEmailActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_EMAIL_CHANGE);
            }
        } else {
            startActivity(FollowCategoriesNewActivity.class);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (isEditProfile) {
            finish();
        } else {
            //
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            CommonUtility.hideSoftKeyboard(context);
            if (checkInternet()) {
                validateUserInput();
                return true;
            }

        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        try {
            state.putParcelable("bitmap", ((Bitmap) user_profile_image.getTag()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        state.putString("user_name", usernameEditText.getText().toString().trim());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bitmap bitmap = savedInstanceState.getParcelable("bitmap");
        if (bitmap != null) {
            user_profile_image.setTag(bitmap);
            mImageBitmap = bitmap;
            user_profile_image.setImageBitmap(bitmap);
        }
        String username = savedInstanceState.getString("user_name");

        if (!TextUtils.isEmpty(username)) {
            usernameEditText.setText(username);
        }
    }

    public void setImage(Bitmap bitmap) {
        if (bitmap != null) {
            if (currentSelectedImage.equalsIgnoreCase("bg")) {
                needToUpdateBg = true;
                mBgImageBitmap = bitmap;
                bgImageView.setImageBitmap(bitmap);
            } else {
                needToUpdatePic = true;
                user_profile_image.setImageBitmap(bitmap);
                mImageBitmap = bitmap;
                user_profile_image.setTag(bitmap);
            }
        }
    }

    public void setDefaultImage(String url) {
        bgPic = url;
        mBgImageBitmap = null;
        CommonUtility.setImage(context, url, bgImageView, R.drawable.dum_list_item_product);
    }

    public void getFBProfilePic() {
        if (checkInternet()) {
            Intent i = new Intent(context, FbSignActivity.class);
            i.putExtra("getProfilePic", true);
            i.putExtra("getFriendList", false);
            startActivityForResult(i, REQUEST_CODE_FB_PIC);
        }
    }


    public void twitterLoogedIn() {
        if (checkInternet()) {
            boolean logedIn = SessionStore.isTwitterLogedIn(context);
            if (logedIn) {
                new GetTwitterInfo().execute();
            } else {
                Intent intent = new Intent(context, TwitterOAuthActivity.class);
                intent.putExtra("getProfilePic", true);
                startActivityForResult(intent, REQUEST_CODE_TWIT_PIC);
            }
        }
    }


    public void instagramLoogedIn() {
        if (checkInternet()) {
            InstagramUtility instagramUtility = new InstagramUtility(context, true, "", false, new InstagramCallBack() {
                @Override
                public void setProfilePic(String url) {
                    needToUpdatePic = true;
                    mImageBitmap = null;
                    profilePic = url;
                    new DownloadImage().execute(profilePic);
                    //CommonUtility.setImage(context, profilePic, user_profile_image, R.drawable.dum_user);
                }

                @Override
                public void setPictureList(ArrayList<String> photoList) {
                }

                @Override
                public void setPictureListPost(ArrayList<InstagramImage> photoList) {

                }
            });
            instagramUtility.inItInstgram();
        }
    }

    public void startCropActivity(@NonNull Uri uri) {
        mDestinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
        // UCrop uCrop = UCrop.of(uri, mDestinationUri);

        //uCrop = basisConfig(uCrop);
        // uCrop = advancedConfig(uCrop);

        //uCrop.start(context);
        CropImage.activity(uri)
                .setRequestedSize(700, 700, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                //.setMaxCropResultSize(2000, 2000)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .setOutputUri(mDestinationUri)
                .start(context);
    }

    private UCrop basisConfig(@NonNull UCrop uCrop) {

        //uCrop = uCrop.withAspectRatio(1, 1);
        try {
            float ratioX = Float.valueOf(2);
            float ratioY = Float.valueOf(1);
            if (ratioX > 0 && ratioY > 0) {
                uCrop = uCrop.withAspectRatio(ratioX, ratioY);
            }
        } catch (NumberFormatException e) {
            Log.d("UCrop", "Number please");

        }


        return uCrop;
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();


        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);

        options.setCompressionQuality(100);

        //  options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);


        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {

        if (result != null) {
            onActivityResult(UCrop.REQUEST_CROP, RESULT_OK
                    , result);
        } else
            AlertUtils.showToast(EditProfileActivity.this, R.string.toast_cannot_retrieve_cropped_image);
//        final Uri resultUri = UCrop.getOutput(result);
//        if (resultUri != null) {
//            ResultActivity.startWithUri(SampleActivity.this, resultUri);
//        } else {
//            Toast.makeText(SampleActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
//        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e("UCrop Error", "handleCropError: ", cropError);
            Toast.makeText(EditProfileActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(EditProfileActivity.this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressBarDialog = new ProgressBarDialog(context);
            mProgressBarDialog.show();
        }

        @Override
        protected Uri doInBackground(String... URL) {

            String imageURL = URL[0];
            Uri url = null;

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
                //  String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), bitmap, "Title", null);
                url = bitmapToUriConverter(bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return url;
        }

        @Override
        protected void onPostExecute(Uri result) {
            // Set the bitmap into ImageView
            if (result != null) {
                Utils.path = result;
                performNewCrop(result);
            } else
                Syso.info("Could not download the image, Please try again.");
            // Close progressdialog
            mProgressBarDialog.dismiss();
        }
    }

    private class GetTwitterInfo extends AsyncTask<Void, Void, User> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarDialog = new ProgressBarDialog(context);
            progressBarDialog.show();
        }

        @Override
        protected User doInBackground(Void... params) {
            return TwitterOAuthActivity.getUserInfo(context);
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            progressBarDialog.dismiss();
            System.out.println("Twitter id>>" + user);
            if (user != null) {
                needToUpdatePic = true;
                mImageBitmap = null;
                profilePic = user.getOriginalProfileImageURL();
                CommonUtility.setImage(context, profilePic, user_profile_image, R.drawable.profile_icon);
            } else {
                AlertUtils.showToast(context, "Failed to connect with twitter. Please try again.");
            }
        }
    }
}
