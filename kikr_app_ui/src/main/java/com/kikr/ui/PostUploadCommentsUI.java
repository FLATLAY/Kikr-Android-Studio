package com.kikr.ui;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.flatlay.R;
import com.kikr.utility.CommonUtility;
import com.kikrlib.bean.CollectionImages;
import com.kikrlib.bean.Product;
import com.kikrlib.bean.ProfileCollectionList;

import java.util.ArrayList;
import java.util.List;

public class PostUploadCommentsUI {
    FragmentActivity mContext;
    List<CollectionImages> data;
    LayoutInflater mInflater;
    boolean isAddProduct;
    ArrayList<Product> addProductListNew = new ArrayList<>();

    public PostUploadCommentsUI(FragmentActivity context, ProfileCollectionList collection_images, boolean isAddProduct, ArrayList<Product> addProductListNew) {
        super();
        this.mContext = context;
        this.isAddProduct = isAddProduct;
        if (isAddProduct)
            this.addProductListNew.addAll(addProductListNew);
        else
            this.data = collection_images.getCollection_images();

        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        LinearLayout ll = new LinearLayout(mContext);
        //LayoutParams layoutParams =new LinearLayout.LayoutParams(CommonUtility.getDeviceWidth(mContext)/2, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        if (isAddProduct) {
            for (int i = 0; i < addProductListNew.size(); i++) {
                View convertView = (LinearLayout) mInflater.inflate(R.layout.post_upload_comments_small, null);

                ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);

                CommonUtility.setImage(mContext, addProductListNew.get(i).getProductimageurl(), activity_product_list_product_image, R.drawable.dum_list_item_product);
                convertView.setLayoutParams(layoutParams);
                ll.addView(convertView);

            }

        } else {
            for (int i = 0; i < data.size(); i++) {
                View convertView = (LinearLayout) mInflater.inflate(R.layout.post_upload_comments_small, null);

                ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);


                CollectionImages images = data.get(i);


                CommonUtility.setImage(mContext, images.getProductimageurl(), activity_product_list_product_image, R.drawable.dum_list_item_product);
                convertView.setLayoutParams(layoutParams);
                ll.addView(convertView);

            }
        }

        return ll;
    }


}

