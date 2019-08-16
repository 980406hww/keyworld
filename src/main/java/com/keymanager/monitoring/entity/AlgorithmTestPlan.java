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
@TableName("t_algorithm_test_plan")
public class AlgorithmTestPlan{

    private static final long serialVersionUID = 1L;

    /**
     * 算法测试计划id
     */
    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    /**
     * 算法测试计划名称
     */
    @TableField("fAlgorithmTestPlanName")
    private String algorithmTestPlanName;

    /**
     * 操作组合名称
     */
    @TableField("fOperationCombineName")
    private String operationCombineName;

    /**
     * 机器分组
     */
    @TableField("fMachineGroup")
    private String machineGroup;

    /**
     * 测试间隔日期（每隔多少天生成一批测试数据）
     */
    @TableField("fTestIntervalDay")
    private Integer testIntervalDay;

    /**
     * 测试词数
     */
    @TableField("fTestKeywordCount")
    private Integer testKeywordCount;

    /**
     * 测试词排名区间
     */
    @TableField("fTestkeywordRankBegin")
    private String testkeywordRankBegin;

    /**
     * 测试词排名区间
     */
    @TableField("fTestkeywordRankEnd")
    private String testkeywordRankEnd;

    /**
     * 刷量
     */
    @TableField("fOptimizePlanCount")
    private Integer optimizePlanCount;

    /**
     * 状态 0：暂停 1：激活
     */
    @TableField("fStatus")
    private Integer status;

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

    public String getAlgorithmTestPlanName() {
        return algorithmTestPlanName;
    }

    public void setAlgorithmTestPlanName(String algorithmTestPlanName) {
        this.algorithmTestPlanName = algorithmTestPlanName;
    }

    public String getOperationCombineName() {
        return operationCombineName;
    }

    public void setOperationCombineName(String operationCombineName) {
        this.operationCombineName = operationCombineName;
    }

    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }

    public Integer getTestIntervalDay() {
        return testIntervalDay;
    }

    public void setTestIntervalDay(Integer testIntervalDay) {
        this.testIntervalDay = testIntervalDay;
    }

    public Integer getTestKeywordCount() {
        return testKeywordCount;
    }

    public void setTestKeywordCount(Integer testKeywordCount) {
        this.testKeywordCount = testKeywordCount;
    }

    public String getTestkeywordRankBegin() {
        return testkeywordRankBegin;
    }

    public void setTestkeywordRankBegin(String testkeywordRankBegin) {
        this.testkeywordRankBegin = testkeywordRankBegin;
    }

    public String getTestkeywordRankEnd() {
        return testkeywordRankEnd;
    }

    public void setTestkeywordRankEnd(String testkeywordRankEnd) {
        this.testkeywordRankEnd = testkeywordRankEnd;
    }

    public Integer getOptimizePlanCount() {
        return optimizePlanCount;
    }

    public void setOptimizePlanCount(Integer optimizePlanCount) {
        this.optimizePlanCount = optimizePlanCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
