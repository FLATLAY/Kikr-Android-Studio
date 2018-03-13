package com.flatlay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.ProductDetailGridAdapter;
import com.flatlaylib.bean.Product;

import java.util.List;

/**
 * Created by RachelDi on 3/10/18.
 */

public class ViewInsProductFragment extends BaseFragment {
    private GridView products_grid;
    private ProductDetailGridAdapter productDetailGridAdapter;
    private View mainView;
    private List<Product> data;
    private LinearLayout backIconLayout;

    public ViewInsProductFragment(List<Product> data){
        this.data=data;
    }
    @Override
    public void initUI(Bundle savedInstanceState) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.all_ins_products, null);
        return mainView;
    }


    @Override
    public void setData(Bundle bundle) {
//        data = (List<Product>) bundle.getSerializable("data");
        products_grid = (GridView) mainView.findViewById(R.id.products_grid);
        backIconLayout=(LinearLayout) mainView.findViewById(R.id.backIconLayout);
        backIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.onBackPressed();
            }
        });
        productDetailGridAdapter=new ProductDetailGridAdapter(mContext,data,1);
        products_grid.setAdapter(productDetailGridAdapter);
    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
    }
}
