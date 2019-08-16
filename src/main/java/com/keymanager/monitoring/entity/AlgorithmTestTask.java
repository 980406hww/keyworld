package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

/**
 * <p>
 * 算法测试任务表
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@TableName("t_algorithm_test_task")
public class AlgorithmTestTask {

    private static final long serialVersionUID = 1L;

    /**
     * 算法测试任务ID
     */
    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    /**
     * 算法测试计划表ID
     */
    @TableId(value = "fAlgorithmTestPlanUuid")
    private Long algorithmTestPlanUuid;

    /**
     * 关键字分组（由算法测试计划名称+日期组合而成）
     */
    @TableField("fKeywordGroup")
    private String keywordGroup;

    /**
     * 客户名称（由算法测试计划名称+日期组成）
     */
    @TableField("fCustomerName")
    private String customerName;

    /**
     * 实际测试词数
     */
    @TableField("fActualKeywordCount")
    private Integer actualKeywordCount;

    /**
     * 开始测试日期
     */
    @TableField("fStartDate")
    private Date startDate;

    /**
     * 创建时间
     */
    @TableField("fCreateTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("fUpdateTime")
    private Date updateTime;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public Long getAlgorithmTestPlanUuid() {
        return algorithmTestPlanUuid;
    }

    public void setAlgorithmTestPlanUuid(Long algorithmTestPlanUuid) {
        this.algorithmTestPlanUuid = algorithmTestPlanUuid;
    }

    public String getKeywordGroup() {
        return keywordGroup;
    }

    public void setKeywordGroup(String keywordGroup) {
        this.keywordGroup = keywordGroup;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getActualKeywordCount() {
        return actualKeywordCount;
    }

    public void setActualKeywordCount(Integer actualKeywordCount) {
        this.actualKeywordCount = actualKeywordCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
