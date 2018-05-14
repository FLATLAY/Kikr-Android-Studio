package com.flatlay.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.UserResult;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by RachelDi on 4/1/18.
 */

public class CartItemsAdapter extends BaseAdapter {
    private FragmentActivity mContext;
    private List<Product> productLists;
    private LayoutInflater mInflater;
    final String TAG = "CartItemsAdapter";
    private DeleteItemsListener listener;


    public CartItemsAdapter(FragmentActivity mContext, List<Product> productLists, DeleteItemsListener listener) {
        this.mContext = mContext;
        this.productLists = productLists;
        this.listener = listener;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Product> data) {
        this.productLists = data;
    }

    @Override
    public int getCount() {
        return productLists.size();
    }

    @Override
    public Product getItem(int i) {
        return productLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cart_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.brand_name = (TextView) convertView.findViewById(R.id.brand_name);
            viewHolder.product_name = (TextView) convertView.findViewById(R.id.brand_name);
            viewHolder.color_text = (TextView) convertView.findViewById(R.id.brand_name);
            viewHolder.size_text = (TextView) convertView.findViewById(R.id.brand_name);
            viewHolder.price_text = (TextView) convertView.findViewById(R.id.brand_name);
            viewHolder.product_image = (RoundedImageView) convertView.findViewById(R.id.product_image);
            viewHolder.cancel_icon = (ImageView) convertView.findViewById(R.id.cancel_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.brand_name.setTypeface(FontUtility.setMontserratLight(mContext));
            viewHolder.product_name.setTypeface(FontUtility.setMontserratLight(mContext));
            viewHolder.color_text.setTypeface(FontUtility.setMontserratLight(mContext));
            viewHolder.size_text.setTypeface(FontUtility.setMontserratLight(mContext));
            viewHolder.price_text.setTypeface(FontUtility.setMontserratLight(mContext));
            String productImage = getItem(i).getProductimageurl();
            final Product currentProduct = getItem(i);
            if (productImage != null && productImage.length() > 0)
                CommonUtility.setImage(mContext, viewHolder.product_image, productImage);
            viewHolder.product_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", currentProduct);
                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
                    detail.setArguments(bundle);
                    ((HomeActivity) mContext).addFragment(new FragmentDiscoverDetail());
                }
            });
            viewHolder.brand_name.setText(getItem(i).getManufacturename());
            viewHolder.product_name.setText(getItem(i).getProductname());
            viewHolder.color_text.setText(getItem(i).getColor());
            viewHolder.size_text.setText(getItem(i).getSize());
            if (getItem(i).getSaleprice() == null || getItem(i).getSaleprice().length() <= 0)
                viewHolder.price_text.setText(getItem(i).getRetailprice());
            else
                viewHolder.price_text.setText(getItem(i).getSaleprice());
            viewHolder.cancel_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeFromCart(getItem(i).getId(), i);
                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        TextView brand_name, product_name, color_text, size_text, price_text, quantity_text;
        RoundedImageView product_image;
        ImageView cancel_icon;
    }

    public void removeFromCart(final String id, final int i) {

        final CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                for (int i = 0; i < productLists.size(); i++) {
                    if (id.equals(productLists.get(i).getProductcart_id())) {
                        productLists.remove(i);
                    }
                }
                UserPreference.getInstance().decCartCount();
                listener.onDelete(i);
                AlertUtils.showToast(mContext, "Product removed successfully");
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        cartApi.removeFromCart(UserPreference.getInstance().getUserID(), id);
        cartApi.execute();
    }

    public interface DeleteItemsListener { // create an interface

        void onDelete(int position);
    }

}
