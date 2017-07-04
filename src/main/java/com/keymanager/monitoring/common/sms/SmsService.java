package com.keymanager.monitoring.common.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
;


public class SmsService {
	
	private String accName;
	
	private String accPwd;
	
	public String sendSms(String mobies, String content){
		StringBuffer sb = new StringBuffer("http://www.lx198.com/sdk/send?");
		try {
			sb.append("&accName="+accName); 
			sb.append("&accPwd="+MD5.getMd5String(accPwd)); 
			sb.append("&aimcodes="+mobies);
			sb.append("&content="+URLEncoder.encode(content+"【顺时科技】","UTF-8"));
			sb.append("&bizId="+BizNumberUtil.createBizId());
			sb.append("&dataType=string");
			URL url = new URL(sb.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			return in.readLine();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccPwd() {
		return accPwd;
	}

	public void setAccPwd(String accPwd) {
		this.accPwd = accPwd;
	}
}
