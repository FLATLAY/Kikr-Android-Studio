package com.flatlay.fragment;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.PictureUtils;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.TaggedItem;
import com.flatlaylib.bean.TaggedProducts;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class FragmentInspirationPost extends BaseFragment implements
		OnClickListener {
	private LinearLayout tagPeopleLayout, tagBrandLayout;
	private TextView uploadTextView, tagBrandText, tagPeopleText;
	private ImageView postImageView;
	private EditText descriptionEditText;
	private Bitmap bmp;
	private ProgressBarDialog mProgressBarDialog;
	private TaggedItem taggedItem = new TaggedItem();
	private TaggedProducts taggedProducts = new TaggedProducts();
	private String description;
	byte[] byteArray;
	private String isImage;
	private String filePath;
	private Inspiration inspiration;
	private String imageUrl;

	public FragmentInspirationPost(Inspiration inspiration,String isImage) {
		this.inspiration = inspiration;
		this.isImage = isImage;
	}
	
	public FragmentInspirationPost(Bitmap bmp,String imageUrl, String isImage) {
		this.bmp = bmp;
		this.isImage = isImage;
		this.imageUrl = imageUrl;
	}
	
	public FragmentInspirationPost(byte[] byteArray,String isImage,String filePath) {
		this.byteArray = byteArray;
		this.filePath = filePath;
		this.isImage = isImage;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_inspiration_post, null);
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		tagPeopleLayout = (LinearLayout) getView().findViewById(R.id.tagPeopleLayout);
		tagBrandLayout = (LinearLayout) getView().findViewById(R.id.tagBrandLayout);
		uploadTextView = (TextView) getView().findViewById(R.id.uploadTextView);
		tagBrandText = (TextView) getView().findViewById(R.id.tagBrandText);
		tagPeopleText = (TextView) getView().findViewById(R.id.tagPeopleText);
		postImageView = (ImageView) getView().findViewById(R.id.postImageView);
		descriptionEditText = (EditText) getView().findViewById(R.id.descriptionEditText);
	}

	@Override
	public void setData(Bundle bundle) {
		if (isImage.equals("no")) {
			// MICRO_KIND, size: 96 x 96 thumbnail
			bmp = ThumbnailUtils.createVideoThumbnail(filePath,Thumbnails.MICRO_KIND);
			postImageView.setImageBitmap(bmp);
			tagPeopleLayout.setVisibility(View.GONE);
			tagBrandLayout.setVisibility(View.GONE);
		} else if(bmp!=null){
			postImageView.setImageBitmap(bmp);
		}else if(imageUrl!=null){
			CommonUtility.setImage(mContext, imageUrl, postImageView, R.drawable.dum_list_item_product);
		}
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		tagPeopleLayout.setOnClickListener(this);
		tagBrandLayout.setOnClickListener(this);
		uploadTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tagPeopleLayout:
			CommonUtility.hideSoftKeyboard(mContext);
			if (checkInternet()) {
				addFragment(new FragmentInspirationImageTag(bmp,imageUrl, taggedItem));
			}
			break;
		case R.id.tagBrandLayout:
			CommonUtility.hideSoftKeyboard(mContext);
			if (checkInternet()) {
				addFragment(new FragmentInspirationImageTag(bmp,imageUrl, taggedProducts));
			}
			break;
		case R.id.uploadTextView:
			CommonUtility.hideSoftKeyboard(mContext);
			validateInput();
			break;
		}
	}

	private void validateInput() {
		description = descriptionEditText.getText().toString().trim();
		if (bmp == null && imageUrl == null) {
			AlertUtils.showToast(mContext, R.string.alert_no_image_selected);
		} else if (TextUtils.isEmpty(description)) {
			AlertUtils.showToast(mContext,
					R.string.alert_no_description_entered);
		} else {
			if (checkInternet()) {
				new GetImage().execute();
				// uploadInspiration(description);
			}
		}
	}

	private void uploadInspiration(byte[] image) {
		final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(
				new ServiceCallback() {

					@Override
					public void handleOnSuccess(Object object) {
						mProgressBarDialog.dismiss();
						Syso.info("In handleOnSuccess>>" + object);
						InspirationRes inspirationRes = (InspirationRes) object;
						if (inspirationRes != null) {
							AlertUtils.showToast(mContext,
									inspirationRes.getMessage());
//							((HomeActivity) mContext).loadFragment(new FragmentInspirationSection());
							((HomeActivity) mContext).loadFragment(new FragmentDiscoverNew(2,false));
						}
					}

					@Override
					public void handleOnFailure(ServiceException exception, Object object) {
						mProgressBarDialog.dismiss();
						Syso.info("In handleOnFailure>>" + object);
						if (object != null) {
							InspirationRes response = (InspirationRes) object;
							AlertUtils.showToast(mContext, response.getMessage());
						} else {
							AlertUtils.showToast(mContext, R.string.invalid_response);
						}
					}
				});
		if (image != null || imageUrl !=null) {
			String width = bmp!=null? String.valueOf(bmp.getWidth()) : "";
			String height = bmp!=null? String.valueOf(bmp.getHeight()) : "";
			inspirationSectionApi.uploadImage(UserPreference.getInstance()
					.getUserID(), image, isImage, description, taggedItem,
					taggedProducts,width,height,imageUrl);
			inspirationSectionApi.execute();
		}else{
			mProgressBarDialog.dismiss();
			AlertUtils.showToast(mContext, "No image found");
		}
		
	}

	private class GetImage extends AsyncTask<Void, Void, byte[]> {

		@Override
		protected void onPreExecute() {
			mProgressBarDialog = new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
			super.onPreExecute();
		}

		@Override
		protected byte[] doInBackground(Void... params) {
			// TODO Auto-generated method stub
			if (isImage.equals("no")) {
				return byteArray;
			} else {
				return PictureUtils.getByteArray2(bmp);
			}
			
		}

		@Override
		protected void onPostExecute(byte[] result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			uploadInspiration(result);
		}

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Syso.info("In onHiddenChanged "
				+ taggedProducts.getSelectedProductsId().length());
		if (taggedProducts.getSelectedProductsId().length() > 0)
			tagBrandText.setText(taggedProducts.getSelectedProductsId().split(
					",").length
					+ " Product");
		else
			tagBrandText.setText("");

		if (!TextUtils.isEmpty(taggedItem.getSelectedItemType()))
			tagPeopleText.setText(taggedItem.getSelectedItemName());
		else
			tagPeopleText.setText("");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		HomeActivity.menuTextCartCount.setVisibility(View.VISIBLE);
	}
}
