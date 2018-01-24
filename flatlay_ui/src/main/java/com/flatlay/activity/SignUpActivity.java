package com.flatlay.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.RegisterUserApi;
import com.flatlaylib.db.HelpPreference;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.DeviceUtils;
import com.flatlaylib.utils.Syso;

public class SignUpActivity extends BaseFragment implements OnClickListener, OnKeyListener, ServiceCallback {
    private EditText mEmailEditText, mPasswordEditText, mreemailEditText;
    private ProgressBarDialog mProgressBarDialog;
    private TextView mMaleTextView, mFemaleTextView, mbackToLanding, mSignUp;
    private boolean mIsMaleSelected = true;
    private boolean ratherNotSelected = false;
    private Button mNextButton;
    private View mainView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.signup2, container, false);
        return mainView;
    }

    private void setOnKeyListener() {
        mPasswordEditText.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backToLanding:
                CommonUtility.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.baseFrameLayout, new LandingActivity(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nextButton:
                CommonUtility.hideSoftKeyboard(getActivity());
                validateUserInput();
                break;
            case R.id.maleTextView:
                mIsMaleSelected = true;
                ratherNotSelected = false;
                resetGender();
                break;
            case R.id.femaleTextView:
                mIsMaleSelected = false;
                ratherNotSelected = false;
                resetGender();
                break;
        }
    }


    private void resetGender() {
        // TODO Auto-generated method stub
        if (mIsMaleSelected) {
            mMaleTextView.setTextColor(getResources().getColor(R.color.black));
            mFemaleTextView.setTextColor(getResources().getColor(R.color.black));
            mMaleTextView.setBackgroundResource(R.drawable.flatlayloginboardernew1);
            mFemaleTextView.setBackgroundResource(R.drawable.flatlayloginboardernew2);

        } else {
            mMaleTextView.setTextColor(getResources().getColor(R.color.black));
            mFemaleTextView.setTextColor(getResources().getColor(R.color.black));
            mMaleTextView.setBackgroundResource(R.drawable.flatlayloginboardernew2);
            mFemaleTextView.setBackgroundResource(R.drawable.flatlayloginboardernew1);
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        // CommonUtility.noTitleActivity(getActivity());
        mEmailEditText = (EditText) mainView.findViewById(R.id.emailEditText);
        mPasswordEditText = (EditText) mainView.findViewById(R.id.passwordEditText);
        mreemailEditText = (EditText) mainView.findViewById(R.id.reemailEditText);
        mMaleTextView = (TextView) mainView.findViewById(R.id.maleTextView);
        mFemaleTextView = (TextView) mainView.findViewById(R.id.femaleTextView);
        mbackToLanding = (TextView) mainView.findViewById(R.id.backToLanding);
        mNextButton = (Button) mainView.findViewById(R.id.nextButton);
        mSignUp = (TextView) mainView.findViewById(R.id.signUpText);
        SignUpTextWatcher watcher = new SignUpTextWatcher();
        mEmailEditText.addTextChangedListener(watcher);
        mPasswordEditText.addTextChangedListener(watcher);
        mreemailEditText.addTextChangedListener(watcher);
        setUpTextType();
        setOnKeyListener();

    }

//    @Override
//    public void setupData() {
//
//    }
//
//    @Override
//    public void headerView() {
//        hideHeader();
//    }

    public void setUpTextType() {
        mEmailEditText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mreemailEditText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mPasswordEditText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mMaleTextView.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mFemaleTextView.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mbackToLanding.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mNextButton.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mSignUp.setTypeface(FontUtility.setMontserratLight(getActivity()));
    }

    @Override
    public void setClickListener() {
        mbackToLanding.setOnClickListener(this);
        mMaleTextView.setOnClickListener(this);
        mFemaleTextView.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            CommonUtility.hideSoftKeyboard(getActivity());
            validateUserInput();
            return true;
        }
        return false;
    }

    private void validateUserInput() {

        boolean isValid = true;
        String email = mEmailEditText.getText().toString().trim();
        String reemail = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String gender = mIsMaleSelected ? "male" : "female";
        if (ratherNotSelected)
            gender = "rather not";
        if (email.length() == 0) {
            isValid = false;
            mEmailEditText.requestFocus();
            this.mEmailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            AlertUtils.showToast(getActivity(), R.string.alert_register_email);
        } else if (!CommonUtility.isEmailValid(email)) {
            isValid = false;
            mEmailEditText.requestFocus();
            this.mEmailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            AlertUtils.showToast(getActivity(),
                    R.string.alert_register_email_verification);
        } else if (!reemail.equals(email)) {
            mreemailEditText.requestFocus();
            this.mreemailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            AlertUtils.showToast(getActivity(),
                    "Email does not match");
        } else if (password.length() == 0) {
            isValid = false;
            mPasswordEditText.requestFocus();
            this.mPasswordEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            AlertUtils.showToast(getActivity(), R.string.alert_register_password);
        } else if (password.length() < AppConstants.PASSWORD_MIN_LENGTH) {
            isValid = false;
            mPasswordEditText.requestFocus();
            this.mPasswordEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            AlertUtils.showToast(getActivity(), R.string.alert_register_password_length);
        }

        if (isValid && checkInternet()) {
            this.mNextButton.setTextColor(Color.WHITE);
            this.mEmailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew2);
            this.mreemailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew2);
            this.mPasswordEditText.setBackgroundResource(R.drawable.flatlayloginboardernew2);
            registerViaEamil(email, password, gender);
        }

    }

    private void registerViaEamil(String email, String password, String gender) {

        mProgressBarDialog = new ProgressBarDialog(getActivity());
        mProgressBarDialog.show();

        final RegisterUserApi service = new RegisterUserApi(this);
        service.registerViaEmail(email, password, gender, DeviceUtils.getPhoneModel(), CommonUtility.getDeviceTocken(getActivity()), Screen.UserNameScreen, "android", CommonUtility.getDeviceId(getActivity()));
        service.execute();

        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                service.cancel();
            }
        });
    }

    @Override
    public void handleOnSuccess(Object object) {
        mProgressBarDialog.dismiss();
        Syso.info("In handleOnSuccess>>" + object);
        if (object != null) {
            RegisterUserResponse response = (RegisterUserResponse) object;
            setUserPreferences(response);
            setHelpPreference();
            Bundle bundle = new Bundle();
            bundle.putString("email", "");
            //what about gender? password??
            startActivity(EditProfileActivity.class,bundle);
        }
        //necessary?
        getActivity().finish();
    }

    private void setUserPreferences(RegisterUserResponse response) {
        UserPreference.getInstance().setUserID(response.getId());
        UserPreference.getInstance().setCurrentScreen(Screen.UserNameScreen);
        UserPreference.getInstance().setAccessToken(response.gettoken());
        UserPreference.getInstance().setIsCreateWalletPin(true);
        UserPreference.getInstance().setcheckedIsConnected(true);
        UserPreference.getInstance().setEmail(mEmailEditText.getText().toString().trim());
        UserPreference.getInstance().setCartID(response.getCart_id());
        UserPreference.getInstance().setPassword();
    }

    private void setHelpPreference() {
        HelpPreference.getInstance().setHelpCart("yes");
        HelpPreference.getInstance().setHelpCollection("yes");
        HelpPreference.getInstance().setHelpInspiration("yes");
        HelpPreference.getInstance().setHelpKikrCards("yes");
        HelpPreference.getInstance().setHelpPinMenu("yes");
        HelpPreference.getInstance().setHelpSideMenu("yes");
        HelpPreference.getInstance().setHelpFriendsSideMenu("yes");
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        mProgressBarDialog.dismiss();
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            RegisterUserResponse response = (RegisterUserResponse) object;
            AlertUtils.showToast(getActivity(), response.getMessage());
        } else {
            AlertUtils.showToast(getActivity(), R.string.invalid_response);
        }
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }


    private class SignUpTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            String reEmail = mreemailEditText.getText().toString();

            if (email != null && email.length() != 0 && CommonUtility.isEmailValid(email)
                    && password != null && password.length() != 0 && password.length() >= AppConstants.PASSWORD_MIN_LENGTH
                    && reEmail != null && reEmail.equals(email)) {
                mNextButton.setTextColor(Color.WHITE);
            } else
                mNextButton.setTextColor(Color.GRAY);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}

