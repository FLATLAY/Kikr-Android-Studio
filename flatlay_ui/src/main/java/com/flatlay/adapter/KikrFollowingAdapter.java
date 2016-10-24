package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.flatlaylib.bean.FollowingKikrModel;
import com.flatlaylib.bean.Inspiration;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class KikrFollowingAdapter extends RecyclerView.Adapter<KikrFollowingAdapter.ViewHolder> {

    private ArrayList<FollowingKikrModel.DataBean> followingList;
    private Context context;
    String likeinsp = "likeinsp";
    String commentinsp = "commentinsp";

    public KikrFollowingAdapter(Context context, ArrayList<FollowingKikrModel.DataBean> followingList) {
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

      //  String oldDate = followingList.get(i).getDateadded();

        String userstring=followingList.get(i).getMessage();
        //String onlyuserimagepic=firstword(userstring);
        String[] result = userstring.split(" ", 2);
        String first = result[0];
        String rest = result[1];
        String time;
        System.out.println("First: " + first);
        System.out.println("Rest: " + rest);

        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        Calendar calLocal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calServer = Calendar.getInstance();

        try {
            Date dd = df.parse(followingList.get(i).getDateadded());
            calServer.setTime(dd);

//            Date date1 = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
//            Date date2 = simpleDateFormat.parse(oldDate);

//            viewHolder.user_following.setText(first);
//            viewHolder.followingsubstring.setText(rest);
            viewHolder.followingsubstring.setText(Html.fromHtml("<b><font color=\"#000000\" size=\"9\"  >" + first+ "</font></b>" + "<font color=\"#777777\">" + "&nbsp;" + rest + "</font>"));
            Picasso.with(context).load(followingList.get(i).getImg()).into(viewHolder.userimagepic);
       //     Picasso.with(context).load(followingList.get(i).getImg()).into(viewHolder.follower_user_image);
           // viewHolder.week.setText( CommonUtility.calculateTimeDiff(calServer, calLocal));

            if(commentinsp.equals(followingList.get(i).getType()))
            {
                viewHolder.follower_user_image.setImageResource(R.drawable.chat_icon_green);
            }
           else
            {
                //    follower_user_image.setImageResource(R.drawable.shareclicked);
                viewHolder.follower_user_image.setImageResource(R.drawable.ic_heart_red);
            }
            time=CommonUtility.messageCenter(calServer,calLocal);
            viewHolder.followingsubstring.append("   " +time.toString());


            viewHolder.follower_user_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((HomeActivity) context).checkInternet()) {
                        String likecommentimage=followingList.get(i).getExtras();
                        //abhi={"user_idrec":"1467","user_idsend":"1467","otherdata":{"inspiration_id":"1404"}}
                        System.out.print(likecommentimage);
                        try {

                            JSONObject jObject  = new JSONObject(likecommentimage);
                            String userid= jObject.getString("user_idrec");
                            System.out.print(userid);
                            String usersend = jObject.getString("user_idsend");
                            System.out.print(usersend);
                            JSONObject innerJObject = jObject.getJSONObject("otherdata");
                            String inspiration_id = innerJObject.getString("inspiration_id");
                            System.out.print(inspiration_id);
                            Inspiration inspirationsetvalue=new Inspiration();
                            String dateadded=followingList.get(i).getDateadded();
                            inspirationsetvalue.setUser_id(usersend);
                            inspirationsetvalue.setInspiration_id(inspiration_id);
                            inspirationsetvalue.setDateadded(dateadded);
                            addFragment(new FragmentInspirationDetail(inspirationsetvalue,false,true));

//                          Map<String,String> map = new HashMap<String,String>();
//                            Iterator iter = jObject.keys();
//                            while(iter.hasNext()){
//                                String key = (String)iter.next();
//                                String value = jObject.getString(key);
//                                map.put(key,value);
//                              //  JSONObject innerJObject = jObject.getJSONObject(key);
//
//                            }


                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });


            viewHolder.followingsubstring.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        if (((HomeActivity) context).checkInternet()) {
                         //   Toast.makeText(context,"hello",Toast.LENGTH_SHORT).show();
                            addFragment(new FragmentProfileView(followingList.get(i).getTbl_user_idsend(), "no"));
                        }

                }
            });
            viewHolder.userimagepic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((HomeActivity) context).checkInternet()) {
                        //   Toast.makeText(context,"hello",Toast.LENGTH_SHORT).show();
                        addFragment(new FragmentProfileView(followingList.get(i).getTbl_user_idsend(), "no"));
                    }

                }
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }



        //  Picasso.with(context).load(followingList.get(i).getHotel_image()).into(viewHolder.imageView_Hotel);


        //viewholder.product_inflater_layout.removeAllViews();

        //  Picasso.with(context).load(followingList.get(i).getHotel_image()).into(viewHolder.imageView_Hotel);


        //  this is used for to show item in gridview
        viewHolder.gridview.setAdapter(new FollowingImagesSet(context));


    }

    public String firstword(String user) {
//        String result = "";  // Return empty string if no space found
//
//        for(int i = 0; i < user.length(); i++)
//        {
//            if(user.charAt(i) == ' ')
//            {
//                result = user.substring(0, i);
//                break; // because we're done
//            }
//        }
//
//        return result;
        String[] result = user.split(" ", 2);
        String first = result[0];
        String rest = result[1];
        System.out.println("First: " + first);
        System.out.println("Rest: " + rest);
       // return result;

        return first;
    }


    @Override
    public int getItemCount() {

        return followingList.size();
    }

    public String printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long weekInMilli = daysInMilli * 7;
        //long monthInMill=weekInMilli * 30;

        long elapsedWeeks = different / weekInMilli;
        different = different % weekInMilli;
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if (elapsedDays > 0)
            return String.valueOf(elapsedDays) + "d";
        else if (elapsedHours > 0)
            return String.valueOf(elapsedHours) + "h";
        else if (elapsedMinutes > 0)
            return String.valueOf(elapsedMinutes) + "m";
        else if (elapsedSeconds > 0)
            return String.valueOf(elapsedSeconds) + "s";
        else
            return "";

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_Hotel;
        private TextView user_following, week,followingsubstring;
        LinearLayout product_inflater_layout;
        GridView gridview;
        ImageView userimagepic,follower_user_image;

        public ViewHolder(View view) {
            super(view);

            imageView_Hotel = (ImageView) view.findViewById(R.id.searchImageView);
            user_following = (TextView) view.findViewById(R.id.user_following);
            gridview = (GridView) view.findViewById(R.id.gridview);
            week = (TextView) view.findViewById(R.id.week);
            userimagepic= (ImageView) view.findViewById(R.id.user_image);
            follower_user_image= (ImageView) view.findViewById(R.id.follower_user_image);
            followingsubstring= (TextView) view.findViewById(R.id.followingsubstring);
            //  product_inflater_layout = (LinearLayout) view.findViewById(R.id.product_inflater_layout);

        }
    }
    private void addFragment(Fragment fragment) {
        ((HomeActivity) context).addFragment(fragment);
    }


}
