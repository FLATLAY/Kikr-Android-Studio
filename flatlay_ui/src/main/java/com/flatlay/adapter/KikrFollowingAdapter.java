package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentInspirationDetail;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.MyProfileApi;
import com.flatlaylib.bean.FollowingKikrModel;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.UserData;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.MyProfileRes;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by RachelDi on 3/1/18.
 */

public class KikrFollowingAdapter extends RecyclerView.Adapter<KikrFollowingAdapter.ViewHolder> {

    private ArrayList<FollowingKikrModel.DataBean> followingList;
    private FragmentActivity context;
    String likeinsp = "likeinsp";
    String commentinsp = "commentinsp";
    private List<UserData> userDetails;


    public KikrFollowingAdapter(FragmentActivity context, ArrayList<FollowingKikrModel.DataBean> followingList) {
        this.followingList = followingList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kikrfollowingadapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final ViewHolder holder2 = viewHolder;
        Log.e("success", "33--" + followingList.size());
        String userString = followingList.get(i).getMessage();
        String notificationType = followingList.get(i).getType();
        String extras = followingList.get(i).getExtras();
        String getHtmlcontent = followingList.get(i).getHtmlcontent();
        String userName = "";
        String notitification = "";
        Log.e("ddddd", extras);
        Log.e("ddddd--", getHtmlcontent);
        try {
            JSONObject jObject = new JSONObject(extras);
            String user_idsend = jObject.getString("user_idsend");
            JSONObject innerJObject = jObject.getJSONObject("otherdata");
            String inspiration_id = innerJObject.getString("inspiration_id");
            Inspiration inspirationsetvalue = new Inspiration();
//            inspirationsetvalue.setUser_id(user_idrec);
            inspirationsetvalue.setInspiration_id(inspiration_id);

            final MyProfileApi myProfileApi = new MyProfileApi(new ServiceCallback() {
                @Override
                public void handleOnSuccess(Object object) {
                    MyProfileRes myProfileRes = (MyProfileRes) object;
                    userDetails = myProfileRes.getUser_data();
                    if (userDetails.get(0).getProfile_pic().equals(""))
                        Picasso.with(context).load(R.drawable.profile_icon).into(holder2.user_image);
                    else
                    Picasso.with(context).load(userDetails.get(0).getProfile_pic()).into(holder2.user_image);
                }

                @Override
                public void handleOnFailure(ServiceException exception, Object object) {

                }
            });
            myProfileApi.getUserProfileDetail(user_idsend, UserPreference.getInstance().getUserID());
            myProfileApi.execute();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        if (userString.contains("commented") && notificationType.equals("commentinsp")) {
            userName = userString.split(" commented")[0];
            viewHolder.type_image.setImageResource(R.drawable.speaking_bubble_tealfilled);
            notitification = userString.split(userName + " ")[1];
        } else if (userString.contains("liked") && notificationType.equals("likeinsp")) {
            userName = userString.split(" liked")[0];
            viewHolder.type_image.setImageResource(R.drawable.tealheart2);
            notitification = userString.split(userName + " ")[1];
        } else if (userString.contains("following") && notificationType.equals("follow")) {
            userName = userString.split(" is following")[0];
            notitification = userString.split(userName + " ")[1];
            viewHolder.type_image.setImageResource(R.drawable.following_white_on_teal);

        }
        Log.e("success", "44--" + notitification);

        String time;
        Calendar calLocal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calServer = Calendar.getInstance();

        try {
            Date dd = df.parse(followingList.get(i).getDateadded());
            calServer.setTime(dd);
            viewHolder.followingsubstring.setTypeface(FontUtility.setMontserratLight(context));
            viewHolder.followingsubstring.setText(Html.fromHtml
                    (userName + " " + notitification));

            if (followingList.get(i).getImg().equals("")) {
                Picasso.with(context).load(R.drawable.profile_icon).into(viewHolder.user_image);
            } else {
                Picasso.with(context).load(followingList.get(i).getImg()).into(viewHolder.user_image);
            }

//            if (commentinsp.equals(followingList.get(i).getType())) {
//                viewHolder.follower_user_image.setImageResource(R.drawable.speaking_bubble_tealfilled);
//            } else {se
//                viewHolder.follower_user_image.setImageResource(R.drawable.heartoutlineteal);
//            }
            time = CommonUtility.messageCenter(calServer, calLocal);
            viewHolder.followingsubstring.append("   " + time.toString());

//            viewHolder.follower_user_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (((HomeActivity) context).checkInternet()) {
//                        String likecommentimage=followingList.get(i).getExtras();
//                        System.out.print(likecommentimage);
//                        try {
//
//                            JSONObject jObject  = new JSONObject(likecommentimage);
//                            String userid= jObject.getString("user_idrec");
//                            System.out.print(userid);
//                            String usersend = jObject.getString("user_idsend");
//                            System.out.print(usersend);
//                            JSONObject innerJObject = jObject.getJSONObject("otherdata");
//                            String inspiration_id = innerJObject.getString("inspiration_id");
//                            System.out.print(inspiration_id);
//
//                            Inspiration inspirationsetvalue=new Inspiration();
//                            String dateadded=followingList.get(i).getDateadded();
//                            inspirationsetvalue.setUser_id(usersend);
//                            inspirationsetvalue.setInspiration_id(inspiration_id);
//                            inspirationsetvalue.setDateadded(dateadded);
////                            addFragment(new FragmentInspirationDetail(inspirationsetvalue,false,true));
//                        } catch (JSONException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }
//            });
//
//
//            viewHolder.followingsubstring.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (((HomeActivity) context).checkInternet()) {
//                        addFragment(new FragmentProfileView
//                                (followingList.get(i).getTbl_user_idsend(), "no"));
//                    }
//
//                }
//            });
//            viewHolder.userimagepic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (((HomeActivity) context).checkInternet()) {
//                        addFragment(new FragmentProfileView
//                                (followingList.get(i).getTbl_user_idsend(), "no"));
//                    }
//
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.gridview.setAdapter(new FollowingImagesSet(context));

    }

    @Override
    public int getItemCount() {
        return followingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_Hotel;
        private TextView user_following, week, followingsubstring;
        LinearLayout product_inflater_layout;
        GridView gridview;
        ImageView user_image, follower_user_image,type_image;

        public ViewHolder(View view) {
            super(view);

            imageView_Hotel = (ImageView) view.findViewById(R.id.searchImageView);
            user_following = (TextView) view.findViewById(R.id.user_following);
            gridview = (GridView) view.findViewById(R.id.gridview);
            week = (TextView) view.findViewById(R.id.week);
            user_image = (ImageView) view.findViewById(R.id.user_image);
            type_image = (ImageView) view.findViewById(R.id.type_image);
            follower_user_image = (ImageView) view.findViewById(R.id.follower_user_image);
            followingsubstring = (TextView) view.findViewById(R.id.followingsubstring);
        }
    }


}

