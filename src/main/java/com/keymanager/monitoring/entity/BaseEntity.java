package com.keymanager.monitoring.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 3922222059082125030L;
	
	@TableId(value = "fUuid", type= IdType.AUTO)
	private Long uuid;
	
	@TableField(value = "fUpdateTime")
	private Date updateTime;
	
	@TableField(value = "fCreateTime")
	private Date createTime;

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
}
