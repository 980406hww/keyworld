package com.keymanager.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SogouIndexAndPositionHelper extends IndexAndPositionHelper{
	private final String searchEngineURL = "http://www.sogou.com/web?query=%s&page=1&num=50";
	final int urlLength = 50;
	
	@Override
	public int getPosition(String keyword, String targetURL) {
		String url = String.format(getSearchEngineURL(), URLEncoder.encode(keyword));
		String pageContent = getPageContent(url);
		String[] contentElements = pageContent.split(targetURL);
		if (contentElements.length >= 2) {
			int position = 0;
			String indexPattern = "<div id=\"rb_[0-9]{1,2}\" class=\"rb\">";
			Pattern pat = Pattern.compile(indexPattern);  
			Matcher mat = pat.matcher(contentElements[0]);
			List<String> indexList = new ArrayList<String>();
			String val;
			while (mat.find()){  
	      val = mat.group();
	      val = val.replace("<div id=\"rb_", "").replace("\" class=\"rb\">", "");
	      indexList.add(val);  
	    }
			if (indexList.size() > 0){
				return Integer.parseInt(indexList.get(indexList.size() - 1)) + 1;
			}
			return position;
		}
		return NOT_FOUND;
	}

	@Override
	String getSearchEngineURL() {
		return searchEngineURL;
	}

	@Override
	int getURLLength() {
		return urlLength;
	}
}
