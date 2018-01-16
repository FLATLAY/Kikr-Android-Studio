package com.flatlay.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
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
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.LoginUserApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.DeviceUtils;
import com.flatlaylib.utils.StringUtils;
import com.flatlaylib.utils.Syso;


public class LoginActivity extends BaseFragment implements OnClickListener, OnKeyListener, ServiceCallback {
    private View mainView;
    private TextView mBackButton, mLogin;
    private TextView mForgotPassword;
    private Button mLoginButton;
    private EditText mEmailEditText, mPasswordEditText;
    private ProgressBarDialog progressBarDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.login2, container, false);
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

        mBackButton = (TextView) mainView.findViewById(R.id.backToLanding);
        mForgotPassword = (TextView) mainView.findViewById(R.id.forgotPasswordTextView);
        mEmailEditText = (EditText) mainView.findViewById(R.id.emailEditText);
        mPasswordEditText = (EditText) mainView.findViewById(R.id.passwordEditText);
        mLoginButton = (Button) mainView.findViewById(R.id.loginButton);
        mLogin = (TextView) mainView.findViewById(R.id.loginText);
        LoginTextWatcher watcher = new LoginTextWatcher();
        mPasswordEditText.addTextChangedListener(watcher);
        mEmailEditText.addTextChangedListener(watcher);
        setUpTextType();
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    private class LoginTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String email = mEmailEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            if (email != null && email.length() != 0
                    && StringUtils.isEmailValid(email)
                    && password != null && password.length() != 0) {
                mLoginButton.setTextColor(Color.WHITE);
            } else
                mLoginButton.setTextColor(Color.GRAY);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public void setUpTextType() {
        mEmailEditText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mPasswordEditText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mBackButton.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mLoginButton.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mLogin.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mForgotPassword.setTypeface(FontUtility.setMontserratLight(getActivity()));
    }

    @Override
    public void setClickListener() {
        mBackButton.setOnClickListener(this);
        mForgotPassword.setOnClickListener(this);
        mPasswordEditText.setOnKeyListener(this);
        mLoginButton.setOnClickListener(this);
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
            case R.id.forgotPasswordTextView:
                this.mPasswordEditText.setBackgroundResource(R.drawable.flatlayloginboardernew2);
                CommonUtility.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.baseFrameLayout, new ForgotPassword(), null)
                        .addToBackStack(null)
                        .commit();
                mEmailEditText.setText("");
                mPasswordEditText.setText("");
                break;
            case R.id.loginButton:
                CommonUtility.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.baseFrameLayout, new LoginActivity(), null)
                        .addToBackStack(null)
                        .commit();
                validateUserInput();
                break;
        }

    }

    private void validateUserInput() {

        boolean isValid = true;
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if (email.length() == 0) {
            isValid = false;
            this.mEmailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            mEmailEditText.requestFocus();
            AlertUtils.showToast(getActivity(), R.string.alert_register_email);
        } else if (!StringUtils.isEmailValid(email)) {
            isValid = false;
            mEmailEditText.requestFocus();
            this.mEmailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            AlertUtils.showToast(getActivity(), R.string.alert_register_email_verification);
        } else if (password.length() == 0) {
            isValid = false;
            mPasswordEditText.requestFocus();
            this.mPasswordEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            AlertUtils.showToast(getActivity(), R.string.alert_register_password);
        }

        if (isValid) {
            mLoginButton.setTextColor(Color.WHITE);
            this.mEmailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew2);
            this.mPasswordEditText.setBackgroundResource(R.drawable.flatlayloginboardernew2);
            loginViaEamil(email, password);
        }
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

    private void loginViaEamil(String email, String password) {
        progressBarDialog = new ProgressBarDialog(getActivity());
        progressBarDialog.show();

        final LoginUserApi service = new LoginUserApi(this);
        service.loginViaEmail(email, password, DeviceUtils.getPhoneModel(),
                CommonUtility.getDeviceTocken(getActivity()), "android",
                CommonUtility.getDeviceId(getActivity()));
        service.execute();

        progressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                service.cancel();
            }
        });
    }

    @Override
    public void handleOnSuccess(Object object) {
        progressBarDialog.dismiss();
        Syso.info("In handleOnSuccess>>" + object);
        if (object != null) {
            RegisterUserResponse response = (RegisterUserResponse) object;
            String currentScreen = response.getCurrent_screen();
            UserPreference.getInstance().setUserID(response.getId());
            UserPreference.getInstance().setIsCreateWalletPin(true);
            UserPreference.getInstance().setcheckedIsConnected(true);

            UserPreference.getInstance().setEmail(response.getEmail());
            UserPreference.getInstance().setAccessToken(response.gettoken());
            UserPreference.getInstance().setUserName(response.getUsername());
            UserPreference.getInstance().setCartID(response.getCart_id());
            UserPreference.getInstance().setProfilePic(response.getProfile_pic());
            UserPreference.getInstance().setBgImage(response.getBackground_pic());

            if (!TextUtils.isEmpty(currentScreen)) {
                CommonUtility.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.baseFrameLayout, new IntroductionActivity(), null)
                        .addToBackStack(null)
                        .commit();
            } else {
            }
            UserPreference.getInstance().setPassword();
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        progressBarDialog.dismiss();
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            RegisterUserResponse response = (RegisterUserResponse) object;
            AlertUtils.showToast(getActivity(), response.getMessage());
        } else {
            String invaild = "Invalid Email Id/Password";
            AlertUtils.showToast(getActivity(), invaild);
        }
    }

}
