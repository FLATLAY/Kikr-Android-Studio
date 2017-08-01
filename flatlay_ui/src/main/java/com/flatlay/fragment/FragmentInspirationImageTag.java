package com.flatlay.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.adapter.AutocompleteCustomArrayAdapter;
import com.flatlay.dialog.InspirationCollectionListDialog;
import com.flatlay.ui.CustomAutoCompleteView;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.TagView;
import com.flatlay.utility.AppConstants;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.EditInspirationApi;
import com.flatlaylib.api.InterestSectionApi;
import com.flatlaylib.api.SearchApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.InterestSection;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.SearchUser;
import com.flatlaylib.bean.TaggedItem;
import com.flatlaylib.bean.TaggedProducts;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.InterestSectionRes;
import com.flatlaylib.service.res.SearchRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class FragmentInspirationImageTag extends BaseFragment implements View.OnClickListener {
	private Bitmap bmp;
	private ImageView uploadImageView;
	private FrameLayout overlay;
	private CustomAutoCompleteView searchUserEditText;
	private List<InterestSection> list = new ArrayList<InterestSection>();
	private ArrayList<SearchUser> usersList = new ArrayList<SearchUser>();
//	private TextView textView;
	private TagView tagView;
	private ArrayList<String> temp_usersList = new ArrayList<String>();
	private ArrayList<String> temp_usersListId = new ArrayList<String>();
	private AutocompleteCustomArrayAdapter adapter;
	private float x = 0, y = 0;
	private RadioGroup tagRadioGroup;
	private RadioButton peopleBtn, storeBtn, brandBtn;
//	private String PEOPLE = "user";
//	private String BRAND = "brand";
//	private String STORE = "store";
	private String PRODUCT = "product";
	private String COLLECTION = "collection";
	private String isSelected;
	private ProgressBar progressBarUserTag;
	private TaggedItem taggedItemLocal=new TaggedItem(),selectedItem;
	private TaggedProducts taggedProducts;
	private boolean isTaggingProduct = false;
	private List<String> selectedProductsId=new ArrayList<String>();
	private List<String> selectedProducts=new ArrayList<String>();
	private List<String> selectedProductsXY=new ArrayList<String>();
	private String START_TEXT="Search";
	private float scaleFactor=1;
	private Inspiration inspiration;
	private String url;
	private ProgressBarDialog mProgressBarDialog;
	private String imageUrl;
	private LinearLayout linearEditTextView;
	private TextView collection_text;

	public FragmentInspirationImageTag(Bitmap bmp, String imageUrl, TaggedItem taggedItem) {
		this.bmp = bmp;
		this.selectedItem = taggedItem;
//		taggedItemLocal=selectedItem;
		taggedItemLocal.setSelectedItem(selectedItem.getSelectedItem());
		taggedItemLocal.setSelectedItemName(selectedItem.getSelectedItemName());
		taggedItemLocal.setSelectedItemType(selectedItem.getSelectedItemType());
		taggedItemLocal.setSelectedItemXY(selectedItem.getSelectedItemXY());
		isTaggingProduct = false;
		isSelected = COLLECTION;
		this.imageUrl = imageUrl;
	}
	
	public FragmentInspirationImageTag(Bitmap bmp,String imageUrl,  TaggedProducts taggedProducts) {
		this.bmp = bmp;
		this.taggedProducts = taggedProducts;
		isTaggingProduct = true;
		this.imageUrl = imageUrl;
		isSelected = PRODUCT;
	}

	public FragmentInspirationImageTag(Inspiration inspiration, TaggedProducts taggedProducts) {
		this.inspiration = inspiration;
		this.url = inspiration.getInspiration_image();
		this.taggedProducts = taggedProducts;
		isTaggingProduct = true;
		isSelected = PRODUCT;
	}
	
	public FragmentInspirationImageTag(Inspiration inspiration, TaggedItem taggedItem) {
		this.inspiration = inspiration;
		this.url = inspiration.getInspiration_image();
		this.selectedItem = taggedItem;
		taggedItemLocal.setSelectedItem(selectedItem.getSelectedItem());
		taggedItemLocal.setSelectedItemName(selectedItem.getSelectedItemName());
		taggedItemLocal.setSelectedItemType(selectedItem.getSelectedItemType());
		taggedItemLocal.setSelectedItemXY(selectedItem.getSelectedItemXY());
		isTaggingProduct = false;
		isSelected = COLLECTION;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		float aa=getResources().getDisplayMetrics().density;
		Syso.info("Device width: "+CommonUtility.getDeviceWidth(mContext)+", height: "+CommonUtility.getDeviceHeight(mContext)+", density: "+aa);

		return inflater.inflate(R.layout.fragment_inspiration_tag_image, null);
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		tagView=new TagView(mContext);
//		textView = new TextView(mContext);
//		textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		uploadImageView = (ImageView) getView().findViewById(R.id.tagImageView);
		progressBarUserTag = (ProgressBar) getView().findViewById(R.id.progressBarUserTag);
		tagRadioGroup = (RadioGroup) getView().findViewById(R.id.tagRadioGroup);
		peopleBtn = (RadioButton) getView().findViewById(R.id.peopleBtn);
		storeBtn = (RadioButton) getView().findViewById(R.id.storeBtn);
		brandBtn = (RadioButton) getView().findViewById(R.id.brandBtn);
		overlay = (FrameLayout) getView().findViewById(R.id.overlay);
		searchUserEditText = (CustomAutoCompleteView) getView().findViewById(R.id.searchUserEditText);
		linearEditTextView = (LinearLayout) getView().findViewById(R.id.linearEditTextView);
		collection_text = (TextView) getView().findViewById(R.id.collection_text);
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		collection_text.setOnClickListener(this);
	}

	
	@Override
	public void setData(Bundle bundle) {
		if (bmp!=null) {
			setScalingFactor();
		}
		setDetails();
		searchUserEditText.addTextChangedListener(new CustomAutoCompleteTextChangedListener(mContext));
        Log.w("FragmentInspDetail","3");
		adapter = new AutocompleteCustomArrayAdapter(mContext,R.layout.list_item, temp_usersList);
		searchUserEditText.setAdapter(adapter);
		searchUserEditText.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				CommonUtility.hideSoftKeyboard(mContext);
				Syso.info("abcd:   " + temp_usersList.get(arg2));
//				textView.setText(temp_usersList.get(arg2));
//				setText(textView, temp_usersList.get(arg2));
				tagView.setTagText(temp_usersList.get(arg2));
				if (isTaggingProduct) {
					if (!selectedProductsId.contains(temp_usersListId.get(arg2))) {
						selectedProductsId.add(temp_usersListId.get(arg2));
						selectedProductsXY.add(x+","+y);
						selectedProducts.add(temp_usersList.get(arg2));
					}
				} else {
					taggedItemLocal.setSelectedItem(temp_usersListId.get(arg2));
					taggedItemLocal.setSelectedItemName(temp_usersList.get(arg2));
					taggedItemLocal.setSelectedItemType(isSelected);
					taggedItemLocal.setSelectedItemXY(x + "," + y);
					Syso.info("12345678 taggedItemLocal>>"+taggedItemLocal);
				}
				searchUserEditText.setText("");
			}
		});
		if (url!=null) 
			CommonUtility.setImage(mContext, url, uploadImageView, R.drawable.dum_list_item_brand);
		else if(bmp!=null){
			uploadImageView.setImageBitmap(bmp);
		Syso.info("1234567>>>>> width:" + bmp.getWidth() + ", height:"
				+ bmp.getHeight());
		}else if(imageUrl != null){
			CommonUtility.setImage(mContext, imageUrl, uploadImageView, R.drawable.dum_list_item_brand);
		}


//		tagRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
//				taggedItemLocal = new TaggedItem();
//				if (checkedId == R.id.peopleBtn) {
//					isSelected = PEOPLE;
//					//removeTag();
//				} else if (checkedId == R.id.storeBtn) {
//					isSelected = STORE;
//					//removeTag();
//				} else if (checkedId == R.id.brandBtn) {
//					isSelected = BRAND;
//					//removeTag();
//				}
//			}
//		});
	}

	
	private void setDetails() {
		if (taggedItemLocal!=null&&!isTaggingProduct) {
			if (!TextUtils.isEmpty(taggedItemLocal.getSelectedItemName())){
//				textView.setText(taggedItemLocal.getSelectedItemName());
				tagView.setTagText(taggedItemLocal.getSelectedItemName());
//				setText(textView,taggedItemLocal.getSelectedItemName());
				overlay.addView(tagView);
//				textView.setBackgroundColor(getResources().getColor(R.color.black));
//				textView.setTextColor(getResources().getColor(R.color.white));
			}
			if (!TextUtils.isEmpty(taggedItemLocal.getSelectedItemXY())) {
				String[] xy = taggedItemLocal.getSelectedItemXY().split(",");
//				textView.setX(Float.parseFloat(xy[0]));
//				textView.setY(Float.parseFloat(xy[1]));
				tagView.setXY(Float.parseFloat(xy[0])/scaleFactor, Float.parseFloat(xy[1])/scaleFactor);
				taggedItemLocal.setSelectedItemXY(Float.parseFloat(xy[0])/scaleFactor+","+Float.parseFloat(xy[1])/scaleFactor);
			}
//			setRadioBtn();
			linearEditTextView.setVisibility(View.GONE);
		} else if(taggedProducts!=null){
			tagRadioGroup.setVisibility(View.GONE);
			collection_text.setVisibility(View.GONE);
			if (!TextUtils.isEmpty(taggedProducts.getSelectedProductsXY())) {
				setPoints(taggedProducts.getSelectedProductsXY());
			}
		}
	}

	private void setPoints(String selectedProductxy) {
		String[] points = selectedProductxy.split("-");
		for (int i = 0; i < points.length; i++) {
			Syso.info("1234567890 point is comming........");
			String[] xy = points[i].split(",");
//			TextView textView = new TextView(mContext);
			TagView tagView=new TagView(mContext);
//			textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//			textView.setText(taggedProducts.getSelectedProducts().split(",")[i]);
//			setText(textView, taggedProducts.getSelectedProducts().split(",")[i]);
			tagView.setTagText( taggedProducts.getSelectedProducts().split(AppConstants.NAME_SEPRATER)[i]);
			overlay.addView(tagView);
//			textView.setBackgroundColor(getResources().getColor(R.color.black));
//			textView.setTextColor(getResources().getColor(R.color.white));
//			textView.setX(Float.parseFloat(xy[0]));
//			textView.setY(Float.parseFloat(xy[1]));
			tagView.setXY(Float.parseFloat(xy[0])/scaleFactor, Float.parseFloat(xy[1])/scaleFactor);
			selectedProductsId.add(taggedProducts.getSelectedProductsId().split(",")[i]);
			selectedProductsXY.add(Float.parseFloat(xy[0])/scaleFactor+","+Float.parseFloat(xy[1])/scaleFactor);
			selectedProducts.add(taggedProducts.getSelectedProducts().split(AppConstants.NAME_SEPRATER)[i]);
		}
	}

//	private void setRadioBtn() {
//
//		if (taggedItemLocal.getSelectedItemType().equals(BRAND)) {
//			brandBtn.setChecked(true);
//		}else if (taggedItemLocal.getSelectedItemType().equals(PEOPLE)) {
//			peopleBtn.setChecked(true);
//		}else if (taggedItemLocal.getSelectedItemType().equals(STORE)) {
//			storeBtn.setChecked(true);
//		}
//	}

	private void removeTag() {
		if (overlay.getChildCount() > 1) {
			overlay.removeViewAt(overlay.getChildCount() - 1);
		}
		searchUserEditText.setVisibility(View.GONE);
		searchUserEditText.setText("");
	}
	
	private void searchProduct(final String userInput) {
		progressBarUserTag.setVisibility(View.VISIBLE);
		final SearchApi searchApi = new SearchApi(
				new ServiceCallback() {

					@Override
					public void handleOnSuccess(Object object) {
						progressBarUserTag.setVisibility(View.GONE);
						Syso.info("In handleOnSuccess>>" + object);
						SearchRes interestSectionRes = (SearchRes) object;
						List<Product> list = interestSectionRes.getData();
						if (list.size() > 0) {
							usersList.clear();
							temp_usersListId.clear();
							temp_usersList.clear();
							for (int i = 0; i < list.size(); i++) {
								SearchUser searchUser = new SearchUser();
								searchUser.setUserId(list.get(i).getId());
								searchUser.setUsername(list.get(i).getProductname());
								addSearchUser(searchUser);
							}
							setAdapter(userInput);
						}
					}

					@Override
					public void handleOnFailure(ServiceException exception,
							Object object) {
						progressBarUserTag.setVisibility(View.GONE);
						Syso.info("In handleOnFailure>>" + object);
						if (object != null) {
							SearchRes response = (SearchRes) object;
							AlertUtils.showToast(mContext,response.getMessage());
						} else {
							AlertUtils.showToast(mContext,R.string.invalid_response);
						}
					}
				});
		searchApi.searchProduct(UserPreference.getInstance().getUserID(), userInput, "0");
		searchApi.execute();
	}
	
	private void search(final String userInput) {
		progressBarUserTag.setVisibility(View.VISIBLE);
		final InterestSectionApi interestSectionApi = new InterestSectionApi(
				new ServiceCallback() {

					@Override
					public void handleOnSuccess(Object object) {
						progressBarUserTag.setVisibility(View.GONE);
						Syso.info("In handleOnSuccess>>" + object);
						InterestSectionRes interestSectionRes = (InterestSectionRes) object;
						list = interestSectionRes.getData();
						if (list.size() > 0) {
							usersList.clear();
							temp_usersListId.clear();
							temp_usersList.clear();
							for (int i = 0; i < list.size(); i++) {
								SearchUser searchUser = new SearchUser();
								searchUser.setUserId(list.get(i).getId());
//								if (isSelected.equals(PEOPLE))
//									searchUser.setUsername(list.get(i).getUsername());
//								else
//									searchUser.setUsername(list.get(i).getName());
								addSearchUser(searchUser);
							}
							setAdapter(userInput);
						}
					}

					@Override
					public void handleOnFailure(ServiceException exception,
							Object object) {
						progressBarUserTag.setVisibility(View.GONE);
						Syso.info("In handleOnFailure>>" + object);
						if (object != null) {
							InterestSectionRes response = (InterestSectionRes) object;
							AlertUtils.showToast(mContext,response.getMessage());
						} else {
							AlertUtils.showToast(mContext,R.string.invalid_response);
						}
					}
				});
		Syso.info("isselected:  " + isSelected);
//		if (isSelected.equalsIgnoreCase(STORE)) {
//			interestSectionApi.searchStore(UserPreference.getInstance()
//					.getUserID(), userInput, "0");
//		} else if (isSelected.equalsIgnoreCase(BRAND)) {
//			interestSectionApi.searchBrand(UserPreference.getInstance()
//					.getUserID(), userInput, "0");
//		} else if (isSelected.equalsIgnoreCase(PEOPLE)) {
//			interestSectionApi.searchUser(UserPreference.getInstance()
//					.getUserID(), userInput, "0");
//		}
//		interestSectionApi.execute();
	}

	protected void addSearchUser(SearchUser searchUser) {
		boolean isContain = false;
		Syso.info("111111111111111111111 >" + searchUser.getUserId());
		for (int i = 0; i < usersList.size(); i++) {
			if (usersList.get(i).getUserId().equals(searchUser.getUserId())){
				isContain = true;
			}
		}
		for(int j=0;j<selectedProductsId.size();j++){
			Syso.info("22222222222222 >"+selectedProductsId.get(j));
			if(selectedProductsId.get(j).equals(searchUser.getUserId())){
				isContain = true;
			}
		}
		if (!isContain && !TextUtils.isEmpty(searchUser.getUsername()))
			usersList.add(searchUser);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.collection_text:
				InspirationCollectionListDialog inspirationCollectionListDialog  =new InspirationCollectionListDialog(mContext,taggedItemLocal);
				inspirationCollectionListDialog.show();
				break;
		}
	}

	public class CustomAutoCompleteTextChangedListener implements TextWatcher {

		public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
		Context context;

		public CustomAutoCompleteTextChangedListener(Context context) {
			this.context = context;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence userInput, int start,
				int before, int count) {
			try {
				if (userInput.toString().length() > 1) {
					if (isSelected.equals(PRODUCT) && checkInternet()) {
						searchProduct(userInput.toString());
					} else if(checkInternet()){
						search(userInput.toString());
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setAdapter(String userInput) {
		temp_usersList.clear();
		temp_usersListId.clear();
		adapter.notifyDataSetChanged();
		for (SearchUser ss : usersList) {
			if (ss != null) {
				if (ss.getUsername().toUpperCase()
						.contains(userInput.toString().toUpperCase())) {
					temp_usersList.add(ss.getUsername());
					temp_usersListId.add(ss.getUserId());
				}
			}
		}
		adapter.notifyDataSetChanged();
		Syso.info("12345678 Temp user list>>>>"+temp_usersList);
        Log.w("FragmentInspDetail","4");
		adapter = new AutocompleteCustomArrayAdapter(mContext,R.layout.list_item, temp_usersList);
		temp_usersList.add(userInput);
		temp_usersListId.add("0");
		searchUserEditText.setAdapter(adapter);
	}
	
	public void done() {
		if (isTaggingProduct) {
			Syso.info("abcd:  "+getIDList());
			Syso.info("abcd:  "+getXYList());
			Syso.info("abcd:  "+getNameList());
		taggedProducts.setSelectedProductsId(getIDList());
		taggedProducts.setSelectedProductsXY(getXYList());
		taggedProducts.setSelectedProducts(getNameList());
		if (url!=null) {
			removeTags();
		}
		} else {
			taggedItemLocal.setSelectedItemXY(x + "," + y);
			taggedItemLocal.setSelectedItemType(COLLECTION);
			Syso.info("abcd:  "+taggedItemLocal.getSelectedItemName());
			Syso.info("abcd:  "+taggedItemLocal.getSelectedItem());
			Syso.info("abcd:  "+taggedItemLocal.getSelectedItemType());
			Syso.info("abcd:  "+taggedItemLocal.getSelectedItemXY());
			selectedItem.setSelectedItemName(taggedItemLocal.getSelectedItemName());
			selectedItem.setSelectedItem(taggedItemLocal.getSelectedItem());
			selectedItem.setSelectedItemType(taggedItemLocal.getSelectedItemType());
			selectedItem.setSelectedItemXY(getActualValue(taggedItemLocal.getSelectedItemXY()));
			if (url!=null) {
				removeTags();
			}
		}
	}
	
	private void removeTags() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final EditInspirationApi editInspirationApi = new EditInspirationApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
//				mProgressBarDialog.dismiss();
				addTags();
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
//				mProgressBarDialog.dismiss();
				addTags();
			}
		});
		if (isTaggingProduct) {
			editInspirationApi.removeProductsTags(UserPreference.getInstance().getUserID(),
				inspiration.getInspiration_id());
		}else{
		editInspirationApi.removeTag(UserPreference.getInstance().getUserID(),
				inspiration.getInspiration_id());
		}
		editInspirationApi.execute();
	}


	private void addTags() {
//		mProgressBarDialog = new ProgressBarDialog(mContext);
//		mProgressBarDialog.show();
		final EditInspirationApi editInspirationApi = new EditInspirationApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				if (isTaggingProduct) {
					inspiration.setProduct_id(taggedProducts.getSelectedProductsId());
					inspiration.setProduct_name(taggedProducts.getSelectedProducts());
					inspiration.setProduct_xy(taggedProducts.getSelectedProductsXY());
				}else{
					inspiration.setItem_id(selectedItem.getSelectedItem());
					inspiration.setItem_type(selectedItem.getSelectedItemType());
					inspiration.setItem_name(selectedItem.getSelectedItemName());
					inspiration.setItem_xy(selectedItem.getSelectedItemXY());
				}
				((HomeActivity)mContext).loadFragment(new FragmentInspirationDetail(inspiration,false));
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
			}
		});
		if (isTaggingProduct) {
			editInspirationApi.addTag(UserPreference.getInstance().getUserID(),
					inspiration.getInspiration_id(),
					"product",
					taggedProducts.getSelectedProductsId(),
					taggedProducts.getSelectedProducts(),
					taggedProducts.getSelectedProductsXY());
		} else {
			editInspirationApi.addTag(UserPreference.getInstance().getUserID(),
					inspiration.getInspiration_id(),
					selectedItem.getSelectedItemType(),
					selectedItem.getSelectedItem(),
					selectedItem.getSelectedItemName(),
					selectedItem.getSelectedItemXY());
		}
		editInspirationApi.execute();
	}
	
	private String getIDList() {
		String selectedProducts = "";
		for (int i = 0; i < selectedProductsId.size(); i++) {
			if (i == 0) {
				selectedProducts = selectedProducts + ""
						+ selectedProductsId.get(i);
			} else
				selectedProducts = selectedProducts + ","
						+ selectedProductsId.get(i);
		}
		return selectedProducts;
	}
	
	private String getNameList() {
		String selectedProductslist = "";
		for (int i = 0; i < selectedProducts.size(); i++) {
			if (i == 0) {
				selectedProductslist = selectedProductslist + ""
						+ selectedProducts.get(i);
			} else
				selectedProductslist = selectedProductslist +AppConstants.NAME_SEPRATER
						+ selectedProducts.get(i);
		}
		Syso.info("product name>>>>"+selectedProductslist);
		return selectedProductslist;
	}
	
	private String getXYList() {
		String selectedProducts = "";
		for (int i = 0; i < selectedProductsXY.size(); i++) {
			String xy=getActualValue(selectedProductsXY.get(i));
			if (i == 0) {
				selectedProducts = selectedProducts + ""
						+ xy;
			} else
				selectedProducts = selectedProducts + "-"
						+ xy;
		}
		return selectedProducts;
	}
	
//	private void setText(TextView tv,String value){
//		if(tv!=null&&value!=null){
//			if(value.length()>15){
//				String newValue=value.substring(0,15)+"...";
//				tv.setText(newValue);
//			}else{
//				tv.setText(value);
//			}
//		}
//	}
	
	private String getActualValue(String string) {
		try{
		float x=Float.parseFloat(string.split(",")[0]);
		float y=Float.parseFloat(string.split(",")[1]);
		float newX=x*scaleFactor;
		float newY=y*scaleFactor;
		return newX+","+newY;
		}catch(Exception e){
			e.printStackTrace();
			return string;
		}
	}

	private void setScalingFactor() {
		float dWidth=CommonUtility.getDeviceWidth(mContext);
		float dHeight=CommonUtility.getDeviceHeight(mContext);
		float imgWidth=bmp.getWidth();
		float imgHeight=bmp.getHeight();
		if(imgWidth>dWidth){
			scaleFactor=imgWidth/dWidth;
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		HomeActivity.menuTextCartCount.setVisibility(View.INVISIBLE);
	}
	
}
