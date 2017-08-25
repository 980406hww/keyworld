package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.sql.Timestamp;
import java.util.Date;

@TableName(value = "t_customer_keyword")
public class CustomerKeyword extends BaseEntity {
    private static final long serialVersionUID = -1101942701283949852L;

    @TableField(value = "fCustomerUuid")
    private long customerUuid;

    @TableField(value = "fTerminalType")
    private String terminalType;

    @TableField(value = "fKeyword")
    private String keyword;

    @TableField(value = "fUrl")
    private String url;

    @TableField(value = "fType")
    private String type;

    @TableField(value = "fOriginalUrl")
    private String originalUrl;

    @TableField(value = "fTitle")
    private String title;

    @TableField(value = "fSequence")
    private int sequence;

    @TableField(value = "fCaptureTitleQueryTime")
    private Date captureTitleQueryTime;

    @TableField(value = "fCapturedTitle")
    private int capturedTitle;

    @TableField(value = "fAutoUpdateNegativeDateTime")
    private Date autoUpdateNegativeDateTime;

    @TableField(value = "fBigKeyword")
    private boolean bigKeyword;

    @TableField(value = "fSnapshotDateTime")
    private String snapshotDateTime;

    @TableField(value = "fSearchEngine")
    private String searchEngine;

    @TableField(value = "fInitialIndexCount")
    private Integer initialIndexCount;

    @TableField(value = "fInitialPosition")
    private Integer initialPosition;

    @TableField(value = "fCurrentIndexCount")
    private Integer currentIndexCount;

    @TableField(value = "fCurrentPosition")
    private Integer currentPosition;

    @TableField(value = "fQueryDate")
    private Date queryDate;

    @TableField(value = "fQueryCount")
    private int queryCount;

    @TableField(value = "fInvalidRefreshCount")
    private int invalidRefreshCount;

    @TableField(value = "fQueryTime")
    private Date queryTime;

    @TableField(value = "fQueryInterval")
    private int queryInterval;

    @TableField(value = "fServiceProvider")
    private String serviceProvider;

    @TableField(value = "fOptimizeGroupName")
    private String optimizeGroupName;

    @TableField(value = "fOptimizePlanCount")
    private int optimizePlanCount;

    @TableField(value = "fOptimizedCount")
    private int optimizedCount;

    @TableField(value = "fOptimizedPercentage")
    private Double optimizedPercentage;

    @TableField(value = "fOptimizeDate")
    private Date optimizeDate;

    @TableField(exist = false)
    private String contactPerson;

    @TableField(value = "fLastOptimizeDateTime")
    private Date lastOptimizeDateTime;

    @TableField(value = "fOptimizePositionFirstPercentage")
    private int optimizePositionFirstPercentage;

    @TableField(value = "fOptimizePositionSecondPercentage")
    private int optimizePositionSecondPercentage;

    @TableField(value = "fOptimizePositionThirdPercentage")
    private int optimizePositionThirdPercentage;

    @TableField(value = "fPositionFirstCost")
    private Double positionFirstCost;

    @TableField(value = "fPositionSecondCost")
    private Double positionSecondCost;

    @TableField(value = "fPositionThirdCost")
    private Double positionThirdCost;

    @TableField(value = "fPositionForthCost")
    private Double positionForthCost;

    @TableField(value = "fPositionFifthCost")
    private Double positionFifthCost;

    @TableField(value = "fPositionFirstFee")
    private Double positionFirstFee;

    @TableField(value = "fPositionSecondFee")
    private Double positionSecondFee;

    @TableField(value = "fPositionThirdFee")
    private Double positionThirdFee;

    @TableField(value = "fPositionForthFee")
    private Double positionForthFee;

    @TableField(value = "fPositionFifthFee")
    private Double positionFifthFee;

    @TableField(value = "fPositionFirstPageFee")
    private Double positionFirstPageFee;

    @TableField(value = "fCapturePositionQueryTime")
    private Date capturePositionQueryTime;

    @TableField(value = "fCaptureIndexQueryTime")
    private Date captureIndexQueryTime;

    @TableField(value = "fCollectMethod")
    private String collectMethod;

    @TableField(value = "fStartOptimizedTime")
    private Date startOptimizedTime;

    @TableField(value = "fEffectiveFromTime")
    private Date effectiveFromTime;

    @TableField(value = "fEffectiveToTime")
    private Date effectiveToTime;

    @TableField(value = "fPaymentEffectiveFromTime")
    private Date paymentEffectiveFromTime;

    @TableField(value = "fPaymentEffectiveFromTime")
    private Date paymentEffectiveToTime;

    @TableField(value = "fRelatedKeywords")
    private String relatedKeywords;

    @TableField(value = "fPaymentStatus")
    private String paymentStatus;

    @TableField(value = "fOrderNumber")
    private String orderNumber;

    @TableField(value = "fRemarks")
    private String remarks;

    @TableField(value = "fStatus")
    private int status;

    public long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Date getCaptureTitleQueryTime() {
        return captureTitleQueryTime;
    }

    public void setCaptureTitleQueryTime(Date captureTitleQueryTime) {
        this.captureTitleQueryTime = captureTitleQueryTime;
    }

    public int getCapturedTitle() {
        return capturedTitle;
    }

    public void setCapturedTitle(int capturedTitle) {
        this.capturedTitle = capturedTitle;
    }

    public Date getAutoUpdateNegativeDateTime() {
        return autoUpdateNegativeDateTime;
    }

    public void setAutoUpdateNegativeDateTime(Date autoUpdateNegativeDateTime) {
        this.autoUpdateNegativeDateTime = autoUpdateNegativeDateTime;
    }

    public boolean isBigKeyword() {
        return bigKeyword;
    }

    public void setBigKeyword(boolean bigKeyword) {
        this.bigKeyword = bigKeyword;
    }

    public String getSnapshotDateTime() {
        return snapshotDateTime;
    }

    public void setSnapshotDateTime(String snapshotDateTime) {
        this.snapshotDateTime = snapshotDateTime;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public Integer getInitialIndexCount() {
        return initialIndexCount;
    }

    public void setInitialIndexCount(Integer initialIndexCount) {
        this.initialIndexCount = initialIndexCount;
    }

    public Integer getInitialPosition() {
        return initialPosition;
    }

    public void setInitialPosition(Integer initialPosition) {
        this.initialPosition = initialPosition;
    }

    public Integer getCurrentIndexCount() {
        return currentIndexCount;
    }

    public void setCurrentIndexCount(Integer currentIndexCount) {
        this.currentIndexCount = currentIndexCount;
    }

    public Integer getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(Integer currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
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

    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }

    public int getQueryInterval() {
        return queryInterval;
    }

    public void setQueryInterval(int queryInterval) {
        this.queryInterval = queryInterval;
    }

    public String getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
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

    public Double getOptimizedPercentage() {
        return optimizedPercentage;
    }

    public void setOptimizedPercentage(Double optimizedPercentage) {
        this.optimizedPercentage = optimizedPercentage;
    }

    public Date getOptimizeDate() {
        return optimizeDate;
    }

    public void setOptimizeDate(Date optimizeDate) {
        this.optimizeDate = optimizeDate;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Date getLastOptimizeDateTime() {
        return lastOptimizeDateTime;
    }

    public void setLastOptimizeDateTime(Date lastOptimizeDateTime) {
        this.lastOptimizeDateTime = lastOptimizeDateTime;
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

    public Double getPositionFirstCost() {
        return positionFirstCost;
    }

    public void setPositionFirstCost(Double positionFirstCost) {
        this.positionFirstCost = positionFirstCost;
    }

    public Double getPositionSecondCost() {
        return positionSecondCost;
    }

    public void setPositionSecondCost(Double positionSecondCost) {
        this.positionSecondCost = positionSecondCost;
    }

    public Double getPositionThirdCost() {
        return positionThirdCost;
    }

    public void setPositionThirdCost(Double positionThirdCost) {
        this.positionThirdCost = positionThirdCost;
    }

    public Double getPositionForthCost() {
        return positionForthCost;
    }

    public void setPositionForthCost(Double positionForthCost) {
        this.positionForthCost = positionForthCost;
    }

    public Double getPositionFifthCost() {
        return positionFifthCost;
    }

    public void setPositionFifthCost(Double positionFifthCost) {
        this.positionFifthCost = positionFifthCost;
    }

    public Double getPositionFirstFee() {
        return positionFirstFee;
    }

    public void setPositionFirstFee(Double positionFirstFee) {
        this.positionFirstFee = positionFirstFee;
    }

    public Double getPositionSecondFee() {
        return positionSecondFee;
    }

    public void setPositionSecondFee(Double positionSecondFee) {
        this.positionSecondFee = positionSecondFee;
    }

    public Double getPositionThirdFee() {
        return positionThirdFee;
    }

    public void setPositionThirdFee(Double positionThirdFee) {
        this.positionThirdFee = positionThirdFee;
    }

    public Double getPositionForthFee() {
        return positionForthFee;
    }

    public void setPositionForthFee(Double positionForthFee) {
        this.positionForthFee = positionForthFee;
    }

    public Double getPositionFifthFee() {
        return positionFifthFee;
    }

    public void setPositionFifthFee(Double positionFifthFee) {
        this.positionFifthFee = positionFifthFee;
    }

    public Double getPositionFirstPageFee() {
        return positionFirstPageFee;
    }

    public void setPositionFirstPageFee(Double positionFirstPageFee) {
        this.positionFirstPageFee = positionFirstPageFee;
    }

    public Date getCapturePositionQueryTime() {
        return capturePositionQueryTime;
    }

    public void setCapturePositionQueryTime(Date capturePositionQueryTime) {
        this.capturePositionQueryTime = capturePositionQueryTime;
    }

    public Date getCaptureIndexQueryTime() {
        return captureIndexQueryTime;
    }

    public void setCaptureIndexQueryTime(Date captureIndexQueryTime) {
        this.captureIndexQueryTime = captureIndexQueryTime;
    }

    public String getCollectMethod() {
        return collectMethod;
    }

    public void setCollectMethod(String collectMethod) {
        this.collectMethod = collectMethod;
    }

    public Date getStartOptimizedTime() {
        return startOptimizedTime;
    }

    public void setStartOptimizedTime(Date startOptimizedTime) {
        this.startOptimizedTime = startOptimizedTime;
    }

    public Date getEffectiveFromTime() {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Date effectiveFromTime) {
        this.effectiveFromTime = effectiveFromTime;
    }

    public Date getEffectiveToTime() {
        return effectiveToTime;
    }

    public void setEffectiveToTime(Date effectiveToTime) {
        this.effectiveToTime = effectiveToTime;
    }

    public Date getPaymentEffectiveFromTime() {
        return paymentEffectiveFromTime;
    }

    public void setPaymentEffectiveFromTime(Date paymentEffectiveFromTime) {
        this.paymentEffectiveFromTime = paymentEffectiveFromTime;
    }

    public Date getPaymentEffectiveToTime() {
        return paymentEffectiveToTime;
    }

    public void setPaymentEffectiveToTime(Date paymentEffectiveToTime) {
        this.paymentEffectiveToTime = paymentEffectiveToTime;
    }

    public String getRelatedKeywords() {
        return relatedKeywords;
    }

    public void setRelatedKeywords(String relatedKeywords) {
        this.relatedKeywords = relatedKeywords;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
