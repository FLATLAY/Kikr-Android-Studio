package com.flatlay.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlaylib.api.BrandListApi;
import com.flatlaylib.bean.BrandList;
import com.flatlaylib.bean.BrandResult;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.BrandListRes;
import com.flatlaylib.service.res.ProductListRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

import java.util.List;

/**
 * Created by RachelDi on 3/20/18.
 */

public class AllBrandGridAdapter extends BaseAdapter {

    private FragmentActivity mContext;
    private List<BrandList> data;
    private LayoutInflater mInflater;


    public AllBrandGridAdapter(FragmentActivity context, List<BrandList> data) {
        super();
        this.mContext = context;
        this.data = data;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<BrandList> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public BrandList getItem(int index) {
        return data.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_brand_grid, null);
            viewholder = new ViewHolder();
            viewholder.brandImage = (ImageView) convertView.findViewById(R.id.brandImage);
            viewholder.follow_text = (TextView) convertView.findViewById(R.id.follow_text);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.follow_text.setTypeface(FontUtility.setMontserratLight(mContext));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (CommonUtility.getDeviceWidth(mContext)) / 8);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.setMargins(0, 15, 0, 0);
        if (data.get(position).getIs_followed().equalsIgnoreCase("yes")) {
            viewholder.follow_text.setText("Following");
        } else {
            viewholder.follow_text.setText("Follow +");
        }
        if (data.size() > position) {
            viewholder.brandImage.setLayoutParams(layoutParams);
            CommonUtility.setImage(mContext, viewholder.brandImage, data.get(position).getImg());
        }
        viewholder.follow_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.get(position).getIs_followed().equalsIgnoreCase("no")) {
                    addBrand(data.get(position).getId(), viewholder.follow_text, data.get(position));
                } else {
                    deleteBrand(data.get(position).getId(), viewholder.follow_text, data.get(position));

                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private ImageView brandImage;
        private TextView follow_text;
    }

    public void addBrand(String brand_id, final View v, final BrandList brand) {
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                Syso.info("In handleOnSuccess>>" + object);
                AlertUtils.showToast(mContext, "Brand followed");

                ((TextView) v).setText("Following");
                brand.setIs_followed("yes");
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                }
            }
        });
        listApi.addBrands(brand_id);
        listApi.execute();
    }

    public void deleteBrand(String brand_id, final View v, final BrandList brand) {
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {

                AlertUtils.showToast(mContext, "Brand unfollowed");

                ((TextView) v).setText("Follow +");
                brand.setIs_followed("no");
                Syso.info("In handleOnSuccess>>" + object);

            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {

                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductListRes response = (ProductListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                }
            }
        });
        listApi.deleteBrand(brand_id);
        listApi.execute();
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

}

