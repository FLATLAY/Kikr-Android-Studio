package com.flatlay.dialog;

import java.util.List;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.adapter.CollectionListAdapter;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.AddCollectionApi;
import com.flatlaylib.api.CollectionApi;
import com.flatlaylib.bean.CollectionList;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.AddCollectionApiRes;
import com.flatlaylib.service.res.CollectionApiRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class CollectionListDialog extends Dialog implements ServiceCallback {
    private FragmentActivity mContext;
    private LinearLayout add_collection_layout;
    //private TextView share_collection_layout;
    private ListView collection_listing;
    private Product productList;
    private CollectionListDialog collectionListDialog;
    private CollectionListAdapter collectionListAdapter;
    private TextView addToCollectionText, createCollectionText, createCollectionText2;
    private Button doneButton;
    private LinearLayout cancelicon;
    boolean isCreating = false;

    //private ProgressBar progressBarCollection;

    public CollectionListDialog(FragmentActivity context, Product productList) {
        super(context, R.style.AdvanceDialogTheme);
        mContext = context;
        collectionListDialog = this;
        this.productList = productList;
        init();
    }

//    public CollectionListDialog(FragmentActivity context, int theme) {
//        super(context, R.style.AdvanceDialogTheme);
//        mContext = context;
//        init();
//    }

    private void init() {
        setContentView(R.layout.dialog_collection_list);
        setCancelable(true);
        collection_listing = (ListView) findViewById(R.id.collection_listing);
        add_collection_layout = (LinearLayout) findViewById(R.id.add_collection_layout);
        addToCollectionText = (TextView) findViewById(R.id.addToCollectionText);
        addToCollectionText.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        createCollectionText = (TextView) findViewById(R.id.createCollectionText);
        createCollectionText.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        createCollectionText2 = (TextView) findViewById(R.id.createCollectionText2);
        createCollectionText2.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        cancelicon = (LinearLayout) findViewById(R.id.cancellayout);
        doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setTypeface(FontUtility.setMontserratLight(getOwnerActivity()));
        //share_collection_layout = (TextView) findViewById(R.id.share_collection_layout);
        if (checkInternet())
            getCollectionList();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.9f;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);
        cancelicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final AddCollectionApi collectionApi = new AddCollectionApi(this);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCreating) {
                    String newCollection = createCollectionText2.getText().toString().trim();
                    if (newCollection != null && newCollection.length() > 0)
                        collectionApi.addNewCollection(newCollection);
                    collectionApi.execute();
                    getCollectionList();
                    createCollectionText2.setVisibility(View.GONE);
                    createCollectionText.setVisibility(View.VISIBLE);
                } else {
                    dismiss();
                }
            }
        });
        createCollectionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AddCollectionDialog dialog = new AddCollectionDialog(mContext,productList.getId(),collectionListDialog);
                //dialog.show();
                createCollectionText.setVisibility(View.GONE);
                createCollectionText2.setVisibility(View.VISIBLE);
                isCreating = true;
            }
        });
//		share_collection_layout.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				((HomeActivity) mContext).shareProductCollection("");
//			}
//		});
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
//		progressBarCollection.setVisibility(View.VISIBLE);
        final CollectionApi collectionApi = new CollectionApi(this);
        collectionApi.getCollectionList(UserPreference.getInstance().getUserID());
        collectionApi.execute();
    }

    @Override
    public void handleOnSuccess(Object object) {
        //	progressBarCollection.setVisibility(View.GONE);
        Syso.info("In handleOnSuccess>>" + object);
        if (object instanceof AddCollectionApiRes) {
            AddCollectionApiRes collectionApiRes = (AddCollectionApiRes) object;
            AlertUtils.showToast(mContext, collectionApiRes.getMessage());
        } else {
            CollectionApiRes collectionApiRes = (CollectionApiRes) object;
            List<CollectionList> collectionLists = collectionApiRes.getCollection();
            if (collectionLists.size() > 0) {
                if (collectionListAdapter == null) {
                    collectionListAdapter = new CollectionListAdapter(mContext, collectionLists,
                            productList, collectionListDialog);
                    collection_listing.setAdapter(collectionListAdapter);
                } else {
                    collectionListAdapter.setData(collectionLists);
                    collectionListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            if (object instanceof AddCollectionApiRes) {
                AddCollectionApiRes response = (AddCollectionApiRes) object;
                AlertUtils.showToast(mContext, response.getMessage());
            } else {
                CollectionApiRes response = (CollectionApiRes) object;
                AlertUtils.showToast(mContext, response.getMessage());
            }
        } else {
            AlertUtils.showToast(mContext, R.string.invalid_response);
        }
    }
}
