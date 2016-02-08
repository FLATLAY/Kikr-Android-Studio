package com.kikr.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kikr.BaseFragment;
import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.adapter.SearchProductAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikrlib.api.GetProductsByCategoryApi;
import com.kikrlib.bean.Categories;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.GetProductsByCategoryRes;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;
import java.util.List;


public class FragmentSearchProduct extends BaseFragment {

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
    int[] categoryImages = {R.drawable.ic_category_clothings, R.drawable.ic_category_shoes, R.drawable.ic_category_jewelry,
            R.drawable.ic_category_sports, R.drawable.ic_category_personalcare,
            R.drawable.ic_category_babyproducts, R.drawable.ic_category_electronics, R.drawable.ic_category_videogames,
            R.drawable.ic_category_computers, R.drawable.ic_category_pets, R.drawable.ic_category_musicalinstruments};
    public FragmentSearchProduct() {
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
        mRecyclerView = (RecyclerView) mainView.findViewById(R.id.rvCategriesList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new SearchProductAdapter(getActivity(), categoriesName);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new SearchProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    Categories cat = dummy.get(position);
                    cat.validate();
                    String str = cat.getDisplayName();


                    addFragment(new FragmentSearchSubCategories(str, cat,categoryImages[position]));
                } catch (Exception ex) {
                    Log.e("", ex.getMessage());
                }
            }
        });

        mProgressBarDialog = new ProgressBarDialog(mContext);
        mProgressBarDialog.show();
        getCategories();

    }

    private void getCategories() {
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
                            cat.getCat1().trim().equalsIgnoreCase("video games")) {
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
                for (int i = 0; i < dummy.size(); i++) {
                    categoriesName.add(dummy.get(i).getCat1());
                }
                mAdapter.notifyDataSetChanged();
                mProgressBarDialog.dismiss();

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

    @Override
    public void setClickListener() {

    }


}
