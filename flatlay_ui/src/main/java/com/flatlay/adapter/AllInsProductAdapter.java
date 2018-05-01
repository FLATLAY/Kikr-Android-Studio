package com.flatlay.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.activity.ProductDetailWebViewActivity;
import com.flatlay.dialog.CollectionListDialog;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.dialog.ShareDialog;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyBubbleActions;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by RachelDi on 3/20/18.
 */

public class AllInsProductAdapter extends BaseAdapter {

    private FragmentActivity mContext;
    private LayoutInflater mInflater;
    private List<Product> products;
    private String postlink;
    private static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/product/";

    public AllInsProductAdapter(FragmentActivity context, List<Product> products) {
        super();
        this.mContext = context;
        this.products = products;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Product> data) {
        this.products = data;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int index) {
        return products.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewholder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.all_product_adapter, null);
            viewholder = new ViewHolder();
            viewholder.productImage = (ImageView) convertView.findViewById(R.id.inspirationImage);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        final Product currentProduct = getItem(position);
        CommonUtility.setImage(mContext, viewholder.productImage, getItem(position).getProductimageurl());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((HomeActivity)mContext).checkInternet()) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", currentProduct);
                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
                    detail.setArguments(bundle);
                    ((HomeActivity) mContext).addFragment(detail);
                }
            }
        });

        final Product product=products.get(position);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(final View v) {
                MyBubbleActions.on(v)
                        .addAction("Like", R.drawable.small_gray_heart, new MyBubbleActions.Callback() {
                            @Override
                            public void doAction() {
                                if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                                    CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                                    createAccountDialog.show();
                                } else {
                                    likeInspiration(product, new UiUpdate() {
                                        @Override
                                        public void updateUi() {
                                        }
                                    });
                                }
                            }
                        })
                        .addAction("Add to Collection", R.drawable.small_gray_plus, new MyBubbleActions.Callback() {
                            @Override
                            public void doAction() {
                                if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                                    CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                                    createAccountDialog.show();
                                } else {
                                    CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, product);
                                    collectionListDialog.getWindow().setBackgroundDrawableResource(R.color.real_transparent);
                                    collectionListDialog.show();
                                }
                            }
                        })
                        .addAction("Add to Cart", R.drawable.small_gray_cart, new MyBubbleActions.Callback() {
                            @Override
                            public void doAction() {
                                addProductToCart(product);

                            }
                        })
                        .addAction("View On Store Site", R.drawable.small_gray_store, new MyBubbleActions.Callback() {
                            @Override
                            public void doAction() {
                                Intent intent = new Intent(mContext, ProductDetailWebViewActivity.class);
                                intent.putExtra("data", new Gson().toJson(product));
                                mContext.startActivity(intent);
                            }
                        })
                        .addAction("Share", R.drawable.small_gray_kopie, new MyBubbleActions.Callback() {
                            @Override
                            public void doAction() {
                                postlink = SHARE_POST_LINK + product.getId();
                                ShareDialog dialog = new ShareDialog(mContext,
                                        product.getProductimageurl(), postlink);
                                dialog.show();
                            }
                        })

                        .show();
                return true;
            }
        });

        return convertView;
    }

//    public boolean checkInternet() {
//        if (CommonUtility.isOnline(mContext)) {
//            return true;
//        } else {
//            CommonUtility.showNoInternetAlert(mContext);
//            return false;
//        }
//    }

    public void likeInspiration(final Product product, final UiUpdate uiUpdate) {
        Syso.info("123 like id: " + product.getLike_info().getLike_id()
                + ", count:" + product.getLike_info().getLike_count());
        final InspirationSectionApi inspirationSectionApi = new InspirationSectionApi(
                new ServiceCallback() {

                    @Override
                    public void handleOnSuccess(Object object) {

                        InspirationRes inspirationRes = (InspirationRes) object;
                        String likeId = inspirationRes.getLike_id();

                        if (TextUtils.isEmpty(likeId)) {
                            product.getLike_info().setLike_id("");
                            product.getLike_info()
                                    .setLike_count(
                                            (CommonUtility.getInt(product
                                                    .getLike_info()
                                                    .getLike_count()) - 1)
                                                    + "");
                            AlertUtils.showToast(mContext, "Unliked");
                        } else {
                            product.getLike_info().setLike_id(likeId);
                            product.getLike_info()
                                    .setLike_count(
                                            (CommonUtility.getInt(product
                                                    .getLike_info()
                                                    .getLike_count()) + 1)
                                                    + "");
                            AlertUtils.showToast(mContext, "Liked");
                        }
                        if (uiUpdate != null) {
                            uiUpdate.updateUi();
                        }
//                        AlertUtils.showToast(context, inspirationRes.getMessage());
                    }

                    @Override
                    public void handleOnFailure(ServiceException exception,
                                                Object object) {
                        if (object != null) {
                            InspirationRes inspirationRes = (InspirationRes) object;
                            String message = inspirationRes.getMessage();
                            AlertUtils.showToast(mContext, message);
                        } else {
                            AlertUtils.showToast(mContext, R.string.invalid_response);
                        }
                    }
                });
        if (TextUtils.isEmpty(product.getLike_info().getLike_id())) {
            inspirationSectionApi.postLike(UserPreference.getInstance().getUserID(), product.getId(), "product");
        } else {
            inspirationSectionApi.removeLike(UserPreference.getInstance().getUserID(), product.getLike_info().getLike_id());
        }
        inspirationSectionApi.execute();
    }

    public void addProductToCart(Product product) {
//        mProgressBarDialog = new ProgressBarDialog(context);
//        mProgressBarDialog.show();
        final CartApi cartApi = new CartApi(new ServiceCallback() {

            @Override
            public void handleOnSuccess(Object object) {
                if (object != null) {
                    CartRes response = (CartRes) object;
                    UserPreference.getInstance().incCartCount();
                    AlertUtils.showToast(mContext, response.getMessage());
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {
                Syso.info("In handleOnFailure>>" + object);
                if (object != null) {
                    CartRes response = (CartRes) object;
                    AlertUtils.showToast(mContext, response.getMessage());
                } else {
                    AlertUtils.showToast(mContext, R.string.invalid_response);
                }
            }
        });
        String fromUserId = product.getFrom_user_id() == null ? "" : product.getFrom_user_id();
        String fromCollection = product.getFrom_collection_id() == null ? "" : product.getFrom_collection_id();
        cartApi.addToCart(UserPreference.getInstance().getUserID(), product.getId(), "1", UserPreference.getInstance().getCartID(), fromUserId, fromCollection, "", "", product.getSelected_values());
        cartApi.execute();
    }

    public class ViewHolder {
        private ImageView productImage;
    }
}


