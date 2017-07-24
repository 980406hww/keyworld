package com.keymanager.value;

import com.keymanager.util.Utils;

import java.sql.Timestamp;
import java.util.List;

public class BaiduUrl{
	List<BaiduUrlElement> baiduUrlElementList;
	public BaiduUrl(){
		super();
	}

	public List<BaiduUrlElement> getBaiduUrlElementList() {
		return baiduUrlElementList;
	}

	public void setBaiduUrlElementList(List<BaiduUrlElement> baiduUrlElementList) {
		this.baiduUrlElementList = baiduUrlElementList;
	}
}

