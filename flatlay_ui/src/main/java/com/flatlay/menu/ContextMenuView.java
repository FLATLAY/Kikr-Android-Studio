package com.flatlay.menu;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.flatlay.BaseActivity;
import com.flatlay.R;
import com.flatlay.activity.HomeActivity;
import com.flatlay.activity.ProductDetailWebViewActivity;
import com.flatlay.dialog.CollectionListDialog;
import com.flatlay.dialog.CreateAccountDialog;
import com.flatlay.dialog.ShareDialog;
import com.flatlay.utility.CommonUtility;
import com.flatlay.utility.UiUpdate;
import com.flatlaylib.api.InspirationSectionApi;
import com.flatlaylib.bean.Product;
import com.flatlaylib.db.UserPreference;
import com.flatlaylib.service.ServiceCallback;
import com.flatlaylib.service.ServiceException;
import com.flatlaylib.service.res.InspirationRes;
import com.flatlaylib.utils.AlertUtils;
import com.flatlaylib.utils.Syso;

/**
 * Created by RachelDi on 2/22/18.
 */

public class ContextMenuView extends FrameLayout {

    private static final int[] ITEM_DRAWABLES = {R.drawable.small_gray_heart, R.drawable.small_gray_plus,
            R.drawable.small_gray_cart, R.drawable.small_gray_kopie};


    private static final int[] ITEM_DRAWABLES_SELECTED = {R.drawable.likegreen, R.drawable.greenplus,
            R.drawable.ic_cart_selected, R.drawable.sharegreen1};
    private static final String[] ITEM_NAMES = {"Like", "Add to Collection", "Add to Cart", "Share"};
    private static final String[] ITEM_NAMES2 = {"Unlike", "Add to Collection ", "Add to Cart", "Share"};


    private ArcLayout arcLayout;
    private int lastSelectedPos = -1, angle = 120, fromAng = 210;
    private Product product;
    private ProductDetailWebViewActivity mContext;
    private UiUpdate uiUpdate;
    private String postlink;
    private static String SHARE_POST_LINK = "Find it @FLATLAY http://flat-lay.com/product/";

    public ContextMenuView(Context context) {
        super(context);
        inIt(context);
    }

    public ContextMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inIt(context);
    }

    public ContextMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inIt(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                checkSelection(ev);
                break;
            case MotionEvent.ACTION_UP:
                setVisibility(View.GONE);
                if (lastSelectedPos != -1) {
                    itemDidDisappear();
                    optionSelected();
                } else {
                    showHideOption();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    private void checkSelection(MotionEvent ev) {
        int childCount = arcLayout.getChildCount();
        View selectedView = null;
        for (int i = 0; i < childCount; i++) {
            View v = arcLayout.getChildAt(i);
            Rect rect = new Rect();
            v.getLocalVisibleRect(rect);
            Rect rect2 = new Rect();
            rect2.left = (int) (arcLayout.getX() + v.getX());
            rect2.right = rect2.left + rect.right;
            rect2.top = (int) (v.getY() + arcLayout.getY() + arcLayout.getMeasuredHeight() / 2 - rect.bottom);
            rect2.bottom = rect2.top + rect.bottom;
            if (rect2.contains((int) ev.getX(), (int) ev.getY())) {
                selectedView = v;
            }
        }
        selectView(selectedView);
    }

    private View lastView = null;

    private void selectView(View selectedView) {
        int childCount = arcLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = arcLayout.getChildAt(i);
            if (selectedView != null && v.equals(selectedView)) {
                ImageView view = (ImageView) v;
                view.setImageResource(ITEM_DRAWABLES_SELECTED[i]);
                mContext.showLableTextView(getName(i), (view.getLeft() + view.getRight()) / 2, view.getTop());
                lastSelectedPos = i;
            } else {
                ((ImageView) v).setImageResource(ITEM_DRAWABLES[i]);
            }
        }
        if (selectedView == null) {
            lastSelectedPos = -1;
            mContext.hideLableTextView();
        }
    }

    private String getName(int i) {
        if (product != null) {
            return TextUtils.isEmpty(product.getLike_info().getLike_id()) ? ITEM_NAMES[i] : ITEM_NAMES2[i];
        } else {
            return ITEM_NAMES[i];
        }
    }

    public void inIt(Context context) {
        arcLayout = new ArcLayout(context);
        arcLayout.setChildSize((int) getResources().getDimension(R.dimen.menuChildSize));
        arcLayout.setArc(fromAng, fromAng + angle);
        for (int i = 0; i < ITEM_DRAWABLES.length; i++) {
            ImageView item = new ImageView(context);
            item.setImageResource(ITEM_DRAWABLES[i]);
            arcLayout.addView(item);
            item.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
        addView(arcLayout);
    }

    public void setXY(float x, float y) {
        arcLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int widht = arcLayout.getMeasuredWidth();
        int height = arcLayout.getMeasuredHeight();
        arcLayout.setX(x - widht / 2);
        arcLayout.setY((float) (y - 0.75 * height)); //h+h/5
        Rect menuRect = new Rect((int) arcLayout.getX(), (int) arcLayout.getY(), (int) arcLayout.getX() + widht, (int) arcLayout.getY() + height);
        Rect deviceRect = new Rect(0, 0, getWidth(), getHeight());
        int angleFrom = calculateAngle(menuRect, deviceRect);
        arcLayout.setArc(angleFrom, angleFrom + angle);
        showHideOption();
    }

    private int calculateAngle(Rect menuRect, Rect deviceRect) {
        boolean isCutInLeft = false, isCutInRight = false, isCutInTop = false, isCutInBottom = false;
        if (menuRect.left - deviceRect.left < 0) {
            isCutInLeft = true;
        }
        if (deviceRect.right - menuRect.right < 0) {
            isCutInRight = true;
        }
        if (menuRect.top - deviceRect.top < 0) {
            isCutInTop = true;
        }
        if (deviceRect.bottom - menuRect.bottom < 0) {
            isCutInBottom = true;
        }

        if (!isCutInLeft && !isCutInRight && !isCutInTop && !isCutInBottom) {
            return fromAng;
        } else if (isCutInLeft && isCutInTop) {
            return 360;
        } else if (isCutInRight && isCutInTop) {
            return 90;
        } else if (isCutInLeft) {
            return 270;
        } else if (isCutInRight) {
            return 270 - angle;
        } else if (isCutInTop) {
            return 170 - angle;
        } else {
            return fromAng;
        }
    }

    public void showHideOption() {
        arcLayout.switchState(true);
    }

    private void itemDidDisappear() {
        final int itemCount = arcLayout.getChildCount();
        for (int i = 0; i < itemCount; i++) {
            View item = arcLayout.getChildAt(i);
            item.clearAnimation();
        }
        arcLayout.switchState(false);
    }

    private Animation bindItemAnimation(final View child, final boolean isClicked, final long duration) {
        Animation animation = createItemDisapperAnimation(duration, isClicked, child);
        child.setAnimation(animation);
        return animation;
    }

    private Animation createItemDisapperAnimation(final long duration, final boolean isClicked, View child) {
        AnimationSet animationSet = new AnimationSet(true);

        if (lastView != null && child.equals(lastView) && !isClicked) {
            animationSet.addAnimation(new ScaleAnimation(1.3f, 1.0f, 1.3f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        } else {
            animationSet.addAnimation(new ScaleAnimation(1.0f, isClicked ? 1.3f : 1.0f, 1.0f, isClicked ? 1.3f : 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f));
        }

        animationSet.setDuration(duration);
        animationSet.setFillAfter(true);

        return animationSet;
    }

    public void setProduct(Product product, ProductDetailWebViewActivity mContext) {
        this.product = product;
        this.mContext = mContext;
    }

    public void setUiUpdate(UiUpdate uiUpdate) {
        this.uiUpdate = uiUpdate;
    }

    public void unSelectIcons() {
        int childCount = arcLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = arcLayout.getChildAt(i);
            ImageView view = (ImageView) v;
            view.setImageResource(ITEM_DRAWABLES[i]);
        }
        mContext.hideLableTextView();
    }

    private void optionSelected() {
        switch (lastSelectedPos) {
            case 0:
                if (mContext.checkInternet())
                    if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                        CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                        createAccountDialog.show();
                    } else {
                        likeInspiration(product, uiUpdate);
                    }

                break;
            case 1:
                if (mContext.checkInternet()) {
                    if (UserPreference.getInstance().getPassword() == "" || UserPreference.getInstance().getEmail() == "" || UserPreference.getInstance().getUserName() == "") {
                        CreateAccountDialog createAccountDialog = new CreateAccountDialog(mContext);
                        createAccountDialog.show();
                    } else {
                        CollectionListDialog collectionListDialog = new CollectionListDialog(mContext, product);
                        collectionListDialog.show();
                    }

                }
                break;
            case 2:
                if (mContext.checkInternet()) {

//                    mContext.addProductToCart(product);
                }
                break;
            case 3:
                postlink = SHARE_POST_LINK + product.getId();
                ShareDialog dialog = new ShareDialog(mContext, product.getProductimageurl(), postlink);
                dialog.show();
                break;
        }
    }

    public ArcLayout getArcLayout() {
        return arcLayout;
    }

    public void likeInspiration(final Product product, final UiUpdate uiUpdate) {
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


}

