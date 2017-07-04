package com.keymanager.util;

import java.net.URLEncoder;


public class SoIndexAndPositionHelper extends IndexAndPositionHelper {
	private final String searchEngineURL = "http://www.so.com/s?q=%s&pn=%d";
	int urlLength = 24;
	
	@Override
	public int getPosition(String keyword, String targetURL) {
		int totalRecordCount = 0;
		for(int i = 1; i < 4; i++){
			String url = String.format(getSearchEngineURL(), URLEncoder.encode(keyword), i);
			String pageContent = getPageContent(url);
			String[] contentElements = pageContent.split(targetURL);
			if (contentElements.length >= 2) {
				String []eachElements = contentElements[0].split("class=\"res-list\">");
				return (totalRecordCount + eachElements.length - 1);
			}else{
				String []eachElements = pageContent.split("class=\"res-list\">");
				totalRecordCount = totalRecordCount + (eachElements.length - 1);
			}
		}
		return NOT_FOUND;
	}

	@Override
	String getSearchEngineURL() {
		return searchEngineURL;
	}

	@Override
	int getURLLength() {
		// TODO Auto-generated method stub
		return 0;
	}
}
