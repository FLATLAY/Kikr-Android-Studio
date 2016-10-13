package com.flatlay.googlewallet;

import com.google.android.gms.wallet.Cart;
import com.google.android.gms.wallet.FullWalletRequest;
import com.google.android.gms.wallet.LineItem;
import com.google.android.gms.wallet.NotifyTransactionStatusRequest;
import com.kikrlib.bean.CartTotalInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class WalletUtil {

    private static final BigDecimal MICROS = new BigDecimal(1000000d);

    private WalletUtil() {}

//    public static MaskedWalletRequest createMaskedWalletRequest(CartTotalInfo cartTotalInfo) {
//
//        // Build a List of all line items
////        List<LineItem> lineItems = buildLineItems(itemInfo, true);
//        List<LineItem> lineItems = buildLineItems(cartTotalInfo);
//
//        // Calculate the cart total by iterating over the line items.
////        String cartTotal = calculateCartTotal(lineItems);
//        String cartTotal = cartTotalInfo.getTotal_price();
//
//
//        return MaskedWalletRequest.newBuilder()
//                .setMerchantName(Constants.MERCHANT_NAME)
//                .setPhoneNumberRequired(true)
//                .setShippingAddressRequired(true)
//                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
//                .setEstimatedTotalPrice(cartTotal)
//                // Create a Cart with the current line items. Provide all the information
//                // available up to this point with estimates for shipping and tax included.
//                .setCart(Cart.newBuilder()
//                        .setCurrencyCode(Constants.CURRENCY_CODE_USD)
//                        .setTotalPrice(cartTotal)
//                        .setLineItems(lineItems)
//                        .build())
//                // Indicate whether we need the Wallet Objects associated with the user.
//                .setShouldRetrieveWalletObjects(true)
//                .build();
//    }

//    private static List<LineItem> buildLineItems(ItemInfo itemInfo, boolean isEstimate) {
//        List<LineItem> list = new ArrayList<LineItem>();
//        String itemPrice = toDollars(itemInfo.priceMicros);
//
//        list.add(LineItem.newBuilder()
//                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
//                .setDescription(itemInfo.name)
//                .setQuantity("1")
//                .setUnitPrice(itemPrice)
//                .setTotalPrice(itemPrice)
//                .build());
//
//        String shippingPrice = toDollars(
//                isEstimate ? itemInfo.estimatedShippingPriceMicros : itemInfo.shippingPriceMicros);
//
//        list.add(LineItem.newBuilder()
//                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
//                .setDescription(Constants.DESCRIPTION_LINE_ITEM_SHIPPING)
//                .setRole(LineItem.Role.SHIPPING)
//                .setTotalPrice(shippingPrice)
//                .build());
//
//        String tax = toDollars(
//                isEstimate ? itemInfo.estimatedTaxMicros : itemInfo.taxMicros);
//
//        list.add(LineItem.newBuilder()
//                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
//                .setDescription(Constants.DESCRIPTION_LINE_ITEM_TAX)
//                .setRole(LineItem.Role.TAX)
//                .setTotalPrice(tax)
//                .build());
//        return list;
//    }
    
    private static List<LineItem> buildLineItems(CartTotalInfo cartTotalInfo) {
        List<LineItem> list = new ArrayList<LineItem>();

        list.add(LineItem.newBuilder()
                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
                .setDescription("Sub Total")
                .setTotalPrice(cartTotalInfo.getSub_total())
                .build());
        
        list.add(LineItem.newBuilder()
                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
                .setDescription("Handaling")
                .setTotalPrice(cartTotalInfo.getHandling())
                .build());

        list.add(LineItem.newBuilder()
                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
                .setDescription(Constants.DESCRIPTION_LINE_ITEM_SHIPPING)
                .setRole(LineItem.Role.SHIPPING)
                .setTotalPrice(cartTotalInfo.getEst_shippping())
                .build());

        list.add(LineItem.newBuilder()
                .setCurrencyCode(Constants.CURRENCY_CODE_USD)
                .setDescription(Constants.DESCRIPTION_LINE_ITEM_TAX)
                .setRole(LineItem.Role.TAX)
                .setTotalPrice(cartTotalInfo.getEst_tax())
                .build());
        return list;
    }

    private static String calculateCartTotal(List<LineItem> lineItems) {
        BigDecimal cartTotal = BigDecimal.ZERO;

        // Calculate the total price by adding up each of the line items
        for (LineItem lineItem: lineItems) {
            BigDecimal lineItemTotal = lineItem.getTotalPrice() == null ?
                    new BigDecimal(lineItem.getUnitPrice())
                            .multiply(new BigDecimal(lineItem.getQuantity())) :
                    new BigDecimal(lineItem.getTotalPrice());

            cartTotal = cartTotal.add(lineItemTotal);
        }

        return cartTotal.setScale(2, RoundingMode.HALF_EVEN).toString();
    }

    public static FullWalletRequest createFullWalletRequest(CartTotalInfo cartTotalInfo,String googleTransactionId) {

//        List<LineItem> lineItems = buildLineItems(itemInfo, false);
        List<LineItem> lineItems = buildLineItems(cartTotalInfo);


//        String cartTotal = calculateCartTotal(lineItems);
        String cartTotal = cartTotalInfo.getTotal_price();

        return FullWalletRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setCart(Cart.newBuilder()
                        .setCurrencyCode(Constants.CURRENCY_CODE_USD)
                        .setTotalPrice(cartTotal)
                        .setLineItems(lineItems)
                        .build())
                .build();
    }

    @SuppressWarnings("javadoc")
    public static NotifyTransactionStatusRequest createNotifyTransactionStatusRequest(
            String googleTransactionId, int status) {
        return NotifyTransactionStatusRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setStatus(status)
                .build();
    }

    private static String toDollars(long micros) {
        return new BigDecimal(micros).divide(MICROS)
                .setScale(2, RoundingMode.HALF_EVEN).toString();
    }
}
