package com.flatlay.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.flatlay.BaseFragment;
import com.flatlay.R;
import com.flatlay.adapter.DealListAdapter;
import com.flatlay.adapter.StoreListAdapter;
import com.flatlay.ui.ProgressBarDialog;
import com.flatlay.utility.GPSTracker;
import com.kikrlib.api.DealsApi;
import com.kikrlib.api.GetNearByDealsApi;
import com.kikrlib.api.TokenApi;
import com.kikrlib.bean.DealToken;
import com.kikrlib.bean.NearByDeal;
import com.kikrlib.bean.TopDeals;
import com.kikrlib.db.DatabaseHelper;
import com.kikrlib.db.UserPreference;
import com.kikrlib.db.dao.FavoriteDealsDAO;
import com.kikrlib.service.ServiceCallback;
import com.kikrlib.service.ServiceException;
import com.kikrlib.service.XMLParser;
import com.kikrlib.service.res.NearByDealsRes;
import com.kikrlib.utils.AlertUtils;
import com.kikrlib.utils.StringUtils;
import com.kikrlib.utils.Syso;

public class FragmentDeals extends BaseFragment implements OnClickListener,OnMarkerClickListener {
	private View mainView;
	private GoogleMap map;
	private ListView dealsList;
	private LinearLayout mapLayout;
	private Button dealNearByBtn,dealPopularBtn,dealFavoriteBtn,dealOnlineBtn,dealStoresBtn,dealMallsBtn,dealViewListBtn;
	private List<Button> btnsTop = new ArrayList<Button>();
	private List<TopDeals> topDeals = new ArrayList<TopDeals>();
	private ProgressBarDialog mProgressBarDialog;
	private GPSTracker gps;
	private double latitude;
	private double longitude;
	private String latlng="";
	private String TITLE = "title";
	private String ITEM = "item";
	private String DESCRIPTION = "description";
	private String DEALID = "szgd:dealid";
	private String LINK = "link";
	private String IMAGELINK = "szgd:imagelink";
	private String MERCHANTNAME = "szgd:merchant";
	private String EXPIRYTIME = "szgd:expdate";
	private Marker marker;
	private boolean isFavoriteList=false;
	private boolean isOnlineList=false;
	private boolean isPopularList=false;
	private LinearLayout searchYourDealLayout;
	private Spinner searchYourDealEditText;
	private Map<Marker, String> markersMap = new HashMap<Marker, String>();
	private int count=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {		
		mainView = inflater.inflate(R.layout.fragment_deals, null);
		return mainView;
	}

	@Override
	public void initUI(Bundle savedInstanceState) {
		setUpMapIfNeeded();
		gps = new GPSTracker(mContext);
		mapLayout = (LinearLayout) mainView.findViewById(R.id.mapLayout);
		dealsList = (ListView) mainView.findViewById(R.id.dealsList);
		dealNearByBtn = (Button) mainView.findViewById(R.id.dealNearByBtn);
		dealPopularBtn = (Button) mainView.findViewById(R.id.dealPopularBtn);
		dealFavoriteBtn = (Button) mainView.findViewById(R.id.dealFavoriteBtn);
		dealOnlineBtn = (Button) mainView.findViewById(R.id.dealOnlineBtn);
		dealStoresBtn = (Button) mainView.findViewById(R.id.dealStoresBtn);
		dealMallsBtn = (Button) mainView.findViewById(R.id.dealMallsBtn);
		dealViewListBtn = (Button) mainView.findViewById(R.id.dealViewListBtn);
		btnsTop.add(dealNearByBtn);
		btnsTop.add(dealPopularBtn);
		btnsTop.add(dealFavoriteBtn);
		btnsTop.add(dealOnlineBtn);
		searchYourDealLayout = (LinearLayout) mainView.findViewById(R.id.searchYourDealLayout);
		searchYourDealEditText = (Spinner) mainView.findViewById(R.id.searchYourDealEditText);
//		latitude=34.190246;
//		longitude=-118.605033;
//		getNearByStores();
		if (gps.canGetLocation()) {
			try {
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				latlng = latitude+","+longitude;
				getNearByStores();
				Syso.info("latlng   "+latitude + "      " + longitude);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void getLocation() {
		gps=new GPSTracker(mContext);
		count++;
		if (gps.canGetLocation()) {
			try {
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				latlng = latitude+","+longitude;
				getNearByStores();
				Syso.info("latlng   "+latitude + "      " + longitude);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setUpMapIfNeeded() {
		try {
			if (map == null) {
				SupportMapFragment supportMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
				if(supportMapFragment!=null) {
					map = supportMapFragment.getMap();
					if (map != null) {
						map.setMyLocationEnabled(true);
						map.setOnMarkerClickListener(this);
						map.setOnCameraChangeListener(new OnCameraChangeListener() {

							@Override
							public void onCameraChange(CameraPosition arg0) {
							}
						});
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void addMarkers(List<NearByDeal> nearByDeals) {
		if(map!=null) {
			for (int i = 0; i < nearByDeals.size(); i++) {
				LatLng latLng = new LatLng(StringUtils.getDoubleValue(nearByDeals.get(i).getLat()), StringUtils.getDoubleValue(nearByDeals.get(i).getLng()));
				marker = map.addMarker(new MarkerOptions()
						.position(latLng)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)).draggable(true)
						.title(nearByDeals.get(i).getName()));
				markersMap.put(marker, nearByDeals.get(i).getImg());
				// Locate the first location
				if (i == 0) {
					map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
					map.animateCamera(CameraUpdateFactory.zoomTo(12));
				}
			}
		}
		map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker mk) {
				addFragment(new FragmentStoreDeals(mk.getTitle(),markersMap.get(mk)));
			}
		});
	}
	
	@Override
	public void setData(Bundle bundle) {
		searchYourDealEditText.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
									   int arg2, long arg3) {
				if (checkInternet() && !(searchYourDealEditText.getSelectedItemPosition() == 0)) {
					topDeals.clear();
					new searchDeals().execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	@Override
	public void refreshData(Bundle bundle) {

	}

	@Override
	public void setClickListener() {
		dealNearByBtn.setOnClickListener(this);
		dealPopularBtn.setOnClickListener(this);
		dealFavoriteBtn.setOnClickListener(this);
		dealOnlineBtn.setOnClickListener(this);
		dealStoresBtn.setOnClickListener(this);
		dealMallsBtn.setOnClickListener(this);
		dealViewListBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dealViewListBtn:
			checkForListOrMap();
			break;
		case R.id.dealNearByBtn:
			searchYourDealLayout.setVisibility(View.GONE);
			isFavoriteList = false;
			hideDataNotFound();
			topDeals.clear();
			mapLayout.setVisibility(View.VISIBLE);
			dealsList.setVisibility(View.GONE);
			dealViewListBtn.setVisibility(View.VISIBLE);
			dealStoresBtn.setVisibility(View.VISIBLE);
			dealMallsBtn.setVisibility(View.VISIBLE);
			changeBackground(dealNearByBtn);
//			checkForListOrMap();
			break;
		case R.id.dealPopularBtn:
			isFavoriteList = false;
			isOnlineList = false;
			isPopularList = true;
			getPopularDeals();
			break;
		case R.id.dealFavoriteBtn:
			isFavoriteList = true;
			isOnlineList = false;
			getFavoriteDeals();
			break;
		case R.id.dealOnlineBtn:
			isFavoriteList = false;
			isPopularList = false;
			isOnlineList = true;
			getOnlineDeals();
			break;
		case R.id.dealStoresBtn:
			isFavoriteList = true;
			dealStoresBtn.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
			dealMallsBtn.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			break;
		case R.id.dealMallsBtn:
			isFavoriteList = true;
			dealStoresBtn.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
			dealMallsBtn.setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
			break;
		default:
			break;
		}
	}
	
	
	public void getNearByStores() {
		mProgressBarDialog=new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		GetNearByDealsApi api=new GetNearByDealsApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				NearByDealsRes dealsRes=(NearByDealsRes) object;
				List<NearByDeal> nearByDeals = dealsRes.getData();
				if (nearByDeals!=null && nearByDeals.size()>0) {
					addMarkers(nearByDeals);
					dealsList.setAdapter(new StoreListAdapter(mContext, nearByDeals));
				}
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				NearByDealsRes dealsRes=(NearByDealsRes) object;
				AlertUtils.showToast(mContext, dealsRes.getMessage());
			}
		});
		if(latitude!=0&&longitude!=0) {
			api.getNearByDeals(UserPreference.getInstance().getUserID(), Double.toString(latitude), Double.toString(longitude), "2");
			api.execute();
		}else if(count<=5) {
			mProgressBarDialog.dismiss();
			getLocation();
		}else{
			mProgressBarDialog.dismiss();
			AlertUtils.showToast(mContext,"No offers found for your location. Please try again later");
		}
	}
	
	private void getPopularDeals() {
		searchYourDealLayout.setVisibility(View.VISIBLE);
		topDeals.clear();
		dealsList.setAdapter(null);
		if (checkInternet()) {
			new GetPopularOffers().execute();
		}
		dealsList.setVisibility(View.VISIBLE);
		mapLayout.setVisibility(View.GONE);
		dealViewListBtn.setVisibility(View.GONE);
		dealsList.setVisibility(View.VISIBLE);
		dealStoresBtn.setVisibility(View.GONE);
		dealMallsBtn.setVisibility(View.GONE);
		changeBackground(dealPopularBtn);
	}

	private void getOnlineDeals() {
		searchYourDealLayout.setVisibility(View.VISIBLE);
		topDeals.clear();
		dealsList.setAdapter(null);
		if (checkInternet()) {
			new GetOnlineOffers().execute();
		}
		dealStoresBtn.setVisibility(View.GONE);
		dealMallsBtn.setVisibility(View.GONE);
		dealsList.setVisibility(View.VISIBLE);
		mapLayout.setVisibility(View.GONE);
		dealViewListBtn.setVisibility(View.GONE);
		changeBackground(dealOnlineBtn);
	}

	private void getFavoriteDeals() {
		searchYourDealLayout.setVisibility(View.GONE);
		topDeals.clear();
		dealsList.setAdapter(null);
		FavoriteDealsDAO dao = new FavoriteDealsDAO(DatabaseHelper.getDatabase());
		topDeals = dao.getList();
		setAdapter();
		dealsList.setVisibility(View.VISIBLE);
		mapLayout.setVisibility(View.GONE);
		dealViewListBtn.setVisibility(View.GONE);
		dealStoresBtn.setVisibility(View.GONE);
		dealMallsBtn.setVisibility(View.GONE);
		changeBackground(dealFavoriteBtn);
	}
	

	public void refreshList(){
		if (isFavoriteList) 
			getFavoriteDeals();
		else if (isPopularList) {
			getPopularDeals();
		} else if (isOnlineList) {
			getOnlineDeals();
		} else{

		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			refreshList();
		}
	}
	
	private void checkForListOrMap() {
		if (dealViewListBtn.getText().toString().equals(getResources().getString(R.string.view_map_text))) {
			dealsList.setVisibility(View.GONE);
			mapLayout.setVisibility(View.VISIBLE);
			dealViewListBtn.setText(R.string.view_list_text);
		} else if (dealViewListBtn.getText().equals(getResources().getString(R.string.view_list_text))) {
			dealsList.setVisibility(View.VISIBLE);
			mapLayout.setVisibility(View.GONE);
			dealViewListBtn.setText(R.string.view_map_text);
		}
	}

	private void changeBackground(Button button){
		for (int i = 0; i < btnsTop.size(); i++) {
			if (btnsTop.get(i)==button) 
				btnsTop.get(i).setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_active));
			else
				btnsTop.get(i).setBackground(getResources().getDrawable(R.drawable.ic_interest_button_bg_inactive));
		}
	}
	
	@Override
	public void onDestroyView() 
	     {
	        super.onDestroyView(); 
	        Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
	        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			 if(fragment!=null)
	      	  ft.remove(fragment);
	        ft.commit();
	    }

	@Override
	public boolean onMarkerClick(Marker marker) {
//		addFragment(new FragmentStoreDeals(marker.getTitle(),markersMap.get(marker)));
		return false;
	}
	
	public void getAccessTocken() {
		mProgressBarDialog=new ProgressBarDialog(mContext);
		mProgressBarDialog.show();
		
		TokenApi api=new TokenApi(new ServiceCallback() {
			
			@Override
			public void handleOnSuccess(Object object) {
				mProgressBarDialog.dismiss();
				DealToken token=(DealToken) object;
				String accessTocken=token.getAccess_token();
				Syso.info("Token:"+accessTocken);
			}
			
			@Override
			public void handleOnFailure(ServiceException exception, Object object) {
				mProgressBarDialog.dismiss();
				DealToken token=(DealToken) object;
				AlertUtils.showToast(mContext, token.getError_description());
			}
		});
		api.getTocken();
		api.execute();
	}

	private class searchDeals extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			mProgressBarDialog = new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			return new DealsApi().getDealsByCategory(UserPreference.getInstance().getUserID(),searchYourDealEditText.getSelectedItem().toString().replace(" ", "%20"));
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressBarDialog.dismiss();
			super.onPostExecute(result);
			Syso.info("result: "+result);
			if (result!=null&&result.length()>0) {
				parseData(result);
			}
			
		}
	}
	
	private class GetPopularOffers extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			mProgressBarDialog = new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			return new DealsApi().getPopularOffers(UserPreference.getInstance().getUserID(),latlng);
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressBarDialog.dismiss();
			super.onPostExecute(result);
			Syso.info("result: "+result);
			if (result!=null&&result.length()>0) {
				searchYourDealEditText.setSelection(0);
				parseData(result);
			}
		}
	}

	private class GetOnlineOffers extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			mProgressBarDialog = new ProgressBarDialog(mContext);
			mProgressBarDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... params) {
			return new DealsApi().getOnlineOffers(UserPreference.getInstance().getUserID(),latlng);
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressBarDialog.dismiss();
			super.onPostExecute(result);
			Syso.info("result: "+result);
			searchYourDealEditText.setSelection(0);
			if (result!=null&&result.length()>0) {
				parseData(result);
			}
		}
	}
	
	public void parseData(String xml) {
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
			deal.setLatlng(getLatLng(xmlParser.getValue(e, LINK)));
			topDeals.add(deal);
		}
		setAdapter();
	}

	private void setAdapter() {
		if (topDeals.size()>0){
			hideDataNotFound();
			dealsList.setAdapter(new DealListAdapter(mContext, topDeals,isFavoriteList));
		}
		else
			showDataNotFound();
	}


	private String getLatLng(String link) {
		String[] finallatlng = null;
		if (!TextUtils.isEmpty(link) && link.contains("&locn=")) {
			String[] latlng = link.split("&locn=");
			if(latlng.length>1)
			finallatlng = latlng[1].split("&");
			else
			finallatlng = new String[]{""};
		}else
			finallatlng = new String[]{""};
		Syso.info("finallatlng[0]:   "+finallatlng[0]);
		return finallatlng[0];
	}

	// An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
 
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(mContext);
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        @Override
        protected void onPostExecute(List<Address> addresses) {
 
            if(addresses==null || addresses.size()==0){
                AlertUtils.showToast(mContext, "No Location found");
            }
 
            // Clears all the existing markers on the map
            map.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
              String addressText = String.format("%s, %s",
              address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
              address.getCountryName());
              marker = map.addMarker(new MarkerOptions()
					  .position(latLng)
					  .title(addressText));
                // Locate the first location
                if(i==0)
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }
    
    
  
    
}
