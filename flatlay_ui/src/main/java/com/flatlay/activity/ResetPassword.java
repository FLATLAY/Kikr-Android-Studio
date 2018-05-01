package com.flatlay.activity;

import android.graphics.Color;
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
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.RegisterUserApi;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class ResetPassword extends BaseFragment implements OnKeyListener,
        OnClickListener, ServiceCallback {

    private EditText resetPinEditText, passwordEditText, confirmPasswordEditText;
    private boolean resend = false;
    private TextView mResetPass, mbackToLanding,resendPinButton;
    private Button mReset;
    private View mainView;
    public final static String TAG="ResetPassword";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.reset2, container, false);
        Log.w(TAG,"ResetPassword");
        return mainView;
    }

    @Override
    public void initUI(Bundle savedInstanceState){
        resetPinEditText = (EditText) mainView.findViewById(R.id.resetPinEditText);
        passwordEditText = (EditText) mainView.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = (EditText) mainView.findViewById(R.id.confirmPasswordEditText);
        resendPinButton = (TextView) mainView.findViewById(R.id.resendPinButton);
        mResetPass = (TextView) mainView.findViewById(R.id.resetPassword);
        mbackToLanding = (TextView) mainView.findViewById(R.id.backToLanding);
        mReset = (Button) mainView.findViewById(R.id.resetButton);
        ResetTextWatcher watcher = new ResetTextWatcher();
        resetPinEditText.addTextChangedListener(watcher);
        passwordEditText.addTextChangedListener(watcher);
        confirmPasswordEditText.addTextChangedListener(watcher);
        setUpTextType();
    }

    @Override
    public void setClickListener() {
        confirmPasswordEditText.setOnKeyListener(this);
        resendPinButton.setOnClickListener(this);
        mbackToLanding.setOnClickListener(this);
        mReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resendPinButton:
                if (checkInternet()) {
                    resend = true;
                    doAuthanticate();
                }
                break;
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
        }
    }

    private void doAuthanticate() {
//        progressBarDialog = new ProgressBarDialog(getActivity());
//        progressBarDialog.show();
        final RegisterUserApi service = new RegisterUserApi(this);
        service.forgotPassword(getArguments().getString("email"));
        service.execute();

//        progressBarDialog.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                service.cancel();
//            }
//        });
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
        String resetpin = resetPinEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmpassword = confirmPasswordEditText.getText().toString().trim();

        if (resetpin.length() == 0) {
            isValid = false;
            resetPinEditText.requestFocus();
            AlertUtils.showToast(getActivity(), R.string.alert_blank_pin);
        } else if (password.length() == 0) {
            isValid = false;
            passwordEditText.requestFocus();
            AlertUtils.showToast(getActivity(), R.string.alert_blank_password);
        } else if (password.length() < AppConstants.PASSWORD_MIN_LENGTH) {
            isValid = false;
            passwordEditText.requestFocus();
            AlertUtils.showToast(getActivity(), R.string.alert_register_password_length);
        } else if (confirmpassword.length() == 0) {
            isValid = false;
            confirmPasswordEditText.requestFocus();
            AlertUtils.showToast(getActivity(), R.string.alert_register_confirm_password);
        } else if (!password.equals(confirmpassword)) {
            isValid = false;
            passwordEditText.requestFocus();
            AlertUtils.showToast(getActivity(), R.string.alert_password_not_matched);
        }
        if (isValid && checkInternet()) {
            doAuthanticate(resetpin, password);
        }
    }

    private void doAuthanticate(String resetpin, String password) {

//        progressBarDialog = new ProgressBarDialog(getActivity());
//        progressBarDialog.show();
        final RegisterUserApi service = new RegisterUserApi(this);
        service.resetPassword(getArguments().getString("email"), resetpin,
                password);
        service.execute();

//        progressBarDialog.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                service.cancel();
//            }
//        });
    }

    public void setUpTextType() {
        mResetPass.setTypeface(FontUtility.setMontserratLight(getActivity()));
        resetPinEditText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        passwordEditText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        confirmPasswordEditText.setTypeface(FontUtility.setMontserratLight(getActivity()));
        resendPinButton.setTypeface(FontUtility.setMontserratLight(getActivity()));
        mbackToLanding.setTypeface(FontUtility.setMontserratLight(getActivity()));
    }

    @Override
    public void handleOnSuccess(Object object) {
//        progressBarDialog.dismiss();
        Syso.info("In handleOnSuccess>>" + object);
        if (resend) {
            resend = false;
            AlertUtils.showToast(getActivity(), R.string.alert_pin_resent);
        } else {
            AlertUtils.showToast(getActivity(), R.string.alert_password_reset);
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
//        progressBarDialog.dismiss();
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

    private class ResetTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String pin = resetPinEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmpassword = confirmPasswordEditText.getText().toString().trim();

            if (pin != null && pin.length() != 0
                    && password != null && password.length() != 0 && password.length() >= AppConstants.PASSWORD_MIN_LENGTH
                    && confirmpassword != null && password.equals(confirmpassword)) {
                mReset.setTextColor(Color.WHITE);
            } else
                mReset.setTextColor(Color.GRAY);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
