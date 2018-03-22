package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.bean.BrandResult;
import com.flatlaylib.bean.Inspiration;

import java.util.List;

/**
 * Created by RachelDi on 3/15/18.
 */

public class StoreGridAdapter extends BaseAdapter {

    private FragmentActivity mContext;
    private List<BrandResult> data;
    private LayoutInflater mInflater;


    public StoreGridAdapter(FragmentActivity context, List<BrandResult> data) {
        super();
        this.mContext = context;
        this.data = data;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<BrandResult> data){
        this.data=data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public BrandResult getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        if (convertView==null) {
            convertView = mInflater.inflate(R.layout.adapter_brand_grid,null);
            viewholder = new StoreGridAdapter.ViewHolder();
            viewholder.brandImage= (ImageView) convertView.findViewById(R.id.brandImage);
            viewholder.follow_text= (TextView) convertView.findViewById(R.id.follow_text);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.follow_text.setTypeface(FontUtility.setMontserratLight(mContext));
        if(data.size()>position)
            CommonUtility.setImage(mContext, viewholder.brandImage, data.get(position).getImg());
        return convertView;
    }

    public class ViewHolder {
        private ImageView brandImage;
//        private LinearLayout follow_layout;
        private TextView follow_text;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

}
