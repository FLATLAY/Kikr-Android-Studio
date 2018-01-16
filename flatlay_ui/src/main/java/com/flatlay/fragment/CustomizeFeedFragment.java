package com.flatlay.fragment;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import android.widget.AdapterView;

import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.flatlay.BaseFragment;

import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;



public class CustomizeFeedFragment extends BaseFragment implements View.OnClickListener, ServiceCallback, AdapterView.OnItemClickListener, MultiAutoCompleteTextView.OnEditorActionListener {

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }

    @Override
    public void handleOnSuccess(Object object) {

    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {

    }

    @Override
    public void initUI(Bundle savedInstanceState) {

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

