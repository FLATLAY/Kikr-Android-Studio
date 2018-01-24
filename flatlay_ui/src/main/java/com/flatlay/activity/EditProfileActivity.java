package com.flatlay.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.circle_crop.circle_crop.ImageCropActivity;
import com.flatlay.circle_crop.circle_crop.Utils;
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
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.api.SocialDetailApi;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.bean.UserData2;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.EditProfileRes;
import com.flatlaylib.service.res.MyProfileRes;
import com.flatlaylib.service.res.SocialDetailRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import twitter4j.User;

public class EditProfileActivity extends BaseActivity implements OnClickListener {
    private ProgressBarDialog mProgressBarDialog;
    private Button nextButton;
    private ImageView user_profile_image;
    private EditText usernameEditText;
    private Bitmap mImageBitmap;
    private Bitmap mBgImageBitmap;
    private TextView editTextView1;
    private TextView editedit2;
    private TextView titleCreateYourProfile;
    private TextView nameTextView;
    ImageView camera;

    private TextView save1;
    private TextView save2;
    // private TextView editTextView;

    private EditText fbEdit;

    private ImageView fbIcon;
    private ImageView insIcon;
    private ImageView pinIcon;
    private ImageView twiIcon;
    private ImageView tubeIcon;

    private String profilePic = "";
    private String bgPic = "";
    private String oldUsername = "";
    private String newUsername = "";
    private boolean isEditProfile = false;
    private String currentSelectedImage = "profile";
    private EditProfileActivity editProfileActivity;
    private boolean needToUpdateName = false;
    private boolean needToUpdatePic = false;
    private boolean needToUpdateBg = false;
    private final static int REQUEST_CODE_FB_PIC = 1004;
    private final static int REQUEST_CODE_TWIT_PIC = 1005;
    private final static int REQUEST_CODE_INS_PIC = 1000000;
    private final static int REQUEST_CODE_PIN_PIC = 1000000;
    private final static int REQUEST_CODE_TUBE_PIC = 1000000;
    private ProgressBarDialog progressBarDialog;
    private String from = "";
    private Uri picUri;
    final int CROP_PIC = 1006;
    int index = 0;
    String[] socialInfo = new String[5];
    String[] originSocial = {"https://www.facebook.com/",
            "https://www.instagram.com/", "https://www.pinterest.com/",
            "https://www.twitter.com/", "https://www.youtube.com/"};
    String[] originSocial2 = {"https://www.facebook.com/",
            "https://www.instagram.com/", "https://www.pinterest.com/",
            "https://www.twitter.com/", "https://www.youtube.com/"};
    Boolean changeMadeToName = false;
    Boolean changeMadeToSocial = false;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("Activity:", "EditProfileActivity");
        if (getIntent().hasExtra("is_edit_profile")) {
            //?????????????
            isEditProfile = getIntent().getBooleanExtra("is_edit_profile", false);
        } else {
            //????????
            UserPreference.getInstance().setCurrentScreen(Screen.UserNameScreen);
            updateScreen(Screen.UserNameScreen);
        }
        CommonUtility.noTitleActivity(context);
        setContentView(R.layout.activity_edit_profile);
        editProfileActivity = this;
        if (getIntent().hasExtra("from")) {
            from = getIntent().getStringExtra("from");
        }
        usernameEditText.addTextChangedListener(new MyTextWatcher());
        fbEdit.addTextChangedListener(new MyTextWatcher2());
//        newFb = originSocial[0];
//        newPin = originSocial[1];
//        newIns = originSocial[2];
//        newTwi = originSocial[3];
//        newTube = originSocial[4];

    }

    String newFb;
    String newIns;
    String newTube;
    String newTwi;
    String newPin;

    Boolean wantToGoNext = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cameraIcon:
                currentSelectedImage = "profile";
                PictureUtils.showAddPictureAlert(context, editProfileActivity);
                break;

            case R.id.editTextView1:
                usernameEditText.setVisibility(View.VISIBLE);
                save1.setVisibility(View.VISIBLE);
                usernameEditText.setText(oldUsername);
                editTextView1.setVisibility(View.GONE);

            case R.id.editedit2:
                fbEdit.setVisibility(View.VISIBLE);
                save2.setVisibility(View.VISIBLE);
                fbEdit.setText(originSocial[0]);
                editedit2.setVisibility(View.GONE);
                break;

            case R.id.usernameEditText:
                save1.setTextColor(Color.BLACK);
                break;

            case R.id.fbEditText:
                save2.setTextColor(Color.BLACK);
                break;

            case R.id.saveTextView1:
                newUsername = usernameEditText.getText().toString();
                save1.setTextColor(Color.GRAY);
                usernameEditText.clearFocus();
                doRegister();
               // setOldInfo();
                usernameEditText.setVisibility(View.GONE);
                save1.setVisibility(View.GONE);
                editTextView1.setVisibility(View.VISIBLE);
                break;

            case R.id.saveTextView2:
                socialInfo[index] = fbEdit.getText().toString();
                save2.setTextColor(Color.GRAY);
                updateSocial();
                break;

            case R.id.fbIcon:
                fbIcon.setImageResource(R.drawable.facebook2);
                fbEdit.setText(originSocial[0]);
                pinIcon.setImageResource(R.drawable.pinterest_1);
                twiIcon.setImageResource(R.drawable.twitter_1);
                insIcon.setImageResource(R.drawable.ig1);
                tubeIcon.setImageResource(R.drawable.youtube_1);
//                if (UserPreference.getInstance().getUserFb() != null&& UserPreference.getInstance().getUserFb().length()>0
//                        && !UserPreference.getInstance().getUserFb().equals("https://www.facebook.com/")) {
//                    fbEdit.setVisibility(View.VISIBLE);
//                    save2.setVisibility(View.VISIBLE);
//                    fbEdit.setText(originSocial[0]);
//                } else {
//                    fbEdit.setVisibility(View.INVISIBLE);
//                    save2.setVisibility(View.INVISIBLE);
//                }
                index = 0;
                break;

            case R.id.pinIcon:
                pinIcon.setImageResource(R.drawable.pinterest2);
                fbEdit.setText(originSocial[2]);
                twiIcon.setImageResource(R.drawable.twitter_1);
                fbIcon.setImageResource(R.drawable.facebook1);
                insIcon.setImageResource(R.drawable.ig1);
                tubeIcon.setImageResource(R.drawable.youtube_1);
                newPin = fbEdit.getText().toString();
                index = 1;
//                if (UserPreference.getInstance().getUserPin() != null&& UserPreference.getInstance().getUserPin().length()>0
//                        && !UserPreference.getInstance().getUserPin().equals("https://www.pinterest.com/")) {
//                    fbEdit.setVisibility(View.VISIBLE);
//                    save2.setVisibility(View.VISIBLE);
//                    fbEdit.setText(originSocial[2]);
//                } else {
//                    fbEdit.setVisibility(View.INVISIBLE);
//                    save2.setVisibility(View.INVISIBLE);
//
//                }
                break;

            case R.id.insIcon:
                insIcon.setImageResource(R.drawable.ig);
                fbEdit.setText(originSocial[1]);
                pinIcon.setImageResource(R.drawable.pinterest_1);
                twiIcon.setImageResource(R.drawable.twitter_1);
                fbIcon.setImageResource(R.drawable.facebook1);
                tubeIcon.setImageResource(R.drawable.youtube_1);
                newIns = fbEdit.getText().toString();
//                if (UserPreference.getInstance().getUserIns() != null&& UserPreference.getInstance().getUserIns().length()>0
//                        && !UserPreference.getInstance().getUserIns().equals("https://www.instagram.com/")) {
//                    fbEdit.setVisibility(View.VISIBLE);
//                    save2.setVisibility(View.VISIBLE);
//                    fbEdit.setText(originSocial[1]);
//                } else {
//                    fbEdit.setVisibility(View.INVISIBLE);
//                    save2.setVisibility(View.INVISIBLE);
//
//                }
                index = 2;
                break;

            case R.id.twiIcon:
                twiIcon.setImageResource(R.drawable.twitter2);
                pinIcon.setImageResource(R.drawable.pinterest_1);
                fbEdit.setText(originSocial[3]);
                fbIcon.setImageResource(R.drawable.facebook1);
                tubeIcon.setImageResource(R.drawable.youtube_1);
                insIcon.setImageResource(R.drawable.ig1);
                newTwi = fbEdit.getText().toString();
//                if (UserPreference.getInstance().getUserTwi() != null&& UserPreference.getInstance().getUserTwi().length()>0
//                        && !UserPreference.getInstance().getUserTwi().equals("https://www.twitter.com/")) {
//                    fbEdit.setVisibility(View.VISIBLE);
//                    save2.setVisibility(View.VISIBLE);
//                    fbEdit.setText(originSocial[3]);
//                } else {
//                    fbEdit.setVisibility(View.INVISIBLE);
//                    save2.setVisibility(View.INVISIBLE);
//
//                }
                index = 3;
                break;

            case R.id.tubeIcon:
                tubeIcon.setImageResource(R.drawable.youtube);
                fbEdit.setText(originSocial[4]);
                pinIcon.setImageResource(R.drawable.pinterest_1);
                twiIcon.setImageResource(R.drawable.twitter_1);
                fbIcon.setImageResource(R.drawable.facebook1);
                insIcon.setImageResource(R.drawable.ig1);
                newTube = fbEdit.getText().toString();
//                if (UserPreference.getInstance().getUserTube() != null && UserPreference.getInstance().getUserTube().length()>0
//                        && !UserPreference.getInstance().getUserTube().equals("https://www.youtube.com/")) {
//                    fbEdit.setVisibility(View.VISIBLE);
//                    save2.setVisibility(View.VISIBLE);
//                    fbEdit.setText(originSocial[4]);
//                } else {
//                    fbEdit.setVisibility(View.INVISIBLE);
//                    save2.setVisibility(View.INVISIBLE);
//                }
                index = 4;
                Log.e("ahhhhh", UserPreference.getInstance().getUserTube());
                break;

//            case R.id.editTextView:
//                fbEdit.setVisibility(View.VISIBLE);
//                save2.setVisibility(View.VISIBLE);
//                fbEdit.setText(originSocial2[index]);
//                break;
            case R.id.nextButton:
                wantToGoNext = true;
                validateUserInput();
                break;
        }
    }

    @Override
    public void initLayout() {
        nextButton = (Button) findViewById(R.id.nextButton);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        user_profile_image = (ImageView) findViewById(R.id.user_profile_image);
//        oldUsername = usernameEditText.getText().toString().trim();
        usernameEditText.setFilters(new InputFilter[]{filter});
        save1 = (TextView) findViewById(R.id.saveTextView1);
        save2 = (TextView) findViewById(R.id.saveTextView2);
        fbEdit = (EditText) findViewById(R.id.fbEditText);
        fbIcon = (ImageView) findViewById(R.id.fbIcon);
        insIcon = (ImageView) findViewById(R.id.insIcon);
        pinIcon = (ImageView) findViewById(R.id.pinIcon);
        twiIcon = (ImageView) findViewById(R.id.twiIcon);
        tubeIcon = (ImageView) findViewById(R.id.tubeIcon);
        titleCreateYourProfile = (TextView) findViewById(R.id.titleCreateYourProfile);
        camera = (ImageView) findViewById(R.id.cameraIcon);
        editTextView1 = (TextView) findViewById(R.id.editTextView1);
        editedit2 = (TextView) findViewById(R.id.editedit2);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        Picasso.with(this).load(R.drawable.profile_icon).resize(150, 150).into(user_profile_image);
        usernameEditText.setVisibility(View.GONE);
        save1.setVisibility(View.GONE);
        fbEdit.setVisibility(View.GONE);
        save2.setVisibility(View.GONE);
//        if (UserPreference.getInstance().getUserFb() != null
//                && !UserPreference.getInstance().getUserFb().equals("https://www.facebook.com/")) {
//            fbEdit.setVisibility(View.VISIBLE);
//            save2.setVisibility(View.VISIBLE);
//            fbEdit.setText(UserPreference.getInstance().getUserFb());
//        } else {
//            fbEdit.setVisibility(View.INVISIBLE);
//            save2.setVisibility(View.INVISIBLE);
//
//        }
        // editTextView = (TextView) findViewById(R.id.editTextView);
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

        if (getIntent().hasExtra("is_edit_profile")) {
            isEditProfile = getIntent().getBooleanExtra("is_edit_profile", false);
        }
        setOldInfo();
    }

    private List<UserData> userDetails;


//    public void setName() {
//        usernameEditText.setText(PictureUtils.choice);
//    }

//    public String[] generateRandomName() {
//        String[] result = new String[4];
//        newUsername = usernameEditText.getText().toString().trim();
//        int i = 0;
//        while (i < 4) {
//
//            i++;
//        }
//        return result;
//    }


    public void setOldInfo() {
        final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                MyProfileRes myProfileRes = (MyProfileRes) object;
                userDetails = myProfileRes.getUser_data();
                if (!userDetails.get(0).getProfile_pic().equals("")) {
                    CommonUtility.setImage(context, userDetails.get(0).getProfile_pic(), user_profile_image, R.drawable.profile_icon);
                }
                if (userDetails.get(0).getUsername() != null) {
                    usernameEditText.setText(userDetails.get(0).getUsername());
                    oldUsername = userDetails.get(0).getUsername();
                    nameTextView.setText(oldUsername);
                    Log.e("oldddddd", oldUsername);
                    setOldSocial();

                } else
                    usernameEditText.setText("Choose a username");
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

            }
        });

        myProfileApi.getUserProfileDetail(UserPreference.getInstance().getUserID(), UserPreference.getInstance().getUserID());
        myProfileApi.execute();
        if (index == 0 && UserPreference.getInstance().getUserID() != null) {
            fbEdit.setText(UserPreference.getInstance().getUserFb());
        }
    }


    public void setOldSocial() {
        // setOldInfo();
        //modify original social
        final SocialDetailApi socialDetailApi = new SocialDetailApi(new ServiceCallback() {
            @Override
            public void handleOnSuccess(Object object) {
                SocialDetailRes socialDetailRes = (SocialDetailRes) object;
                UserData2 data = socialDetailRes.getData();
                String fb = data.getFacebook();
                String ins = data.getInstagram();
                String tube = data.getYoutube();
                String twi = data.getTwitter();
                String pin = data.getPinterest();
                if (fb.length() > 0) {
                    originSocial[0] = fb;

                }
                if (ins.length() > 0) {
                    originSocial[1] = ins;

                }
                if (pin.length() > 0) {
                    originSocial[2] = pin;

                }
                if (twi.length() > 0) {
                    originSocial[3] = twi;
                }
                if (tube.length() > 0) {
                    originSocial[4] = tube;
                }

                newFb = originSocial[0];
                newPin = originSocial[1];
                newIns = originSocial[2];
                newTwi = originSocial[3];
                newTube = originSocial[4];
//                if (UserPreference.getInstance().getUserFb() != null
//                        && !UserPreference.getInstance().getUserFb().equals("https://www.facebook.com/")) {
//                    fbEdit.setVisibility(View.VISIBLE);
//                    save2.setVisibility(View.VISIBLE);
//                    fbEdit.setText(originSocial[0]);
//                } else {
//                    fbEdit.setVisibility(View.INVISIBLE);
//                    save2.setVisibility(View.INVISIBLE);
//
//                }

            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        socialDetailApi.getProfileByUserName(oldUsername);
        socialDetailApi.execute();
        Log.e("oldddddd222", oldUsername);
    }

    @Override
    public void headerView() {
        hideHeader();
    }

    @Override
    public void setUpTextType() {
        usernameEditText.setTypeface(FontUtility.setMontserratLight(context));
        titleCreateYourProfile.setTypeface(FontUtility.setMontserratRegular(context));
        fbEdit.setTypeface(FontUtility.setMontserratLight(context));
        nextButton.setTypeface(FontUtility.setMontserratLight(context));
        save1.setTypeface(FontUtility.setMontserratLight(context));
        save2.setTypeface(FontUtility.setMontserratLight(context));
        editTextView1.setTypeface(FontUtility.setMontserratLight(context));
        editedit2.setTypeface(FontUtility.setMontserratLight(context));
        nameTextView.setTypeface(FontUtility.setMontserratLight(context));
    }

    @Override
    public void setClickListener() {
        nextButton.setOnClickListener(this);
        // usernameEditText.setOnEditorActionListener(this);
        user_profile_image.setOnClickListener(this);
        save2.setOnClickListener(this);
        save1.setOnClickListener(this);
        camera.setOnClickListener(this);
        fbIcon.setOnClickListener(this);
        insIcon.setOnClickListener(this);
        pinIcon.setOnClickListener(this);
        twiIcon.setOnClickListener(this);
        tubeIcon.setOnClickListener(this);
        editTextView1.setOnClickListener(this);
        editedit2.setOnClickListener(this);
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
                            setNewImage(thePic);
                        }
                    }
                }
                needToUpdatePic = true;
            } else if (requestCode == PictureUtils.REQUEST_CODE_GALLERY) {
                Uri uri = data.getData();
                if (uri != null) {
                    picUri = uri;
                    if (currentSelectedImage.equalsIgnoreCase("profile")) {
                        Utils.path = picUri;
                        performNewCrop(picUri);
                    } else
                        performCropGallery(picUri);
                }
                needToUpdatePic = true;
            } else if (requestCode == CROP_PIC) {
                String filePath = Environment.getExternalStorageDirectory()
                        + "/temporary_holder.jpg";
                Bitmap thumbnail = null;
                try {
                    thumbnail = BitmapFactory.decodeStream(getContentResolver().openInputStream(Utils.path));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                setNewImage(thumbnail);
                mImageBitmap = thumbnail;
                needToUpdatePic = true;
            }
        }

    }

    private void performCropGallery(Uri picUri) {
        startCropActivity(picUri);
    }


    private class DownloadImage extends AsyncTask<String, Void, Uri> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBarDialog = new ProgressBarDialog(context);
            mProgressBarDialog.show();
        }

        @Override
        protected Uri doInBackground(String... URL) {

            String imageURL = URL[0];
            Uri url = null;

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
                url = bitmapToUriConverter(bitmap);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return url;
        }

        @Override
        protected void onPostExecute(Uri result) {
            if (result != null) {
                Utils.path = result;
                performNewCrop(result);
            } else
                Syso.info("Could not download the image, Please try again.");
            mProgressBarDialog.dismiss();
        }
    }

    public Uri bitmapToUriConverter(Bitmap mBitmap) {
        Uri uri = null;
        try {
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), mBitmap.getHeight(),
                    true);
            File file = new File(getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = openFileOutput(file.getName(),
                    Context.MODE_PRIVATE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
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

    boolean isValid = true;

    private void validateUserInput() {
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
        if (newUsername.equals(oldUsername)) {
            needToUpdateName = false;
        } else {
            needToUpdateName = true;
        }
        upload();
    }

    private void upload() {
        mProgressBarDialog = new ProgressBarDialog(context);
        mProgressBarDialog.show();
        if (needToUpdateName) {
            setResult(RESULT_OK);
            doRegister();
            updateSocial();
            mProgressBarDialog.dismiss();
        } else {
            if (mProgressBarDialog.isShowing())
                mProgressBarDialog.dismiss();
        }
    }

    private void doRegister() {
        if (needToUpdatePic) {
            setResult(RESULT_OK);
            uploadImage();
        }
        final EditProfileApi uploadProfileImage = new EditProfileApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        needToUpdateName = false;
                        UserPreference.getInstance().setUserName(newUsername);
                        nameTextView.setText(newUsername);
                        oldUsername = newUsername;
                       if (wantToGoNext) goToNextScreen();
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {
                        if (object != null) {
                            isValid = false;
                            EditProfileRes editProfileRes = (EditProfileRes) object;
                            AlertUtils.showToast(context, editProfileRes.getMessage());
                        }
                    }
                });
        uploadProfileImage.addUsername(UserPreference.getInstance().getUserID(), newUsername);
        uploadProfileImage.execute();
    }

    private void updateSocial() {
        newFb = socialInfo[0];
        newPin = socialInfo[1];
        newIns = socialInfo[2];
        newTwi = socialInfo[3];
        newTube = socialInfo[4];

        final EditProfileApi updateSocial = new EditProfileApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {
                        needToUpdateName = false;
                        UserPreference.getInstance().setUserFb(newFb);
                        UserPreference.getInstance().setUserIns(newIns);
                        UserPreference.getInstance().setUserTube(newTube);
                        UserPreference.getInstance().setUserTwi(newTwi);
                        UserPreference.getInstance().setUserPin(newPin);
                        upload();
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {
                        if (object != null) {
                            EditProfileRes editProfileRes = (EditProfileRes) object;
                            //AlertUtils.showToast(context, editProfileRes.getMessage());
                        }
                    }
                });
        updateSocial.updateUserSocial(UserPreference.getInstance().getUserID(), newFb, newIns, newTwi, newPin, newTube);
        updateSocial.execute();
    }

    private void uploadImage() {
        final byte[] profileImage = PictureUtils.getByteArray(mImageBitmap);
        final EditProfileApi editProfileApi = new EditProfileApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                needToUpdatePic = false;
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
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

    private void goToNextScreen() {
        if (isEditProfile) {
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
        }
    }

    @Override
    public void onBackPressed() {
        if (isEditProfile) {
            finish();
        } else {
        }
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

    public void setNewImage(Bitmap bitmap) {
        if (bitmap != null) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
            needToUpdatePic = true;
            user_profile_image.setImageBitmap(resizedBitmap);
            mImageBitmap = resizedBitmap;
            user_profile_image.setTag(resizedBitmap);
        }
    }


    private Uri mDestinationUri;

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    public void startCropActivity(@NonNull Uri uri) {
        mDestinationUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/temporary_holder.jpg"));
        final int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            if (checkIfAlreadyhavePermission()) {

            }
        }

        CropImage.activity(uri)
                .setRequestedSize(700, 700, CropImageView.RequestSizeOptions.RESIZE_INSIDE)
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .setOutputUri(mDestinationUri)
                .start(context);
    }

    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String username = usernameEditText.getText().toString();

            if (username != null && username.length() > 0 && username.length() <= AppConstants.USERNAME_MAX_LENGTH) {
                nextButton.setTextColor(Color.WHITE);
            } else
                nextButton.setTextColor(Color.GRAY);
            save1.setTextColor(Color.BLACK);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private class MyTextWatcher2 implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            save2.setTextColor(Color.BLACK);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
