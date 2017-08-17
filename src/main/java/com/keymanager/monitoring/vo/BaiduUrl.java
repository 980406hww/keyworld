package com.keymanager.monitoring.vo;

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

