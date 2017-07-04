package com.keymanager.value;


public class VPSProviderInfoVO implements Comparable {
	private int uuid;
	private int id;
	private String clientIDPrefix;
	private String cityName;
	private String district;
	private int price;
	private int maxSequence;

	public String optionName(){
		return String.format("%s区-%d-价格:%d]", this.getDistrict(), this.getId(), this.getPrice());
	}

	public String categoryName(){
		return String.format("%s-%s", this.getClientIDPrefix(), this.getCityName());
	}

	public String format(){
		return String.format("%s(%s)", this.categoryName(), this.optionName());
	}

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientIDPrefix() {
		return clientIDPrefix;
	}

	public void setClientIDPrefix(String clientIDPrefix) {
		this.clientIDPrefix = clientIDPrefix;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getMaxSequence() {
		return maxSequence;
	}

	public void setMaxSequence(int maxSequence) {
		this.maxSequence = maxSequence;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public int compareTo(Object o) {
		int result = this.getClientIDPrefix().compareTo(((VPSProviderInfoVO)o).getClientIDPrefix());
		if(result == 0){
			return this.getCityName().compareTo(((VPSProviderInfoVO)o).getCityName());
		}else{
			return result;
		}
	}
}
