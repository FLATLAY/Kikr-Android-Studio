package com.kikr.feed_adapter_copy;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentProductBasedOnType;
import com.kikr.fragment.FragmentProfileView;
import com.kikr.model.Item;
import com.kikr.model.SearchResult;
import com.kikr.model.SectionItem;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.ui.RoundImageView;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.BrandListApi;
import com.kikrlib.api.FollowUserApi;
import com.kikrlib.api.InterestSectionApi;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.BrandListRes;
import com.kikrlib.service.res.FollowUserRes;
import com.kikrlib.service.res.InterestSectionRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

import java.util.ArrayList;


public class CustomizeFeedAllAdapter extends ArrayAdapter<Item> {

    private FragmentActivity mContext;
    private ArrayList<Item> items;
    private LayoutInflater vi;

    private ProgressBarDialog mProgressBarDialog;

    public CustomizeFeedAllAdapter(FragmentActivity context, ArrayList<Item> items) {
        super(context, 0, items);
        this.mContext = context;
        this.items = items;


        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(ArrayList<Item> items) {
        this.items.addAll(items);
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
                sectionView.setVisibility(View.GONE);
                v.setVisibility(View.GONE);

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
                if(item.getIs_followed().equalsIgnoreCase("yes")){
                    follow_btn.setText("FOLLOWING");
                    follow_btn.setBackgroundResource((R.drawable.followgreen));
//                    int imgResource = R.drawable.ic_check_following;
//                    follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
                    follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
                }else{
                    follow_btn.setText("FOLLOW   ");
                    follow_btn.setBackgroundResource(R.drawable.btn_borderbg);
//                    int imgResource = R.drawable.ic_add_follow;
//                    follow_btn.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
                    follow_btn.setTextColor(mContext.getResources().getColor(R.color.white));
                }
                if (item.getId().equals(UserPreference.getInstance().getUserID())) {
                    follow_btn.setVisibility(View.GONE);
                } else {
                    follow_btn.setVisibility(View.VISIBLE);
                }
                CommonUtility.setImage(mContext, item.getImg(), user_image, R.drawable.ic_placeholder_brand);
                v.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (((HomeActivity) mContext).checkInternet())
                            if (item.getSection_name().equalsIgnoreCase("peoples"))
                                addFragment(new FragmentProfileView(item.getId(), item.getIs_followed()));
                            else if (item.getSection_name().equalsIgnoreCase("stores")) {
                                addFragment(new FragmentProductBasedOnType("store", item.getName(), item.getId()));
                            } else if (item.getSection_name().equalsIgnoreCase("brands")) {
                                addFragment(new FragmentProductBasedOnType("brand", item.getName(), item.getId()));
                            }
                    }
                });

                follow_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(item.getIs_followed()) && ((HomeActivity) mContext).checkInternet()) {
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
        ((HomeActivity) mContext).addFragment(fragment);
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

