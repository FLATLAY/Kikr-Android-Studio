package com.flatlay.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.BaseActivityWithVideo;
import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.activity.EditProfileActivity;
import com.flatlay.activity.FollowCategoriesNewActivity;
import com.flatlay.activity.HomeActivity;
import com.flatlay.dialog.ShareProfileDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyMaterialContentOverflow3;
import com.flatlaylib.api.LogoutApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;

/**
 * Created by RachelDi on 4/27/18.
 */

public class ProfileChoiceFragment extends BaseFragment implements View.OnClickListener {
    private View mainView;
    private ImageView info_image2;
    private MyMaterialContentOverflow3 overflow2;
    private boolean isOnFeedPage = true;
    private TextView text1, text2, text3, text4, text5;
    private ShareProfileDialog shareProfileDialog;

    public ProfileChoiceFragment(MyMaterialContentOverflow3 overflow2, boolean isOnFeedPage) {
        this.overflow2 = overflow2;
        this.isOnFeedPage = isOnFeedPage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.profile_choice_layout, null);
        return mainView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_image2:
                overflow2.setOpen();
                ((HomeActivity) mContext).myAddFragment(new MyFeedCollectionFragment(overflow2, false, isOnFeedPage));
                break;
            case R.id.text1:
                overflow2.setOpen();
                shareProfileDialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
                shareProfileDialog.show();
                break;
            case R.id.text2:
                Bundle bundle = new Bundle();
                bundle.putBoolean("is_edit_profile", true);
                startActivity(EditProfileActivity.class, bundle);
                break;
            case R.id.text3:
                overflow2.setOpen();
                ((HomeActivity) mContext).myAddFragment(new MyFeedCollectionFragment(overflow2, true, true));
                break;
            case R.id.text4:
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("isFromHome", true);
                startActivity(FollowCategoriesNewActivity.class, bundle2);
                break;
            case R.id.text5:
                logoutUser();
                UserPreference.getInstance().setUserID("");
                startActivity(BaseActivityWithVideo.class);
                mContext.finish();
                break;
        }
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        info_image2 = (ImageView) mainView.findViewById(R.id.info_image2);
        text1 = (TextView) mainView.findViewById(R.id.text1);
        text1.setTypeface(FontUtility.setMontserratLight(mContext));
        text2 = (TextView) mainView.findViewById(R.id.text2);
        text2.setTypeface(FontUtility.setMontserratLight(mContext));
        text3 = (TextView) mainView.findViewById(R.id.text3);
        text3.setTypeface(FontUtility.setMontserratLight(mContext));
        text4 = (TextView) mainView.findViewById(R.id.text4);
        text4.setTypeface(FontUtility.setMontserratLight(mContext));
        text5 = (TextView) mainView.findViewById(R.id.text5);
        text5.setTypeface(FontUtility.setMontserratLight(mContext));
        shareProfileDialog = new ShareProfileDialog(mContext);
    }

    @Override
    public void setData(Bundle bundle) {

    }

    @Override
    public void refreshData(Bundle bundle) {

    }

    @Override
    public void setClickListener() {
        info_image2.setOnClickListener(this);
        text1.setOnClickListener(this);
        text2.setOnClickListener(this);
        text3.setOnClickListener(this);
        text4.setOnClickListener(this);
        text5.setOnClickListener(this);

    }

    private void logoutUser() {
        LogoutApi logoutApi = new LogoutApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
            }
        });
        logoutApi.logoutUser(UserPreference.getInstance().getUserID(), CommonUtility.getDeviceTocken(mContext));
        logoutApi.execute();
    }
}
