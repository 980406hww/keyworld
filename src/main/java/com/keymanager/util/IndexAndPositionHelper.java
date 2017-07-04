package com.keymanager.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.keymanager.db.DBUtil;

public abstract class IndexAndPositionHelper {
	protected static int NOT_FOUND = 0;
	private final String chinazIndexURL = "http://tool.chinaz.com/baidu/words.aspx";
	private static Map<String, IndexAndPositionHelper> searchEngineMap = new HashMap<String, IndexAndPositionHelper>();
	static{
		searchEngineMap.put("百度", new BaiduIndexAndPositionHelper());
		searchEngineMap.put("360", new SoIndexAndPositionHelper());
		searchEngineMap.put("搜狗", new SogouIndexAndPositionHelper());
	}
	
	public static void main(String[] args) {

//	System.out.println(getPosition("百度", "赌球信用网 ", "www.ihain.cn:8401/"));
	
	//System.out.println(getIndexCount("百度", "%E6%90%AC%E5%AE%B6"));
//	
//	System.out.println(getPosition("360", "深圳办证", "auto.qq.com/a/20120810/000089"));
//	
//	System.out.println(getIndexCount("360", "深圳办证"));
	
//	System.out.println(getPosition("搜狗", "深圳办证", "www.sxdaily.com.cn"));
//	
//	System.out.println(getIndexCount("搜狗", "深圳办证"));
		for(int type = 2; type < 12; type++){
			for(int i = 1; i < 20; i++){
				String[] contentPageUrls = getZangAoPageList("http://zangao.cndog.net/page/zangao-" + type + "-" + i + ".htm", "http://zangao.cndog.net");
				if(contentPageUrls != null){
					for(String contentPageUrl : contentPageUrls){
						if(contentPageUrl != null){
							String [] pageParts = getZangAoPageContent(contentPageUrl);
							putIntoDB(pageParts);
						}
					}
				}
			}
		}
	}

	private static String[] getZangAoPageList(String url, String baseURL) {
		String pageContent = getPageContent(url);
		if (!Utils.isNullOrEmpty(pageContent)) {
			System.out.println(pageContent);
			String[] pageContentParts = pageContent
					.split("<div id=\"article_content\"><dl>");
			if (pageContentParts.length == 2) {
				String[] titleParts = pageContentParts[1]
						.split("<dd class=\"article_content_nextPage\">");
				if (titleParts.length == 2) {
					String pageContent2 = titleParts[0];
					pageContent2 = pageContent2.replace("								   ", "");
					System.out.println("==============================="
							+ pageContent2);
					String[] pageContent3 = pageContent2
							.split(" target=_blank");
					String[] results = new String[pageContent3.length];
					int i = 0;
					for (String highLink : pageContent3) {
						String[] urls = highLink.split("href=");
						if (urls.length == 2) {
							String foundUrl = urls[1];
							if (foundUrl.startsWith("http")) {
								results[i++] = foundUrl;
								System.out.println(foundUrl);
							} else {
								results[i++] = baseURL + foundUrl;
							}
							System.out.println(results[i - 1]);
						}
					}
					return results;
				}
			}
		}
		return null;
	}
	
	private static String[] getZangAoPageContent(String url) {
		String pageContent = getPageContent(url);
		// System.out.println(pageContent);
		if (!Utils.isNullOrEmpty(pageContent)) {
			String[] pageContentParts = pageContent.split("<h1>");
			if (pageContentParts.length == 2) {
				String[] titleParts = pageContentParts[1].split("</h1>");
				if (titleParts.length == 2) {
					String title = titleParts[0];
					System.out.println("==============================="
							+ title);
					String articles[] = titleParts[1]
							.split("<dd><div class=\"google\">");
					if (articles.length == 2) {
						articles[1] = articles[1].replace("</dd> <dd>",
								"</dd><dd>");
						articles[1] = articles[1].replace("</dd>  <dd>",
								"</dd><dd>");
						articles[1] = articles[1].replace(
								"</script></div>	<P>", "</script></div><P>");
						articles[1] = articles[1].replace(
								"</script> </div>	<P>", "</script></div><P>");
						articles[1] = articles[1].replace(
								"</script> </div><P>", "</script></div><P>");

						String[] articleParts = articles[1].split("</dd><dd>");
						String[] pageParts = new String[2];
						pageParts[0] = title;
						pageParts[1] = articleParts[0].split("</script></div>")[1];
						pageParts[1] = pageParts[1].replaceAll(
								"zangao.cndog.net", "www.51yza.com");
						System.out
								.println("======================================================");
						System.out.println(pageParts[1]);
						return pageParts;
					}
				}
			}
		}
		return null;
	}
	
	public static void putIntoDB(String[] articles) {
		try {
			String url = "jdbc:mysql://42.51.154.82:28136/db_resource";
			String user = "root";
			String pwd = "mancy**do$2";

			// 加载驱动，这一句也可写为：Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// 建立到MySQL的连接
			Connection conn = DriverManager.getConnection(url, user, pwd);

			PreparedStatement ps = null;
			String preSql;
			try {
				preSql = "INSERT INTO t_blog_content(fTitle, fContent, fGroup, fUpdateTime, fCreateTime) VALUES(?, ?, '51yza', NOW(), NOW())";
				ps = conn.prepareStatement(preSql);
				int i = 1;
				ps.setString(i++, articles[0]);
				ps.setString(i++, articles[1]);
				ps.executeUpdate();
			} finally {
				DBUtil.closePreparedStatement(ps);
			}
			conn.close();
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}
	}
	
	public static IndexAndPositionHelper getInstance(String searchEngine){
		return searchEngineMap.get(searchEngine);
	}
	
	public static int getPosition(String searchEngine, String keyword, String targetURL){
		System.out.println("Keyword is :" + keyword);
		System.out.println("searchEngine is :" + searchEngine);
		IndexAndPositionHelper indexAndPositionHelper = searchEngineMap.get(searchEngine);
		if (indexAndPositionHelper != null && !Utils.isNullOrEmpty(targetURL)){
			return indexAndPositionHelper.getPosition(keyword, targetURL);
		}
		return NOT_FOUND;
	}
	
	public static int getIndexCount(String searchEngine, String keyword){
		System.out.println("Index Keyword is :" + keyword);
		System.out.println("Index searchEngine is :" + searchEngine);
		IndexAndPositionHelper indexAndPositionHelper = searchEngineMap.get(searchEngine);
		if (indexAndPositionHelper != null){
			return indexAndPositionHelper.getSearchEngineIndexCount(keyword);
		}
		return NOT_FOUND;
	}
	
	protected String getIndexURL(){
		return chinazIndexURL;
	}

	abstract String getSearchEngineURL();

	abstract int getURLLength();
	
	public String targetURLSubstring(String targetURL){
		String tmpTargetURL = targetURL;
		if (targetURL.length() > getURLLength()){
			tmpTargetURL = tmpTargetURL.substring(0, getURLLength());
		}
		return tmpTargetURL;
	}
	
	protected static String getPageContent(String originlURL) {
		InputStream is = null;
		BufferedReader br = null;
		try {
			System.out.println("originlURL is :" + originlURL);
			URL url = new URL(originlURL);
			is = url.openStream(); // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			StringBuilder contentBuilder = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				contentBuilder.append(line);
			}
			return contentBuilder.toString();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException ignore) {
			}
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ignore) {
			}
		}
		return "";
	}

	protected String http(String url, Map<String, String> params) {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
			}
			sb.substring(0, sb.length() - 1);
		}
		// System.out.println("send_url:" + url);
		// System.out.println("send_data:" + sb.toString());

		// 尝试发送请求

		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}

		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	public int getSearchEngineIndexCount(String keyword) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("kw", keyword);
		String pageContent = http(getIndexURL(), parameters);
//		System.out.println(pageContent);
		String[] contentElements = pageContent.split("<span ><em>" + keyword + "</em></span>");
		if (contentElements.length > 1) {
			String[] subContentElements = contentElements[1]
					.split("<input  isget=\"false\" type=\"submit\" class=\"butcha\" value=\"查询\" />");
			String indexPattern = "target=\"_blank\">[0-9]{1,10}</a>";
			Pattern pat = Pattern.compile(indexPattern);
			Matcher mat = pat.matcher(subContentElements[0]);
			List<String> indexList = new ArrayList<String>();
			String val;
			while (mat.find()) {
				val = mat.group();
				val = val.replace("target=\"_blank\">", "").replace("</a>", "");
				indexList.add(val);
			}
			if (indexList.size() == 2) {
				return Integer.parseInt(indexList.get(0));
			}
		}
		return NOT_FOUND;
	}

	abstract public int getPosition(String keyword, String targetURL);
}