package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 *
 * 组织机构
 *
 */
@TableName(value = "t_organization")
public class Organization  implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "fUuid", type= IdType.AUTO)
	private Long id;

	/** 组织名 */
	@NotBlank
	@TableField(value = "fOrganizationName")
	private String organizationName;

	/** 地址 */
	@TableField(value = "fAddress")
	private String address;

	/** 编号 */
	@NotBlank
	@TableField(value = "fCode")
	private String code;

	/** 图标 */
	@JsonProperty("iconCls")
	@TableField(value = "fIcon")
	private String icon;

	/** 父级主键 */
	@TableField(value = "fParentID")
	private Long pid;

	/** 排序 */
	@TableField(value = "fSequence")
	private Integer sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}
