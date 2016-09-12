package com.kikr.dialog;

import com.kikrlib.bean.ProductChildOption;

public interface DialogCallback {
	
	public void setColor(String color, int position);
	public void setSize(String size);
	public void setQuantity(String quantity);
	public void setOption(String option);
	public void setFit(String fit);
	void setOption(String option, int position);
	public void addOption(ProductChildOption child);
}
