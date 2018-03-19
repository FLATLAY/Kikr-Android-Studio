package com.flatlay.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.activity.ProductDetailWebViewActivity;
import com.flatlay.dialog.CollectionListDialog;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.dialog.ShareDialog;
import com.flatlay.fragment.FragmentDiscoverDetail;
import com.flatlay.fragment.ViewInsProductFragment;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.FontUtility;
import com.flatlay.utility.MyBubbleActions;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.api.CartApi;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.bean.Inspiration;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.CartRes;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import me.samthompson.bubbleactions.BubbleActions;

/**
 * Created by RachelDi on 1/22/18.
 */

public class InspirationProductUI {
    FragmentActivity mContext;
    List<Product> data;
    LayoutInflater mInflater;
    Inspiration inspiration;
    int width, height;
    int index;
    String postlink;
    static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/flatlay/";
    static String SHARE_POST_LINK2 = "http://flat-lay.com/flatlay/";

    //	View view;
    int productWidth;

    public InspirationProductUI(FragmentActivity context, Inspiration inspiration, View convertView, int width, int height, int index) {
        super();
        this.mContext = context;
//		view=convertView;
        this.data = (ArrayList<Product>) inspiration.getProducts();
        this.inspiration = inspiration;
        this.width = width;
        this.height = height;
        this.index = index;
        mFragmentStack = new Stack<String>();
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView() {
        LinearLayout ll = new LinearLayout(mContext);
        //LayoutParams layoutParams =new LinearLayout.LayoutParams(CommonUtility.getDeviceWidth(mContext)/2, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < data.size(); i++) {
            View convertView = null;
            if (index == 1) {
                layoutParams.setMargins(0, 15, 15, 0);
                convertView = (LinearLayout) mInflater.inflate(R.layout.inspiration_product_list, null);
            }
            if (index == 0) {
                layoutParams.setMargins(0, 5, 5, 0);
                convertView = (LinearLayout) mInflater.inflate(R.layout.inspiration_product_list2, null);
                productWidth = layoutParams.height;
            }
            ImageView activity_product_list_product_image = (ImageView) convertView.findViewById(R.id.activity_product_list_product_image);
            if (index == 1) {
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(width, width);

                activity_product_list_product_image.setLayoutParams(layoutParams2);
            }
            final Product product = data.get(i);

            CommonUtility.setImage(mContext, product.getProductimageurl(), activity_product_list_product_image, R.drawable.dum_list_item_product);
            convertView.setLayoutParams(layoutParams);
            ll.addView(convertView);
            convertView.setTag(i);

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", data.get((Integer) v.getTag()));
                    FragmentDiscoverDetail detail = new FragmentDiscoverDetail();
                    detail.setArguments(bundle);
                    ((HomeActivity)mContext).addFragment(detail);
                }
            });
            convertView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(final View v) {
                    MyBubbleActions.on(v)
                            .addAction("Like", R.drawable.small_gray_heart, new MyBubbleActions.Callback() {
                                @Override
                                public void doAction() {
                                    // Toast.makeText(v.getContext(), "Like", Toast.LENGTH_SHORT).show();
                                    if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                                        CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                                        createAccountDialog.show();
                                    } else {
                                        likeInspiration(product, new UiUpdate() {
                                            @Override
                                            public void updateUi() {
                                                // TextView likeCountTextView = (TextView) v.findViewById(R.id.likeCountTextView);
                                                //  likeCountTextView.setText(TextUtils.isEmpty(data.get((Integer) v.getTag()).getike_info().getLike_count()) ? "0" : data.get((Integer) v.getTag()).getLike_info().getLike_count());
                                            }
                                        });
                                    }
                                }
                            })
                            .addAction("Add to Collection", R.drawable.small_gray_plus, new MyBubbleActions.Callback() {
                                @Override
                                public void doAction() {
                                    // Toast.makeText(v.getContext(), "Add to Collection", Toast.LENGTH_SHORT).show();
                                    if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                                        CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                                        createAccountDialog.show();
                                    } else {
                                        CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, product);
                                        collectionListDialog.show();
                                    }
                                }
                            })
                            .addAction("Add to Cart", R.drawable.small_gray_cart, new MyBubbleActions.Callback() {
                                @Override
                                public void doAction() {
                                    // Toast.makeText(v.getContext(), "Add to Cart", Toast.LENGTH_SHORT).show();
                                    addProductToCart(product);

                                }
                            })
                            .addAction("View On Store Site", R.drawable.small_gray_store, new MyBubbleActions.Callback() {
                                @Override
                                public void doAction() {
                                    // Toast.makeText(v.getContext(), "View On Store Site", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext, ProductDetailWebViewActivity.class);
                                    intent.putExtra("data", new Gson().toJson(product));
                                    mContext.startActivity(intent);
                                }
                            })
                            .addAction("Share", R.drawable.small_gray_kopie, new MyBubbleActions.Callback() {
                                @Override
                                public void doAction() {
                                    // Toast.makeText(v.getContext(), "Share", Toast.LENGTH_SHORT).show();
                                    postlink = SHARE_POST_LINK + inspiration.getInspiration_id();
                                    ShareDialog dialog = new ShareDialog(mContext, inspiration.getInspiration_image(), postlink);
                                    dialog.show();
                                }
                            })

                            .show();
                    return true;
                }
            });
        }

        if (data.size() > 3)

        {
            RelativeLayout convertView = (RelativeLayout) mInflater.inflate(R.layout.layout_load_more, null);
            TextView text = (TextView) convertView.findViewById(R.id.textView1);
            if (index == 1) {
                RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(500, 500);
                layoutParams2.setMargins(0, 15, 15, 15);
                text.setTextSize(25);
                convertView.setLayoutParams(layoutParams2);
            }
            if (index == 0) {
                RelativeLayout.LayoutParams layoutParams2 =
                        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams2.setMargins(0, 15, 15, 15);
                convertView.setLayoutParams(layoutParams2);
            }
            text.setTypeface(FontUtility.setMontserratLight(mContext));
            convertView.setBackgroundResource(R.drawable.blackbordertextview);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("data", data);
                    ViewInsProductFragment detail = new ViewInsProductFragment(data);
//                    detail.setArguments(bundle);
                    ((HomeActivity) mContext).addFragment(detail);
                }
            });
            ll.addView(convertView);
        }

        return ll;
    }

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
                //  mProgressBarDialog.dismiss();
                if (object != null) {
                    CartRes response = (CartRes) object;
                    UserPreference.getInstance().incCartCount();
                    //  refreshCartCount();
                    AlertUtils.showToast(mContext, response.getMessage());
                }
            }

            @Override
            public void handleOnFailure(ServiceException exception,
                                        Object object) {
                //mProgressBarDialog.dismiss();
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
    public Stack<String> mFragmentStack;
    public FragmentTransaction transaction;
    private Fragment mContent;

}

