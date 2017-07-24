package com.keymanager.value;

import com.keymanager.util.Constants;
import com.keymanager.util.Utils;

public abstract class CustomerKeywordCommonPartVO {
	private String keyword;
	private String url;
	private String phoneUrl;
	private String originalUrl;
	private String originalPhoneUrl;
	
	private int currentPosition;
	private int jisuCurrentPosition;
	private int chupingCurrentPosition;
	
	private double positionFirstFee;
	private double positionSecondFee;
	private double positionThirdFee;
	private double positionForthFee;
	private double positionFifthFee;
	private double positionFirstPageFee;

	private double jisuPositionFirstFee;
	private double jisuPositionSecondFee;
	private double jisuPositionThirdFee;
	private double jisuPositionForthFee;
	private double jisuPositionFifthFee;
	private double jisuPositionFirstPageFee;

	private double chupingPositionFirstFee;
	private double chupingPositionSecondFee;
	private double chupingPositionThirdFee;
	private double chupingPositionForthFee;
	private double chupingPositionFifthFee;
	private double chupingPositionFirstPageFee;
	private int status;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPhoneUrl() {
		return phoneUrl;
	}

	public void setPhoneUrl(String phoneUrl) {
		this.phoneUrl = phoneUrl;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getOriginalPhoneUrl() {
		return originalPhoneUrl;
	}

	public void setOriginalPhoneUrl(String originalPhoneUrl) {
		this.originalPhoneUrl = originalPhoneUrl;
	}

	public double getPositionFirstFee() {
		return positionFirstFee;
	}

	public void setPositionFirstFee(double positionFirstFee) {
		this.positionFirstFee = positionFirstFee;
	}

	public double getPositionSecondFee() {
		return positionSecondFee;
	}

	public void setPositionSecondFee(double positionSecondFee) {
		this.positionSecondFee = positionSecondFee;
	}

	public double getPositionThirdFee() {
		return positionThirdFee;
	}

	public void setPositionThirdFee(double positionThirdFee) {
		this.positionThirdFee = positionThirdFee;
	}

	public double getPositionForthFee() {
		return positionForthFee;
	}

	public void setPositionForthFee(double positionForthFee) {
		this.positionForthFee = positionForthFee;
	}

	public double getPositionFifthFee() {
		return positionFifthFee;
	}

	public void setPositionFifthFee(double positionFifthFee) {
		this.positionFifthFee = positionFifthFee;
	}

	public double getPositionFirstPageFee() {
		return positionFirstPageFee;
	}

	public void setPositionFirstPageFee(double positionFirstPageFee) {
		this.positionFirstPageFee = positionFirstPageFee;
	}

	public double getJisuPositionFirstFee() {
		return jisuPositionFirstFee;
	}

	public void setJisuPositionFirstFee(double jisuPositionFirstFee) {
		this.jisuPositionFirstFee = jisuPositionFirstFee;
	}

	public double getJisuPositionSecondFee() {
		return jisuPositionSecondFee;
	}

	public void setJisuPositionSecondFee(double jisuPositionSecondFee) {
		this.jisuPositionSecondFee = jisuPositionSecondFee;
	}

	public double getJisuPositionThirdFee() {
		return jisuPositionThirdFee;
	}

	public void setJisuPositionThirdFee(double jisuPositionThirdFee) {
		this.jisuPositionThirdFee = jisuPositionThirdFee;
	}

	public double getJisuPositionForthFee() {
		return jisuPositionForthFee;
	}

	public void setJisuPositionForthFee(double jisuPositionForthFee) {
		this.jisuPositionForthFee = jisuPositionForthFee;
	}

	public double getJisuPositionFifthFee() {
		return jisuPositionFifthFee;
	}

	public void setJisuPositionFifthFee(double jisuPositionFifthFee) {
		this.jisuPositionFifthFee = jisuPositionFifthFee;
	}

	public double getJisuPositionFirstPageFee() {
		return jisuPositionFirstPageFee;
	}

	public void setJisuPositionFirstPageFee(double jisuPositionFirstPageFee) {
		this.jisuPositionFirstPageFee = jisuPositionFirstPageFee;
	}

	public double getChupingPositionFirstFee() {
		return chupingPositionFirstFee;
	}

	public void setChupingPositionFirstFee(double chupingPositionFirstFee) {
		this.chupingPositionFirstFee = chupingPositionFirstFee;
	}

	public double getChupingPositionSecondFee() {
		return chupingPositionSecondFee;
	}

	public void setChupingPositionSecondFee(double chupingPositionSecondFee) {
		this.chupingPositionSecondFee = chupingPositionSecondFee;
	}

	public double getChupingPositionThirdFee() {
		return chupingPositionThirdFee;
	}

	public void setChupingPositionThirdFee(double chupingPositionThirdFee) {
		this.chupingPositionThirdFee = chupingPositionThirdFee;
	}

	public double getChupingPositionForthFee() {
		return chupingPositionForthFee;
	}

	public void setChupingPositionForthFee(double chupingPositionForthFee) {
		this.chupingPositionForthFee = chupingPositionForthFee;
	}

	public double getChupingPositionFifthFee() {
		return chupingPositionFifthFee;
	}

	public void setChupingPositionFifthFee(double chupingPositionFifthFee) {
		this.chupingPositionFifthFee = chupingPositionFifthFee;
	}

	public double getChupingPositionFirstPageFee() {
		return chupingPositionFirstPageFee;
	}

	public void setChupingPositionFirstPageFee(double chupingPositionFirstPageFee) {
		this.chupingPositionFirstPageFee = chupingPositionFirstPageFee;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getPcFee(int pcCurrentPosition) {
		double pcFee = 0;
		if (!Utils.isNullOrEmpty(getUrl()) && pcCurrentPosition > 0) {
			switch (pcCurrentPosition) {
			case 1:
				pcFee = this.getPositionFirstFee();
				break;
			case 2:
				pcFee = this.getPositionSecondFee();
				break;
			case 3:
				pcFee = this.getPositionThirdFee();
				break;
			case 4:
				pcFee = this.getPositionForthFee();
				break;
			case 5:
				pcFee = this.getPositionFifthFee();
				break;
			default:
				if (pcCurrentPosition <= 10) {
					pcFee = this.getPositionFirstPageFee();
				} else {
					pcFee = 0;
				}
				break;
			}
		}
		return pcFee;
	}

	public double getJisuFee(int jisuCurrentPosition) {
		double jisuFee = 0;
		if (!Utils.isNullOrEmpty(getPhoneUrl()) && jisuCurrentPosition > 0) {
			switch (jisuCurrentPosition) {
			case 1:
				jisuFee = this.getJisuPositionFirstFee();
				break;
			case 2:
				jisuFee = this.getJisuPositionSecondFee();
				break;
			case 3:
				jisuFee = this.getJisuPositionThirdFee();
				break;
			case 4:
				jisuFee = this.getJisuPositionForthFee();
				break;
			case 5:
				jisuFee = this.getJisuPositionFifthFee();
				break;
			default:
				if (jisuCurrentPosition <= 10) {
					jisuFee = this.getJisuPositionFirstPageFee();
				} else {
					jisuFee = 0;
				}
				break;
			}
		}
		return jisuFee;
	}

	public double getChupingFee(int chupingCurrentPosition) {
		double chupingFee = 0;
		if (!Utils.isNullOrEmpty(getPhoneUrl()) && chupingCurrentPosition > 0) {
			switch (chupingCurrentPosition) {
			case 1:
				chupingFee = this.getChupingPositionFirstFee();
				break;
			case 2:
				chupingFee = this.getChupingPositionSecondFee();
				break;
			case 3:
				chupingFee = this.getChupingPositionThirdFee();
				break;
			case 4:
				chupingFee = this.getChupingPositionForthFee();
				break;
			case 5:
				chupingFee = this.getChupingPositionFifthFee();
				break;
			default:
				if (chupingCurrentPosition <= 10) {
					chupingFee = this.getChupingPositionFirstPageFee();
				} else {
					chupingFee = 0;
				}
				break;
			}
		}
		return chupingFee;
	}
	
	public abstract double getJisuFee();
	public abstract double getPcFee();
	public abstract double getChupingFee();

	public String getPcFeeString() {
		double pcFee = getPcFee();
		return pcFee > 0 ? Utils.removeDoubleZeros(pcFee) : "";
	}

	public String getJisuFeeString() {
		double jisuFee = getJisuFee();
		return jisuFee > 0 ? Utils.removeDoubleZeros(jisuFee) : "";
	}

	public String getChupingFeeString() {
		double chupingFee = getChupingFee();
		return chupingFee > 0 ? Utils.removeDoubleZeros(chupingFee) : "";
	}

	public double getSubTotal() {
		return getChupingFee() + getJisuFee() + getPcFee();
	}
	
	public double getOnlyOneShoujiFee(){
		return getChupingFee() > 0 ? getChupingFee() : getJisuFee();
	}
	
	public double getSubTotal(String collectMethod) {
		if(Constants.COLLECT_METHOD_ALL.equals(collectMethod)){
			return getChupingFee() + getJisuFee() + getPcFee();
		}else if(Constants.COLLECT_METHOD_JUSTPC.equals(collectMethod)){
			return getPcFee() > 0 ? getPcFee() : getTotalShoujiFee();
		}else if(Constants.COLLECT_METHOD_JUSTSHOUJI.equals(collectMethod)){
			return (getTotalShoujiFee() > 0) ? getTotalShoujiFee() : getPcFee();
		}else if(Constants.COLLECT_METHOD_BOTHPC_AND_SHOUJI_JUSTONE.equals(collectMethod)){
			return (getPcFee() + getOnlyOneShoujiFee());
		}
		return 0;
	}

	private double getTotalShoujiFee() {
		return getChupingFee() + getJisuFee();
	}

	public String getSubTotalString() {
		double subTotal = getSubTotal();
		return subTotal > 0 ? Utils.removeDoubleZeros(subTotal) : "";
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public int getJisuCurrentPosition() {
		return jisuCurrentPosition;
	}

	public void setJisuCurrentPosition(int jisuCurrentPosition) {
		this.jisuCurrentPosition = jisuCurrentPosition;
	}

	public int getChupingCurrentPosition() {
		return chupingCurrentPosition;
	}

	public void setChupingCurrentPosition(int chupingCurrentPosition) {
		this.chupingCurrentPosition = chupingCurrentPosition;
	}
}