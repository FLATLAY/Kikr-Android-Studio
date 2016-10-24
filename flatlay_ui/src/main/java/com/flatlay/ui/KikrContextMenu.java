package com.flatlay.ui;

import com.flatlaylib.utils.Syso;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class KikrContextMenu extends FrameLayout{
	
	public KikrContextMenu(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		Syso.info("12345 : in dispatch touch event");
		return super.dispatchTouchEvent(ev);
	}
}
