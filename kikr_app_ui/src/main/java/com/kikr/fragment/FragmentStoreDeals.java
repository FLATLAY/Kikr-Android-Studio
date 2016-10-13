package com.kikr.fragment;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kikr.BaseFragment;
import com.flatlay.R;
import com.kikr.adapter.DealListAdapter;
import com.kikr.ui.ProgressBarDialog;
import com.kikr.utility.CommonUtility;
import com.kikrlib.api.DealsApi;
import com.kikrlib.bean.Deals;
import com.kikrlib.bean.TopDeals;
import com.kikrlib.db.UserPreference;
import com.kikrlib.service.XMLParser;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.Syso;

public class FragmentStoreDeals extends BaseFragment implements OnClickListener {
	private View mainView;
	private String name,image;
	private Button inStoreDeals,onlineDeals;
	private ListView dealsList;
	private List<TopDeals> onlineDealsList = new ArrayList<TopDeals>();
	private List<TopDeals> instoreDealsList = new ArrayList<TopDeals>();
	public ProgressBarDialog mProgressBarDialog;
	private TextView storeName;
	private ImageView storeImage;
	private String TITLE = "title";
	private String ITEM = "item";
	private String DESCRIPTION = "description";
	private String DEALID = "szgd:dealid";
	private String LINK = "link";
	private String IMAGELINK = "szgd:imagelink";
	private String MERCHANTNAME = "szgd:merchant";
	private String EXPIRYTIME = "szgd:expdate";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {		
		mainView = inflater.inflate(R.layout.fragment_store_deals, null);
		return mainView;
	}
	
	public FragmentStoreDeals(String name,String image){
		this.name = name;
		this.image = image;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		inStoreDeals = (Button) mainView.findViewById(R.id.inStoreDeals);
		onlineDeals = (Button) mainView.findViewById(R.id.onlineDeals);
		dealsList = (ListView) mainView.findViewById(R.id.dealsList);
		storeName = (TextView) mainView.findViewById(R.id.storeName);
		storeImage = (ImageView) mainView.findViewById(R.id.storeImage);
	}

	@Override
	public void setData(Bundle bundle) {
		storeName.setText(name);
		CommonUtility.setImage(mContext, image, storeImage, R.drawable.dum_image_small_list_item);
		if (checkInternet()) 
			new getInStoreDeals().execute();
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		inStoreDeals.setOnClickListener(this);
		onlineDeals.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.onlineDeals:
			if (onlineDealsList!=null && onlineDealsList.size()>0) {
				dealsList.setAdapter(new DealListAdapter(mContext, onlineDealsList, false));
			}
			onlineDeals.setBackground(mContext.getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
			inStoreDeals.setBackground(mContext.getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			break;
		case R.id.inStoreDeals:
			if (instoreDealsList!=null && instoreDealsList.size()>0) {
				dealsList.setAdapter(new DealListAdapter(mContext, instoreDealsList, false));
			}
			onlineDeals.setBackground(mContext.getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			inStoreDeals.setBackground(mContext.getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
			break;
		default:
			break;
		}
	}
	
	private class getOnlineDeals extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			mProgressBarDialog = new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			return new DealsApi().getOnlineDealsByStore(UserPreference.getInstance().getUserID(),name.replace(" ", "%20"));
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressBarDialog.dismiss();
			super.onPostExecute(result);
			Syso.info("result: "+result);
			if (result!=null&&result.length()>0) {
				parseData(result,false);
			}
		}
	}
	
	private class getInStoreDeals extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			mProgressBarDialog = new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			return new DealsApi().getInStoreDealsByStore(UserPreference.getInstance().getUserID(),name.replace(" ", "%20"));
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressBarDialog.dismiss();
			super.onPostExecute(result);
			Syso.info("result: "+result);
			if (result!=null&&result.length()>0) {
				parseData(result,true);
			}
			new getOnlineDeals().execute();
		}
	}
	
	public void parseData(String xml,boolean isInStore) {
		try{
		List<TopDeals> topDeals = new ArrayList<TopDeals>();
		XMLParser xmlParser = new XMLParser();
		Document doc = xmlParser.getDomElement(xml); // getting DOM element
		NodeList nl = doc.getElementsByTagName(ITEM);
		// looping through all item nodes <item>
		for (int i = 0; i < nl.getLength(); i++) {
			Element e = (Element) nl.item(i);
			Syso.info("data:   "+xmlParser.getValue(e, DEALID));
			TopDeals deal = new TopDeals();
			deal.setTitle(xmlParser.getValue(e, TITLE));
			deal.setDescription(xmlParser.getValue(e, DESCRIPTION));
			deal.setLink(xmlParser.getValue(e, LINK));
			deal.setDealid(xmlParser.getValue(e, DEALID));
			deal.setMerchantName(xmlParser.getValue(e, MERCHANTNAME));
			deal.setExpiryTime(xmlParser.getValue(e, EXPIRYTIME));
			deal.setImagelink(xmlParser.getValue(e, IMAGELINK));
//			deal.setLatlng(getLatLng(xmlParser.getValue(e, LINK)));
			topDeals.add(deal);
		}
		if (isInStore) {
			instoreDealsList.addAll(topDeals);
			setAdapter();
		}else{
			onlineDealsList.addAll(topDeals);
		}
		}catch(Exception e){
			e.printStackTrace();
			AlertUtils.showToast(mContext, "No data found, please try again");
		}
	}

	private void setAdapter() {
		if (instoreDealsList.size()>0){
			hideDataNotFound();
			dealsList.setAdapter(new DealListAdapter(mContext, instoreDealsList, false));
		}
		else
			showDataNotFound();
	}

}
