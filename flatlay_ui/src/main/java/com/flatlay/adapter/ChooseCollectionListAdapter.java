package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.ChooseCollectionDialog;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.utils.AlertUtils;

import java.util.List;

/**
 * Created by RachelDi on 2/13/18.
 */

public class ChooseCollectionListAdapter extends BaseAdapter implements ServiceCallback {
    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<CollectionList> data;
    private ChooseCollectionDialog collectionListDialog;
//    private Product product;


    public ChooseCollectionListAdapter(FragmentActivity context, List<CollectionList> data,
                                       ChooseCollectionDialog collectionListDialog) {
        super();
        this.mContext = context;
        this.data = data;
//        this.product = product;
        this.collectionListDialog = collectionListDialog;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<CollectionList> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CollectionList getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.adapter_collection_choose_item, null);
            viewHolder = new ViewHolder();
            viewHolder.collection_name = (TextView) view.findViewById(R.id.collection_name);
            viewHolder.collection_name.setTypeface(FontUtility.setMontserratLight(mContext));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.collection_name.setText(getItem(i).getName());
//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                if (((HomeActivity) mContext).checkInternet())
//                    addCollectionToProduct(getItem(i).getId(), product);
//            }
//        });
        return view;
    }

    @Override
    public void handleOnSuccess(Object object) {
        CollectionApiRes collectionApiRes = (CollectionApiRes) object;
        AlertUtils.showToast(mContext, collectionApiRes.getMessage());
        collectionListDialog.dismiss();
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        if (object != null) {
            CollectionApiRes response = (CollectionApiRes) object;
            AlertUtils.showToast(mContext, response.getMessage());
        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }

    public class ViewHolder {
        TextView collection_name;
    }

//    private void addCollectionToProduct(String collection_id, Product product) {
//
//        String from_user_id = TextUtils.isEmpty(product.getFrom_user_id()) ? "" :
//                product.getFrom_user_id();
//        String from_collection_id = TextUtils.isEmpty(product.getFrom_collection_id()) ? "" :
//                product.getFrom_collection_id();
//
//        final CollectionApi collectionApi = new CollectionApi(this);
//        collectionApi.addProductInCollection(UserPreference.getInstance().getUserID(),
//                collection_id, product.getId(), from_user_id, from_collection_id,
//                product.getProductimageurl());
//        collectionApi.execute();
//    }
}
