package com.kikr.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.kikr.utility.FontUtility;
 
public class GroupListAdapter extends BaseExpandableListAdapter {
 
    private FragmentActivity context;
    private List<String> categories;
    private HashMap<String, List<String>> listDataChild;
    private Integer[] imgid;
 
    public GroupListAdapter(FragmentActivity context, List<String> categories, HashMap<String, List<String>> listDataChild2, Integer[] imgid) {
        this.context = context;
        this.categories = categories;
        this.listDataChild = listDataChild2;
        this.imgid = imgid;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.categories.get(groupPosition)).get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
        final String childText = (String) getChild(groupPosition, childPosition);
 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_subgroup_item, null);
        }
 
        TextView txtListChild = (TextView) convertView.findViewById(R.id.subgroupListTextView);
        txtListChild.setText(childText);
        txtListChild.setTypeface(FontUtility.setProximanovaLight(this.context));
        
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.categories.get(groupPosition)).size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.categories.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.categories.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        final ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_group, null);
			viewHolder = new ViewHolder();
			viewHolder.lblListHeader = (TextView) convertView.findViewById(R.id.groupTextView);
			viewHolder.imgListHeader=(ImageView) convertView.findViewById(R.id.groupImageview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
 
		viewHolder.lblListHeader.setTypeface(FontUtility.setProximanovaLight(context), Typeface.BOLD);
		viewHolder.lblListHeader.setText(headerTitle);
		viewHolder.imgListHeader.setImageResource(imgid[groupPosition]);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    public class ViewHolder {
		TextView lblListHeader;
		ImageView imgListHeader;
	}
    
}