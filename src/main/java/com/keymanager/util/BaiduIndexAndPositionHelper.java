package com.keymanager.util;

import java.net.URLEncoder;



public class BaiduIndexAndPositionHelper extends IndexAndPositionHelper {
	 final String searchEngineURL = "http://www.baidu.com/s?wd=%s&pn=%d";
	 final int urlLength = 21;

	@Override
	public int getPosition(String keyword, String targetURL) {
		for (int i = 0; i < 4; i++) {
			System.out.println("Keyword before :" + keyword);
			String url = String.format(getSearchEngineURL(), URLEncoder.encode(keyword), (i * 10));
			System.out.println("URL starting:" + url);
			String pageContent = getPageContent(url);
//			System.out.println(pageContent);
			String tmpTargetURL = this.targetURLSubstring(targetURL);
			String[] contentElements = pageContent.split(tmpTargetURL);
			
			if (contentElements.length >= 2) {
				int position = 0;
				String[] subContentElements = contentElements[0].split("<div class=\"result c-container \" id=\"");
				String subStr = subContentElements[subContentElements.length - 1].substring(0, 2);

				String index = subStr.replace("\"", "");
				if (index != null && index.trim() != "") {
					try {
						position = Integer.parseInt(index.trim());
					} catch (Exception ex) {

					}
				}
				return position;
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
		return urlLength;
	}
}
