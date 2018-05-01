package com.flatlay.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.RegisterUserApi;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.flatlaylib.utils.StringUtils;

public class ForgotPassword extends BaseFragment implements OnKeyListener,
        OnClickListener, ServiceCallback {
    private EditText mEmailEditText;
    private String email;
    private TextView mBackToLanding, mForgotPassword;
    private Button mReset;
    private View mainView;
    public final static String TAG="ForgotPassword";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.activity_email2, container, false);
        Log.w(TAG,"ForgotPassword");
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState){
        mEmailEditText = (EditText) mainView.findViewById(R.id.emailEditText);
        mEmailEditText.setTypeface(FontUtility.setProximanovaLight(getActivity()));
        mForgotPassword = (TextView) mainView.findViewById(R.id.forgotPassword);
        mBackToLanding = (TextView) mainView.findViewById(R.id.backToLanding);
        mBackToLanding.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mReset = (Button) mainView.findViewById(R.id.resetButton);
        ForgotPassTextWatcher watcher = new ForgotPassTextWatcher();
        mEmailEditText.addTextChangedListener(watcher);
        setUpTextType();
    }

    public void setUpTextType() {
        mEmailEditText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mForgotPassword.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mBackToLanding.setTypeface(FontUtility.setMontserratLight(getActivity()));
    }

    @Override
    public void setClickListener() {
        mEmailEditText.setOnKeyListener(this);
        mBackToLanding.setOnClickListener(this);
        mReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backToLanding:
                CommonUtility.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.baseFrameLayout, new LoginActivity(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.resetButton:
                CommonUtility.hideSoftKeyboard(getActivity());
                validateUserInput();
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            validateUserInput();
            return true;
        }
        return false;
    }

    private void validateUserInput() {

        boolean isValid = true;
        email = mEmailEditText.getText().toString().trim();

        if (email.length() == 0) {
            isValid = false;
            mEmailEditText.requestFocus();
            this.mEmailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            AlertUtils.showToast(getActivity(), R.string.alert_register_email);
        } else if (!StringUtils.isEmailValid(email)) {
            isValid = false;
            mEmailEditText.requestFocus();
            this.mEmailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew3);
            AlertUtils.showToast(getActivity(), R.string.alert_register_email_verification);
        }

        if (isValid && checkInternet()) {
            this.mEmailEditText.setBackgroundResource(R.drawable.flatlayloginboardernew2);
            this.mBackToLanding.setTextColor(Color.WHITE);
            doAuthanticate(email);
        }

    }

    private void doAuthanticate(String email) {
        final RegisterUserApi service = new RegisterUserApi(this);
        service.forgotPassword(email);
        service.execute();
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void handleOnSuccess(Object object) {
        Syso.info("In handleOnSuccess>>" + object);
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        ResetPassword reset = new ResetPassword();
        reset.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.baseFrameLayout, reset, null)
                .addToBackStack(null)
                .commit();

        AlertUtils.showToast(getActivity(), R.string.alert_email_has_sent);
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            RegisterUserResponse response = (RegisterUserResponse) object;
            AlertUtils.showToast(getActivity(), response.getMessage());
        } else {
            AlertUtils.showToast(getActivity(), R.string.invalid_response);
        }
    }

    private class ForgotPassTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String email = mEmailEditText.getText().toString();

            if (email != null && email.length() != 0 && CommonUtility.isEmailValid(email)) {
                mReset.setTextColor(Color.WHITE);
            } else
                mReset.setTextColor(Color.GRAY);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}


