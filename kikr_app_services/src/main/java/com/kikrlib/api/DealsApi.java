package com.kikrlib.api;

import com.kikrlib.service.XMLParser;
import com.kikrlib.utils.Syso;

public class DealsApi{

	private String api_key="ice790tk378sz158";
	private String aff_id="201505087108";

	public String getPopularOffers(String user_id,String latlng) {
		String URL = "http://api.spotzot.com/api/rss/offers/topoffers/?api_key=" +api_key+
				"&aff_id="+aff_id+"&uid="+user_id+"&locn="+latlng;
		Syso.info("url:   "+URL);
		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromHttpGetUrl(URL);
		return xml;
	}
	
	public String getOnlineOffers(String user_id,String latlng) {
		String URL = "http://api.spotzot.com/api/rss/offers/topoffers/?api_key=" +api_key+
				"&aff_id="+aff_id+"&uid="+user_id+"&locn="+latlng+"&online=y";
		Syso.info("url:   "+URL);
		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromHttpGetUrl(URL);
		return xml;
	}
	
	public String getDealsByCategory(String user_id,String text) {
		String URL = "http://www.spotzot.com/feeds/categ/"+text+"?api_key=" +api_key+
				"&aff_id="+aff_id+"&uid="+user_id;
		Syso.info("url:   "+URL);
		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromHttpGetUrl(URL);
		return xml;
	}
	
	public String getOnlineDealsByStore(String user_id,String storename) {
		String URL = "http://www.spotzot.com/feeds/store/"+storename+"?api_key=" +api_key+
				"&aff_id="+aff_id+"&uid="+user_id+"&online=y";
		Syso.info("url:   "+URL);
		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromHttpGetUrl(URL);
		return xml;
	}
	
	public String getInStoreDealsByStore(String user_id,String storename) {
		String URL = "http://www.spotzot.com/feeds/store/"+storename+"?api_key=" +api_key+
				"&aff_id="+aff_id+"&uid="+user_id+"&instore=y";
		Syso.info("url:   "+URL);
		XMLParser parser = new XMLParser();
		String xml = parser.getXmlFromHttpGetUrl(URL);
		return xml;
	}


	
}
