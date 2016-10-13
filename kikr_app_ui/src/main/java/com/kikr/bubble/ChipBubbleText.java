package com.kikr.bubble;


import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.flatlay.R;

public class ChipBubbleText {

    private MultiAutoCompleteTextView multiAutoCompleteTextView;
    private static String[] adapterValue;
    private int threshold;
    private Context context;
    private ChipPropery chipPropery;

    public ChipBubbleText(Context context, MultiAutoCompleteTextView multiAutoCompleteTextView,
                          String[] adapterValue, int threshold) {
        super();
        this.context = context;
        this.multiAutoCompleteTextView = multiAutoCompleteTextView;
        this.adapterValue = adapterValue;
        this.threshold = threshold;
        chipPropery = new ChipPropery();
        chipPropery.setChipColor("#00FFFFFF");

        chipPropery.setChipTextSize(12);
    }

    public static String[] getAdapterValue() {
        return adapterValue;
    }

    public static void setAdapterValue(String[] adapterValue) {
        ChipBubbleText.adapterValue = adapterValue;
    }

    public void setChipColor(String color) {
        chipPropery.setChipColor(color);
    }

    public void setChipTextSize(float chipTextSize) {
        chipPropery.setChipTextSize(chipTextSize);
    }

    public void setChipTextColor(int color) {
        chipPropery.setChipTextColor(color);
    }

    public void initialize() {
        // TODO Auto-generated method stub
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.bubble_layout, R.id.bubbleText, adapterValue);
        multiAutoCompleteTextView.setTokenizer(new SpaceTokenizer());
        multiAutoCompleteTextView.setThreshold(threshold);
        multiAutoCompleteTextView.requestFocus();
        multiAutoCompleteTextView.setAdapter(adapter);

        multiAutoCompleteTextView.addTextChangedListener(new ChipBubbleTextWatcher(context, multiAutoCompleteTextView, chipPropery));
        multiAutoCompleteTextView.setOnClickListener(new ChioBubbleOnClickListner(context, multiAutoCompleteTextView, chipPropery));
        multiAutoCompleteTextView.setSingleLine(true);

        // multiAutoCompleteTextView.setO
    }

    public class SpaceTokenizer implements MultiAutoCompleteTextView.Tokenizer {

        public int findTokenStart(CharSequence text, int cursor) {
            int i = cursor;

            while (i > 0 && text.charAt(i - 1) != ' ') {
                i--;
            }
            while (i < cursor && text.charAt(i) == ' ') {
                i++;
            }

            return i;
        }

        public int findTokenEnd(CharSequence text, int cursor) {
            int i = cursor;
            int len = text.length();

            while (i < len) {
                if (text.charAt(i) == ' ') {
                    return i;
                } else {
                    i++;
                }
            }

            return len;
        }

        public CharSequence terminateToken(CharSequence text) {
            int i = text.length();

            while (i > 0 && text.charAt(i - 1) == ' ') {
                i--;
            }

            if (i > 0 && text.charAt(i - 1) == ' ') {
                return text;
            } else {
                if (text instanceof Spanned) {
                    SpannableString sp = new SpannableString(text + " ");
                    TextUtils.copySpansFrom((Spanned) text, 0, text.length(),
                            Object.class, sp, 0);
                    return sp;
                } else {
                    return text + " ";
                }
            }
        }
    }


}
