package com.flatlay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.AllInsProductAdapter;
import com.flatlay.adapter.ProductDetailGridAdapter;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.bean.Product;

import java.util.List;

/**
 * Created by RachelDi on 3/10/18.
 */

public class ViewInsProductFragment extends BaseFragment {
    private GridView products_grid;
    private AllInsProductAdapter allInsProductAdapter;
    private View mainView;
    private List<Product> data;
    private LinearLayout backIconLayout;
    private TextView nameText, likeCount;
    private ImageView heartIcon;
    private int totalLikeCount = 0;
    private String collectionName;
    private int index = 0;

    public ViewInsProductFragment(List<Product> data, String collectionName) {
        this.data = data;
        this.collectionName = collectionName;
        index = 0;
    }

    public ViewInsProductFragment(List<Product> data) {
        this.data = data;
        index = 1;
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
        backIconLayout = (LinearLayout) mainView.findViewById(R.id.backIconLayout);
        backIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.onBackPressed();
            }
        });
        allInsProductAdapter = new AllInsProductAdapter(mContext, data);
        likeCount = (TextView) mainView.findViewById(R.id.likeCount);
        likeCount.setTypeface(FontUtility.setMontserratLight(mContext));
        nameText = (TextView) mainView.findViewById(R.id.nameText);
        nameText.setTypeface(FontUtility.setMontserratLight(mContext));
        heartIcon = (ImageView) mainView.findViewById(R.id.heartIcon);
        products_grid.setAdapter(allInsProductAdapter);
        if (index == 0) {
            likeCount.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.VISIBLE);
            heartIcon.setVisibility(View.VISIBLE);
            for (int i = 0; i < data.size(); i++) {
                totalLikeCount += Integer.parseInt(data.get(i).getLike_info().getLike_count());
            }
            likeCount.setText("" + totalLikeCount);
            nameText.setText("" + collectionName);
        }
        if (index == 1) {
            likeCount.setVisibility(View.GONE);
            nameText.setVisibility(View.GONE);
            heartIcon.setVisibility(View.GONE);
        }

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
    }
}
