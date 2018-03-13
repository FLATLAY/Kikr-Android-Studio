package com.flatlay.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.adapter.FollowCategoryNewAdapter;
import com.flatlay.utility.AppConstants.Screen;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.CategoryListApi;
import com.flatlaylib.bean.Category;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CategoryRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class FollowCategoriesNewActivity extends BaseActivity implements OnClickListener, ServiceCallback {
    private ListView listView;
    private FollowCategoryNewAdapter categoryAdapter;
    private LinearLayout backIconLayout;
    //  public static TextView mRightText;
    // private ProgressBarDialog mProgressBarDialog;
    //private TextView mDataNotFoundTextView;
    private int pagenum = 0;
    private boolean isLoading = false, isFirstTime = true, isFromHome = false;
    public static int selectedCount = 0;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        CommonUtility.noTitleActivity(context);
        UserPreference.getInstance().setCurrentScreen(Screen.CategoryScreen);
        setContentView(R.layout.activity_follow_categories_new);
        if (checkInternet())
            getCategoryList();
        else
            showReloadOption();
        updateScreen(Screen.CategoryScreen);
        hideHeader();
        selectedCount = 0;
    }

    @Override
    public void initLayout() {
        listView = (ListView) findViewById(R.id.categoryGridView);
        nextButton = (Button) findViewById(R.id.nextButton);
        backIconLayout = (LinearLayout) findViewById(R.id.backIconLayout);

        //	mDataNotFoundTextView=(TextView) findViewById(R.id.dataNotFoundTextView);
    }

    @Override
    public void setupData() {
        listView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view,
                                             int scrollState) {
                // Do nothing
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                System.out.println("123456 in onScroll fvi" + firstVisibleItem + ", vic" + visibleItemCount + ", tic" + totalItemCount);
                if (!isLoading && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (checkInternet()) {
                        pagenum++;
                        isFirstTime = false;
                        getCategoryList();
                    }
                }
            }
        });

        if (getIntent().hasExtra("isFromHome")) {
            isFromHome = getIntent().getBooleanExtra("isFromHome", false);
            if (isFromHome) {
                backIconLayout.setVisibility(View.VISIBLE);
                nextButton.setText("SAVE");
                nextButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            }
        } else {
            if (selectedCount > 0)
                goToNextScreen();
            else
                AlertUtils.showToast(context, "Follow at least 1 category.");
        }

    }

    @Override
    public void headerView() {

    }


    @Override
    public void setUpTextType() {
        nextButton.setTypeface(FontUtility.setMontserratLight(this));
    }

    @Override
    public void setClickListener() {
//        nextButton.setOnClickListener(this);
        backIconLayout.setOnClickListener(this);
    }

    private void showReloadOption() {
        showDataNotFound();
        TextView textView = getDataNotFound();
        if (textView != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkInternet())
                        getCategoryList();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.nextButton:
//                if (selectedCount > 0)
//                    goToNextScreen();
//                else
//                    AlertUtils.showToast(context, "Follow at least 1 category.");
//                break;

            case R.id.backIconLayout:
                onBackPressed();
                break;
        }

    }

    private void getCategoryList() {
        isLoading = !isLoading;
        //  mProgressBarDialog = new ProgressBarDialog(context);
        if (pagenum > 0)
            showFooter();
        //else
        //   mProgressBarDialog.show();

        final CategoryListApi listApi = new CategoryListApi(this);
        listApi.getCategoryList(Integer.toString(pagenum));
        listApi.execute();

//        mProgressBarDialog.setOnCancelListener(new OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                isLoading = !isLoading;
//                listApi.cancel();
//            }
//        });
    }


    @Override
    public void handleOnSuccess(Object object) {
//        if (mProgressBarDialog.isShowing())
//            mProgressBarDialog.dismiss();
//        else
        hideFutter();
        hideDataNotFound();
        isLoading = !isLoading;
        CategoryRes categoryRes = (CategoryRes) object;
        List<Category> categories = categoryRes.getData();
        if (categories.size() < 10) {
            isLoading = true;
        }
        if (categories.size() == 0 && isFirstTime) {
            //	mDataNotFoundTextView.setVisibility(View.VISIBLE);
        } else if (categories.size() > 0 && isFirstTime) {
            //mDataNotFoundTextView.setVisibility(View.GONE);

            List<Category> reorderedCategories = new ArrayList<Category>();
            List<Integer> imgList = new ArrayList<>();
            int womensImage = R.drawable.womens_fashion;
            int mensImage = R.drawable.mens_fashion;
            int unisexImage = R.drawable.unisex_fashion;
            int accessoryImage = R.drawable.accessories;
            int healthImage = R.drawable.health_and_beauty;
            int sportsImage = R.drawable.sports_and_fitness;
            int kidsImage = R.drawable.kids_fashion;
            int electronicsImage = R.drawable.electronics;
            int entertainmentImage = R.drawable.entertainment;
            int homeImage = R.drawable.home_goods;

            reorderedCategories.add(categories.get(9));
            reorderedCategories.add(categories.get(6));
            reorderedCategories.add(categories.get(8));
            reorderedCategories.add(categories.get(0));
            reorderedCategories.add(categories.get(3));
            reorderedCategories.add(categories.get(7));
            reorderedCategories.add(categories.get(5));
            reorderedCategories.add(categories.get(1));
            reorderedCategories.add(categories.get(2));
            reorderedCategories.add(categories.get(4));

            imgList.add(womensImage);
            imgList.add(mensImage);
            imgList.add(unisexImage);
            imgList.add(accessoryImage);
            imgList.add(healthImage);
            imgList.add(sportsImage);
            imgList.add(kidsImage);
            imgList.add(electronicsImage);
            imgList.add(entertainmentImage);
            imgList.add(homeImage);

            categoryAdapter = new FollowCategoryNewAdapter(context, reorderedCategories, imgList);
            listView.setAdapter(categoryAdapter);

            for (int i = 0; i < categoryAdapter.getCount(); i++) {
                if (categoryAdapter.getItem(i).getIs_followed().equalsIgnoreCase("yes"))
                    selectedCount++;
            }

        } else {
            categoryAdapter.setData(categories);
            categoryAdapter.notifyDataSetChanged();
        }
    }

    private void goToNextScreen() {
        startActivity(PinGuideActivity.class);
//		finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void handleOnFailure(ServiceException exception, Object object) {
//        if (mProgressBarDialog.isShowing())
//            mProgressBarDialog.dismiss();
//        else
        hideFutter();
        isLoading = !isLoading;
        Syso.info("In handleOnFailure>>" + object);
        if (object != null) {
            CategoryRes response = (CategoryRes) object;
            AlertUtils.showToast(context, response.getMessage());
        } else {
            AlertUtils.showToast(context, R.string.invalid_response);
        }
    }

}
