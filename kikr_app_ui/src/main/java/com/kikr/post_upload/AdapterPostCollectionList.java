package com.kikr.post_upload;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kikr.R;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.ui.PostUploadCommentsUI;
import com.kikrlib.bean.ProfileCollectionList;
import com.kikrlib.bean.TaggedItem;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Admin on 3/1/2016.
 */
public class AdapterPostCollectionList extends BaseAdapter {
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    List<ProfileCollectionList> data;
    private String user_id;
    private FragmentProfileView fragmentProfileView;
    private TaggedItem taggedItem;
    private FragmentPostUploadTag inspirationCollectionListDialog;
    final private HashMap<Integer, Boolean> mSelection;

    public AdapterPostCollectionList(FragmentActivity context, List<ProfileCollectionList> data, String user_id, FragmentProfileView fragmentProfileView, TaggedItem taggedItem, FragmentPostUploadTag inspirationCollectionListDialog) {
        super();
        this.mContext = context;
        this.data = data;
        this.user_id = user_id;
        this.taggedItem = taggedItem;
        this.fragmentProfileView = fragmentProfileView;
        this.inspirationCollectionListDialog = inspirationCollectionListDialog;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelection = new HashMap<Integer, Boolean>();
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ProfileCollectionList getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public void setNewSelection(int position, boolean value) {
        clearSelection();
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection.clear();
    }

    public boolean isItemTagged() {
        if (mSelection.size() > 0)
            return true;
        else
            return false;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_post_collection_list_item, null);


            convertView.setBackgroundColor(convertView.getResources().getColor(android.R.color.background_light)); //default color
            viewHolder = new ViewHolder();

            viewHolder.collection_name = (TextView) convertView.findViewById(R.id.tvCollectionName);
            viewHolder.productLayout = (HorizontalScrollView) convertView.findViewById(R.id.productLayout);
            viewHolder.productInflaterLayout = (LinearLayout) convertView.findViewById(R.id.productInflaterLayout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.collection_name.setText(getItem(position).getName());
        viewHolder.productInflaterLayout.removeAllViews();
        viewHolder.productInflaterLayout.addView(new PostUploadCommentsUI(mContext, getItem(position), false, null).getView());
        viewHolder.productLayout.scrollTo(0, 0);

        if (mSelection.get(position) != null) {

            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.header_background));// this is a selected position so make it red
        } else {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.postuploadlistcolor));
        }
        viewHolder.productInflaterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTagSelection(position);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setTagSelection(position);

            }
        });

        return convertView;


    }

    private void setTagSelection(int position) {
        if (!isPositionChecked(position)) {
            ProfileCollectionList selectedItem = getItem(position);
            data.remove(position);
            data.add(0, selectedItem);

            setNewSelection(0, true);
            ((FragmentPostUploadTag) inspirationCollectionListDialog).tagitemshow(selectedItem);

        } else {
            removeSelection(position);
        }

        ((FragmentPostUploadTag) inspirationCollectionListDialog).hideSubmit();

    }

    public class ViewHolder {
        private HorizontalScrollView productLayout;
        private LinearLayout productInflaterLayout;

        TextView collection_name;


    }

}
