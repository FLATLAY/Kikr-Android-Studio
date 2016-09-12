package com.kikr.googlewallet;

import com.google.android.gms.analytics.ecommerce.Product;

import java.math.BigDecimal;

public class ItemInfo {

    public final String name;
    public final String description;
    // Micros are used for prices to avoid rounding errors when converting between currencies
    public final long priceMicros;
    // The estimated tax used to calculate the estimated total price for a Masked Wallet request.
    public final long estimatedTaxMicros;
    // The estimated shipping price used with a Masked Wallet request.
    public final long estimatedShippingPriceMicros;
    // Actual tax and shipping price that should be calculated based on the shipping address
    // received in a MaskedWallet and used when fetching a Full Wallet.
    public final long taxMicros;
    public final long shippingPriceMicros;
    public final String currencyCode;
    public final String sellerData;
    public final int imageResourceId;

    public ItemInfo(String name, String description, long price, long shippingPrice,
            String currencyCode, String sellerData, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.priceMicros = price;
        this.estimatedTaxMicros = (int) (price * 0.10);
        this.taxMicros = (int) (price * 0.10);
        // put in an estimated shipping price
        this.estimatedShippingPriceMicros = 10000000L;
        this.shippingPriceMicros = shippingPrice;
        this.currencyCode = currencyCode;
        this.sellerData = sellerData;
        this.imageResourceId = imageResourceId;
    }

    @Override
    public String toString() {
        return name;
    }

    public Product toProduct() {
        Product product = new Product()
                .setId(name)
                .setName(name)
                .setCategory("bikes")
                .setBrand("Google")
                .setPrice(round(priceMicros/10000, 2, BigDecimal.ROUND_HALF_EVEN))
                .setQuantity(1);
        return product;
    }

    public double totalPrice() {
        return round((taxMicros + shippingPriceMicros + priceMicros)/10000
                , 2
                , BigDecimal.ROUND_HALF_EVEN);
    }

    public static double round(double number, int precision, int roundingMode){
        return new BigDecimal(number).setScale(precision, roundingMode).doubleValue();
    }

    public long getTotalPrice() {
        return priceMicros + taxMicros + shippingPriceMicros;
    }
}
