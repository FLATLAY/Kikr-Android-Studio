package com.flatlay.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.flatlay.R;
import com.flatlay.activity.HomeActivity;

public class AutocompleteCustomArrayAdapter extends ArrayAdapter<String> {

	final String TAG = "AutocompleteCustomArrayAdapter.java";
		
    Context mContext;
    int layoutResourceId;
    ArrayList<String> data= null;

    public AutocompleteCustomArrayAdapter(Context mContext, int layoutResourceId,ArrayList<String> data) {

        super(mContext, layoutResourceId, data);
        
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        Log.w("Activity","AutoCompleteCustomArrayAdapter");
    }
    @Override
    public int getCount() {
    	return data!=null?data.size():0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	try{
    		
	        /*
	         * The convertView argument is essentially a "ScrapView" as described is Lucas post 
	         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
	         * It will have a non-null value when ListView is asking you recycle the row layout. 
	         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
	         */
	        if(convertView==null){
	            // inflate the layout
	            LayoutInflater inflater = ((HomeActivity) mContext).getLayoutInflater();
	            convertView = inflater.inflate(R.layout.list_item, parent, false);
	        }
	        
	   	        // get the TextView and then set the text (item name) and tag (item ID) values
	        TextView textViewItem = (TextView) convertView.findViewById(R.id.tv_item);
	        textViewItem.setText(data.get(position));
	        	        
	    } catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        return convertView;
        
    }
}