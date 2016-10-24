package com.flatlay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.dialog.DialogCallback;
import com.flatlay.dialog.ProductMainOptionDialog;
import com.flatlaylib.bean.ProductMainOption;

public class ProductMainOptionListAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private ProductMainOption optionList;
	private DialogCallback dialogCallback;
	int selectedPos=-1;
	TextView optionTextValue;
	ProductMainOptionDialog productMainOptionDialog;

	public ProductMainOptionListAdapter(ProductMainOptionDialog productMainOptionDialog, Context context,DialogCallback dialogCallback, ProductMainOption optionList2, String selectedOption, TextView optionTextValue) {
		super();
		this.mContext = context;
		this.dialogCallback = dialogCallback;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.optionTextValue = optionTextValue;
		for(int i=0;i<optionList2.getOptionList().size();i++){
			if(optionList2.getOptionList().get(i).getText().equals(selectedOption))
				selectedPos=i;
		}
		this.productMainOptionDialog = productMainOptionDialog;
		this.optionList = optionList2;
	}

	@Override
	public int getCount() {
		return optionList.getOptionList().size();
	}

	@Override
	public Object getItem(int index) {
		return 0;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_color_list, null);
			viewHolder = new ViewHolder();
			viewHolder.colorName = (TextView) convertView.findViewById(R.id.colorName);
			viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(position==selectedPos)
			viewHolder.checkBox.setChecked(true);
		else
			viewHolder.checkBox.setChecked(false);
		
		viewHolder.colorName.setText(optionList.getOptionList().get(position).getText());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				optionTextValue.setText(optionList.getOptionList().get(position).getText());
//				if(((HomeActivity)mContext).checkInternet())
					dialogCallback.addOption(optionList.getOptionList().get(position));
					productMainOptionDialog.dismiss();
			}
		});
		return convertView;
	}

	public class ViewHolder {
		TextView colorName;
		CheckBox checkBox;
	}

}
