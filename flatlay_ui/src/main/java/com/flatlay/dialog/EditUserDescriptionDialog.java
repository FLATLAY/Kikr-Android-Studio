package com.flatlay.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.EditProfileDescriptionApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CommonRes;
import com.flatlaylib.utils.AlertUtils;

public class EditUserDescriptionDialog extends Dialog implements TextView.OnEditorActionListener {
    private FragmentActivity mContext;
    private EditText editText;
    //	private Inspiration inspiration;
    private String description;
    private TextView descriptionTextView;


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

    public EditUserDescriptionDialog(FragmentActivity context, String description, TextView descriptionTextView) {
        super(context, R.style.AdvanceDialogTheme);
        mContext = context;
        this.description = description;
        this.descriptionTextView = descriptionTextView;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_edit_description);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        editText = (EditText) findViewById(R.id.desriptionEditTaxt);
        editText.setFilters(new InputFilter[]{filter});
        if (!TextUtils.isEmpty(description)) {
            editText.setText(description);
            editText.setSelection(editText.getText().length());
        }
        editText.setOnEditorActionListener(this);
        TextView cancelTextView = (TextView) findViewById(R.id.cancelTextView);
        TextView okTextView = (TextView) findViewById(R.id.okTextView);
        cancelTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtility.hideKeypad(mContext, editText);
                dismiss();
            }
        });
        okTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtility.hideKeypad(mContext, editText);
                validate();
            }
        });
    }

    protected void validate() {
        String description = editText.getText().toString().trim();
        if (description.length() == 0) {
            AlertUtils.showToast(mContext, "Enter Description");
            return;
        } else
            updateDescription(description);
    }

    private void updateDescription(final String description) {
//        mProgressBarDialog = new ProgressBarDialog(mContext);
//        mProgressBarDialog.show();

        final EditProfileDescriptionApi editProfileApi = new EditProfileDescriptionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
              //  mProgressBarDialog.dismiss();
                CommonRes commonRes = (CommonRes) object;
               // AlertUtils.showToast(mContext, "DescriUpdated");
                UserPreference.getInstance().setIsRefreshProfile(true);
                descriptionTextView.setText(description);
                dismiss();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
               // mProgressBarDialog.dismiss();
                if (object != null) {
                    CommonRes response = (CommonRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        editProfileApi.editDescription(UserPreference.getInstance().getUserID(), description);
        editProfileApi.execute();

//        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                editProfileApi.cancel();
//            }
//        });
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            CommonUtility.hideSoftKeyboard(mContext);
            validate();
            return true;
        }
        return false;
    }
}
