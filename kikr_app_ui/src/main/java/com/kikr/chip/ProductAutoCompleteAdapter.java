package com.kikr.chip;

import android.content.Context;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anshumaan on 6/15/2016.
 */
public class ProductAutoCompleteAdapter extends ArrayAdapter<Product> {

    List<Product> products = new ArrayList<>();
    Context mContext;
    int viewResourceId;

    public ProductAutoCompleteAdapter(Context context, int viewResourceId, List<Product> products) {
        super(context, viewResourceId, products);
        this.mContext = context;
        this.products.addAll(products);

        this.viewResourceId = viewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        Product product = products.get(position);
        if (product != null) {
            ImageView productImage = (ImageView) v.findViewById(R.id.productImage);
            TextView productName = (TextView) v.findViewById(R.id.productName);

            productName.setText(product.getProductname());
            CommonUtility.setImage((FragmentActivity) mContext, product.getProductimageurl(), productImage, R.drawable.dum_list_item_product);
        }
        return v;
    }

}
