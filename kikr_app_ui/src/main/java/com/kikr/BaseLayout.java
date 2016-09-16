package com.kikr;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class BaseLayout extends LinearLayout {

	// Duration of sliding animation, in milliseconds
	private static final int SLIDING_DURATION = 500;

	// Query Scroller every 16 milliseconds
	private static final int QUERY_INTERVAL = 16;

	// MainLayout width
	int mainLayoutWidth;

	// Sliding menu
	private View leftMenu;
	private View rightMenu;

	// Main content
	private View content;
	private boolean isLeftMenu = true;

	// menu does not occupy some right space
	// This should be updated correctly later in onMeasure
	private static int menuRightMargin = 150;
	private static int menuLeftMargin = 150;

	// The state of menu
	private enum MenuState {
		HIDING, HIDDEN, SHOWING, SHOWN,
	};

	// content will be layouted based on this X offset
	// Normally, contentXOffset = menu.getLayoutParams().width = this.getWidth -
	// menuRightMargin
	private int contentXOffset;

	// menu is hidden initially
	private MenuState currentMenuState = MenuState.HIDDEN;

	// Scroller is used to facilitate animation
	private Scroller menuScroller = new Scroller(this.getContext(),
			new EaseInInterpolator());

	private Runnable menuRunnable = new MenuRunnable();
	private Handler menuHandler = new Handler();

	// Previous touch position
	int prevX = 0;

	// Is user dragging the content
	boolean isDragging = false;

	// Used to facilitate ACTION_UP
	int lastDiffX = 0;

	public BaseLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseLayout(Context context) {
		super(context);
	}


	// Ask all children to measure themselves and compute the measurement of
	// this
	// layout based on the children
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		mainLayoutWidth = MeasureSpec.getSize(widthMeasureSpec);
		menuRightMargin = mainLayoutWidth * 10 / 50;
		menuLeftMargin = mainLayoutWidth * 10 / 50;
	}

	// This is called when MainLayout is attached to window
	// At this point it has a Surface and will start drawing.
	// Note that this function is guaranteed to be called before onDraw
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		leftMenu = this.getChildAt(0);
		rightMenu = this.getChildAt(1);
		content = this.getChildAt(2);

		// Initially hide the menu
		leftMenu.setVisibility(View.GONE);
		rightMenu.setVisibility(View.GONE);
	}

	// Called from layout when this view should assign a size and position to
	// each of its children
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
//		System.out.println("in onLayout "+ isLeftMenu+ " left "+left+" top "+top+" right "+right+" bottom "+bottom);
		// True if MainLayout 's size and position has changed
		// If true, calculate child views size
		if (changed) {
			// content View occupies the full height and width
			LayoutParams contentLayoutParams = (LayoutParams) content.getLayoutParams();
			contentLayoutParams.height = this.getHeight();
			contentLayoutParams.width = this.getWidth();

			LayoutParams menuLayoutParams = (LayoutParams) leftMenu.getLayoutParams();
			menuLayoutParams.height = this.getHeight();
			menuLayoutParams.width = this.getWidth() - menuRightMargin;

			LayoutParams menuRightLayoutParams = (LayoutParams) rightMenu.getLayoutParams();
			menuRightLayoutParams.height = this.getHeight();
			menuRightLayoutParams.width = this.getWidth() - menuLeftMargin;
		}

		// Layout the child views
		if (isLeftMenu) {
			leftMenu.layout(left, top, right - menuRightMargin, bottom);
			content.layout(left + contentXOffset, top, right + contentXOffset,bottom);
		} else {
			rightMenu.layout(left + menuLeftMargin, top, right, bottom);
			content.layout(left - contentXOffset, top, right - contentXOffset,bottom);
		}
	}

	// Custom methods for MainLayout
	// Used to show/hide menu accordingly
	public void toggleMenu(boolean value) {
		isLeftMenu = value;
		// Do nothing if sliding is in progress
		if (currentMenuState == MenuState.HIDING|| currentMenuState == MenuState.SHOWING)
			return;

		switch (currentMenuState) {
		case HIDDEN:
			currentMenuState = MenuState.SHOWING;
			if (isLeftMenu) {
				leftMenu.setVisibility(View.VISIBLE);
				rightMenu.setVisibility(View.GONE);
				menuScroller.startScroll(0, 0,leftMenu.getLayoutParams().width, 0, SLIDING_DURATION);
			} else {
				rightMenu.setVisibility(View.VISIBLE);
				leftMenu.setVisibility(View.GONE);
				menuScroller.startScroll(0, 0,-(rightMenu.getLayoutParams().width), 0,SLIDING_DURATION);
			}
			break;
		case SHOWN:
			currentMenuState = MenuState.HIDING;
			if (isLeftMenu)
				menuScroller.startScroll(contentXOffset, 0, -contentXOffset, 0,SLIDING_DURATION);
			else
				menuScroller.startScroll(-rightMenu.getLayoutParams().width, 0,rightMenu.getLayoutParams().width, 0, SLIDING_DURATION);
			break;
		default:
			break;
		}

		// Begin querying
		menuHandler.postDelayed(menuRunnable, QUERY_INTERVAL);
		// Invalite this whole MainLayout, causing onLayout() to be called
		this.invalidate();
	}

	// Query Scroller
	protected class MenuRunnable implements Runnable {
		@Override
		public void run() {
			boolean isScrolling = menuScroller.computeScrollOffset();
			adjustContentPosition(isScrolling);
		}
	}

	// Adjust content View position to match sliding animation
	private void adjustContentPosition(boolean isScrolling) {
			int scrollerXOffset = menuScroller.getCurrX();

		content.offsetLeftAndRight(scrollerXOffset - contentXOffset);
		contentXOffset = scrollerXOffset;

		// Invalidate this whole MainLayout, causing onLayout() to be called
		this.invalidate();

		// Check if animation is in progress
		if (isScrolling)
			menuHandler.postDelayed(menuRunnable, QUERY_INTERVAL);
		else
			this.onMenuSlidingComplete();
	}

	// Called when sliding is complete
	private void onMenuSlidingComplete() {
		switch (currentMenuState) {
		case SHOWING:
			currentMenuState = MenuState.SHOWN;
			break;
		case HIDING:
			currentMenuState = MenuState.HIDDEN;
			if (isLeftMenu)
				leftMenu.setVisibility(View.GONE);
			else
				rightMenu.setVisibility(View.GONE);
			break;
		default:
			return;
		}
	}

	// Make scrolling more natural. Move more quickly at the end
	protected class EaseInInterpolator implements Interpolator {
		@Override
		public float getInterpolation(float t) {
			return (float) Math.pow(t - 1, 5) + 1;
		}

	}

	// Is menu completely shown
	public boolean isMenuShown() {
		return currentMenuState == MenuState.SHOWN;
	}

		
}
