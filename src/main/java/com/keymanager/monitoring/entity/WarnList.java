package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_warn_list")
public class WarnList extends BaseEntity {
	@TableField(value = "fTerminalType")
	private String terminalType;

	@TableField(value = "fKeyword")
	private String keyword;

	@TableField(value = "fTitle")
	private String title;

	@TableField(value = "fUrl")
	private String url;

	@TableField(value = "fDesc")
	private String desc;

	@TableField(value = "fPosition")
	private Integer position;

	@TableField(value = "fOriginalUrl")
	private String originalUrl;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
}
