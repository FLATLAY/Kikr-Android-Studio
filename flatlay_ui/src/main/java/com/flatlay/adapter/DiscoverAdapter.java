package com.flatlay.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.fragment.FragmentDiscover;
import com.flatlay.fragment.FragmentProductBasedOnType;
import com.flatlay.fragment.FragmentProfileView;
import com.flatlay.ui.ProductListUI;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.TagView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.BrandListApi;
import com.flatlaylib.api.ProductBasedOnBrandApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.bean.ProductFeedItem;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.BrandListRes;
import com.flatlaylib.service.res.ProductBasedOnBrandRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

public class DiscoverAdapter extends BaseAdapter {
    boolean isProfile = false;
    private FragmentActivity mContext;
    private FragmentDiscover mFragmentDiscover;
    private LayoutInflater mInflater;
    private List<ProductFeedItem> brandsArray;
    private ProgressBarDialog mProgressBarDialog;
    private boolean isLoading = false;
    List<Product> data;

    public DiscoverAdapter(FragmentActivity context, List<ProductFeedItem> brandsArray, FragmentDiscover fragmentDiscover) {
        super();
        this.mContext = context;
        this.mFragmentDiscover = fragmentDiscover;
        this.brandsArray = brandsArray;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void removeItem(int position) {
        brandsArray.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return brandsArray.size();
    }

    @Override
    public ProductFeedItem getItem(int index) {
        return brandsArray.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.fragment_collections_item, null);
            viewholder = new ViewHolder();
            viewholder.user_name = (TextView) convertView.findViewById(R.id.user_name);

//			viewholder.progressBar_follow_brand = (ProgressBar) convertView.findViewById(R.id.progressBar_follow_brand);
//			viewholder.follow_btn_layout = (LinearLayout) convertView.findViewById(R.id.follow_btn_layout);
            viewholder.brand_image = (ImageView) convertView.findViewById(R.id.brand_image);
            viewholder.tag_layout = (LinearLayout) convertView.findViewById(R.id.tag_layout);
            viewholder.user_profile_image = (ImageView) convertView.findViewById(R.id.user_profile_image);
            //viewholder.follow_btn = (TextView) convertView.findViewById(R.id.follow_btn);
            viewholder.product_layout = (HorizontalScrollView) convertView.findViewById(R.id.product_layout);
            viewholder.product_inflater_layout = (LinearLayout) convertView.findViewById(R.id.product_inflater_layout);
            viewholder.activity_product_list_category_image = (TextView) convertView.findViewById(R.id.activity_product_list_category_image);
            viewholder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            viewholder.noProductTextView = (TextView) convertView.findViewById(R.id.noProductTextView);
            viewholder.imgDelete = (ImageView) convertView.findViewById(R.id.imgDelete);
            //viewholder.layout = (LinearLayout) convertView.findViewById(R.id.linearBlankWhite);
            //viewholder.productView = (View) convertView.findViewById(R.id.productView);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.user_profile_image.setVisibility(View.GONE);
        isProfile = false;
        viewholder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

        if (getItem(position).getType().equals("people")) {
            viewholder.user_profile_image.setVisibility(View.VISIBLE);
            isProfile = true;
            if (getItem(position).getItem_image().equals("")) {
                viewholder.brand_image.setImageResource(R.drawable.profile_bg);

            } else
                CommonUtility.setImage(mContext, getItem(position).getItem_image(), viewholder.brand_image, R.drawable.profile_bg);

            CommonUtility.setImage(mContext, getItem(position).getProfile_pic(), viewholder.user_profile_image, R.drawable.dum_user);
        } else {
            CommonUtility.setImage(mContext, getItem(position).getItem_image(), viewholder.brand_image, R.drawable.profile_bg);
        }
        viewholder.user_profile_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addFragment(new FragmentProfileView(getItem(position).getItem_id(), "no"));
            }
        });
        String name = getItem(position).getItem_name();
        if (name != null && !name.equals(""))
            viewholder.user_name.setText(name);
        else
            viewholder.user_name.setText("Unknown");


        try {
            String tagArr[] = getItem(position).getProducts().get(position).getPrimarycategory().split(">");
            viewholder.tag_layout.removeAllViews();
            for (int i = 0; i < tagArr.length; i++) {
                if(i<=1) {
                    TagView tag = new TagView(mContext);
                    tag.setTextColor(mContext.getResources().getColor(R.color.white));
                    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams2.setMargins(3, 0, 0, 0);
                    tag.setLayoutParams(layoutParams2);
                    tag.setTextSize(12f);
                    tag.setPadding(3, 3, 3, 3);
                    tag.setBackgroundResource(R.drawable.rect_button_gray);
                    tag.setText(tagArr[i]);
                    viewholder.tag_layout.addView(tag);
                }
               if(i==2)
               {
                   TagView tag = new TagView(mContext);
                   tag.setTextColor(mContext.getResources().getColor(R.color.white));
                   LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                   layoutParams2.setMargins(3, 0, 0, 0);
                   tag.setLayoutParams(layoutParams2);
                   tag.setTextSize(12f);
                   tag.setPadding(3, 3, 3, 3);
                   tag.setBackgroundResource(R.drawable.rect_button_gray);
                   tag.setText("...");
                   viewholder.tag_layout.addView(tag);
                   break;
               }

            }

        } catch (Exception ex) {

        }
        data = getItem(position).getProducts();

        if (data.size() < 5) {
            brandsArray.get(position).setLoadMore(false);
        }
        viewholder.product_inflater_layout.removeAllViews();
        if (data.size() > 0) {
            viewholder.noProductTextView.setVisibility(View.GONE);
//			viewholder.activity_product_list_category_image.setVisibility(View.VISIBLE);
            viewholder.product_inflater_layout.addView(new ProductListUI(mContext, getItem(position), mFragmentDiscover, convertView, isProfile).getView());
//			viewholder.activity_product_list_category_image.setText(data.get(0).getPrimarycategory());
//			viewholder.layout.setVisibility(View.GONE);

            viewholder.product_layout.post(new Runnable() {
                @Override
                public void run() {
                    viewholder.product_layout.scrollTo(150, 0);
                }
            });

        } else {
            viewholder.noProductTextView.setVisibility(View.VISIBLE);
            if (!getItem(position).getType().equals("people")) {
                viewholder.noProductTextView.setText("Coming Soon");
                //	convertView.setEnabled(false);
//			viewholder.layout.setVisibility(View.VISIBLE);
            }

//			viewholder.activity_product_list_category_image.setVisibility(View.GONE);
        }
        viewholder.product_layout.scrollTo(0, 0);
//		if (data.size() == 0) {
//		//	viewholder.productView.setVisibility(View.VISIBLE);
//			viewholder.layout.setVisibility(View.VISIBLE);
//		} else {
//		//	viewholder.productView.setVisibility(View.GONE);
//			viewholder.layout.setVisibility(View.GONE);
//		}
        ;
        if (brandsArray.get(position).isLoading()) {
            viewholder.progressBar.setVisibility(View.VISIBLE);
        } else {
            viewholder.progressBar.setVisibility(View.GONE);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewholder.noProductTextView.getVisibility() == View.GONE) {
                    addFragment(new FragmentProductBasedOnType(getItem(position).getType(), getItem(position).getItem_name(), getItem(position).getItem_id()));
                }
            }
        });
        return convertView;
    }


    private void followBrand(String brand_id, final int position, final View v) {
//		mProgressBarDialog = new ProgressBarDialog(mContext);
//		mProgressBarDialog.show();
        v.findViewById(R.id.follow_btn).setVisibility(View.GONE);
        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);

        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
//				mProgressBarDialog.dismiss();
                Syso.info("In handleOnSuccess>>" + object);
//					brandsArray.get(position).setIs_followed("yes");
                v.findViewById(R.id.follow_btn).setVisibility(View.VISIBLE);
                v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                notifyDataSetChanged();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				mProgressBarDialog.dismiss();
                v.findViewById(R.id.follow_btn).setVisibility(View.VISIBLE);
                v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    BrandListRes response = (BrandListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        listApi.addBrands(brand_id);
        listApi.execute();
    }

    public void unfollowBrand(String brand_id, final int position, final View v) {
        v.findViewById(R.id.follow_btn).setVisibility(View.GONE);
        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);

        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                v.findViewById(R.id.follow_btn).setVisibility(View.VISIBLE);
                v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                Syso.info("In handleOnSuccess>>" + object);
                notifyDataSetChanged();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                v.findViewById(R.id.follow_btn).setVisibility(View.VISIBLE);
                v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    BrandListRes response = (BrandListRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        listApi.deleteBrand(brand_id);
        listApi.execute();
    }

    public class ViewHolder {
        TextView user_name;
        TagView tag_name;
        ImageView brand_image, user_profile_image, imgDelete;
        //ProgressBar progressBar_follow_brand;
        //TextView follow_btn;
        HorizontalScrollView product_layout;
        LinearLayout product_inflater_layout;
        TextView activity_product_list_category_image, noProductTextView;
        ProgressBar progressBar;
        LinearLayout tag_layout;
        //LinearLayout layout;
        //View productView;
    }

    private void getProductList(int pageno, ProductFeedItem productFeedItem, final int position) {
        isLoading = !isLoading;
        String item_type = productFeedItem.getType();
        final ProductBasedOnBrandApi productBasedOnBrandApi = new ProductBasedOnBrandApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                isLoading = !isLoading;
                brandsArray.get(position).setLoading(false);
                Syso.info("In handleOnSuccess>>" + object);
                ProductBasedOnBrandRes productBasedOnBrandRes = (ProductBasedOnBrandRes) object;
                List<Product> productLists = productBasedOnBrandRes.getData();
                if (productLists.size() < 10) {
                    brandsArray.get(position).setLoadMore(false);
                }
                if (productLists.size() > 0) {
                    if (brandsArray.get(position).getPagenum() == 0) {
                        for (int i = 0; i < 5; i++) {
                            if (productLists.size() > 0)
                                productLists.remove(0);
                        }
                        if (productLists.size() > 0)
                            brandsArray.get(position).getProducts().addAll(productLists);
                    } else {
                        brandsArray.get(position).getProducts().addAll(productLists);
                    }
                    brandsArray.get(position).setPagenum(brandsArray.get(position).getPagenum() + 1);
//						LogUtils.info("1234 data size and position "+currentPosition+">>"+brandsArray.get(currentPosition).getProducts().size());
                }
                notifyDataSetChanged();
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                isLoading = !isLoading;
                brandsArray.get(position).setLoading(false);
                notifyDataSetChanged();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    ProductBasedOnBrandRes response = (ProductBasedOnBrandRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });

        if (item_type.equalsIgnoreCase("brand")) {
            productBasedOnBrandApi.getProductsBasedOnBrandList(UserPreference.getInstance().getUserID(), Integer.toString(pageno), productFeedItem.getItem_name());
        } else if (item_type.equalsIgnoreCase("store")) {
            productBasedOnBrandApi.getProductsBasedOnStore(UserPreference.getInstance().getUserID(), Integer.toString(pageno), productFeedItem.getItem_name(), productFeedItem.getItem_id());
        } else if (item_type.equalsIgnoreCase("people")) {
            productBasedOnBrandApi.getProductsBasedOnUser(productFeedItem.getItem_id(), Integer.toString(pageno), UserPreference.getInstance().getUserID());
        }

        productBasedOnBrandApi.execute();
    }

    private void addFragment(Fragment fragment) {
        ((HomeActivity) mContext).addFragment(fragment);
    }

}
