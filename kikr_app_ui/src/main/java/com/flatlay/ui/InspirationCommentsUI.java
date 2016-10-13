package com.flatlay.ui;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.utility.CommonUtility;
import com.kikrlib.bean.Comment;
import com.kikrlib.db.UserPreference;

import java.util.ArrayList;
import java.util.List;

public class InspirationCommentsUI {
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<Comment> data = new ArrayList<Comment>();
    private FragmentInspirationDetail fragmentInspirationDetail;

    public InspirationCommentsUI(FragmentActivity context, List<Comment> data, FragmentInspirationDetail fragmentInspirationDetail) {
        super();
        this.mContext = context;
        this.data = data;
        this.fragmentInspirationDetail = fragmentInspirationDetail;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getView() {

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for ( int i = 0; i < data.size(); i++) {
            View convertView = (LinearLayout) mInflater.inflate(R.layout.adapter_comments, null);
            RelativeLayout layoutComment=(RelativeLayout)convertView.findViewById(R.id.layoutComment);
            TextView userName = (TextView) convertView.findViewById(R.id.userName);
            TextView commentText = (TextView) convertView.findViewById(R.id.commentText);
            ImageView deletecomment= (ImageView) convertView.findViewById(R.id.deleteComment);
            RoundImageView userImage = (RoundImageView) convertView.findViewById(R.id.userImage);
            convertView.setLayoutParams(layoutParams);
            ll.addView(convertView);
            convertView.setTag(i);
            layoutComment.setTag(i);
            userName.setTag(i);
            deletecomment.setTag(i);
            if (!TextUtils.isEmpty(data.get(i).getUser_name()))
                userName.setText(data.get(i).getUser_name());
            else
                userName.setText("Unknown");
            if (!TextUtils.isEmpty(data.get(i).getComment()))
                commentText.setText(data.get(i).getComment());
            else
                commentText.setText("");

            if (!TextUtils.isEmpty(data.get(i).getProfile_pic()))
                CommonUtility.setImage(mContext, data.get(i).getProfile_pic(), userImage, R.drawable.dum_user_profile_img);
            else
                userImage.setImageResource(R.drawable.dum_user_profile_img);

            if (UserPreference.getInstance().getUserID().equals(data.get(i).getUser_id())){
               deletecomment.setVisibility(View.GONE);
                deletecomment.setVisibility(View.VISIBLE);
            }
            else {
                deletecomment.setVisibility(View.VISIBLE);
                deletecomment.setVisibility(View.GONE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((HomeActivity) mContext).addFragment(new FragmentProfileView(data.get((Integer) v.getTag()).getUser_id(), "no"));
                }
            });
//            convertView.setOnLongClickListener(new View.OnLongClickListener() {
//
//
//                @Override
//                public boolean onLongClick(View view) {
//                    if (data.get((Integer) view.getTag()).getUser_id().equals(UserPreference.getInstance().getUserID())) {
//                        fragmentInspirationDetail.showRemovePopup(data.get((Integer) view.getTag()).getComment_id());
//                    }
//                    return true;
//                }
//            });

            deletecomment.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {


                    if (data.get((Integer) view.getTag()).getUser_id().equals(UserPreference.getInstance().getUserID()))
                    {
                        fragmentInspirationDetail.showRemovePopup(data.get((Integer) view.getTag()).getComment_id());
                    }

                }
            });


        }

        return ll;
    }

}
