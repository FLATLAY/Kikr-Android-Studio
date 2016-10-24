package com.flatlay.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.CategoryRowUI;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlaylib.api.GetProductsByCategoryApi;
import com.flatlaylib.bean.Categories;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.GetProductsByCategoryRes;
import com.flatlaylib.utils.Syso;

public class FragmentSearch extends BaseFragment implements OnClickListener {

	private View mainView;
	private List<Categories> categories = new ArrayList<Categories>();
    private ProgressBarDialog mProgressBarDialog;
    private String category = null;
    LinearLayout mainList;
    public static TextView txtSearch;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_search, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		mainList = (LinearLayout) mainView.findViewById(R.id.mainList);
		txtSearch = (TextView) mainView.findViewById(R.id.txtSearch);
		txtSearch.setClickable(true);
		txtSearch.setTextColor(this.getResources().getColor(R.color.neongreen));
		txtSearch.setOnClickListener(this);
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		getCategories();
	}
	
	private void getCategories() {
		GetProductsByCategoryApi checkPointsStatusApi = new GetProductsByCategoryApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				Syso.info("In handleOnSuccess>>" + object);
				GetProductsByCategoryRes getProductsByCategoryRes = (GetProductsByCategoryRes) object;
				categories = getProductsByCategoryRes.getData();
				List<Categories> temp = new ArrayList<Categories>();
				List<Categories> dummy = new ArrayList<Categories>();
				for(Categories cat: categories) {
					if(cat.getCat1().trim().equalsIgnoreCase("baby products") ||
					   cat.getCat1().trim().equalsIgnoreCase("clothing & accessories") ||
					   cat.getCat1().trim().equalsIgnoreCase("computers & accessories") ||
					   cat.getCat1().trim().equalsIgnoreCase("electronics") ||
					   cat.getCat1().trim().equalsIgnoreCase("health & personal care") ||
					   cat.getCat1().trim().equalsIgnoreCase("jewelry") ||
					   cat.getCat1().trim().equalsIgnoreCase("musical instruments") ||
					   cat.getCat1().trim().equalsIgnoreCase("pet supplies") ||
					   cat.getCat1().trim().equalsIgnoreCase("shoes") ||
					   cat.getCat1().trim().equalsIgnoreCase("sports & outdoors") ||
					   cat.getCat1().trim().equalsIgnoreCase("video games")) {
						temp.add(cat);
					}
				}
				dummy.add(0, temp.get(1));
				dummy.add(1, temp.get(8));
				dummy.add(2, temp.get(5));
				dummy.add(3, temp.get(9));
				dummy.add(4, temp.get(4));
				dummy.add(5, temp.get(0));
				dummy.add(6, temp.get(3));
				dummy.add(7, temp.get(10));
				dummy.add(8, temp.get(2));
				dummy.add(9, temp.get(7));
				dummy.add(10, temp.get(6));
				for(Categories cat : dummy) {
					cat.validate();
					
					mainList.addView(new CategoryRowUI(mContext, cat, true).getView());
					Log.e("first batch categories",cat.getCat1());
				}
		        mProgressBarDialog.dismiss();
		        txtSearch.setGravity(Gravity.CENTER);
		        txtSearch.setTextColor(mContext.getResources().getColor(R.color.aquamarine2));
		        String txt = "Categories To Follow";
//		        txtSearch.setTypeface(null, Typeface.BOLD);
		        txtSearch.setText(txt);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				
			}
		});
		checkPointsStatusApi.getCategory(UserPreference.getInstance().getUserID(), category);
		checkPointsStatusApi.execute();
	}

	@Override
	public void setData(Bundle bundle) {
	}

	@Override
	public void refreshData(Bundle bundle) {
	}

	@Override
	public void setClickListener() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtSearch:
				addFragment(new FragmentCategories());
			break;
		}
	}
	
	public void addFragment(Fragment fragment) {
		((HomeActivity) mContext).addFragment(fragment);
	}

	

}