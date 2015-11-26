package com.kikr.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kikr.BaseFragment;
import com.kikr.KikrApp;
import com.kikr.KikrApp.TrackerName;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.InspirationAdapter;
import com.kikr.dialog.CreateAccountDialog;
import com.kikr.dialog.HelpInspirationDialog;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.InspirationFeedApi;
import com.kikrlib.bean.Inspiration;
import com.kikrlib.db.HelpPreference;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.InspirationFeedRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentInspirationSection extends BaseFragment implements OnClickListener,OnItemClickListener,ServiceCallback{
	private View mainView;
	private ListView inspirationList;
	private ProgressBarDialog mProgressBarDialog;
	int page=0;
	private LinearLayout uploadButton;
	private List<Inspiration> product_list=new ArrayList<Inspiration>();
	private boolean isLoading=false;
	private boolean isFirstTime = true;
	private int firstVisibleItem=0,visibleItemCount=0,totalItemCount=0;
	private View loaderView;
	private InspirationAdapter inspirationAdapter;
	private TextView loadingTextView;
	private boolean isViewAll;
	private String userId;
	
	public FragmentInspirationSection(boolean isViewAll,String userId) {
		this.isViewAll = isViewAll;
		this.userId = userId;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_inspiration_section, null);
		return mainView;
	}


	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.uploadButton: 
			if( UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == ""){
				CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
				createAccountDialog.show();
			}else{
				addFragment(new FragmentInspirationImage());
			}
			break;
		}
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		inspirationList=(ListView) mainView.findViewById(R.id.inspirationList);		
		loaderView = View.inflate(mContext, R.layout.footer, null);
		uploadButton = (LinearLayout) mainView.findViewById(R.id.uploadButton);
		loadingTextView = (TextView) mainView.findViewById(R.id.loadingTextView);
		
		Tracker t = ((KikrApp) mContext.getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.setScreenName("Fragment Inspiration Section");
		t.send(new HitBuilders.ScreenViewBuilder().build());
		
		if(!isViewAll)
			uploadButton.setVisibility(View.GONE);
	}

	@Override
	public void setData(Bundle bundle) {
		if(checkInternet())	
			getInspirationFeedList();
		else
			showReloadOption();
		
		inspirationList.setOnItemClickListener(this);
		inspirationList.setOnScrollListener(new OnScrollListener() {
			   @Override
			   public void onScrollStateChanged(AbsListView view, int scrollState) {}
			   @Override
			   public void onScroll(AbsListView view, int firstVisibleItem,  int visibleItemCount, int totalItemCount) {
				   FragmentInspirationSection.this.firstVisibleItem=firstVisibleItem;
				   FragmentInspirationSection.this.visibleItemCount=visibleItemCount;
				   FragmentInspirationSection.this.totalItemCount=totalItemCount;
			    if(!isLoading&&firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
			    	if(checkInternet2()){
				    	page++;
				    	isFirstTime=false;
				    	getInspirationFeedList();
			    	}else{
			    		showReloadFotter();
			    	}
			    }
			   }
			  });
		
		////////////////////////////////
//		if (HelpPreference.getInstance().getHelpInspiration().equals("yes")) {
//			HelpPreference.getInstance().setHelpInspiration("no");
//			HelpInspirationDialog helpInspirationDialog = new HelpInspirationDialog(mContext);
//			helpInspirationDialog.show();
//		}
		
	}
	
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if(menuVisible){
			if (HelpPreference.getInstance().getHelpInspiration().equals("yes")) {
				HelpPreference.getInstance().setHelpInspiration("no");
				HelpInspirationDialog helpInspirationDialog = new HelpInspirationDialog(mContext);
				helpInspirationDialog.show();
			}
		}
	}
	
	private void getInspirationFeedList() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		isLoading = !isLoading;
		if (!isFirstTime) {
			showFotter();
		} else {
			loadingTextView.setVisibility(View.VISIBLE);
//			mProgressBarDialog.show();
		}
		
		final InspirationFeedApi inspirationFeedApi = new InspirationFeedApi(this);
		inspirationFeedApi.getInspirationFeed(userId,isViewAll,String.valueOf(page));
		inspirationFeedApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				showDataNotFound();
				inspirationFeedApi.cancel();
			}
		});
	}
	
	@Override
	public void handleOnSuccess(Object object) {
		if (!isFirstTime) {
			hideFotter();
		} else {
			loadingTextView.setVisibility(View.GONE);
//			mProgressBarDialog.dismiss();
		}
		hideDataNotFound();
		isLoading=!isLoading;
		Syso.info("In handleOnSuccess>>" + object);
		InspirationFeedRes inspirationFeedRes = (InspirationFeedRes) object;
		product_list.addAll(inspirationFeedRes.getData());
		if(inspirationFeedRes.getData().size()<10){
			isLoading=true;
		}
		if(product_list.size()==0&&isFirstTime){
			showDataNotFound();
		}if (product_list.size()>0 && isFirstTime) {
			inspirationAdapter = new InspirationAdapter(mContext,product_list,isViewAll);
			inspirationList.setAdapter(inspirationAdapter);
		}else if(inspirationAdapter!=null){
			inspirationAdapter.setData(product_list);
			inspirationAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		if (!isFirstTime) {
			inspirationList.removeFooterView(loaderView);
		} else {
			loadingTextView.setVisibility(View.GONE);
//			mProgressBarDialog.dismiss();
		}
		isLoading=!isLoading;
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			InspirationFeedRes response = (InspirationFeedRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}


	@Override
	public void refreshData(Bundle bundle) {
		
	}

	@Override
	public void setClickListener() {
		uploadButton.setOnClickListener(this);
	}
	

	private void showReloadOption() {
		showDataNotFound();
		TextView textView=getDataNotFound();
		Syso.info("text view>>"+textView);
		if(textView!=null){
			Syso.info("12233 inside text view text view>>"+textView);
			textView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(checkInternet())
						getInspirationFeedList();
					Syso.info("text view>>");
				}
			});
		}
	}
	
	protected void showReloadFotter() {
		TextView textView=getReloadFotter();
		textView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkInternet()){
					page++;
			    	isFirstTime=false;
			    	getInspirationFeedList();
				}
			}
		});
	}
	
	public void refresh(){
		Handler handler=new Handler();
		Runnable runnable=new Runnable() {
			
			@Override
			public void run() {
				((HomeActivity)mContext).loadFragment(new FragmentInspirationSection(true,UserPreference.getInstance().getUserID()));
			}
		};
		handler.postDelayed(runnable, 100);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			getInspirationFeedList();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		HomeActivity.menuTextCartCount.setVisibility(View.VISIBLE);
	}
	
}
