package com.kikr.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.SearchProductAdapter;
import com.kikr.bubble.ChipBubbleOnTouchListner;
import com.kikr.bubble.ChipBubbleText;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.GetProductsByCategoryApi;
import com.kikrlib.bean.Categories;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.GetProductsByCategoryRes;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class FragmentSearchProduct extends BaseFragment implements MultiAutoCompleteTextView.OnEditorActionListener {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    SearchProductAdapter mAdapter;
    public static String lastUserId;
    FragmentSearchProduct fragmentSearchProduct;
    String user_id;
    ArrayList<String> categoriesName = new ArrayList<>();
    View mainView;
    private List<Categories> categories = new ArrayList<Categories>();
    private ProgressBarDialog mProgressBarDialog;
    private String category = null;
    List<Categories> dummy;
    MultiAutoCompleteTextView searchTextItems;
    ImageButton searchButton;
    static int position = -1;
    boolean isFragmentContainer=true;
    int[] categoryImages = {R.drawable.ic_category_clothings, R.drawable.ic_category_shoes, R.drawable.ic_category_jewelry,
            R.drawable.ic_category_sports, R.drawable.ic_category_personalcare,
            R.drawable.ic_category_babyproducts, R.drawable.ic_category_electronics, R.drawable.toys_icon,
            R.drawable.ic_category_computers, R.drawable.ic_category_pets, R.drawable.ic_category_musicalinstruments, R.drawable.ic_category_videogames};

    public FragmentSearchProduct() {
      this.isFragmentContainer=true;
    }

    public FragmentSearchProduct(boolean b) {
        this.isFragmentContainer = b;
        try {

            if (lastUserId != null) {
                this.user_id = lastUserId;
                fragmentSearchProduct = this;
            } else {
                ((HomeActivity) getActivity()).onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_product_search, null);
        fragmentSearchProduct = this;
        return mainView;
    }


    @Override
    public void initUI(Bundle savedInstanceState) {
        searchTextItems = (MultiAutoCompleteTextView) mainView.findViewById(R.id.searchMultiText);
        mRecyclerView = (RecyclerView) mainView.findViewById(R.id.rvCategriesList);
        mRecyclerView.setHasFixedSize(true);
        //searchButton = (ImageButton) mainView.findViewById(R.id.button1);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchProductAdapter(getActivity(), categoriesName);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(mRecyclerView.getWindowToken(), 0);
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        mAdapter.SetOnItemClickListener(new SearchProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    getCategories(position);
//                    Categories cat = dummy.get(position);
//                    cat.validate();
//                    String str = cat.getDisplayName();
//

                    //  addFragment(new FragmentSearchSubCategories(str, cat, categoryImages[position],true));
                } catch (Exception ex) {
                    Log.e("", ex.getMessage());
                }
            }
        });

        searchTextItems.setOnEditorActionListener(this);
        if (!isFragmentContainer)
            initData();

    }

    public void initData() {
        getCategories(position);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void getCategories(final int position) {
        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        categoriesName.clear();
        GetProductsByCategoryApi checkPointsStatusApi = new GetProductsByCategoryApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                GetProductsByCategoryRes getProductsByCategoryRes = (GetProductsByCategoryRes) object;
                categories = getProductsByCategoryRes.getData();
                List<Categories> temp = new ArrayList<Categories>();
                dummy = new ArrayList<Categories>();
                for (Categories cat : categories) {
                    if (cat.getCat1().trim().equalsIgnoreCase("baby products") ||
                            cat.getCat1().trim().equalsIgnoreCase("clothing & accessories") ||
                            cat.getCat1().trim().equalsIgnoreCase("computers & accessories") ||
                            cat.getCat1().trim().equalsIgnoreCase("electronics") ||
                            cat.getCat1().trim().equalsIgnoreCase("health & personal care") ||
                            cat.getCat1().trim().equalsIgnoreCase("jewelry") ||
                            cat.getCat1().trim().equalsIgnoreCase("musical instruments") ||
                            cat.getCat1().trim().equalsIgnoreCase("pet supplies") ||
                            cat.getCat1().trim().equalsIgnoreCase("shoes") ||
                            cat.getCat1().trim().equalsIgnoreCase("sports & outdoors") ||
                            cat.getCat1().trim().equalsIgnoreCase("video games") ||
                            cat.getCat1().trim().equalsIgnoreCase("toys & games")) {
                        temp.add(cat);
                    }
                }
                dummy.add(0, temp.get(1));
                dummy.add(1, temp.get(8));
                dummy.add(2, temp.get(5));
                dummy.add(3, temp.get(9));
                dummy.add(4, temp.get(4));
                dummy.add(5, temp.get(0));
                dummy.add(6, temp.get(3));
                dummy.add(7, temp.get(10));
                dummy.add(8, temp.get(2));
                dummy.add(9, temp.get(7));
                dummy.add(10, temp.get(6));
                dummy.add(11, temp.get(11));

                Collections.sort(dummy, new Comparator<Categories>() {
                    @Override
                    public int compare(Categories lhs, Categories rhs) {
                        return lhs.getCat1().compareTo(rhs.getCat1());
                    }
                });
                for (int i = 0; i < dummy.size(); i++) {
                    categoriesName.add(dummy.get(i).getCat1());
                }

                mAdapter.notifyDataSetChanged();
                mProgressBarDialog.dismiss();

                try {
                    if (position != -1) {
                        Categories cat = dummy.get(position);
                        cat.validate();
                        String str = cat.getDisplayName();


                        addFragment(new FragmentSearchSubCategories(str, cat, categoryImages[position], true));
                    }
                } catch (Exception ex) {
                    Log.e("", ex.getMessage());
                }

            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

            }
        });
        checkPointsStatusApi.getCategory(UserPreference.getInstance().getUserID(), category);
        checkPointsStatusApi.execute();
    }


    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    public static int clickCount = 1;

    @Override
    public void setClickListener() {

        searchTextItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(new FragmentSearchAll("..."));
            }
        });


//     searchTextItems.setOnTouchListener(new View.OnTouchListener() {
//         @Override
//         public boolean onTouch(View v, MotionEvent event) {
//
//
//                 addFragment(new FragmentSearchAll("..."));
//
//             return false;
//         }
//
//     });
//        String[] values = {"Ali", "Zak", "Anshumaan", "anshu", "Morteza", "sanchit", "brunette"};
//
//
//        ChipBubbleText cp = new ChipBubbleText(mContext, searchTextItems, values, 1);
//        cp.setChipColor("#07948c");
//        cp.setChipTextColor(R.color.app_text_color);
//        cp.setChipTextSize(18);
//
//        cp.initialize();
//


    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            CommonUtility.hideSoftKeyboard(mContext);
            if (checkInternet()) {
                String str = searchTextItems.getText().toString();
                addFragment(new FragmentSearchAll(str));
                return true;
            }

        }
        return false;
    }


}
