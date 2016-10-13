package com.kikr.dialog;

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentInspirationImageTag;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.bean.TaggedItem;
import com.kikrlib.bean.TaggedProducts;

public class EditInspirationDialog extends Dialog implements android.view.View.OnClickListener{
	private FragmentActivity mContext;
	private TextView editDescriptionText,removeTagText,removeProductsTagText,addTagText,addProductsTagText,cancelText;
	private Inspiration inspiration;
	private TextView deletePost;
	
	public EditInspirationDialog(FragmentActivity context, Inspiration inspiration) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		this.inspiration = inspiration;
		init();
	}

	public EditInspirationDialog(FragmentActivity context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}
	
	private void init() {
		setContentView(R.layout.dialog_edit_inspiration);
		setCancelable(true);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		editDescriptionText = (TextView) findViewById(R.id.editDescriptionText);
		removeTagText = (TextView) findViewById(R.id.removeTagText);
		removeProductsTagText = (TextView) findViewById(R.id.removeProductsTagText);
		addTagText = (TextView) findViewById(R.id.addTagText);
		addProductsTagText = (TextView) findViewById(R.id.addProductsTagText);
		cancelText = (TextView) findViewById(R.id.cancelText);
		deletePost = (TextView) findViewById(R.id.deletePost);
		editDescriptionText.setOnClickListener(this);
		removeTagText.setOnClickListener(this);
		removeProductsTagText.setOnClickListener(this);
		addTagText.setOnClickListener(this);
		addProductsTagText.setOnClickListener(this);
		cancelText.setOnClickListener(this);
		deletePost.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.editDescriptionText:
			EditDescriptionDialog editDescriptionDialog = new EditDescriptionDialog(mContext, inspiration);
			editDescriptionDialog.show();
			dismiss();
			break;
		case R.id.removeTagText:
			RemoveTagDialog removeTagDialog = new RemoveTagDialog(mContext, inspiration);
			removeTagDialog.show();	
			dismiss();
			break;
		case R.id.removeProductsTagText:
			RemoveProductsTagDialog removeProductsTagDialog = new RemoveProductsTagDialog(mContext, inspiration);
			removeProductsTagDialog.show();	
			dismiss();
			break;
		case R.id.addTagText:
			if (!inspiration.getItem_xy().equals("")) {
				RemoveOldTagsDialog removeOldTagsDialog = new RemoveOldTagsDialog(mContext, inspiration);
				removeOldTagsDialog.show();
			} else {
				((HomeActivity)mContext).loadFragment(new FragmentInspirationImageTag(inspiration, new TaggedItem()));
			}
			dismiss();
			break;
		case R.id.addProductsTagText:
			if (!inspiration.getProduct_xy().equals("")) {
				RemoveOldProductTagsDialog removeOldProductTagsDialog = new RemoveOldProductTagsDialog(mContext, inspiration);
				removeOldProductTagsDialog.show();
			} else {
				((HomeActivity)mContext).loadFragment(new FragmentInspirationImageTag(inspiration, new TaggedProducts()));
			}
			dismiss();
			break;
		case R.id.deletePost:
			DeletePostDialog deletePostDialog = new DeletePostDialog(mContext, inspiration);
			deletePostDialog.show();	
			dismiss();
			break;
		case R.id.cancelText:
			dismiss();
			break;
		}
	}

}
