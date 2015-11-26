package com.kikr.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.adapter.LifestyleImageAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.InstagramCallBack;
import com.kikr.utility.InstagramUtility;
import com.kikrlib.api.EditProfileApi;
import com.kikrlib.bean.BgImage;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.EditProfileRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

public class DefaultLifestyleImagesActivity extends BaseActivity implements OnClickListener, ServiceCallback,OnItemClickListener{

	GridView imagesList;
	private ProgressBarDialog mProgressBarDialog;
	private LifestyleImageAdapter lifestyleImageAdapter;
//	List<BgImage> bgImages = new ArrayList<BgImage>();
	List<String> bgImageList = new ArrayList<String>();
	int count = 50;
	boolean isLoadDefault = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CommonUtility.noTitleActivity(context);
		setContentView(R.layout.activity_default_lifestyle_images);
		if(checkInternet()){
			if(getIntent().hasExtra("isDefault")&&!getIntent().getBooleanExtra("isDefault", true)){
				getInstagramList(count,true);
				isLoadDefault = false;
			}
			else
				getBgList();
		}
	}
	
	private void getInstagramList(int count,boolean isShowLoader) {
		isLoading = true;
		InstagramUtility instagramUtility = new InstagramUtility(context, false, count+"", isShowLoader, new InstagramCallBack() {
			@Override
			public void setProfilePic(String url) {}
			
			@Override
			public void setPictureList(ArrayList<String> photoList) {
				if(photoList.size()==10)
					isLoading = false;
				bgImageList.addAll(photoList);
				
				if(lifestyleImageAdapter == null){
					lifestyleImageAdapter = new LifestyleImageAdapter(context, bgImageList);
					imagesList.setAdapter(lifestyleImageAdapter);
				}else{
					lifestyleImageAdapter.notifyDataSetChanged();
				}
			}
		});
		instagramUtility.inItInstgram();
	}

	private void getBgList() {
		mProgressBarDialog = new ProgressBarDialog(context);
		mProgressBarDialog.show();
		
		final EditProfileApi listApi = new EditProfileApi(this);
		listApi.getBgImageUrlList(UserPreference.getInstance().getUserID());
		listApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				listApi.cancel();
			}
		});		
	}

	boolean isLoading = false;
	
	@Override
	public void initLayout() {
		imagesList = (GridView) findViewById(R.id.imagesList);
//		imagesList.setOnScrollListener(new OnScrollListener() {
//			   @Override
//			   public void onScrollStateChanged(AbsListView view, int scrollState) {}
//			   @Override
//			   public void onScroll(AbsListView view, int firstVisibleItem,  int visibleItemCount, int totalItemCount) {
//			    if(!isLoadDefault&&!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
//			    	if(CommonUtility.isOnline(context)){
//			    		count+=10;
//			    		getInstagramList(count, false);
//			    	}
//			    }
//			   }
//			  });
	}

	@Override
	public void setupData() {
		imagesList.setOnItemClickListener(this);
	}
	
	@Override
	public void headerView() {
		setBackHeader();
		getLeftTextView().setOnClickListener(this);
	}

	@Override
	public void setUpTextType() {
		
	}

	@Override
	public void setClickListener() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftTextView:
			finish();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void handleOnSuccess(Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnSuccess>>" + object);
		EditProfileRes editProfileRes = (EditProfileRes) object;
		List<BgImage> bgImages = editProfileRes.getData();
//		List<String> bgImages = new ArrayList<String>();
		if (bgImages.size() > 0) {
			bgImageList = getImageList(bgImages);
			lifestyleImageAdapter = new LifestyleImageAdapter(context, bgImageList);
			imagesList.setAdapter(lifestyleImageAdapter);
		}
	}

	private List<String> getImageList(List<BgImage> bgImages2) {
		List<String> bgImages = new ArrayList<String>();
		for(BgImage bgImage :bgImages2){
			bgImages.add(bgImage.getBackground_pic());
		}
		return bgImages;
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			EditProfileRes response = (EditProfileRes) object;
			AlertUtils.showToast(context, response.getMessage());
		} else {
			AlertUtils.showToast(context, R.string.invalid_response);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent();
		i.putExtra("url", bgImageList.get(arg2));
		setResult(RESULT_OK, i);
		finish();
	}

}
