package com.kikr.googlewallet;

import com.google.android.gms.wallet.Address;
import com.google.android.gms.wallet.LoyaltyWalletObject;
import com.google.android.gms.wallet.MaskedWallet;
import com.flatlay.R;

import android.content.Context;
import android.text.TextUtils;

public class Util {

    static String formatPaymentDescriptions(MaskedWallet maskedWallet) {
        StringBuilder sb = new StringBuilder();
        for (String description : maskedWallet.getPaymentDescriptions()) {
            sb.append(description);
            sb.append("\n");
        }
        if (sb.length() > 0) {
            // remove trailing newline
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    static String formatAddress(Context context, Address address) {
        // different locales may need different address formats, which would be handled in
        // R.string.address_format
        String address2 = address.getAddress2().length() == 0 ?
                address.getAddress2() : address.getAddress2() + "\n";
        String address3 = address.getAddress3().length() == 0 ?
                address.getAddress3() : address.getAddress3() + "\n";
        String addressString = context.getString(R.string.address_format, address.getName(),
                address.getAddress1(), address2, address3, address.getCity(), address.getState(),
                address.getPostalCode());
        return addressString;
    }

    static String formatPrice(Context context, long priceMicros) {
        return context.getString(R.string.price_format, priceMicros / 1000000d);
    }

    public static String formatLoyaltyWalletObject(LoyaltyWalletObject loyaltyWalletObject) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(loyaltyWalletObject.getAccountId())) {
            sb.append(loyaltyWalletObject.getAccountId())
                    .append(", ");
        }
        sb.append(loyaltyWalletObject.getProgramName());
        return sb.toString();
    }
}
