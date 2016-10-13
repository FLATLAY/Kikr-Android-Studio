package com.kikr.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.flatlay.R;
import com.kikrlib.utils.Syso;

public class TagView extends TextView {
	float x=0;
	float y=0;
	int TEXT_MAX_LENGTH=15;
	

	public TagView(Context context) {
		super(context);
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		int pad=(int) getResources().getDimension(R.dimen.padding_small);
		setPadding(pad, pad, pad, pad);
		setBackgroundColor(getResources().getColor(R.color.black));
		setTextColor(getResources().getColor(R.color.white));
	}

	public TagView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TagView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	

	public void setTagText(String value) {
		if(value!=null){
			if(value.length()>15){
				String newValue=value.substring(0,15)+"...";
				setText(newValue);
			}else{
				setText(value);
			}
		}
		setUpdatedXY();
	}

	public void setXY(float x,float y) {
		this.x=x;
		this.y=y;
		setUpdatedXY();
	}
	
	private void setUpdatedXY() {
		float finalX,finalY;
		measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED);
		int widht = getMeasuredWidth();
		int height = getMeasuredHeight();
		float newX = x - widht / 2;
		finalX=newX;
		finalY=y;
		
		if(newX<0)
			finalX=0;
		
//		Resources r = getResources();
//		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, r.getDisplayMetrics());
//		float py = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y, r.getDisplayMetrics());

		setX(finalX);
		setY(finalY);
//		Syso.info("1234567********   X: "+newX+", Y:"+y+"  px:"+px+", py:"+py);
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// TODO Auto-generated method stub
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
//	    int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
//	    Syso.info("1234567********MeasureSpec   parentWidth: "+parentWidth+", parentHeight:"+parentHeight);
//	}
	
}
