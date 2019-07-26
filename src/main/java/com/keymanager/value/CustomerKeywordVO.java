package com.keymanager.value;

import java.sql.Timestamp;

import com.keymanager.enums.CollectMethod;
import com.keymanager.util.Constants;
import com.keymanager.util.IndexAndPositionHelper;
import com.keymanager.util.Utils;

public class CustomerKeywordVO {
	private int uuid;
	private String terminalType;
	private int customerUuid;
	private String contactPerson;
	private String keyword;
	private String url;
	private String originalUrl;
	private String title;
	private String type;
	private String snapshotDateTime;
	private String searchEngine;
	private int initialPosition;
	private int initialIndexCount;
	private int currentIndexCount;
	private int currentPosition;
	
	private Timestamp queryTime;
	private Timestamp queryDate;
	private int queryCount;
	private int queryInterval;
	private int invalidRefreshCount;
	private String serviceProvider;
	private String optimizeGroupName;
	private int optimizePlanCount;
	private int sequence;
	private Timestamp optimizeDate;
	private String machineGroup;
	private int optimizePositionFirstPercentage;
	private int optimizePositionSecondPercentage;
	private int optimizePositionThirdPercentage;
	
	private String relatedKeywords;
	private int optimizedCount;
	private double optimizedPercentage;
	
	private double positionFirstCost;
	private double positionSecondCost;
	private double positionThirdCost;
	private double positionForthCost;
	private double positionFifthCost;
	private double positionFirstFee;
	private double positionSecondFee;
	private double positionThirdFee;
	private double positionForthFee;
	private double positionFifthFee;
	private double positionFirstPageFee;
	
	private String collectMethod;
	private Timestamp startOptimizedTime;
	private Timestamp effectiveFromTime;
	private Timestamp effectiveToTime;

	private Timestamp autoUpdateNegativeTime;

	private Timestamp paymentEffectiveFromTime;
	private Timestamp paymentEffectiveToTime;
	
	private String orderNumber;
	private String paymentStatus;
	private Timestamp lastOptimizeDateTime;

	private String remarks;
	private String receiveStatus;
	private String captureCurrentKeywordStatus;
	private int status;
	private Timestamp updateTime;
	private Timestamp createTime;
	private Timestamp captureCurrentKeywordCountTime;
	private CustomerKeywordAccountLogVO latestCustomerKeywordAccountLog;

	public double getApplicableFee(int positionNumber, String type){
		return getApplicablePCFee(positionNumber);
	}
	
	public double getApplicablePCFee(int positionNumber){
		switch(positionNumber){
			case 1:
				return this.getPositionFirstFee();
			case 2:
				return this.getPositionSecondFee();
			case 3:
				return this.getPositionThirdFee();
			case 4:
				return this.getPositionForthFee();
			case 5:
				return this.getPositionFifthFee();
			default:
				if (positionNumber > 0 && positionNumber <= 10){
					return this.getPositionFirstPageFee();
				}
				return 0;
		}
	}
	
	public double getApplicableCost(int positionNumber){
		switch(positionNumber){
			case 1:
				return this.getPositionFirstCost();
			case 2:
				return this.getPositionSecondCost();
			case 3:
				return this.getPositionThirdCost();
			case 4:
				return this.getPositionForthCost();
			case 5:
				return this.getPositionFifthCost();
			default:
				return 0;
		}
	}

	public String getStatusName(){
		switch(this.getStatus()){
			case 1:
				return "激活";
			case 0:
				return "过期";
			case 2:
				return "新增";
			default:
				return "";
		}
	}
	
	public String getCollectMethodName(){
		return CollectMethod.findByCode(this.getCollectMethod()).getName();
	}
	
	public String searchEngineUrl(){
		return Constants.SEARCH_ENGINE_URL_MAP.get(this.getSearchEngine() + "_" + this.getTerminalType());
	}
	
	public int getPeriodDuration(){
		CollectMethod collectMethod = CollectMethod.findByCode(this.getCollectMethod());
		if (collectMethod != null){
			return collectMethod.getDuration();
		}
		return 0;
	}
	
	public boolean withinAvailableAccountRange(){
		if (this.getEffectiveFromTime() != null && this.getEffectiveToTime() != null 
				&& Utils.getCurrentTimestamp().after(this.getEffectiveFromTime()) 
				&& Utils.getCurrentTimestamp().before(this.getEffectiveToTime())){
			return true;
		}
		return false;
	}
	
	public boolean availableToAddNewAccountLog(String type){
		if (this.getEffectiveToTime() == null || 
				Utils.getCurrentTimestamp().after(this.getEffectiveToTime())){
			return true;
		}
		return false;
	}

	public boolean isUnpaid(){
		return Constants.CUSTOMER_KEYWORD_PAY_STATUS_UN_PAID.equals(this.getReceiveStatus());
	}
	
	public boolean isPushPay(){
		return Constants.CUSTOMER_KEYWORD_PAY_STATUS_PUSH_PAY.equals(this.getReceiveStatus());
	}
	
	public boolean isExpired(){
		return this.getEffectiveToTime() != null && Utils.getCurrentTimestamp().after(this.getEffectiveToTime());
	}
	
	public CustomerKeywordPaymentLogVO generatePaymentLog(){
		CustomerKeywordPaymentLogVO paymentLog = new CustomerKeywordPaymentLogVO();
		paymentLog.setCustomerKeywordUuid(this.getUuid());
		paymentLog.setEffectiveFromTime(this.getPaymentEffectiveToTime() != null ? this.getPaymentEffectiveToTime() : Utils.getCurrentTimestamp());
		paymentLog.setEffectiveToTime(Utils.addDay(paymentLog.getEffectiveFromTime(), this.getPeriodDuration()));
		paymentLog.setPayable(this.getApplicableCost(this.getCurrentPosition()));
		return paymentLog;
	}
	
	public CustomerKeywordAccountLogVO generateAccountLog(String type){
		CustomerKeywordAccountLogVO customerKeywordAccountLog = new CustomerKeywordAccountLogVO();
		Timestamp effectiveFromTime = this.getApplicableEffectiveToTime(type) != null ? this.getApplicableEffectiveToTime(type) : Utils.getCurrentTimestamp();
		Timestamp effectiveToTime = Utils.addDay(effectiveFromTime, this.getPeriodDuration());
		customerKeywordAccountLog.setCustomerKeywordUuid(getUuid());
		customerKeywordAccountLog.setMonth(Utils.formatDatetime(effectiveFromTime, "yyyy-MM"));
		customerKeywordAccountLog.setEffectiveFromTime(effectiveFromTime);
		customerKeywordAccountLog.setEffectiveToTime(effectiveToTime);
		customerKeywordAccountLog.setType(type);
		customerKeywordAccountLog.setReceivable(getApplicableFee(getCurrentPosition(), type));
		customerKeywordAccountLog.setStatus(Constants.ACCOUNT_LOG_STATUS_UN_PAID);
		return customerKeywordAccountLog;
	}
	
	public CustomerKeywordAccountLogVO generateAccountLog(int position, String type){
		CustomerKeywordAccountLogVO customerKeywordAccountLog = generateAccountLog(type);
		customerKeywordAccountLog.setReceivable(getApplicableFee(position, type));
		return customerKeywordAccountLog;
	}
	
	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public int getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(int customerUuid) {
		this.customerUuid = customerUuid;
	}

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

	public String getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}

	public int getInitialPosition() {
		return initialPosition;
	}

	public void setInitialPosition(int initialPosition) {
		this.initialPosition = initialPosition;
	}

	public int getInitialIndexCount() {
		return initialIndexCount;
	}

	public void setInitialIndexCount(int initialIndexCount) {
		this.initialIndexCount = initialIndexCount;
	}

	public int getCurrentIndexCount() {
		return currentIndexCount;
	}

	public int getApplicableCurrentIndexCount(String type){
		return this.getCurrentIndexCount();
	}
	
	public void setCurrentIndexCount(int currentIndexCount) {
		this.currentIndexCount = currentIndexCount;
	}
	
	public int getPercentage(int currentPosition){
		switch (currentPosition/10){
			case 0:
				return this.getOptimizePositionFirstPercentage();
			case 1:
				return this.getOptimizePositionSecondPercentage();
			case 2:
				return this.getOptimizePositionThirdPercentage();
			default:
				return 0;
		}
	}

	public int getCurrentPosition() {
		return currentPosition;
	}

	
	public void setApplicableCurrentPosition(int currentPosition, String type) {
		this.setCurrentPosition(currentPosition);
	}
	
	public void setApplicableCurrentIndexCount(int currentIndexCount, String type) {
		this.setCurrentIndexCount(currentIndexCount);
	}
	
	public void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	public Timestamp getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(Timestamp queryTime) {
		this.queryTime = queryTime;
	}

	public String getCollectMethod() {
		return collectMethod;
	}

	public void setCollectMethod(String collectMethod) {
		this.collectMethod = collectMethod;
	}

	public Timestamp getStartOptimizedTime() {
		return startOptimizedTime;
	}

	public void setStartOptimizedTime(Timestamp startOptimizedTime) {
		this.startOptimizedTime = startOptimizedTime;
	}

	public Timestamp getEffectiveFromTime() {
		return effectiveFromTime;
	}

	public void setEffectiveFromTime(Timestamp effectiveFromTime) {
		this.effectiveFromTime = effectiveFromTime;
	}

	public Timestamp getEffectiveToTime() {
		return effectiveToTime;
	}

	public Timestamp getApplicableEffectiveToTime(String type) {
		return this.getEffectiveToTime();
	}
	
	public void setEffectiveToTime(Timestamp effectiveToTime) {
		this.effectiveToTime = effectiveToTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
	
	public String costString(){
		StringBuilder cost = new StringBuilder("");
		if(this.getPositionFirstCost() > 0){
			cost.append(this.getPositionFirstCostString() + ";");
		}
		if(this.getPositionSecondCost() > 0){
			cost.append(this.getPositionSecondCostString() + ";");
		}
		if(this.getPositionThirdCost() > 0){
			cost.append(this.getPositionThirdCostString() + ";");
		}
		if(this.getPositionForthCost() > 0){
			cost.append("</br>" + this.getPositionForthCostString() + ";");
		}
		if(this.getPositionFifthCost() > 0){
			cost.append(this.getPositionFifthCostString() + ";");
		}
		return cost.toString();		
	}
	
	
	public String feeString(){
		StringBuilder fee = new StringBuilder("");
	  String pcFeeString = this.pcFeeString();
	  boolean first = true;
	  if(!Utils.isNullOrEmpty(pcFeeString)){
	  	fee.append("" + pcFeeString);
	  	first = false;
	  }
	  return fee.toString();		
	}
	
	public int priceCount(){
		int i = 0;
		if(hasPC()){
			i++;
		}
		return i;
	}
	
	public boolean hasPC(){
		return this.getPositionFirstFee() > 0;
	}
	
	public String pcFeeString(){
		StringBuilder fee = new StringBuilder("");
		if(this.getPositionFirstFee() > 0){
			fee.append(this.getPositionFirstFeeString() + ";");
		}
		if(this.getPositionSecondFee() > 0){
			fee.append(this.getPositionSecondFeeString() + ";");
		}
		if(this.getPositionThirdFee() > 0){
			fee.append(this.getPositionThirdFeeString() + ";");
		}
		if(this.getPositionForthFee() > 0){
			fee.append(this.getPositionForthFeeString() + ";");
		}
		if(this.getPositionFifthFee() > 0){
			fee.append(this.getPositionFifthFeeString() + ";");
		}
		if(this.getPositionFirstPageFee() > 0){
			fee.append(this.getPositionFirstPageFeeString() + ";");
		}
		return fee.toString();	
	}
	
	public String generateComplexValue(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.getUuid() + ",");
		CustomerKeywordAccountLogVO customerKeywordAccountLogVO = this.getLatestCustomerKeywordAccountLog();		
		sb.append(customerKeywordAccountLogVO.getUuid() + ",");
		sb.append("1,");
		sb.append(Utils.formatDatetime(this.getEffectiveFromTime(), "yyyy-MM-dd") + ",");
		sb.append(Utils.formatDatetime(this.getEffectiveToTime(), "yyyy-MM-dd") + ",");
		sb.append(customerKeywordAccountLogVO.getReceivable() + ",");
		sb.append(customerKeywordAccountLogVO.getFirstRealCollection() + ",");
		sb.append((customerKeywordAccountLogVO.getReceivable() - customerKeywordAccountLogVO.getFirstRealCollection()) + ",");
		sb.append("1," + Utils.getCurrentDate());
		return sb.toString();
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getReceiveStatus() {
		return receiveStatus;
	}

	public Timestamp getPaymentEffectiveFromTime() {
		return paymentEffectiveFromTime;
	}

	public void setPaymentEffectiveFromTime(Timestamp paymentEffectiveFromTime) {
		this.paymentEffectiveFromTime = paymentEffectiveFromTime;
	}

	public Timestamp getPaymentEffectiveToTime() {
		return paymentEffectiveToTime;
	}

	public void setPaymentEffectiveToTime(Timestamp paymentEffectiveToTime) {
		this.paymentEffectiveToTime = paymentEffectiveToTime;
	}

	public CustomerKeywordAccountLogVO getLatestCustomerKeywordAccountLog() {
		return latestCustomerKeywordAccountLog;
	}

	public void setLatestCustomerKeywordAccountLog(CustomerKeywordAccountLogVO latestCustomerKeywordAccountLog) {
		this.latestCustomerKeywordAccountLog = latestCustomerKeywordAccountLog;
	}

	public String getOptimizeGroupName() {
		return optimizeGroupName;
	}

	public void setOptimizeGroupName(String optimizeGroupName) {
		this.optimizeGroupName = optimizeGroupName;
	}

	public int getOptimizePlanCount() {
		return optimizePlanCount;
	}

	public void setOptimizePlanCount(int optimizePlanCount) {
		this.optimizePlanCount = optimizePlanCount;
	}

	public int getOptimizedCount() {
		return optimizedCount;
	}

	public void setOptimizedCount(int optimizedCount) {
		this.optimizedCount = optimizedCount;
	}
	
//	public String toSummaryString(){
//		IndexAndPositionHelper helper = IndexAndPositionHelper.getInstance(this.searchEngine);
//		String pinyin = PinyinUtils.getStringPinYin(this.getKeyword());
//		String splittedKeyword = Utils.splitWithComma(this.getKeyword());
//		return String.format("%d__col__%s__col__%s__col__%s__col__%s", this.getUuid(), splittedKeyword, pinyin, this.getUrl(), this.getSnapshotDateTime());
//	}

	public String getRelatedKeywords() {
		return relatedKeywords;
	}

	public void setRelatedKeywords(String relatedKeywords) {
		this.relatedKeywords = relatedKeywords;
	}

	public Timestamp getOptimizeDate() {
		return optimizeDate;
	}

	public void setOptimizeDate(Timestamp optimizeDate) {
		this.optimizeDate = optimizeDate;
	}


    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }

    public int getOptimizePositionFirstPercentage() {
		return optimizePositionFirstPercentage;
	}

	public void setOptimizePositionFirstPercentage(int optimizePositionFirstPercentage) {
		this.optimizePositionFirstPercentage = optimizePositionFirstPercentage;
	}

	public int getOptimizePositionSecondPercentage() {
		return optimizePositionSecondPercentage;
	}

	public void setOptimizePositionSecondPercentage(int optimizePositionSecondPercentage) {
		this.optimizePositionSecondPercentage = optimizePositionSecondPercentage;
	}

	public int getOptimizePositionThirdPercentage() {
		return optimizePositionThirdPercentage;
	}

	public void setOptimizePositionThirdPercentage(int optimizePositionThirdPercentage) {
		this.optimizePositionThirdPercentage = optimizePositionThirdPercentage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSnapshotDateTime() {
		return snapshotDateTime;
	}

	public void setSnapshotDateTime(String snapshotDateTime) {
		this.snapshotDateTime = snapshotDateTime;
	}


	
	public String effectiveString(){
		StringBuilder str = new StringBuilder();
		boolean first = true;
		if(this.getEffectiveFromTime() != null){
			str.append("PC:" + Utils.formatDatetime(getEffectiveFromTime(), "MM-dd") + "->" + Utils.formatDatetime(getEffectiveToTime(), "MM-dd"));
			first = false;
		}
		return str.toString();
	}
	
	public String capturePositionString(String type){
		return String.format("%s__col__%s__col__%s__col__%s__col__%s__col__%s", this.getUuid(), this.getKeyword(), 
				this.getUrl(), this.getOriginalUrl(), "", "");
	}

	public String captureIndexString(String type){
		return String.format("%s__col__%s__col__%s", this.getUuid(), type, this.getKeyword());
	}
	
	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public double getPositionFirstCost() {
		return positionFirstCost;
	}

	public void setPositionFirstCost(double positionFirstCost) {
		this.positionFirstCost = positionFirstCost;
	}

	public double getPositionSecondCost() {
		return positionSecondCost;
	}

	public void setPositionSecondCost(double positionSecondCost) {
		this.positionSecondCost = positionSecondCost;
	}

	public double getPositionThirdCost() {
		return positionThirdCost;
	}

	public void setPositionThirdCost(double positionThirdCost) {
		this.positionThirdCost = positionThirdCost;
	}

	public double getPositionForthCost() {
		return positionForthCost;
	}

	public void setPositionForthCost(double positionForthCost) {
		this.positionForthCost = positionForthCost;
	}

	public double getPositionFifthCost() {
		return positionFifthCost;
	}

	public void setPositionFifthCost(double positionFifthCost) {
		this.positionFifthCost = positionFifthCost;
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
	
	public String getPositionFirstCostString() {
		return Utils.removeDoubleZeros(positionFirstCost);
	}
	
	public String getPositionSecondCostString() {
		return Utils.removeDoubleZeros(positionSecondCost);
	}
	
	public String getPositionThirdCostString() {
		return Utils.removeDoubleZeros(positionThirdCost);
	}
	
	public String getPositionForthCostString() {
		return Utils.removeDoubleZeros(positionForthCost);
	}
	
	public String getPositionFifthCostString() {
		return Utils.removeDoubleZeros(positionFifthCost);
	}
	
	public String getPositionFirstFeeString() {
		return Utils.removeDoubleZeros(positionFirstFee);
	}
	
	public String getPositionSecondFeeString() {
		return Utils.removeDoubleZeros(positionSecondFee);
	}
	
	public String getPositionThirdFeeString() {
		return Utils.removeDoubleZeros(positionThirdFee);
	}
	
	public String getPositionForthFeeString() {
		return Utils.removeDoubleZeros(positionForthFee);
	}
	
	public String getPositionFifthFeeString() {
		return Utils.removeDoubleZeros(positionFifthFee);
	}
	
	public String getPositionFirstPageFeeString() {
		return Utils.removeDoubleZeros(positionFirstPageFee);
	}
	
	public double getPositionFirstPageFee() {
		return positionFirstPageFee;
	}

	public void setPositionFirstPageFee(double positionFirstPageFee) {
		this.positionFirstPageFee = positionFirstPageFee;
	}

	public Timestamp getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(Timestamp queryDate) {
		this.queryDate = queryDate;
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}

	public int getInvalidRefreshCount() {
		return invalidRefreshCount;
	}

	public void setInvalidRefreshCount(int invalidRefreshCount) {
		this.invalidRefreshCount = invalidRefreshCount;
	}
	
	public UserAccountLogCustomerKeywordVO createUserAccountLogCustomerKeywordVO(){
		UserAccountLogCustomerKeywordVO userAccountLogCustomerKeywordVO = new UserAccountLogCustomerKeywordVO();
		userAccountLogCustomerKeywordVO.setKeyword(this.getKeyword());
		userAccountLogCustomerKeywordVO.setUrl(this.getUrl());
		userAccountLogCustomerKeywordVO.setOriginalUrl(this.getOriginalUrl());
		userAccountLogCustomerKeywordVO.setCurrentPosition(this.getCurrentPosition());
		userAccountLogCustomerKeywordVO.setPositionFirstFee(this.getPositionFirstFee());
		userAccountLogCustomerKeywordVO.setPositionSecondFee(this.getPositionSecondFee());
		userAccountLogCustomerKeywordVO.setPositionThirdFee(this.getPositionThirdFee());
		userAccountLogCustomerKeywordVO.setPositionForthFee(this.getPositionForthFee());
		userAccountLogCustomerKeywordVO.setPositionFifthFee(this.getPositionFifthFee());
		userAccountLogCustomerKeywordVO.setPositionFirstPageFee(this.getPositionFirstPageFee());
		return userAccountLogCustomerKeywordVO;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public double getOptimizedPercentage() {
		return optimizedPercentage;
	}

	public void setOptimizedPercentage(double optimizedPercentage) {
		this.optimizedPercentage = optimizedPercentage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getLastOptimizeDateTime() {
		return lastOptimizeDateTime;
	}

	public void setLastOptimizeDateTime(Timestamp lastOptimizeDateTime) {
		this.lastOptimizeDateTime = lastOptimizeDateTime;
	}

	public Timestamp getAutoUpdateNegativeTime() {
		return autoUpdateNegativeTime;
	}

	public void setAutoUpdateNegativeTime(Timestamp autoUpdateNegativeTime) {
		this.autoUpdateNegativeTime = autoUpdateNegativeTime;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getQueryInterval() {
		return queryInterval;
	}

	public void setQueryInterval(int queryInterval) {
		this.queryInterval = queryInterval;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getCaptureCurrentKeywordStatus() {
		return captureCurrentKeywordStatus;
	}

	public void setCaptureCurrentKeywordStatus(String captureCurrentKeywordStatus) {
		this.captureCurrentKeywordStatus = captureCurrentKeywordStatus;
	}

	public Timestamp getCaptureCurrentKeywordCountTime() {
		return captureCurrentKeywordCountTime;
	}

	public void setCaptureCurrentKeywordCountTime(Timestamp captureCurrentKeywordCountTime) {
		this.captureCurrentKeywordCountTime = captureCurrentKeywordCountTime;
	}
}