package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;


@TableName(value = "t_role")
public class Role extends BaseEntity {
	
	private static final long serialVersionUID = -7590694637780491359L;

	/** 角色名 */
	@NotBlank
	@TableField(value = "fRoleName")
	private String roleName;

	/** 排序号 */
	@TableField(value = "fSequence")
	private Integer sequence;

	/** 简介 */
	@TableField(value = "fDescription")
	private String description;

	/** 状态 */
	@TableField(value = "fStatus")
	private Integer status;

	@TableField(exist = false)
	private List<Resource> resourceList;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Role{" +
				"roleName='" + roleName + '\'' +
				", sequence=" + sequence +
				", description='" + description + '\'' +
				", status=" + status +
				'}';
	}
}
