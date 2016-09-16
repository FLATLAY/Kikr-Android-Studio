package com.kikr.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.dialog.CollectionListDialog;
import com.kikr.fragment.FragmentDiscoverDetail;
import com.kikr.post_upload.ProductSearchTagging;
import com.kikr.utility.CommonUtility;
import com.kikr.utility.UiUpdate;
import com.kikrlib.bean.Product;

import java.util.List;


public class ProductTaggingListAdapter
        extends BaseAdapter {

    private ProductSearchTagging mContext;
    private LayoutInflater mInflater;
    private List<Product> data;
    boolean isProductSearch = false;

    public ProductTaggingListAdapter(ProductSearchTagging context, List<Product> data) {
        super();
        this.mContext = context;
        this.data = data;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        isProductSearch = false;
    }


	public void setData(List<Product> data){
		this.data.addAll(data);
	}

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
            convertView = mInflater.inflate(R.layout.product_auto_suggest_items, null);
            viewHolder = new ViewHolder();
            viewHolder.activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.productImage);
            viewHolder.activity_product_list_product_name = (TextView) convertView.findViewById(R.id.productName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.activity_product_list_product_name.setText(data.get(position).getProductname());
        CommonUtility.setImage(mContext, data.get(position).getProductimageurl(), viewHolder.activity_product_list_product_image, R.drawable.dum_list_item_product);
//        if (!isProductSearch)
//            convertView.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    CommonUtility.hideSoftKeyboard(mContext);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("data", data.get(position));
////                   mContext.setResult(001,bundle);
////                    addFragment(detail);
//                }
//            });

//        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(final View v) {
//                if (((HomeActivity) mContext).isMenuShowing()) {
//                    ((HomeActivity) mContext).hideContextMenu();
//                } else {
//                    ((HomeActivity) mContext).showContextMenu(data.get(position), new UiUpdate() {
//
//                        @Override
//                        public void updateUi() {
//                            TextView likeCountTextView = (TextView) v.findViewById(R.id.likeCountTextView);
//                            likeCountTextView.setText(TextUtils.isEmpty(data.get((Integer) v.getTag()).getLike_info().getLike_count()) ? "0" : data.get((Integer) v.getTag()).getLike_info().getLike_count());
//                        }
//                    });
//                }
//                return true;
//            }
//        });
        return convertView;
    }

    public class ViewHolder {
        ImageView activity_product_list_product_image, activity_product_list_star;
        TextView activity_product_list_product_name;
    }

//    private void addFragment(Fragment fragment) {
//        ((HomeActivity) mContext).addFragment(fragment);
//    }
}
