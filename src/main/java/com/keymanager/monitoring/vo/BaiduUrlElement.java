package com.keymanager.monitoring.vo;

public class BaiduUrlElement{
	private int id;
	private String baiduUrl;
	private String realUrl;

	public BaiduUrlElement(){
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBaiduUrl() {
		return baiduUrl;
	}

	public void setBaiduUrl(String baiduUrl) {
		this.baiduUrl = baiduUrl;
	}

	public String getRealUrl() {
		return realUrl;
	}

	public void setRealUrl(String realUrl) {
		this.realUrl = realUrl;
	}
}

