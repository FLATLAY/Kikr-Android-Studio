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
import com.flatlay.post_upload.FragmentPostUploadTag;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.bean.Inspiration;

/**
 * Created by RachelDi on 3/31/18.
 */

public class ChoiceOnPostDialog extends Dialog {
    private TextView edit_text, delete_text, cancel_text;
    private Context mContext;
    private Inspiration inspiration;


    public ChoiceOnPostDialog(@NonNull Context context,Inspiration inspiration) {
        super(context);
        this.mContext=context;
        this.inspiration=inspiration;
        init();

    }

    public ChoiceOnPostDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext=context;
        init();
    }

    protected ChoiceOnPostDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext=context;
        init();
    }

    private void init() {
        setContentView(R.layout.choiceonpost_dialog);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        edit_text = (TextView) findViewById(R.id.edit_text);
        edit_text.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        delete_text = (TextView) findViewById(R.id.delete_text);
        delete_text.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        cancel_text = (TextView) findViewById(R.id.cancel_text);
        cancel_text.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ((HomeActivity)mContext).addFragment(new FragmentPostUploadTag(inspiration, true));
            }
        });
        delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                com.flatlay.dialog.ConfirmDeleteDialog confirmDeleteDialog =
                        new com.flatlay.dialog.ConfirmDeleteDialog(mContext, inspiration.getInspiration_id(), inspiration.getUser_id());
                confirmDeleteDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                confirmDeleteDialog.show();
            }
        });
        cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
