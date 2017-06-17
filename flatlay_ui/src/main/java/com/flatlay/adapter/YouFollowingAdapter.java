package com.flatlay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlaylib.bean.YouFollowingModel;

import java.util.ArrayList;


public class YouFollowingAdapter extends RecyclerView.Adapter<YouFollowingAdapter.ViewHolder> {

    private ArrayList<YouFollowingModel> hotelList;
    private Context context;
    public YouFollowingAdapter(Context context, ArrayList<YouFollowingModel> hotelList) {
        this.hotelList = hotelList;
        this.context = context;
        Log.w("Activity","YouFollowingAdapter");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_you_following, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        //  Picasso.with(context).load(hotelList.get(i).getHotel_image()).into(viewHolder.imageView_Hotel);
        viewHolder.textView_Price.setText( "AmitSing "+hotelList.get(i).getHotel_price());


//        for(i=0; i<4;i++)
//        {
//            Picasso.with(context).load(hotelList.get(i).getHotel_image()).into(viewHolder.imageView_Hotel);
//        }
    }

    @Override
    public int getItemCount() {

        return hotelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_Hotel;
        private TextView textView_Price;

        public ViewHolder(View view) {
            super(view);

            imageView_Hotel = (ImageView)view.findViewById(R.id.searchImageView);
            textView_Price = (TextView)view.findViewById(R.id.user_name);

        }
    }
}
