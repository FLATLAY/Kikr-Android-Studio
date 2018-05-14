package com.flatlay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.ChangePasswordApi;
import com.flatlaylib.api.RegisterUserApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.ChangePasswordRes;
import com.flatlaylib.service.res.RegisterUserResponse;
import com.flatlaylib.service.res.WalletPinRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.flatlaylib.utils.StringUtils;

public class ChangeEmailActivity extends BaseActivity implements OnKeyListener, OnClickListener {
    private EditText emailEditText;
    public final static String TAG = "ChangeEmailActivity";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.w(TAG, "ChangeEmailActivity");
        CommonUtility.noTitleActivity(context);
        setContentView(R.layout.activity_change_email);
    }

    @Override
    public void initLayout() {
        emailEditText = (EditText) findViewById(R.id.emailEditText);
    }

    @Override
    public void setupData() {
        if (getIntent().hasExtra("email")) {
            String email = getIntent().getStringExtra("email");
            emailEditText.setText(email);
            emailEditText.setSelection(emailEditText.length());
        }
    }

    @Override
    public void headerView() {
        setBackHeader();
        getLeftTextView().setOnClickListener(this);
        setGoToHome();
    }

    @Override
    public void setUpTextType() {
        emailEditText.setTypeface(FontUtility.setProximanovaSemibold(context));
    }

    @Override
    public void setClickListener() {
        emailEditText.setOnKeyListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftTextView:
                CommonUtility.hideSoftKeyboard(context);
                finish();
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
        String email = emailEditText.getText().toString().trim();

        if (email.length() == 0) {
            isValid = false;
            emailEditText.requestFocus();
            AlertUtils.showToast(context, R.string.alert_register_email);
        } else if (!StringUtils.isEmailValid(email)) {
            isValid = false;
            emailEditText.requestFocus();
            AlertUtils.showToast(context, R.string.alert_register_email_verification);
        }
        if (isValid && checkInternet()) {
            doAuthanticate(email);
        }
    }

    private void doAuthanticate(String email) {

        final RegisterUserApi service = new RegisterUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                if (object != null) {


                    RegisterUserResponse response = (RegisterUserResponse) object;
                    UserPreference.getInstance().setUserID(response.getId());
                    UserPreference.getInstance().setAccessToken(response.gettoken());

                    String email = emailEditText.getText().toString().trim();
                    Intent intent = new Intent();
                    intent.putExtra("email", email);
                    setResult(Activity.RESULT_OK, intent);
                    UserPreference.getInstance().setEmail(emailEditText.getText().toString().trim());
                    AlertUtils.showToast(context, "Email updated successfully");
                    finish();
                    if (UserPreference.getInstance().getPassword() == "")
                        if (checkInternet())
                            checkPassword();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    RegisterUserResponse response = (RegisterUserResponse) object;
                    AlertUtils.showToast(context, response.getMessage());
                } else {
                    AlertUtils.showToast(context, R.string.invalid_response);
                }

            }
        });
        service.updateEmail(UserPreference.getInstance().getUserID(), email);
        service.execute();
    }

    private void checkPassword() {

        final ChangePasswordApi service = new ChangePasswordApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    ChangePasswordRes response = (ChangePasswordRes) object;
                    if (!TextUtils.isEmpty(response.getPassword_created()) && response.getPassword_created().equals("yes")) {
                        Intent i = new Intent(context, ChangePasswordActivity.class);
                        i.putExtra("isCreatePassword", false);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(context, ChangePasswordActivity.class);
                        i.putExtra("isCreatePassword", true);
                        i.putExtra("firsttime", true);
                        startActivity(i);
                    }
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (object != null) {
                    WalletPinRes response = (WalletPinRes) object;
                    AlertUtils.showToast(context, response.getMessage());
                } else {
                    AlertUtils.showToast(context, R.string.invalid_response);
                }
            }
        });
        service.checkPasswordCreated(UserPreference.getInstance().getUserID());
        service.execute();
    }
}
