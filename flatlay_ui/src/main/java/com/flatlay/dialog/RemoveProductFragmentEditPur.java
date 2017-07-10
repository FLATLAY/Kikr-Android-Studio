package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentEditPurchaseItem;
import com.flatlay.fragment.FragmentUserCart;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CallBack;

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
        Log.w("RemoveProductFragmentEP","initUI()");
    }

}
