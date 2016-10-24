package com.flatlay.googlewallet;

import android.content.Context;

import com.flatlay.R;

public class Util {




    static String formatPrice(Context context, long priceMicros) {
        return context.getString(R.string.price_format, priceMicros / 1000000d);
    }

}
