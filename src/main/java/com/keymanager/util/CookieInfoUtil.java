package com.keymanager.util;

import java.util.ArrayList;
import java.util.List;

import com.keymanager.value.CookieInfoVO;

public class CookieInfoUtil {
	public static List<CookieInfoVO> toVO(String ip, String cookieInfos){
		if(!Utils.isNullOrEmpty(cookieInfos)){
			String[] cookieArray = cookieInfos.split(Constants.ROW_SPLITTOR);
			List<CookieInfoVO> cookieInfoVOs = new ArrayList<CookieInfoVO>();
			for(String cookie : cookieArray){
				if(!Utils.isNullOrEmpty(cookie)){
					String [] cookieElementArray = cookie.split(Constants.COLUMN_SPLITTOR);
					CookieInfoVO cookieInfoVO = new CookieInfoVO();
					cookieInfoVO.setIp(ip);
					cookieInfoVO.setDomain(cookieElementArray[0]);
					cookieInfoVO.setKey(cookieElementArray[1]);
					cookieInfoVO.setValue(cookieElementArray[2]);
					cookieInfoVOs.add(cookieInfoVO);
				}
			}
			return cookieInfoVOs;
		}
		return null;
	}
	
	public static String toStr(List<CookieInfoVO> cookieInfoVOs){
		StringBuilder sb = new StringBuilder();
		if(!Utils.isEmpty(cookieInfoVOs)){
			int i = 0;
			for(CookieInfoVO cookieInfoVO : cookieInfoVOs){
				if(i > 0){
					sb.append(Constants.ROW_SPLITTOR);
				}
				sb.append(cookieInfoVO.toStr());
				i++;
			}
		}
		return sb.toString();
	}
}
