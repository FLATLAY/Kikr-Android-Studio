package com.flatlay.post_upload;


import android.os.Bundle;

import android.view.View;

import android.widget.CompoundButton;

import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;

import com.flatlay.BaseFragment;

import com.flatlay.chip.TagsEditText;



import java.util.Collection;

public class FragmentPostUploadTag extends BaseFragment implements CompoundButton.OnCheckedChangeListener, TagsEditText.TagsEditListener, View.OnClickListener, OnLoginCompleteListener, SocialNetworkManager.OnInitializationCompleteListener {

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onLoginSuccess(int socialNetworkID) {

    }

    @Override
    public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {

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

    @Override
    public void onSocialNetworkManagerInitialized() {

    }

    @Override
    public void onTagsChanged(Collection<String> tags) {

    }

    @Override
    public void onEditingFinished() {

    }
}