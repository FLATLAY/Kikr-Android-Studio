package com.flatlay.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.utility.MyMaterialContentOverflow3;

/**
 * Created by RachelDi on 4/30/18.
 */

public class HistoryOrdersFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private MyMaterialContentOverflow3 overflow2;

    public HistoryOrdersFragment(MyMaterialContentOverflow3 overflow2) {
        this.overflow2 = overflow2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.history_orders_layout, null);
        return mainView;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void initUI(Bundle savedInstanceState) {

        FragmentTransaction transaction = mContext.getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.frame_container2, new FragmentAllOrders(new FragmentAllOrders.OrderListener() {
            @Override
            public void onClickButton() {
                overflow2.triggerClose();
            }
        }), null);
        transaction.addToBackStack(null);
        transaction.commit();
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
