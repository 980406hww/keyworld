package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

@TableName(value = "t_ck_optimized_count_log")
public class CustomerKeywordOptimizedCountLog implements Serializable {

	private static final long serialVersionUID = 3922222059082125030L;

	@TableId(value = "fUuid", type= IdType.AUTO)
	private Long uuid;

	@TableField(value = "fCustomerKeywordUuid")
	private Long customerKeywordUuid;

	@TableField(value = "fOptimizedCount")
	private int optimizedCount;

	@TableField(value = "fOptimizedDate")
	private Date optimizedDate;

	public Long getUuid() {
		return uuid;
	}

	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}

	public Long getCustomerKeywordUuid() {
		return customerKeywordUuid;
	}

	public void setCustomerKeywordUuid(Long customerKeywordUuid) {
		this.customerKeywordUuid = customerKeywordUuid;
	}

	public int getOptimizedCount() {
		return optimizedCount;
	}

	public void setOptimizedCount(int optimizedCount) {
		this.optimizedCount = optimizedCount;
	}

	public Date getOptimizedDate() {
		return optimizedDate;
	}

	public void setOptimizedDate(Date optimizedDate) {
		this.optimizedDate = optimizedDate;
	}
}
