package com.kikr.dialog;

import java.util.List;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.CollectionListAdapter;
import com.kikrlib.api.CollectionApi;
import com.kikrlib.bean.CollectionList;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CollectionApiRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class CollectionListDialog extends Dialog implements ServiceCallback{
	private FragmentActivity mContext;
	private LinearLayout add_collection_layout;
	private TextView share_collection_layout;
	private ListView collection_listing;
	private Product productList;
	private CollectionListDialog collectionListDialog;
	private CollectionListAdapter collectionListAdapter;
	private ProgressBar progressBarCollection;

	public CollectionListDialog(FragmentActivity context,Product productList) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		collectionListDialog= this;
		this.productList=productList;
		init();
	}

	public CollectionListDialog(FragmentActivity context, int theme) {
		super(context, R.style.AdvanceDialogTheme);
		mContext = context;
		init();
	}
	
	private void init() {
		setContentView(R.layout.dialog_collection_list);
		setCancelable(true);
		progressBarCollection= (ProgressBar) findViewById(R.id.progressBarCollection);
		collection_listing= (ListView) findViewById(R.id.collection_listing);
		add_collection_layout = (LinearLayout) findViewById(R.id.add_collection_layout);
		share_collection_layout = (TextView) findViewById(R.id.share_collection_layout);
		if(((HomeActivity)mContext).checkInternet())
			getCollectionList();
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0.9f;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(true);
		add_collection_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AddCollectionDialog dialog = new AddCollectionDialog(mContext,productList.getId(),collectionListDialog);
				dialog.show();
			}
		});
		share_collection_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((HomeActivity) mContext).shareProductCollection("");
			}
		});
	}
	
	public void getCollectionList() {
		progressBarCollection.setVisibility(View.VISIBLE);
		final CollectionApi collectionApi = new CollectionApi(this);
		collectionApi.getCollectionList(UserPreference.getInstance().getUserID());
		collectionApi.execute();
	}

	@Override
	public void handleOnSuccess(Object object) {
		progressBarCollection.setVisibility(View.GONE);
		Syso.info("In handleOnSuccess>>" + object);
		CollectionApiRes collectionApiRes = (CollectionApiRes) object;
		List<CollectionList> collectionLists = collectionApiRes.getCollection();
		if (collectionLists.size() > 0) {
			if (collectionListAdapter==null) {
				collectionListAdapter=new CollectionListAdapter(mContext,collectionLists, productList,collectionListDialog);
				collection_listing.setAdapter(collectionListAdapter);
			} else{
				collectionListAdapter.setData(collectionLists);
				collectionListAdapter.notifyDataSetChanged();
			}
			
		}
	}
	


	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		progressBarCollection.setVisibility(View.GONE);
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			CollectionApiRes response = (CollectionApiRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}

}
