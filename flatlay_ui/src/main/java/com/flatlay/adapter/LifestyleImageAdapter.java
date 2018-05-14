package com.flatlay.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.flatlay.R;
import com.flatlay.utility.CommonUtility;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RachelDi on 4/17/18.
 */

public class LifestyleImageAdapter extends BaseAdapter {

    private Activity mContext;
    private LayoutInflater mInflater;
    List<String> bgImages = new ArrayList<String>();

    public LifestyleImageAdapter(Activity context, List<String> bgImages) {
        super();
        this.mContext = context;
        this.bgImages = bgImages;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.w("Activity", "LifestyleImageAdapter");
    }

    @Override
    public int getCount() {
        return bgImages.size();
    }

    @Override
    public String getItem(int index) {
        return bgImages.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_lifestyle_images, null);
            viewHolder = new ViewHolder();
            viewHolder.lifestyleImage = (RoundedImageView) convertView.findViewById(R.id.lifestyleImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CommonUtility.setImage(mContext, viewHolder.lifestyleImage, getItem(position));
        return convertView;
    }

    public class ViewHolder {
        RoundedImageView lifestyleImage;
    }
}
