package com.flatlay.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.CustomizeFeedActivity;
import com.flatlay.model.Item;
import com.flatlay.model.SearchResult;
import com.flatlay.model.SectionItem;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.ui.RoundImageView;
import com.flatlay.utility.CommonUtility;
import com.flatlaylib.api.BrandListApi;
import com.flatlaylib.api.FollowUserApi;
import com.flatlaylib.api.InterestSectionApi;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.BrandListRes;
import com.flatlaylib.service.res.FollowUserRes;
import com.flatlaylib.service.res.InterestSectionRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;


public class CustomizeFeedAllAdapter extends ArrayAdapter<Item> {

    private CustomizeFeedActivity mContext;
    private ArrayList<Item> items;
    private LayoutInflater vi;

    private ProgressBarDialog mProgressBarDialog;

    public CustomizeFeedAllAdapter(CustomizeFeedActivity context, ArrayList<Item> items) {
        super(context, 0, items);
        this.mContext = context;
        this.items = items;

        //sortItems();
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.w("Activity","CustomizeFeedAllAdapter");
    }

    private void sortItems() {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item lhs, Item rhs) {
                return ((SearchResult) lhs).getName().compareTo(((SearchResult) rhs).getName());
            }
        });
    }

    public void setData(ArrayList<Item> items) {
        this.items.addAll(items);
        //sortItems();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Item i = items.get(position);
        if (i != null) {
            if (i.isSection()) {
                SectionItem si = (SectionItem) i;
                v = vi.inflate(R.layout.search_all_section, null);

                v.setOnClickListener(null);
                v.setOnLongClickListener(null);
                v.setLongClickable(false);

                final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
                sectionView.setText(si.getTitle());

            } else {
                final SearchResult item = (SearchResult) i;
                v = vi.inflate(R.layout.search_all_item, null);

                RoundImageView user_image = (RoundImageView) v.findViewById(R.id.user_image);
                TextView user_name = (TextView) v.findViewById(R.id.user_name);
                TextView follow_btn = (TextView) v.findViewById(R.id.follow_btn);
                ProgressBar progressBar_follow_brand = (ProgressBar) v.findViewById(R.id.progressBar_follow_brand);
                if (!TextUtils.isEmpty(item.getName())) {
                    user_name.setText(item.getName());
                    user_name.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    user_name.setText("Unknown");
                }
                if (item.getIs_followed().equalsIgnoreCase("yes")) {
                    follow_btn.setText("FOLLOWED");
                    follow_btn.setBackgroundResource((R.drawable.btn_whitebg));
                    int imgResource = R.drawable.ic_check_following;
                    follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
                    follow_btn.setTextColor(mContext.getResources().getColor(R.color.menu_option_background_selected));
                } else {
                    follow_btn.setText("FOLLOW   ");
                    follow_btn.setBackgroundResource(R.drawable.btn_borderbg);
                    int imgResource = R.drawable.ic_add_follow;
                    follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
                    follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
                }
                if (item.getId().equals(UserPreference.getInstance().getUserID())) {
                    follow_btn.setVisibility(View.GONE);
                } else {
                    follow_btn.setVisibility(View.VISIBLE);
                }
                CommonUtility.setImage(mContext, item.getImg(), user_image, R.drawable.profile_icon);
                v.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        if (((CustomizeFeedActivity) mContext).checkInternet())
//                            if (item.getSection_name().equalsIgnoreCase("peoples"))
//                                addFragment(new FragmentProfileView(item.getId(), item.getIs_followed()));
//                            else if (item.getSection_name().equalsIgnoreCase("stores")) {
//                                addFragment(new FragmentProductBasedOnType("store", item.getName(), item.getId()));
//                            } else if (item.getSection_name().equalsIgnoreCase("brands")) {
//                                addFragment(new FragmentProductBasedOnType("brand", item.getName(), item.getId()));
//                            }
                    }
                });

                follow_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(item.getIs_followed()) && ((CustomizeFeedActivity) mContext).checkInternet()) {
                            mProgressBarDialog = new ProgressBarDialog(mContext);
                            mProgressBarDialog.show();
                            if (item.getSection_name().equalsIgnoreCase("peoples")) {
                                if (item.getIs_followed().equalsIgnoreCase("yes")) {
                                    item.setIs_followed("no");
                                    unFollowUser(item.getId(), v.getRootView());
                                    notifyDataSetChanged();
                                } else {
                                    item.setIs_followed("yes");
                                    followUser(item.getId(), v.getRootView());
                                    notifyDataSetChanged();
                                }
                            } else if (item.getSection_name().equalsIgnoreCase("stores")) {
                                if (item.getIs_followed().equalsIgnoreCase("yes")) {
                                    item.setIs_followed("no");
                                    unFollowStore(item.getId(), v.getRootView());
                                    notifyDataSetChanged();
                                } else {
                                    item.setIs_followed("yes");
                                    followStore(item.getId(), v.getRootView());
                                    notifyDataSetChanged();
                                }
                            } else if (item.getSection_name().equalsIgnoreCase("brands"))
                                if (item.getIs_followed().equalsIgnoreCase("yes")) {
                                    item.setIs_followed("no");
                                    deleteBrand(item.getId(), v.getRootView());
                                    notifyDataSetChanged();
                                } else {
                                    item.setIs_followed("yes");
                                    addBrand(item.getId(), v.getRootView());
                                    notifyDataSetChanged();
                                }
                        }
                    }
                });
            }
        }
        return v;
    }

    public class ViewHolder {
        RoundImageView user_image;
        TextView user_name, follow_btn;

        ProgressBar progressBar_follow_brand;
    }

    public class SectionViewHolder {

        TextView sectionName;


    }

    private void addFragment(Fragment fragment) {
        //((HomeActivity) mContext).addFragment(fragment);
    }

    public void followUser(String id, final View v) {
//		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);

        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnSuccess>>" + object);
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        followUserApi.followUser(UserPreference.getInstance().getUserID(), id);
        followUserApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                followUserApi.cancel();
            }
        });
    }

    public void unFollowUser(String id, final View v) {
//		v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final FollowUserApi followUserApi = new FollowUserApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                // AlertUtils.showToast(mContext, object.toString());
                Syso.info("In handleOnSuccess>>" + object);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
//				v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//				v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    FollowUserRes response = (FollowUserRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        followUserApi.unFollowUser(UserPreference.getInstance().getUserID(), id);
        followUserApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                followUserApi.cancel();
            }
        });
    }

    public void followStore(String id, final View v) {
//        v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
//                if (v.findViewById(R.id.checkImageView) != null) {
//                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//                }
                //AlertUtils.showToast(mContext, object.toString());
                Syso.info("In handleOnSuccess>>" + object);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//                if (v.findViewById(R.id.checkImageView) != null) {
//                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
//                }
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.followStore(UserPreference.getInstance().getUserID(), id);
        interestSectionApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                interestSectionApi.cancel();
            }
        });
    }

    public void unFollowStore(String id, final View v) {
//        v.findViewById(R.id.checkImageView).setVisibility(View.GONE);
//        v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final InterestSectionApi interestSectionApi = new InterestSectionApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
//                if (v.findViewById(R.id.checkImageView) != null) {
//                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//                }
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                // AlertUtils.showToast(mContext, object.toString());
                Syso.info("In handleOnSuccess>>" + object);
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//                if (v.findViewById(R.id.checkImageView) != null) {
//                    v.findViewById(R.id.checkImageView).setVisibility(View.VISIBLE);
//                    v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//                }
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    InterestSectionRes response = (InterestSectionRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        interestSectionApi.unFollowStore(UserPreference.getInstance().getUserID(), id);
        interestSectionApi.execute();

        mProgressBarDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                interestSectionApi.cancel();
            }
        });
    }

    public void deleteBrand(String brand_id, final View v) {
//		v.findViewById(R.id.followButton).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnSuccess>>" + object);
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
//				}
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

    public void addBrand(String brand_id, final View v) {
//		v.findViewById(R.id.followButton).setVisibility(View.GONE);
//		v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.VISIBLE);
        final BrandListApi listApi = new BrandListApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
                Syso.info("In handleOnSuccess>>" + object);
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
            }

            @Override
            public void handleOnFailure(ServiceException exception, Object object) {
                if (mProgressBarDialog.isShowing())
                    mProgressBarDialog.dismiss();
//				if (v.findViewById(R.id.followButton)!=null) {
//					v.findViewById(R.id.followButton).setVisibility(View.VISIBLE);
//					v.findViewById(R.id.progressBar_follow_brand).setVisibility(View.GONE);
//				}
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

}

