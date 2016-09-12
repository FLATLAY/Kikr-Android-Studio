package com.kikr.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kikr.R;
import com.kikr.activity.HomeActivity;
import com.kikr.fragment.FragmentSearch;
import com.kikr.fragment.FragmentSearchResults;
import com.kikrlib.api.GetProductsBySubCategoryApi;
import com.kikrlib.bean.Categories;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.res.GetProductsByCategoryRes;
import com.kikrlib.utils.Syso;

import java.util.List;

public class CategoryRowUI {
	FragmentActivity mContext;
	LayoutInflater mInflater;
	Categories categories;
	LinearLayout layout;
	ProgressBar loader;
	Boolean isMain = false;
	Boolean clickToggle = false;
	ImageView imgDropDown;
	ImageView imgLogo;
	TextView txtSearch;
	LinearLayout layoutRow;
	public CategoryRowUI(FragmentActivity context, Categories categories, boolean isMain) {
		super();
		this.mContext = context;
		this.categories = categories;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isMain = isMain;
	}
	
	public CategoryRowUI(FragmentActivity context, Categories categories) {
		super();
		this.mContext = context;
		this.categories = categories;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public View getView() {
		LinearLayout ll =  new LinearLayout(mContext);
		final View v = mInflater.inflate(R.layout.category_row, null);
		layoutRow = (LinearLayout) v.findViewById(R.id.layoutRow);
		imgLogo = (ImageView)v.findViewById(R.id.imgLogo);
		imgDropDown = (ImageView)v.findViewById(R.id.imgDropDown);
		TextView textView = (TextView) v.findViewById(R.id.catagoryName);
		TextView textTab = (TextView) v.findViewById(R.id.txtTab);
		textView.setText(categories.getDisplayName().length()>0?capitalizeEachWord(categories.getDisplayName().trim()):"Search All");
		textView.setTextColor(mContext.getResources().getColor(getTextColor()));
		
		if(categories.getDisplayName().trim().equalsIgnoreCase("Health & Personal Care"))
			textView.setText("Personal Care & Beauty");
		
		if(categories.getDisplayName().trim().equalsIgnoreCase("Car Seats & Accessories"))
			textView.setText("Car Seats");
		
		if(categories.getDisplayName().trim().equalsIgnoreCase("Pet Supplies"))
			textView.setText("Pets");
		
		if(categories.getDisplayName().trim().equalsIgnoreCase("Audio & Video Accessories"))
			textView.setText("Audio & Video");
		
		if(categories.getDisplayName().trim().equalsIgnoreCase("Video Game Consoles & Accessories"))
			textView.setText("Handheld Game Console Accessories");
		
		if(categories.getDisplayName().trim().equalsIgnoreCase("More Systems"))
			textView.setText("PS2 & XBOX");
		
		if(categories.getLavel() == 0)
			textTab.setText("");
		else if(categories.getLavel() == 1)
			textTab.setText("");
		else if(categories.getLavel() == 2)
			textTab.setText("\t\t\t\t\t\t");
		else if(categories.getLavel() == 3)
			textTab.setText("\t\t\t\t\t\t\t\t\t");
		else if(categories.getLavel() == 4)
			textTab.setText("\t\t\t\t\t\t\t\t\t\t\t\t");
		else if(categories.getLavel() == 5)
			textTab.setText("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
		else if(categories.getLavel() == 6)
			textTab.setText("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
		
		
		layout = (LinearLayout) v.findViewById(R.id.subCategoryList);
		loader = (ProgressBar) v.findViewById(R.id.loader);
		
		if(isMain) {
			imgLogo.setVisibility(View.VISIBLE);
			imgDropDown.setVisibility(View.VISIBLE);
		}
		else {
			imgLogo.setVisibility(View.GONE);
			imgDropDown.setVisibility(View.GONE);
		}
			
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(categories.isHasSubcategory()){
						if(categories.getList()==null && loader.getVisibility()==View.GONE){
							loader.setVisibility(View.VISIBLE);
							getCategories();
							if(categories.getLavel() == 2)
								layoutRow.setBackgroundColor(mContext.getResources().getColor(R.color.aquamarine2));
						}
						else {
							categories.setList(null);
							layout.removeAllViews();
							imgLogo.setImageResource(R.drawable.kikr_trace_logo);
							if(categories.getLavel() == 2)
								layoutRow.setBackgroundColor(mContext.getResources().getColor(R.color.white));
						}
					
				}else if(categories.isSearch()){
					((HomeActivity)mContext).addFragment(new FragmentSearchResults(categories.getSearchRequest()));
				}
				
			}
		});
		ll.addView(v);
		return ll;
	}
	
	int color[] = new int[]{R.color.btn_green, R.color.app_text_color, R.color.black, R.color.darkgrey, R.color.darkgrey, R.color.darkgrey, R.color.darkgrey};
	private int getTextColor() {
		return color[categories.getLavel()-1];
	}
	
	private StringBuilder capitalizeEachWord(String s) {
		StringBuilder result = new StringBuilder(s.length());
		String[] words = s.split("\\s");
		for(int i=0,l=words.length;i<l;++i) {
		  if(i>0) result.append(" ");      
		  result.append(Character.toUpperCase(words[i].charAt(0)))
		        .append(words[i].substring(1));

		}
		return result;
	}

	private void getCategories() {
		GetProductsBySubCategoryApi checkPointsStatusApi = new GetProductsBySubCategoryApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				loader.setVisibility(View.GONE);
				Syso.info("In handleOnSuccess>>" + object);
				GetProductsByCategoryRes getProductsByCategoryRes = (GetProductsByCategoryRes) object;
				List<Categories>  categories2 = getProductsByCategoryRes.getData();
				categories.setList(categories2);
				if(!isMain) {
					for(Categories cat: categories2) {
						cat.validate();
						Log.e("display name",cat.getDisplayName() + "");
						if(cat.getDisplayName().length() == 0 && categories2.size() == 1) {			
							imgDropDown.setVisibility(View.GONE);
							((HomeActivity)mContext).addFragment(new FragmentSearchResults(categories.getSearchRequest()));
							break;
						} else if(cat.getCat2().trim().equalsIgnoreCase("Laptops") && cat.getCat1().trim().equalsIgnoreCase("Computers & Accessories")) {
							((HomeActivity)mContext).addFragment(new FragmentSearchResults(categories.getSearchRequest()));
							break;
						}
						else {
						
							layout.addView(new CategoryRowUI(mContext, cat).getView());
							
							imgDropDown.setVisibility(View.VISIBLE);
						}
							
					}
					FragmentSearch.txtSearch.setText(categories.getSearchRequestForTextView());
				}
				else {
					for(Categories cat: categories2) {
						cat.validate();
						layout.addView(new CategoryRowUI(mContext, cat).getView());
						Log.e("first batch categories",cat.getCat1());
					}
					imgDropDown.setVisibility(View.VISIBLE);
					imgLogo.setImageResource(R.drawable.aquakikr);
					FragmentSearch.txtSearch.setTypeface(null, Typeface.NORMAL);
					FragmentSearch.txtSearch.setGravity(Gravity.LEFT);
					FragmentSearch.txtSearch.setTextColor(mContext.getResources().getColor(R.color.white));
					FragmentSearch.txtSearch.setText(capitalizeEachWord(categories.getCat1().trim()));
				}
				
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				loader.setVisibility(View.GONE);
			}
		});
		checkPointsStatusApi.getCategory(UserPreference.getInstance().getUserID(), categories.getSubcatRequest());
		checkPointsStatusApi.execute();
	}
	
}
