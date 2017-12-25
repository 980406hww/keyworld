package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_snapshot_history")
public class SnapshotHistory extends BaseEntity {
	@TableField(value = "fCustomerUuid")
	private Long customerUuid;

	@TableField(value = "fContactPerson")
	private String contactPerson;

	@TableField(value = "fKeywordTreeId")
	private Long keywordTreeId;

	@TableField(value = "fSearchEngineTreeId")
	private Long searchEngineTreeId;

	@TableField(value = "fSearchEngine")
	private String searchEngine;

	@TableField(value = "fKeyword")
	private String keyword;

	@TableField(value = "fClickCount")
	private Integer clickCount;

	@TableField(value = "fTitle")
	private String title;

	@TableField(value = "fDesc")
	private String desc;

	@TableField(value = "fUrl")
	private String url;

	@TableField(value = "fOrder")
	private Integer order;

	@TableField(value = "fHref")
	private String href;

	@TableField(value = "fHasOfficialWebsiteIndicator")
	private Boolean hasOfficialWebsiteIndicator;

	@TableField(value = "fIsNegative")
	private Boolean isNegative;

	@TableField(value = "fIsExistNegativeList")
	private Boolean isExistNegativeList;

	@TableField(value = "fIsExistPositiveList")
	private Boolean isExistPositiveList;

	public Long getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(Long customerUuid) {
		this.customerUuid = customerUuid;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Long getKeywordTreeId() {
		return keywordTreeId;
	}

	public void setKeywordTreeId(Long keywordTreeId) {
		this.keywordTreeId = keywordTreeId;
	}

	public Long getSearchEngineTreeId() {
		return searchEngineTreeId;
	}

	public void setSearchEngineTreeId(Long searchEngineTreeId) {
		this.searchEngineTreeId = searchEngineTreeId;
	}

	public String getSearchEngine() {
		return searchEngine;
	}

	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getClickCount() {
		return clickCount;
	}

	public void setClickCount(Integer clickCount) {
		this.clickCount = clickCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Boolean getHasOfficialWebsiteIndicator() {
		return hasOfficialWebsiteIndicator;
	}

	public void setHasOfficialWebsiteIndicator(Boolean hasOfficialWebsiteIndicator) {
		this.hasOfficialWebsiteIndicator = hasOfficialWebsiteIndicator;
	}

	public Boolean getIsNegative() {
		return isNegative;
	}

	public void setIsNegative(Boolean negative) {
		isNegative = negative;
	}

	public Boolean getIsExistNegativeList() {
		return isExistNegativeList;
	}

	public void setIsExistNegativeList(Boolean existNegativeList) {
		isExistNegativeList = existNegativeList;
	}

	public Boolean getIsExistPositiveList() {
		return isExistPositiveList;
	}

	public void setIsExistPositiveList(Boolean existPositiveList) {
		isExistPositiveList = existPositiveList;
	}

	public Boolean getNegative() {
		return isNegative;
	}

	public void setNegative(Boolean negative) {
		isNegative = negative;
	}

	public Boolean getExistNegativeList() {
		return isExistNegativeList;
	}

	public void setExistNegativeList(Boolean existNegativeList) {
		isExistNegativeList = existNegativeList;
	}

	public Boolean getExistPositiveList() {
		return isExistPositiveList;
	}

	public void setExistPositiveList(Boolean existPositiveList) {
		isExistPositiveList = existPositiveList;
	}
}
