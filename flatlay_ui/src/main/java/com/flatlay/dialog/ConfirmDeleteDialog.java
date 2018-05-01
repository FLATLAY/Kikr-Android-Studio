package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentDiscoverNew;
import com.flatlay.fragment.FragmentInspirationSection;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.DeletePostApi;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Constants;
import com.flatlaylib.utils.Syso;

/**
 * Created by RachelDi on 3/31/18.
 */

public class ConfirmDeleteDialog extends Dialog {
    private TextView yes_text, cancel_text, sure_text;
    private String inspiration_id,user_id;
    private DeletePostApi deletePostApi;
    private Context mContext;

    public ConfirmDeleteDialog(@NonNull Context context,String inspiration_id, final String user_id) {
        super(context);
        this.inspiration_id=inspiration_id;
        this.user_id=user_id;
        this.mContext=context;
        init();
    }

    public ConfirmDeleteDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext=context;
        init();
    }

    protected ConfirmDeleteDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext=context;
        init();
    }

    private void init() {
        setContentView(R.layout.confirm_delete_dialog);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        yes_text = (TextView) findViewById(R.id.yes_text);
        yes_text.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        cancel_text = (TextView) findViewById(R.id.cancel_text);
        cancel_text.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        sure_text = (TextView) findViewById(R.id.sure_text);
        sure_text.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        yes_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removePost(inspiration_id,user_id);
            }
        });
        cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public void removePost(final String inspiration_id, final String user_id) {
        this.inspiration_id = inspiration_id;
        this.user_id = user_id;

        deletePostApi = new DeletePostApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {

                if (object.toString().equals(Constants.WebConstants.SUCCESS_CODE)) {
                    dismiss();
                    ((HomeActivity)mContext).addFragment(new FragmentInspirationSection());
                    ((HomeActivity) mContext).mFragmentStack.clear();
                    ((HomeActivity) mContext).addFragment(new FragmentDiscoverNew());
                    ((HomeActivity) mContext).onBackPressed();
                }

            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        deletePostApi.removePost(inspiration_id, user_id);
        deletePostApi.execute();
    }
}
