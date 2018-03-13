package com.flatlay.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.flatlay.R;
import com.flatlay.adapter.ChooseCollectionListAdapter;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.utils.AlertUtils;

import java.util.List;

/**
 * Created by RachelDi on 2/13/18.
 */

public class ChooseCollectionDialog extends Dialog implements ServiceCallback {
    private FragmentActivity mContext;
    private ListView collection_listing;
    private ChooseCollectionListAdapter chooseCollectionListAdapter;
    private ChooseCollectionDialog chooseCollectionDialog;
    private List<CollectionList> collectionLists;
    private Button doneButton;
    private ImageView more_items_arrow;
    private int preLast;

//    private Product product;


    public ChooseCollectionDialog(FragmentActivity context) {
        super(context, R.style.AdvanceDialogTheme);
        mContext = context;
        chooseCollectionDialog = this;
//        this.product = product;
        init();
    }

    public CollectionList geCheckedCollection(int i) {
        return chooseCollectionListAdapter.getItem(i);
    }

    private void init() {
        setContentView(R.layout.choose_dialog_list);
        setCancelable(true);
        collection_listing = (ListView) findViewById(R.id.collection_listing);
        more_items_arrow = (ImageView) findViewById(R.id.more_items_arrow);
        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        if (checkInternet())
            getCollectionList();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        collection_listing.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    if (preLast != lastItem) {
                        preLast = lastItem;
                        more_items_arrow.setVisibility(View.INVISIBLE);
                    }
                } else {
                    preLast = -1;
                    more_items_arrow.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean checkInternet() {
        if (CommonUtility.isOnline(mContext)) {
            return true;
        } else {
            CommonUtility.showNoInternetAlert(mContext);
            return false;
        }
    }

    public void getCollectionList() {
        final CollectionApi collectionApi = new CollectionApi(this);
        collectionApi.getCollectionList(UserPreference.getInstance().getUserID());
        collectionApi.execute();
    }

    @Override
    public void handleOnSuccess(Object object) {
        CollectionApiRes collectionApiRes = (CollectionApiRes) object;
        collectionLists = collectionApiRes.getCollection();
        if (collectionLists.size() > 0) {
            if (chooseCollectionListAdapter == null) {
                chooseCollectionListAdapter = new ChooseCollectionListAdapter(mContext,
                        collectionLists, chooseCollectionDialog);
                collection_listing.setAdapter(chooseCollectionListAdapter);
            } else {
                chooseCollectionListAdapter.setData(collectionLists);
                chooseCollectionListAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {

        if (object != null) {

            CollectionApiRes response = (CollectionApiRes) object;
            AlertUtils.showToast(mContext, response.getMessage());

        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }
}
