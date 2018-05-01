package com.flatlay.chip;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.bean.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAutoCompleteAdapter extends ArrayAdapter<Product> {

    List<Product> products = new ArrayList<>();
    Context mContext;
    int viewResourceId;
    final String TAG = "ProductAutoCom";

    public ProductAutoCompleteAdapter(Context context, int viewResourceId, List<Product> products) {
        super(context, viewResourceId, products);
        this.mContext = context;
        this.products.addAll(products);

        this.viewResourceId = viewResourceId;
        Log.w(TAG,"ProductAutoCompleteAdapter");
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
