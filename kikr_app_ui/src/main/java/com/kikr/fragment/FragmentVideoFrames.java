package com.kikr.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.GridVideoAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.Luhn;
import com.kikrlib.api.CardInfoApi;
import com.kikrlib.bean.Card;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.CardInfoRes;
import com.kikrlib.utils.AlertUtils;

public class FragmentVideoFrames extends BaseFragment implements OnClickListener {
	private View mainView;
	ArrayList<Bitmap> frames = new ArrayList<Bitmap>();
	private GridView gridView;
	
	public FragmentVideoFrames(List<Bitmap> frames) {
		this.frames = (ArrayList<Bitmap>) frames;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.fragment_video_grid, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		gridView = (GridView) mainView.findViewById(R.id.gridView);
	}

	@Override
	public void refreshData(Bundle bundle) {
	}

	@Override
	public void setClickListener() {
	}

	@Override
	public void setData(Bundle bundle) {
		gridView.setAdapter(new GridVideoAdapter(mContext,frames));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}
	
}
