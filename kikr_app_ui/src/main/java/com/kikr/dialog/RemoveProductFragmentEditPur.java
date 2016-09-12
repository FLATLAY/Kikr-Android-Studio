package com.kikr.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentEditPurchaseItem;
import com.kikr.fragment.FragmentUserCart;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CallBack;

/**
 * Created by Tycho on 5/25/2016.
 */
public class RemoveProductFragmentEditPur extends Dialog {
    private TextView cancelTextView,okTextView;
    private String id;
    private ProgressBarDialog mProgressBarDialog;
    private Context mContext;
    CallBack callBack;
    String products;
    private FragmentEditPurchaseItem fragmentEditPurchaseItem;
    private FragmentUserCart fragmentUserCart;

    public RemoveProductFragmentEditPur(Context context,String id, FragmentUserCart fragmentUserCart, CallBack callBack) {
        super(context, R.style.AdvanceDialogTheme);
        this.id = id;
        this.mContext = context;
        this.callBack=callBack;
        this.fragmentUserCart = fragmentUserCart;
        init();
    }
    private void init() {
        setContentView(R.layout.dialog_remove_product_from_cart);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        cancelTextView= (TextView) findViewById(R.id.cancelTextView);
        okTextView = (TextView) findViewById(R.id.okTextView);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((HomeActivity)mContext).checkInternet()){
                    fragmentUserCart.removeFromCart(id);
//				removeFromCart();
                    ((HomeActivity) mContext).onBackPressed();
                    dismiss();
                }
            }
        });
    }

}
