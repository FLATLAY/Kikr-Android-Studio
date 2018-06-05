package com.flatlay.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.FollowCategoriesNewActivity;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.CategoryListApi;
import com.flatlaylib.bean.Category;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CategoryRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class FollowCategoryNewAdapter extends BaseAdapter {
    final String TAG = "FollowCategoryNewAda";
    public ArrayList<Category> catList = new ArrayList<Category>();
    public List<Integer> imagesList = new ArrayList<>();
    private LayoutInflater inflater;
    private Activity mContext;

    public FollowCategoryNewAdapter(Activity mContext, List<Category> categories, List<Integer> imagesList) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        catList = (ArrayList<Category>) categories;
        this.mContext = mContext;
        this.imagesList = imagesList;
        Log.w(TAG, "FollowCategoryNewAdapter");
    }


    public void setData(List<Category> data) {
        this.catList.addAll(data);
    }

    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Category getItem(int index) {
        return catList.get(index);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adapter_follow_category_new,arg2,false);
            viewHolder = new ViewHolder();
            viewHolder.load = (ProgressBar) convertView.findViewById(R.id.follow_load);
            viewHolder.categoryNameTextView = (TextView) convertView.findViewById(R.id.categoryNameTextView);
            viewHolder.followBtn = (Button) convertView.findViewById(R.id.followBtn);
            viewHolder.productLayout = (LinearLayout) convertView.findViewById(R.id.productLayout);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            viewHolder.categoryNameTextView.setTypeface(FontUtility.setMontserratLight(mContext));
            viewHolder.followBtn.setTypeface(FontUtility.setMontserratLight(mContext));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.categoryNameTextView.setText(getItem(position).getName());

        viewHolder.productLayout.removeAllViews();


        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(metrics.widthPixels, (int) (metrics.heightPixels * 1 / 11));

        imageView.setBackgroundResource(imagesList.get(position));
        viewHolder.productLayout.addView(imageView, parms);

        viewHolder.followBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (getItem(position).getIs_followed().equalsIgnoreCase("no")) {
                    addCategory(getItem(position).getId(), position, viewHolder.load);
                    viewHolder.followBtn.setText(" Following ");
                    viewHolder.load.setVisibility(View.VISIBLE);
                    viewHolder.followBtn.setTextColor(mContext.getResources().getColor(R.color.white));
                    viewHolder.followBtn.setSelected(true);

                } else {
                    deleteCategory(getItem(position).getId(), position, viewHolder.load);
                    viewHolder.load.setVisibility(View.VISIBLE);
                    viewHolder.followBtn.setText(" Follow ");
                    viewHolder.followBtn.setTextColor(mContext.getResources().getColor(R.color.black));
                    viewHolder.followBtn.setSelected(false);
                }

            }
        });

        if (getItem(position).getIs_followed().equalsIgnoreCase("yes")) {
            viewHolder.followBtn.setText(" Following ");
            //viewHolder.load.setVisibility(View.GONE);
            viewHolder.followBtn.setTextColor(mContext.getResources().getColor(R.color.white));
            viewHolder.followBtn.setSelected(true);
        } else {
            viewHolder.load.setVisibility(GONE);
            //viewHolder.followBtn.setText(" Follow ");
            viewHolder.followBtn.setTextColor(mContext.getResources().getColor(R.color.black));
            viewHolder.followBtn.setSelected(false);
        }


        return convertView;
    }

    public void addCategory(String categoryID, final int position, final ProgressBar load) {
        final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                FollowCategoriesNewActivity.selectedCount++;

                getItem(position).setIs_followed("yes");
                Log.e(TAG, "followed successfully");
                load.setVisibility(GONE);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CategoryRes response = (CategoryRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        listApi.addCategory(UserPreference.getInstance().getUserID(), categoryID);
        listApi.execute();
    }

    public void deleteCategory(String categoryID, final int position,final ProgressBar load) {
        final CategoryListApi listApi = new CategoryListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                Log.e(TAG, "unfollowed");
                FollowCategoriesNewActivity.selectedCount--;
                load.setVisibility(GONE);

                getItem(position).setIs_followed("no");
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CategoryRes response = (CategoryRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        listApi.deleteCategory(UserPreference.getInstance().getUserID(), categoryID);
        listApi.execute();
    }

    public class ViewHolder {
        TextView categoryNameTextView;
        Button followBtn;
        ProgressBar load;
        LinearLayout productLayout;
        RelativeLayout relativeLayout;
    }
}