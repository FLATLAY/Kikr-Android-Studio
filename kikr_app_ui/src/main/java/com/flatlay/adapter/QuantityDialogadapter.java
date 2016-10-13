package com.flatlay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.DialogCallback;
import com.kikrlib.bean.CollectionList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tycho on 8/24/2016.
 */

public class QuantityDialogadapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> size = new ArrayList<String>();
    private DialogCallback dialogCallback;
    private int selectedSize=-1;
    public QuantityDialogadapter(Context context, DialogCallback dialogCallback, List<String> size, String selelctedsize) {
        super();
        this.mContext = context;
        this.dialogCallback = dialogCallback;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.size = size;
    }

    public void setData(List<CollectionList> data){
//		this.data = data;
    }

    @Override
    public int getCount() {
        return size.size();
    }

    @Override
    public Object getItem(int index) {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final QuantityDialogadapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_size_list, null);
            viewHolder = new QuantityDialogadapter.ViewHolder();
            viewHolder.sizeText = (TextView) convertView.findViewById(R.id.sizeText);
            viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (QuantityDialogadapter.ViewHolder) convertView.getTag();
        }
        if(selectedSize==position){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
        }

        viewHolder.sizeText.setText(size.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(((HomeActivity)mContext).checkInternet())
                dialogCallback.setQuantity(size.get(position));
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView sizeText;
        CheckBox checkBox;
    }

}
