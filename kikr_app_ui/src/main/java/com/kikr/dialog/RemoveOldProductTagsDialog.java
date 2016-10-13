package com.kikr.dialog;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.Session;
import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentInspirationImageTag;
import com.kikr.sessionstore.SessionStore;
import com.kikr.utility.PictureUtils;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.TaggedItem;
import com.kikrlib.bean.TaggedProducts;
import com.kikrlib.db.DatabaseHelper;
import com.kikrlib.db.HelpPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.db.dao.FavoriteDealsDAO;
import com.kikrlib.db.dao.UuidDAO;

public class RemoveOldProductTagsDialog extends Dialog{
	private FragmentActivity mContext;
	private TextView cancelTextView,okTextView;
	private Inspiration inspiration;

	public RemoveOldProductTagsDialog(FragmentActivity mContext, Inspiration inspiration) {
		super(mContext, R.style.AdvanceDialogTheme);
		this.mContext=mContext;
		this.inspiration = inspiration;
		init();
	}

	public RemoveOldProductTagsDialog(Context context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_remove_old_tag);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		cancelTextView= (TextView) findViewById(R.id.cancelTextView);
		okTextView = (TextView) findViewById(R.id.okTextView);
		cancelTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		okTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				((HomeActivity)mContext).loadFragment(new FragmentInspirationImageTag(inspiration, new TaggedProducts()));
			}
		});
	}
}
