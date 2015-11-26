package com.kikr.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentInspirationDetail;
import com.kikr.fragment.FragmentProfileView;
import com.kikrlib.bean.Comment;
import com.kikrlib.db.UserPreference;

public class InspirationCommentsUI {
	private FragmentActivity mContext;
	private LayoutInflater mInflater;
	private List<Comment> data = new ArrayList<Comment>();
	private FragmentInspirationDetail fragmentInspirationDetail;

	public InspirationCommentsUI(FragmentActivity context, List<Comment> data,FragmentInspirationDetail fragmentInspirationDetail) {
		super();
		this.mContext = context;
		this.data = data;
		this.fragmentInspirationDetail = fragmentInspirationDetail;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	
	public View getView() {
		LinearLayout ll =  new LinearLayout(mContext);
		ll.setOrientation(LinearLayout.VERTICAL);
		LayoutParams layoutParams =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < data.size(); i++) {
			View convertView = (LinearLayout) mInflater.inflate(R.layout.adapter_comments,null);
			TextView userName = (TextView) convertView.findViewById(R.id.userName);
			TextView commentText = (TextView) convertView.findViewById(R.id.commentText);
			convertView.setLayoutParams(layoutParams);
			ll.addView(convertView);
			convertView.setTag(i);
			userName.setTag(i);
		if (!TextUtils.isEmpty(data.get(i).getUser_name())) 
		userName.setText(data.get(i).getUser_name());
		else
		userName.setText("Unknown");
		if(!TextUtils.isEmpty(data.get(i).getComment()))
		commentText.setText(data.get(i).getComment());
		else
		commentText.setText("");
		userName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((HomeActivity)mContext).addFragment(new FragmentProfileView(data.get((Integer)v.getTag()).getUser_id(), "no"));
			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				if (data.get((Integer) view.getTag()).getUser_id().equals(UserPreference.getInstance().getUserID())) {
					fragmentInspirationDetail.showRemovePopup(data.get((Integer) view.getTag()).getComment_id());
				}
				return false;
			}
		});
	}
		return ll;
	}

}
