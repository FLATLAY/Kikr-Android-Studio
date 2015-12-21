package com.kikr.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.adapter.ActivityCollectionAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.ActivityApi;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.User;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.ActivityRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentActivityCollectionDetail extends BaseFragment implements OnClickListener {
	private View mainView;
	private ListView activity_list;
	private ImageView user_profile_image,bgProfileLayout;
	private TextView user_profile_name,total_payout_text,month_name_heading,collection_created_at,collection_views,collection_payout_text;
	private ProgressBarDialog mProgressBarDialog;
	private String month="",year="";
	private ActivityCollectionAdapter activityPageAdapter;
	private String collectionId;
	private ImageView collection_image_1,collection_image_2,collection_image_3,collection_image_4;
	private TextView collection_name,kikr_commision_text;
	private User user;
	private View v;
	private View view1,view2,view3,view_bottom;
	
	public FragmentActivityCollectionDetail(String month, String year, String collectionId,User user) {
		this.month = month;
		this.year = year;
		this.user = user;
		this.collectionId = collectionId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_activity_collection_detail, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		v = View.inflate(mContext, R.layout.collection_detail_header, null);
		activity_list = (ListView) mainView.findViewById(R.id.activity_list);
		user_profile_image = (ImageView) mainView.findViewById(R.id.user_profile_image);
		bgProfileLayout = (ImageView) mainView.findViewById(R.id.bgProfileLayout);
		user_profile_name = (TextView) mainView.findViewById(R.id.user_profile_name);
		collection_created_at = (TextView) v.findViewById(R.id.collection_created_at);
		collection_views = (TextView) v.findViewById(R.id.collection_views);
		collection_payout_text = (TextView) v.findViewById(R.id.collection_payout_text);
		collection_name = (TextView) v.findViewById(R.id.collection_name);
		month_name_heading = (TextView) mainView.findViewById(R.id.month_name_heading);
		total_payout_text = (TextView) mainView.findViewById(R.id.total_payout_text);
		collection_image_1 = (ImageView) v.findViewById(R.id.collection_image_1);
		collection_image_2 = (ImageView) v.findViewById(R.id.collection_image_2);
		collection_image_3 = (ImageView) v.findViewById(R.id.collection_image_3);
		collection_image_4 = (ImageView) v.findViewById(R.id.collection_image_4);
		view1 =  (View) v.findViewById(R.id.view1);
		view2 =  (View) v.findViewById(R.id.view2);
		view3 =  (View) v.findViewById(R.id.view3);
		view_bottom =  (View) v.findViewById(R.id.view_bottom);
		kikr_commision_text = (TextView) mainView.findViewById(R.id.kikr_commision_text);
		kikr_commision_text.setPaintFlags(kikr_commision_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		setDetails();
	}
	
	private void setDetails() {
		if (user != null) {
			user_profile_name.setText(user.getUser_name());
			if (!user.getProfile_pic().equalsIgnoreCase(""))
				CommonUtility.setImage(mContext, user.getProfile_pic(),user_profile_image, R.drawable.dum_user);
			if (!user.getBackground_pic().equalsIgnoreCase(""))
				CommonUtility.setImage(mContext, user.getBackground_pic(),bgProfileLayout, R.drawable.dum_user);
		}
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		kikr_commision_text.setOnClickListener(this);
	}

	@Override
	public void setData(Bundle bundle) {
		if(checkInternet())
		getActivityDetails();
	}

	private void getActivityDetails() {
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		final ActivityApi activityApi = new ActivityApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				hideDataNotFound();
				Syso.info("In handleOnSuccess>>" + object);
				ActivityRes activityRes = (ActivityRes) object;
				if (activityRes!=null) {
					List<Product> data = activityRes.getProduct_list();
					setDetails(activityRes);
					if (data.size() == 0)
						showDataNotFound();
					else if (data.size() > 0) {
						hideDataNotFound();
						activityPageAdapter = new ActivityCollectionAdapter(mContext,data);
						activity_list.addHeaderView(v);
						activity_list.setAdapter(activityPageAdapter);
					}
				}
			}

			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				Syso.info("In handleOnFailure>>" + object);
				if (object != null) {
					ActivityRes response = (ActivityRes) object;
					AlertUtils.showToast(mContext, response.getMessage());
				} else {
					AlertUtils.showToast(mContext, R.string.invalid_response);
				}
			}
		});
		activityApi.getCollectionDetail(UserPreference.getInstance().getUserID(), month, year, collectionId);
		activityApi.execute();

		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				activityApi.cancel();
			}
		});
	}
	
	private void setDetails(ActivityRes activityRes){
		collection_name.setText(activityRes.getCollection_name());
		month_name_heading.setText(activityRes.getCollection_name());
		if (!TextUtils.isEmpty(activityRes.getPayout()))
		total_payout_text.setText("Collection Total= "+CommonUtility.getFormatedNum(activityRes.getPayout()+" Credits"));
		else
		total_payout_text.setText("Collection Total= 0 Credits");
		collection_payout_text.setText("Collection Views: "+activityRes.getCollection_view()+"\nCollection Payout: "+CommonUtility.getFormatedNum(activityRes.getPayout()+" Credits"));
		collection_created_at.setText(CommonUtility.setChangeDateFormat(activityRes.getLast_update()));
		collection_views.setText("Product Views: "+activityRes.getProduct_views()+"\nTotal Buys: "+activityRes.getTotal_buys());
		if (activityRes.getProduct_list()!=null) {
			LayoutParams layoutParams = new LayoutParams(CommonUtility.getDeviceWidthActivity(mContext)/4, CommonUtility.getDeviceWidthActivity(mContext)/4);
			collection_image_1.setLayoutParams(layoutParams);
			collection_image_2.setLayoutParams(layoutParams);
			collection_image_3.setLayoutParams(layoutParams);
			collection_image_4.setLayoutParams(layoutParams);
			List<ImageView> images = new ArrayList<ImageView>();
			images.add(collection_image_1);
			images.add(collection_image_2);
			images.add(collection_image_3);
			images.add(collection_image_4);
			for(int i=0;i<images.size();i++){
				images.get(i).setVisibility(View.GONE);
			}
			List<View> views = new ArrayList<View>();
			views.add(view1);
			views.add(view2);
			views.add(view3);
			for(int i=0;i<views.size();i++){
				views.get(i).setVisibility(View.GONE);
			}
			for (int i = 0; i < activityRes.getProduct_list().size()&&i<4; i++) {
				CommonUtility.setImage(mContext, activityRes.getProduct_list().get(i).getProductimageurl(), images.get(i), R.drawable.dum_list_item_product);
				images.get(i).setVisibility(View.VISIBLE);
				if (i<=2) {
					views.get(i).setVisibility(View.VISIBLE);
				}
			}
			if(activityRes.getProduct_list().size()>0)
				view_bottom.setVisibility(View.VISIBLE);
			else
				view_bottom.setVisibility(View.GONE);
		}
			}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.kikr_commision_text:
			this.addFragment(new FragmentKikrCreditsScreen());
			break;
		}
	}

}
