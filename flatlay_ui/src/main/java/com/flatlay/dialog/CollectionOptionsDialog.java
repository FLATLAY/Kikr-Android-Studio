package com.flatlay.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentProductBasedOnType;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlaylib.api.ProductBasedOnBrandApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.ProductBasedOnBrandRes;
import com.flatlaylib.utils.AlertUtils;

public class CollectionOptionsDialog extends Dialog implements android.view.View.OnClickListener {
    private Activity mContext;
    private TextView viewCollectionText, renameCollectionText, shareCollectionText, deleteProductFromCollectiontText, deleteCollectionText, cancelText;
    private String userId, collectionId, collectionName;
    private FragmentProfileView fragmentProfileView;
    private ProgressBarDialog mProgressBarDialog;
    private boolean isFirstTime = true;
    private ArrayList<Product> arrayList = new ArrayList<Product>();
    private ProductBasedOnBrandApi productBasedOnBrandApi;
    private int pageno = 0;

    public CollectionOptionsDialog(Activity context, String user_id, String collection_id, FragmentProfileView fragmentProfileView, String collectionName) {
        super(context, R.style.AdvanceDialogTheme);
        mContext = context;
        userId = user_id;
        collectionId = collection_id;
        this.collectionName = collectionName;
        this.fragmentProfileView = fragmentProfileView;
        init();
    }

    public CollectionOptionsDialog(FragmentActivity context, int theme) {
        super(context, R.style.AdvanceDialogTheme);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_collection_option_list);
        setCancelable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.4f;
        //lp.gravity = Gravity.BOTTOM;

        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        viewCollectionText = (TextView) findViewById(R.id.viewCollectionText);
        renameCollectionText = (TextView) findViewById(R.id.renameCollectionText);
        shareCollectionText = (TextView) findViewById(R.id.shareCollectionText);
        deleteProductFromCollectiontText = (TextView) findViewById(R.id.deleteProductFromCollectionText);
        deleteCollectionText = (TextView) findViewById(R.id.deleteCollectionText);
        cancelText = (TextView) findViewById(R.id.cancelText);
        viewCollectionText.setOnClickListener(this);
        renameCollectionText.setOnClickListener(this);
        shareCollectionText.setOnClickListener(this);
        deleteProductFromCollectiontText.setOnClickListener(this);
        deleteCollectionText.setOnClickListener(this);
        cancelText.setOnClickListener(this);
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewCollectionText:
                dismiss();
                addFragment(new FragmentProductBasedOnType(userId, collectionId, true));
                break;
            case R.id.renameCollectionText:
                dismiss();
                RenameCollectionDialog renameCollectionDialog = new RenameCollectionDialog(mContext, fragmentProfileView, collectionId, collectionName);
                renameCollectionDialog.show();
                break;
            case R.id.shareCollectionText:
                if (((HomeActivity) mContext).checkInternet())
                    getCollectionList();
                break;
            case R.id.deleteProductFromCollectionText:
                dismiss();
                addFragment(new FragmentProductBasedOnType(userId, collectionId, false));
                break;
            case R.id.deleteCollectionText:
                dismiss();
                DeleteCollectiontDialog deleteCollectiontDialog = new DeleteCollectiontDialog(mContext, fragmentProfileView, userId, collectionId);
                deleteCollectiontDialog.show();
                break;
            case R.id.cancelText:
                dismiss();
                break;
        }
    }

    private void getCollectionList() {
        isFirstTime = true;
        pageno = 0;
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();

        productBasedOnBrandApi = new ProductBasedOnBrandApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
                List<Product> productLists = productBasedOnBrandRes.getData();
                if (isFirstTime && productLists.size() == 0) {
                    mProgressBarDialog.dismiss();
                    dismiss();
                    AlertUtils.showToast(mContext, mContext.getResources().getString(R.string.alert_collection_no_product));
                    return;
                }
                if (productLists.size() > 0) {
                    arrayList.addAll(productLists);
                }
                if (productLists.size() < 10) {
                    mProgressBarDialog.dismiss();
                    sharaeProduct();
                } else {
                    pageno++;
                    productBasedOnBrandApi.getProductsBasedOnCollectionList(UserPreference.getInstance().getUserID(), String.valueOf(pageno), collectionId);
                    productBasedOnBrandApi.execute();
                }
                isFirstTime = false;
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                mProgressBarDialog.dismiss();
                if (object != null) {
                    ProductBasedOnBrandRes response = (ProductBasedOnBrandRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        productBasedOnBrandApi.getProductsBasedOnCollectionList(UserPreference.getInstance().getUserID(), String.valueOf(pageno), collectionId);
        productBasedOnBrandApi.execute();

        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                productBasedOnBrandApi.cancel();
            }
        });
    }

    protected void sharaeProduct() {
        ((HomeActivity) mContext).shareProductCollection(collectionName);
        dismiss();
    }

}
