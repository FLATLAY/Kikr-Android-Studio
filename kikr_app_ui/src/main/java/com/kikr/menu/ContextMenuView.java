package com.kikr.menu;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.dialog.CollectionListDialog;
import com.kikr.dialog.CreateAccountDialog;
import com.kikr.dialog.ShareDialog;
import com.kikr.utility.UiUpdate;
import com.kikrlib.bean.Product;
import com.kikrlib.db.UserPreference;
import com.kikrlib.utils.Syso;

public class ContextMenuView extends FrameLayout {

    private static final int[] ITEM_DRAWABLES = {R.drawable.likewhite, R.drawable.ic_add_collection_unselect,
            R.drawable.ic_cart_unselect, R.drawable.sharewhite};


    private static final int[] ITEM_DRAWABLES_SELECTED = {R.drawable.likegreen, R.drawable.ic_add_collection_selected,
            R.drawable.ic_cart_selected, R.drawable.sharegreen};
    private static final String[] ITEM_NAMES = {"Like", "Add to Collection", "Add to Cart", "Share"};
    private static final String[] ITEM_NAMES2 = {"Unlike", "Add to Collection ", "Add to Cart", "Share"};


    ArcLayout arcLayout;
    int lastSelectedPos = -1;
    int angle = 120;
    int fromAng = 210;
    private Product product;
    private HomeActivity mContext;
    UiUpdate uiUpdate;

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
        Syso.info("12345 : in dispatch touch event");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Syso.info(">>>>ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Syso.info(">>>>ACTION_MOVE");
                checkSelection(ev);
                break;
            case MotionEvent.ACTION_UP:
                Syso.info(">>>>ACTION_UP");
//			itemDidDisappear();
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
//			Syso.info("X :"+v.getX()+",  Y:"+v.getY()+", XX:"+arcLayout.getX()+", YY"+arcLayout.getY());
            Rect rect = new Rect();
            v.getLocalVisibleRect(rect);
            Rect rect2 = new Rect();
            rect2.left = (int) (arcLayout.getX() + v.getX());
            rect2.right = rect2.left + rect.right;
            rect2.top = (int) (v.getY() + arcLayout.getY() + arcLayout.getMeasuredHeight() / 2 - rect.bottom);
            rect2.bottom = rect2.top + rect.bottom;
//			Syso.info("99999999999 1: Left :"+i+">"+ rect.left+",Right :"+ rect.right+",Top :"+ rect.top+",Bottom :"+ rect.bottom);
//			Syso.info("99999999999 2: Left :"+ rect2.left+",Right :"+ rect2.right+",Top :"+ rect2.top+",Bottom :"+ rect2.bottom);
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
//			 bindItemAnimation(selectedView, true, 100);
//			 lastView=selectedView;
                lastSelectedPos = i;
            } else {
                ((ImageView) v).setImageResource(ITEM_DRAWABLES[i]);
//			 bindItemAnimation(v, false, 100);
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
//		int angel=145;
//		int fromAng=190;
        arcLayout.setArc(fromAng, fromAng + angle);
        for (int i = 0; i < ITEM_DRAWABLES.length; i++) {
            ImageView item = new ImageView(context);
            item.setImageResource(ITEM_DRAWABLES[i]);
        //    item.setBackgroundResource(R.drawable.bg_btn_discover);
            arcLayout.addView(item);
            item.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Syso.info("View touched>>>>>>>");
                    return false;
                }
            });
        }
        addView(arcLayout);
    }

    public void setXY(float x, float y) {
//		int angel=145;
        arcLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int widht = arcLayout.getMeasuredWidth();
        int height = arcLayout.getMeasuredHeight();
        Syso.info("Angle>>> before x " + arcLayout.getX() + ", before Y:" + arcLayout.getY());
        arcLayout.setX(x - widht / 2);
        arcLayout.setY((float) (y - 0.75 * height)); //h+h/5
        Syso.info("Angle>>> Size: width " + arcLayout.getMeasuredWidth() + ", width :" + getWidth() + ", height:" + getHeight() + ", X:" + getX() + ", Y:" + getY());
        Syso.info("Angle>>> after x " + arcLayout.getX() + ", after Y:" + arcLayout.getY());
        Rect menuRect = new Rect((int) arcLayout.getX(), (int) arcLayout.getY(), (int) arcLayout.getX() + widht, (int) arcLayout.getY() + height);
        Rect deviceRect = new Rect(0, 0, getWidth(), getHeight());
        int angleFrom = calculateAngle(menuRect, deviceRect);
        arcLayout.setArc(angleFrom, angleFrom + angle);
        showHideOption();
    }

    private int calculateAngle(Rect menuRect, Rect deviceRect) {
        boolean isCutInLeft = false, isCutInRight = false, isCutInTop = false, isCutInBottom = false;
//		int angel=160;
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
        Syso.info("Angle>>>  isCutInLeft:" + isCutInLeft + ",isCutInRight:" + isCutInRight + ",isCutInTop:" + isCutInTop + ",isCutInBottom:" + isCutInBottom);

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

//	public void onItemTouch(){
//		 final int itemCount = mArcLayout.getChildCount();
//         for (int i = 0; i < itemCount; i++) {
//             View item = mArcLayout.getChildAt(i);
//             if (viewClicked != item) {
//                 bindItemAnimation(item, false, 300);
//             }
//         }
//         mArcLayout.invalidate();
//	}

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
//	        animationSet.addAnimation(new AlphaAnimation(1.0f, 0.0f));

        animationSet.setDuration(duration);
//	        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.setFillAfter(true);

        return animationSet;
    }

    public void setProduct(Product product, HomeActivity mContext) {
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
                        mContext.likeInspiration(product, uiUpdate);
                    }

//			AlertUtils.showToast(getContext(), "Like option selected");
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
//			AlertUtils.showToast(getContext(), "Add to collection option selected");
                break;
            case 2:
                if (mContext.checkInternet()) {
                    //ProductVariableOption.getInstance(mContext, product).getCartId();

                    mContext.addProductToCart(product);
                }
//			AlertUtils.showToast(getContext(), "Checkout option selected");
                break;
            case 3:
//			AlertUtils.showToast(getContext(), "Share option selected");
                ShareDialog dialog = new ShareDialog(mContext, (HomeActivity) mContext, product);
                dialog.show();
//			mContext.shareProduct(product);
                break;
        }
    }

    public ArcLayout getArcLayout() {
        return arcLayout;
    }
}
