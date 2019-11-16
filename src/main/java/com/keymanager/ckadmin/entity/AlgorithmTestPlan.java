package com.keymanager.ckadmin.entity;

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
     * 操作组合id
     */
    @TableField("fOperationCombineId")
    private Long operationCombineId;

    /**
     * 操作组合名称
     */
    @TableField(exist = false)
    private String operationCombineName;

    /**
     * 终端类型
     */
    @TableField("fTerminalType")
    private String terminalType;

    /**
     * 搜索引擎
     */
    @TableField("fSearchEngine")
    private String searchEngine;

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
     * 外部取算法时间
     */
    @TableField("fExecuteQueryTime")
    private Date executeQueryTime;

    /**
     * 状态 0：暂停 1：激活
     */
    @TableField("fExcuteStatus")
    private Integer excuteStatus;

    /**
     * 执行类型 0:一次、1:多次
     */
    @TableField("fExcuteType")
    private Integer excuteType;

    /**
     * 执行次数
     */
    @TableField("fExcuteCount")
    private Integer excuteCount;
    /**
     * 创建人
     */
    @TableField("fCreateBy")
    private String createBy;

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

    public Long getOperationCombineId() {
        return operationCombineId;
    }

    public void setOperationCombineId(Long operationCombineId) {
        this.operationCombineId = operationCombineId;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
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

    public Date getExecuteQueryTime() {
        return executeQueryTime;
    }

    public void setExecuteQueryTime(Date executeQueryTime) {
        this.executeQueryTime = executeQueryTime;
    }

    public Integer getExcuteStatus() {
        return excuteStatus;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setExcuteStatus(Integer excuteStatus) {
        this.excuteStatus = excuteStatus;
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

    public Integer getExcuteType() {
        return excuteType;
    }

    public void setExcuteType(Integer excuteType) {
        this.excuteType = excuteType;
    }

    public Integer getExcuteCount() {
        return excuteCount;
    }

    public void setExcuteCount(Integer excuteCount) {
        this.excuteCount = excuteCount;
    }

    public String getOperationCombineName() {
        return operationCombineName;
    }

    public void setOperationCombineName(String operationCombineName) {
        this.operationCombineName = operationCombineName;
    }
}
