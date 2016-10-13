package com.flatlay.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.GridVideoAdapter;

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
