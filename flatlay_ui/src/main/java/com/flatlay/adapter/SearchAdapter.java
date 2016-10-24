package com.flatlay.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.CollectionListDialog;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.bean.Product;

public class SearchAdapter extends BaseAdapter {

    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<Product> data;
    boolean isProductSearch = false;

    public SearchAdapter(FragmentActivity context, List<Product> data) {
        super();
        this.mContext = context;
        this.data = data;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isProductSearch = false;
    }

    public SearchAdapter(FragmentActivity context, List<Product> data, boolean isProductSearch) {
        super();
        this.mContext = context;
        this.data = data;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.isProductSearch = isProductSearch;

    }

//	public void setData(List<Product> data){
//		this.data.addAll(data);
//	}

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Product getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_product_based_on_brand, null);
            viewHolder = new ViewHolder();
            viewHolder.activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);
            viewHolder.activity_product_list_product_name = (TextView) convertView.findViewById(R.id.activity_product_list_product_name);
            viewHolder.activity_product_list_star = (ImageView) convertView.findViewById(R.id.activity_product_list_star);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.activity_product_list_product_name.setText(data.get(position).getProductname());
        CommonUtility.setImage(mContext, data.get(position).getProductimageurl(), viewHolder.activity_product_list_product_image, R.drawable.dum_list_item_product);
        if (!isProductSearch)
            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    CommonUtility.hideSoftKeyboard(mContext);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data.get(position));
                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail(null);
                    detail.setArguments(bundle);
                    addFragment(detail);
                }
            });
        viewHolder.activity_product_list_star.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (((HomeActivity) mContext).checkInternet()) {
                    CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, data.get(position));
                    collectionListDialog.show();
                }
            }
        });
        convertView.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {
                if (((HomeActivity) mContext).isMenuShowing()) {
                    ((HomeActivity) mContext).hideContextMenu();
                } else {
                    ((HomeActivity) mContext).showContextMenu(data.get(position), new UiUpdate() {

                        @Override
                        public void updateUi() {
                            TextView likeCountTextView = (TextView) v.findViewById(R.id.likeCountTextView);
                            likeCountTextView.setText(TextUtils.isEmpty(data.get((Integer) v.getTag()).getLike_info().getLike_count()) ? "0" : data.get((Integer) v.getTag()).getLike_info().getLike_count());
                        }
                    });
                }
                return true;
            }
        });
        return convertView;
    }

    public class ViewHolder {
        ImageView activity_product_list_product_image, activity_product_list_star;
        TextView activity_product_list_product_name;
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }
}
