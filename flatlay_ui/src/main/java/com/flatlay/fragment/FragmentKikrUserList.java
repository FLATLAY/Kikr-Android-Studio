package com.flatlay.fragment;

import java.util.List;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.KikrUserListAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.bean.FollowUser;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class FragmentKikrUserList extends BaseFragment implements OnClickListener,ServiceCallback {

	private View mainView;
	private ListView kikrUserList;
	private ProgressBarDialog mProgressBarDialog;
	private List<FollowUser> followUsers;
	private int pagenum = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_kikr_user_list, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		kikrUserList=(ListView) mainView.findViewById(R.id.kikrUserList);
	}
	
	private void getFriendsList() {
        Log.w("FragmentKikrUserList","getFriendsList()");
		mProgressBarDialog = new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		final FollowUserApi followUserApi = new FollowUserApi(this);
		followUserApi.getAllKikrUserList(UserPreference.getInstance().getUserID(),Integer.toString(pagenum),"all");
		followUserApi.execute();
		
		mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				followUserApi.cancel();
			}
		});
	}
	
	@Override
	public void handleOnSuccess(Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnSuccess>>" + object);
		FollowUserRes followUserRes = (FollowUserRes) object;
		followUsers = followUserRes.getData();
		if (followUsers.size() == 0)
			showDataNotFound();
		else
		if (followUsers.size()>0) {
			kikrUserList.setAdapter(new KikrUserListAdapter(mContext, followUsers));
		} else{
			AlertUtils.showToast(mContext, R.string.no_users_found_msg);
		}
	}

	@Override
	public void handleOnFailure(ServiceException exception, Object object) {
		mProgressBarDialog.dismiss();
		Syso.info("In handleOnFailure>>" + object);
		if (object != null) {
			FollowUserRes response = (FollowUserRes) object;
			AlertUtils.showToast(mContext, response.getMessage());
		} else {
			AlertUtils.showToast(mContext, R.string.invalid_response);
		}
	}

	@Override
	public void onClick(View v) {
		
		
	}

	@Override
	public void setData(Bundle bundle) {
		getFriendsList();
		
	}

	@Override
	public void refreshData(Bundle bundle) {
		
		
	}

	@Override
	public void setClickListener() {
		
		
	}

}
