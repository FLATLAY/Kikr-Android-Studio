package com.kikr.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.utility.AutoTextSize;

import java.util.ArrayList;


public class SubCategoryAdapter extends BaseAdapter {
    private final Context context;
    ArrayList<String> items;

    public SubCategoryAdapter(Context context, ArrayList<String> items) {


        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View appView = inflater.inflate(R.layout.sub_categories_item, parent, false);

            String str = items.get(position);
            //int cols = 1;

//            AutoTextSize text = (AutoTextSize) appView.findViewById(R.id.sub_category_name);
//
//            if (items.size() > 8 && items.size() < 12) {
//                text.setWidth(300);
//            } else if (items.size() < 9) {
//                text.setWidth(320);
//            }
//            if (items.size() < 5) {
//                text.setWidth(320);
//            } else {
//                text.setWidth(220);
//
//            }
//            if(str.length()<10)
               // text.setWidth(150);
            ((TextView) appView.findViewById(R.id.sub_category_name)).setText(str);
            return appView;
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }

        return null;
    }

    private StringBuilder capitalizeEachWord(String s) {
        StringBuilder result = new StringBuilder(s.length());
        String[] words = s.split("\\s");
        for (int i = 0, l = words.length; i < l; ++i) {
            if (i > 0) result.append(" ");
            result.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1));

        }
        return result;
    }
}
