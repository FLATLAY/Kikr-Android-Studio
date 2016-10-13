package com.flatlay.activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * Created by Tycho on 5/23/2016.
 */
public class MultiLineEditText extends EditText{


    public MultiLineEditText(Context context) {
        super(context);
    }

    public MultiLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiLineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MultiLineEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        InputConnection connection = super.onCreateInputConnection(outAttrs);
        int imeActions = outAttrs.imeOptions&EditorInfo.IME_MASK_ACTION;
        if ((imeActions&EditorInfo.IME_ACTION_DONE) != 0) {
            // clear the existing action
            outAttrs.imeOptions ^= imeActions;
            // set the DONE action
            outAttrs.imeOptions |= EditorInfo.IME_ACTION_DONE;
        }
        if ((outAttrs.imeOptions&EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0) {
            outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
        }
        return connection;
    }


  //  Add this code on main activity which edit text you Want in multiline Android and Edit text will be under Class name
    //-- uses this link   http://stackoverflow.com/questions/5014219/multiline-edittext-with-done-softinput-action-label-on-2-3/


//    descriptionEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//        @Override
//        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//            if (actionId == EditorInfo.IME_ACTION_DONE ||
//                    event.getAction() == KeyEvent.ACTION_DOWN &&
//                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                CommonUtility.hideSoftKeyboard(mContext);
//            }
//            return false;
//        }
//    });


}
