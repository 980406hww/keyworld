package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.keymanager.enums.CollectMethod;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import org.springframework.format.annotation.DateTimeFormat;

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

    @TableField(value = "fUrl", strategy = FieldStrategy.IGNORED)
    private String url;

    @TableField(value = "fType")
    private String type;

    @TableField(value = "fOriginalUrl", strategy = FieldStrategy.IGNORED)
    private String originalUrl;

    @TableField(value = "fTitle", strategy = FieldStrategy.IGNORED)
    private String title;

    @TableField(value = "fSequence", strategy = FieldStrategy.IGNORED)
    private Integer sequence;

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
    private Integer optimizePlanCount;

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

    @TableField(value = "fLastReachStandardDate")
    private Date lastReachStandardDate;

    @TableField(value = "fOptimizePositionFirstPercentage")
    private int optimizePositionFirstPercentage;

    @TableField(value = "fOptimizePositionSecondPercentage")
    private int optimizePositionSecondPercentage;

    @TableField(value = "fOptimizePositionThirdPercentage")
    private int optimizePositionThirdPercentage;

    @TableField(value = "fPositionFirstCost", strategy = FieldStrategy.IGNORED)
    private Double positionFirstCost;

    @TableField(value = "fPositionSecondCost", strategy = FieldStrategy.IGNORED)
    private Double positionSecondCost;

    @TableField(value = "fPositionThirdCost", strategy = FieldStrategy.IGNORED)
    private Double positionThirdCost;

    @TableField(value = "fPositionForthCost", strategy = FieldStrategy.IGNORED)
    private Double positionForthCost;

    @TableField(value = "fPositionFifthCost", strategy = FieldStrategy.IGNORED)
    private Double positionFifthCost;

    @TableField(value = "fPositionFirstFee", strategy = FieldStrategy.IGNORED)
    private Double positionFirstFee;

    @TableField(value = "fPositionSecondFee", strategy = FieldStrategy.IGNORED)
    private Double positionSecondFee;

    @TableField(value = "fPositionThirdFee", strategy = FieldStrategy.IGNORED)
    private Double positionThirdFee;

    @TableField(value = "fPositionForthFee", strategy = FieldStrategy.IGNORED)
    private Double positionForthFee;

    @TableField(value = "fPositionFifthFee", strategy = FieldStrategy.IGNORED)
    private Double positionFifthFee;

    @TableField(value = "fPositionFirstPageFee", strategy = FieldStrategy.IGNORED)
    private Double positionFirstPageFee;

    @TableField(value = "fCapturePositionQueryTime")
    private Date capturePositionQueryTime;

    @TableField(value = "fCaptureIndexQueryTime")
    private Date captureIndexQueryTime;

    @TableField(value = "fCollectMethod", strategy = FieldStrategy.IGNORED)
    private String collectMethod;

    @TableField(value = "fStartOptimizedTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startOptimizedTime;

    @TableField(value = "fEffectiveFromTime")
    private Timestamp effectiveFromTime;

    @TableField(value = "fEffectiveToTime")
    private Timestamp effectiveToTime;

    @TableField(value = "fPaymentEffectiveFromTime")
    private Date paymentEffectiveFromTime;

    @TableField(value = "fPaymentEffectiveFromTime")
    private Date paymentEffectiveToTime;

    @TableField(value = "fOperateSelectKeyword")
    private Boolean operateSelectKeyword;

    @TableField(value = "fOperateRelatedKeyword")
    private Boolean operateRelatedKeyword;

    @TableField(value = "fOperateRecommendKeyword")
    private Boolean operateRecommendKeyword;

    @TableField(value = "fOperateSearchAfterSelectKeyword")
    private Boolean operateSearchAfterSelectKeyword;

    @TableField(value = "fClickUrl", strategy = FieldStrategy.IGNORED)
    private String clickUrl;

    @TableField(value = "fShowPage", strategy = FieldStrategy.IGNORED)
    private String showPage;

    @TableField(value = "fRelatedKeywordPercentage", strategy = FieldStrategy.IGNORED)
    private Integer relatedKeywordPercentage;

    @TableField(value = "fRelatedKeywords", strategy = FieldStrategy.IGNORED)
    private String relatedKeywords;

    @TableField(value = "fNegativeKeywords", strategy = FieldStrategy.IGNORED)
    private String negativeKeywords;

    @TableField(value = "fRecommendKeywords", strategy = FieldStrategy.IGNORED)
    private String recommendKeywords;

    @TableField(value = "fExcludeKeywords", strategy = FieldStrategy.IGNORED)
    private String excludeKeywords;

    @TableField(value = "fPaymentStatus", strategy = FieldStrategy.IGNORED)
    private String paymentStatus;

    @TableField(value = "fOrderNumber", strategy = FieldStrategy.IGNORED)
    private String orderNumber;

    @TableField(value = "fRemarks", strategy = FieldStrategy.IGNORED)
    private String remarks;

    @TableField(value = "fStatus")
    private int status;

    @TableField(value = "fRequireDelete")
    private Boolean requireDelete;

    @TableField(value = "fManualCleanTitle")
    private Boolean manualCleanTitle;

    @TableField(exist = false)
    private Timestamp autoUpdateNegativeTime;

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

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
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

    public Integer getOptimizePlanCount() {
        return optimizePlanCount;
    }

    public void setOptimizePlanCount(Integer optimizePlanCount) {
        this.optimizePlanCount = optimizePlanCount;
    }

    public Integer getOptimizedCount() {
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

    public Date getLastReachStandardDate() {
        return lastReachStandardDate;
    }

    public void setLastReachStandardDate(Date lastReachStandardDate) {
        this.lastReachStandardDate = lastReachStandardDate;
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

    public Boolean getManualCleanTitle() {
        return manualCleanTitle;
    }

    public void setManualCleanTitle(Boolean manualCleanTitle) {
        this.manualCleanTitle = manualCleanTitle;
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

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    public Date getStartOptimizedTime() {
        return startOptimizedTime;
    }

    public void setStartOptimizedTime(Date startOptimizedTime) {
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

    public void setEffectiveToTime(Timestamp effectiveToTime) {
        this.effectiveToTime = effectiveToTime;
    }

    public Timestamp getAutoUpdateNegativeTime() {
        return autoUpdateNegativeTime;
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

    public String getCollectMethodName(){
        return StringUtils.isNotBlank(this.getCollectMethod()) ? CollectMethod.findByCode(this.getCollectMethod()).getName() : null;
    }
    public String getSearchEngineUrl(){
        String searchEngineUrl = Constants.SEARCH_ENGINE_URL_MAP.get(this.getSearchEngine() + "_" + this.getTerminalType()) + this.getKeyword();
        if(this.getSearchEngine() != null){
            if(this.getSearchEngine().equals(Constants.SEARCH_ENGINE_BAIDU)) {
                searchEngineUrl += "&pn=" + this.getPrepareBaiduPageNumber(this.getCurrentPosition());
            } else if(this.getSearchEngine().equals(Constants.SEARCH_ENGINE_SOGOU)) {
                if(this.getTerminalType().equals(TerminalTypeEnum.PC.name())) {
                    searchEngineUrl += "&page=" + ((this.getCurrentPosition() / 10) + 1);
                } else {
                    searchEngineUrl += "&p=" + ((this.getCurrentPosition() / 10) + 1);
                }
            } else if(this.getSearchEngine().equals(Constants.SEARCH_ENGINE_360)) {
                searchEngineUrl += "&pn=" + ((this.getCurrentPosition() / 10) + 1);
            } else if(this.getSearchEngine().equals(Constants.SEARCH_ENGINE_SM)) {
                searchEngineUrl += "&page=" + ((this.getCurrentPosition() / 10) + 1);
            }
        }
        return searchEngineUrl;
    }
    public int getPrepareBaiduPageNumber(int value) {
        int tmpValue = (value > 0 ? (value - 1) : 0);
        String valueString = tmpValue + "";
        String lastDigit = valueString.substring(valueString.length() - 1);
        return tmpValue - Integer.parseInt(lastDigit);
    }

    public String getPositionFirstFeeString() {return Utils.removeDoubleZeros(positionFirstFee)==null?"":Utils.removeDoubleZeros(positionFirstFee);}

    public String getPositionSecondFeeString() {
        return Utils.removeDoubleZeros(positionSecondFee)==null?"":Utils.removeDoubleZeros(positionSecondFee);
    }

    public String getPositionThirdFeeString() {
        return Utils.removeDoubleZeros(positionThirdFee)==null?"":Utils.removeDoubleZeros(positionThirdFee);
    }

    public String getPositionForthFeeString() {
        return Utils.removeDoubleZeros(positionForthFee)==null?"":Utils.removeDoubleZeros(positionForthFee);
    }

    public String getPositionFifthFeeString() {
        return Utils.removeDoubleZeros(positionFifthFee)==null?"":Utils.removeDoubleZeros(positionFifthFee);
    }

    public String getPositionFirstPageFeeString() {
        return Utils.removeDoubleZeros(positionFirstPageFee)==null?"":Utils.removeDoubleZeros(positionFirstPageFee);
    }

    public String getFeeString(){
        StringBuilder fee = new StringBuilder("");
        String pcFeeString = this.pcFeeString();
        boolean first = true;
        if(!Utils.isNullOrEmpty(pcFeeString)){
            fee.append("" + pcFeeString);
            first = false;
        }
        return fee.toString();
    }
    public String pcFeeString(){
        StringBuilder fee = new StringBuilder("");
        if(this.getPositionFirstFee() != null && this.getPositionFirstFee() > 0){
            fee.append(this.getPositionFirstFeeString() + ";");
        }
        if(this.getPositionSecondFee() != null && this.getPositionSecondFee() > 0){
            fee.append(this.getPositionSecondFeeString() + ";");
        }
        if(this.getPositionThirdFee() != null && this.getPositionThirdFee() > 0){
            fee.append(this.getPositionThirdFeeString() + ";");
        }
        if(this.getPositionForthFee() != null && this.getPositionForthFee() > 0){
            fee.append(this.getPositionForthFeeString() + ";");
        }
        if(this.getPositionFifthFee() != null && this.getPositionFifthFee() > 0){
            fee.append(this.getPositionFifthFeeString() + ";");
        }
        if(this.getPositionFirstPageFee() != null && this.getPositionFirstPageFee() > 0){
            fee.append(this.getPositionFirstPageFeeString() + ";");
        }
        return fee.toString();
    }
    public String captureIndexString(String type){
        return String.format("%s__col__%s__col__%s", this.getUuid(), type, this.getKeyword());
    }
    public void setAutoUpdateNegativeTime(Timestamp autoUpdateNegativeTime) {
        this.autoUpdateNegativeTime = autoUpdateNegativeTime;
    }

    public void setApplicableCurrentPosition(int currentPosition, String type) {
        this.setCurrentPosition(currentPosition);
    }

    public String getNegativeKeywords() {
        return negativeKeywords;
    }

    public void setNegativeKeywords(String negativeKeywords) {
        this.negativeKeywords = negativeKeywords;
    }

    public String getRecommendKeywords() {
        return recommendKeywords;
    }

    public void setRecommendKeywords(String recommendKeywords) {
        this.recommendKeywords = recommendKeywords;
    }

    public String getExcludeKeywords() {
        return excludeKeywords;
    }

    public void setExcludeKeywords(String excludeKeywords) {
        this.excludeKeywords = excludeKeywords;
    }

    public Boolean getOperateSelectKeyword() {
        return operateSelectKeyword;
    }

    public void setOperateSelectKeyword(Boolean operateSelectKeyword) {
        this.operateSelectKeyword = operateSelectKeyword;
    }

    public Boolean getOperateRelatedKeyword() {
        return operateRelatedKeyword;
    }

    public void setOperateRelatedKeyword(Boolean operateRelatedKeyword) {
        this.operateRelatedKeyword = operateRelatedKeyword;
    }

    public Boolean getOperateRecommendKeyword() {
        return operateRecommendKeyword;
    }

    public void setOperateRecommendKeyword(Boolean operateRecommendKeyword) {
        this.operateRecommendKeyword = operateRecommendKeyword;
    }

    public Boolean getOperateSearchAfterSelectKeyword() {
        return operateSearchAfterSelectKeyword;
    }

    public void setOperateSearchAfterSelectKeyword(Boolean operateSearchAfterSelectKeyword) {
        this.operateSearchAfterSelectKeyword = operateSearchAfterSelectKeyword;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getShowPage() {
        return showPage;
    }

    public void setShowPage(String showPage) {
        this.showPage = showPage;
    }

    public Integer getRelatedKeywordPercentage() {
        return relatedKeywordPercentage;
    }

    public void setRelatedKeywordPercentage(Integer relatedKeywordPercentage) {
        this.relatedKeywordPercentage = relatedKeywordPercentage;
    }

    public Boolean getRequireDelete() {
        return requireDelete;
    }

    public void setRequireDelete(Boolean requireDelete) {
        this.requireDelete = requireDelete;
    }
}
