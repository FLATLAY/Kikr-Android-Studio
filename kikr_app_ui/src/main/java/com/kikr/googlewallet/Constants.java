package com.kikr.googlewallet;

import com.flatlay.R;
import com.kikrlib.bean.CartTotalInfo;


public class Constants {

    // Environment to use when creating an instance of Wallet.WalletOptions
//    public static final int WALLET_ENVIRONMENT = WalletConstants.ENVIRONMENT_SANDBOX;

    public static final String MERCHANT_NAME = "Kikr";

    // Intent extra keys
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    public static final String EXTRA_ITEM_DETAIL = "EXTRA_ITEM_DETAIL";
    public static final String EXTRA_MASKED_WALLET = "EXTRA_MASKED_WALLET";
    public static final String EXTRA_FULL_WALLET = "EXTRA_FULL_WALLET";

    public static final String CURRENCY_CODE_USD = "USD";

    // values to use with KEY_DESCRIPTION
    public static final String DESCRIPTION_LINE_ITEM_SHIPPING = "Shipping";
    public static final String DESCRIPTION_LINE_ITEM_TAX = "Tax";

    /**
     * Sample list of items for sale. The list would normally be fetched from
     * the merchant's servers.
     */
    public static final ItemInfo[] ITEMS_FOR_SALE = {
            new ItemInfo("Shoes", "Features", 300000000, 9990000, CURRENCY_CODE_USD,
                    "seller data 0", R.drawable.dum_list_item_big_img),
            new ItemInfo("Adjustable Bike", "More features", 400000000, 9990000, CURRENCY_CODE_USD,
                    "seller data 1", R.drawable.dum_items_backpack),
            new ItemInfo("Conference Bike", "Even more features", 600000000, 9990000,
                    CURRENCY_CODE_USD, "seller data 2", R.drawable.dum_items_backpack)
    };

    // To change promotion item, change the item here and also corresponding text/image
    // in fragment_promo_address_lookup.xml layout.
    public static final int PROMOTION_ITEM = 2;
    
	
    public static CartTotalInfo getDummyToatlObject() {
		CartTotalInfo cartTotalInfo=new CartTotalInfo();
		cartTotalInfo.setItem_count("2");
		cartTotalInfo.setSub_total("200");
		cartTotalInfo.setEst_shippping("10");
		cartTotalInfo.setEst_tax("5");
		cartTotalInfo.setHandling("1");
		cartTotalInfo.setTotal_price("216");
		return cartTotalInfo;
	}

}
