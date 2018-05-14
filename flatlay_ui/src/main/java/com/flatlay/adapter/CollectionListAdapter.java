package com.flatlay.adapter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.CollectionListDialog;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class CollectionListAdapter extends BaseAdapter implements ServiceCallback {

    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<CollectionList> data;
    private Product product;
    private CollectionListDialog collectionListDialog;
    final String TAG = "CollectionListAdapter";

    public CollectionListAdapter(FragmentActivity context, List<CollectionList> data,
                                 Product productId, CollectionListDialog collectionListDialog) {
        super();
        this.mContext = context;
        this.data = data;
        this.product = productId;
        this.collectionListDialog = collectionListDialog;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.w(TAG, "CollectionListAdapter");
    }

    public void setData(List<CollectionList> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CollectionList getItem(int index) {
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
            convertView = mInflater.inflate(R.layout.adapter_collection_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.collection_name = (TextView) convertView.findViewById(R.id.collection_name);
            viewHolder.collection_name.setTypeface(FontUtility.setMontserratLight(mContext));
            viewHolder.add_product_to_collection_imageview = (ImageView)
                    convertView.findViewById(R.id.add_product_to_collection_imageview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.collection_name.setText(getItem(position).getName());
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (((HomeActivity) mContext).checkInternet())
                    addCollectionToProduct(getItem(position).getId(), product);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        TextView collection_name;
        ImageView add_product_to_collection_imageview;
    }

    private void addCollectionToProduct(String collection_id, Product product) {

        String from_user_id = TextUtils.isEmpty(product.getFrom_user_id()) ? "" :
                product.getFrom_user_id();
        String from_collection_id = TextUtils.isEmpty(product.getFrom_collection_id()) ? "" :
                product.getFrom_collection_id();

        final CollectionApi collectionApi = new CollectionApi(this);
        collectionApi.addProductInCollection(UserPreference.getInstance().getUserID(),
                collection_id, product.getId(), from_user_id, from_collection_id,
                product.getProductimageurl());
        collectionApi.execute();

    }

    @Override
    public void handleOnSuccess(Object object) {
        Syso.info("In handleOnSuccess>>" + object);
        CollectionApiRes collectionApiRes = (CollectionApiRes) object;
        AlertUtils.showToast(mContext, collectionApiRes.getMessage());
        collectionListDialog.dismiss();
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            CollectionApiRes response = (CollectionApiRes) object;
            AlertUtils.showToast(mContext, response.getMessage());
        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }
}
