package com.kikr.googlewallet;

import com.kikr.KikrApp;
import com.kikr.R;
import com.kikrlib.bean.CartTotalInfo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CartDetailFragment extends Fragment {

    private int mItemId;
    private CartTotalInfo cartTotalInfo;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemId = getActivity().getIntent().getIntExtra(Constants.EXTRA_ITEM_ID, 0);
        cartTotalInfo=(CartTotalInfo) getActivity().getIntent().getSerializableExtra(Constants.EXTRA_ITEM_DETAIL);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_detail, container, false);

        ItemInfo itemInfo = Constants.ITEMS_FOR_SALE[mItemId];

        TextView itemName = (TextView) view.findViewById(R.id.text_item_name);
        itemName.setText(itemInfo.name);

        Drawable itemImage = getResources().getDrawable(itemInfo.imageResourceId);
        int imageSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        int actualWidth = itemImage.getIntrinsicWidth();
        int actualHeight = itemImage.getIntrinsicHeight();
        int scaledHeight = imageSize;
        int scaledWidth = (int) (((float) actualWidth / actualHeight) * scaledHeight);
        itemImage.setBounds(0, 0, scaledWidth, scaledHeight);
        itemName.setCompoundDrawables(itemImage, null, null, null);

        TextView itemPrice = (TextView) view.findViewById(R.id.text_item_price);
        itemPrice.setText(Util.formatPrice(getActivity(), itemInfo.priceMicros));
        TextView shippingCost = (TextView) view.findViewById(R.id.text_shipping_price);
        TextView tax = (TextView) view.findViewById(R.id.text_tax_price);
        TextView total = (TextView) view.findViewById(R.id.text_total_price);
        if ((mItemId == Constants.PROMOTION_ITEM) && getApplication().isAddressValidForPromo()) {
            shippingCost.setText(Util.formatPrice(getActivity(), 0L));
        } else {
            shippingCost.setText(Util.formatPrice(getActivity(), itemInfo.shippingPriceMicros));
        }

        tax.setText(Util.formatPrice(getActivity(), itemInfo.taxMicros));
        total.setText(Util.formatPrice(getActivity(), itemInfo.getTotalPrice()));

        return view;
    }

    private KikrApp getApplication() {
      return (KikrApp) getActivity().getApplication();
  }

}
