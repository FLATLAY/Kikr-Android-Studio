package com.flatlaylib.bean;

import com.flatlaylib.utils.Syso;

public class TaggedItem {
	private String selectedItem = "";
	private String selectedItemType = "";
	private String selectedItemXY = "";
	private String selectedItemName = "";
	
	/**
	 * @return the selectedItem
	 */
	public String getSelectedItem() {
		return selectedItem;
	}
	/**
	 * @param selectedItem the selectedItem to set
	 */
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}
	/**
	 * @return the selectedItemType
	 */
	public String getSelectedItemType() {
		return selectedItemType;
	}
	/**
	 * @param selectedItemType the selectedItemType to set
	 */
	public void setSelectedItemType(String selectedItemType) {
		this.selectedItemType = selectedItemType;
	}
	/**
	 * @return the selectedItemXY
	 */
	public String getSelectedItemXY() {
		return selectedItemXY;
	}
	/**
	 * @param selectedItemXY the selectedItemXY to set
	 */
	public void setSelectedItemXY(String selectedItemXY) {
		this.selectedItemXY = selectedItemXY;
	}
	/**
	 * @return the selectedItemName
	 */
	public String getSelectedItemName() {
		return selectedItemName;
	}
	/**
	 * @param selectedItemName the selectedItemName to set
	 */
	public void setSelectedItemName(String selectedItemName) {
		this.selectedItemName = selectedItemName;
	}
	
	@Override
	public String toString() {
		Syso.info("selectedItem = "+selectedItem+"\n selectedItemType = "+selectedItemType+"\n selectedItemXY = "+selectedItemXY+
				"\nselectedItemName = "+selectedItemName);
		return super.toString();
	}
	
}
