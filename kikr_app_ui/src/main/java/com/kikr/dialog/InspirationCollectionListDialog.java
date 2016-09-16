package com.kikr.dialog;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.CollectionListAdapter;
import com.kikr.adapter.FragmentProfileCollectionAdapter;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.CollectionApi;
import com.kikrlib.api.MyProfileApi;
import com.kikrlib.bean.CollectionList;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProfileCollectionList;
import com.kikrlib.bean.TaggedItem;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CollectionApiRes;
import com.kikrlib.service.res.MyProfileRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

public class InspirationCollectionListDialog extends Dialog implements ServiceCallback{
	private FragmentActivity mContext;
	private ListView collection_listing;
	private List<ProfileCollectionList> collectionLists=new ArrayList<ProfileCollectionList>();
	private FragmentProfileCollectionAdapter collectionListAdapter;
	private ProgressBar progressBarCollection;
	private TaggedItem taggedItemLocal;
	private InspirationCollectionListDialog inspirationCollectionListDialog;
	private RelativeLayout rel_layout;

	public InspirationCollectionListDialog(FragmentActivity context,TaggedItem taggedItemLocal) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		inspirationCollectionListDialog=this;
		this.taggedItemLocal = taggedItemLocal;
		init();
	}

	public InspirationCollectionListDialog(FragmentActivity context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}
	
	private void init() {
		setContentView(R.layout.dialog_inspiration_collection_list);
		setCancelable(true);
		rel_layout = (RelativeLayout) findViewById(R.id.rel_layout);
		rel_layout.setLayoutParams(new FrameLayout.LayoutParams(CommonUtility.getDeviceWidth(mContext),CommonUtility.getDeviceHeight(mContext)*80/100));
		progressBarCollection= (ProgressBar) findViewById(R.id.progressBarCollection);
		collection_listing= (ListView) findViewById(R.id.collection_listing);
		if(((HomeActivity)mContext).checkInternet())
			getCollectionList();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
	}
	
	public void getCollectionList() {
		progressBarCollection.setVisibility(View.VISIBLE);
		final MyProfileApi myProfileApi = new MyProfileApi(this);
		myProfileApi.getUserProfileDetail(UserPreference.getInstance().getUserID(), UserPreference.getInstance().getUserID());
		myProfileApi.execute();
	}

	@Override
	public void handleOnSuccess(Object object) {
		progressBarCollection.setVisibility(View.GONE);
		Syso.info("In handleOnSuccess>>" + object);
		MyProfileRes myProfileRes = (MyProfileRes) object;
		collectionLists= myProfileRes.getCollection_list();
		if (collectionLists.size() > 0) {
			if (collectionListAdapter==null) {
				collectionListAdapter=new FragmentProfileCollectionAdapter(mContext,collectionLists,  UserPreference.getInstance().getUserID(),null,taggedItemLocal,inspirationCollectionListDialog);
				collection_listing.setAdapter(collectionListAdapter);
			}
		}else{
			CommonUtility.showAlert(mContext,"Please create your first collection to tag to your post.");
			dismiss();
		}
	}
	


	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		progressBarCollection.setVisibility(View.GONE);
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			MyProfileRes response = (MyProfileRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}

}
