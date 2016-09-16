package com.kikr.post_upload;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.BaseActivity;
import com.kikr.R;
import com.kikr.adapter.ProductTaggingListAdapter;
import com.kikr.adapter.SearchAdapter;
import com.kikr.adapter.SearchProductAdapter;
import com.kikr.chip.AutoSuggestProduct;
import com.kikr.chip.ProductAutoCompleteAdapter;
import com.kikr.chip.TagsEditText;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.SearchApi;
import com.kikrlib.api.TokenApi;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.SearchRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anshumaan on 7/12/2016.
 */

public class ProductSearchTagging extends BaseActivity implements MultiAutoCompleteTextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    ProgressBarDialog mProgressBarDialog;
    AutoSuggestProduct autoSuggestProduct;
    MultiAutoCompleteTextView multiAutoCompleteTextView;
    ListView listView;
    TextView txtDone;
    ImageView imgSearch;
    public List<Product> data = new ArrayList<Product>();
    boolean isLoading;
    boolean isFirstTime;
    int pageno = 0;
    public static int PRODUCT_SEARCH_TAG=11001;
    TextView noDataFoundTextView;
    ProductTaggingListAdapter searchAdapter;
    String searchString = "";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        CommonUtility.fullScreenActivity(context);
        setContentView(R.layout.product_search_tagging);
    }

    @Override
    public void initLayout() {

        mProgressBarDialog = new ProgressBarDialog(context);
        autoSuggestProduct = new AutoSuggestProduct(context);
        multiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.searchMultiText);
        listView = (ListView) findViewById(R.id.listView_main);
        txtDone = (TextView) findViewById(R.id.txtDone);
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        noDataFoundTextView = (TextView) findViewById(R.id.noDataFoundTextView);
        multiAutoCompleteTextView.setHint("search product.");

    }

    @Override
    public void setupData() {

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                // Do nothing
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
//				   System.out.println("1234 in onScroll fvi"+firstVisibleItem+", vic"+visibleItemCount+", tic"+totalItemCount);
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
//			    	System.out.println("1234 inside if ");
                    if (checkInternet()) {

                        if (pageno > 0) {
                            isFirstTime = false;

                            search();
                        }
                        pageno++;
                    } else {
                        showFooter();
                    }
                }
            }
        });


    }

    @Override
    public void headerView() {
        setBackHeader();
        getLeftTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtility.hideSoftKeyboard(context);
                finish();
            }
        });
        setGoToHome();
    }

    @Override
    public void setUpTextType() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void setClickListener() {
        multiAutoCompleteTextView.setOnEditorActionListener(this);

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(multiAutoCompleteTextView.getText().toString().trim())) {
                    search();
                } else AlertUtils.showToast(context, "Enter product name.");
            }
        });

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        listView.setOnItemClickListener(this);

    }

    private void search() {
        isLoading = !isLoading;


        if (pageno > 0)
            showFooter();
        else
            mProgressBarDialog.show();

        final SearchApi searchApi = new SearchApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFutter();
                noDataFoundTextView.setVisibility(View.GONE);
                CommonUtility.hideSoftKeyboard(context);

                Syso.info("In handleOnSuccess>>" + object);
                isLoading = !isLoading;
                SearchRes searchRes = (SearchRes) object;
                List<Product> data2 = searchRes.getData();
                if (data2.size() < 10) {
                    isLoading = true;
                }
                if (data2.size() == 0 && isFirstTime) {
                    noDataFoundTextView.setVisibility(View.VISIBLE);

                } else if (data2.size() > 0 && isFirstTime) {

                    noDataFoundTextView.setVisibility(View.GONE);
                    data = data2;
                    searchAdapter = new ProductTaggingListAdapter(ProductSearchTagging.this, data);
                    listView.setAdapter(searchAdapter);
                } else {
                    data.addAll(data2);
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                else
                    hideFutter();
                CommonUtility.hideSoftKeyboard(context);

                Syso.info("In handleOnFailure>>" + object);
                isLoading = !isLoading;
                if (object != null) {
                    SearchRes response = (SearchRes) object;
                    AlertUtils.showToast(context, response.getMessage());
                } else {
                    AlertUtils.showToast(context, R.string.invalid_response);
                }
            }
        });
        searchApi.searchProduct(UserPreference.getInstance().getUserID(), searchString, Integer.toString(pageno));
        searchApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isLoading = !isLoading;
                searchApi.cancel();
            }
        });
    }

    /**
     * Called when an action is being performed.
     *
     * @param v        The view that was clicked.
     * @param actionId Identifier of the action.  This will be either the
     *                 identifier you supplied, or {@link EditorInfo#IME_NULL
     *                 EditorInfo.IME_NULL} if being called due to the enter key
     *                 being pressed.
     * @param event    If triggered by an enter key, this is the event;
     *                 otherwise, this is null.
     * @return Return true if you have consumed the action, else false.
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            if (checkInternet()) {
                searchString = multiAutoCompleteTextView.getText().toString();
                if (searchString.length() == 0) {
                    AlertUtils.showToast(context, "Please enter search text.");
                    return true;
                }

                searchString = searchString.trim().replaceAll(",", " ");

                data.clear();
                isFirstTime = true;

                pageno = 0;
                search();
                return true;
            }
        }
        return false;
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonUtility.hideSoftKeyboard(context);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", data.get(position));

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(PRODUCT_SEARCH_TAG, intent);
        finish();

    }
}
