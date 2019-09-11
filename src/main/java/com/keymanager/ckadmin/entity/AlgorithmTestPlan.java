package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
public class AlgorithmTestPlan {

    private static final long serialVersionUID = 1L;

    /**
     * 算法测试计划id
     */
    @TableId(value = "fUuid", type = IdType.AUTO)
    private Long uuid;

    /**
     * 算法测试计划名称
     */
    @NotNull
    @TableField("fAlgorithmTestPlanName")
    private String algorithmTestPlanName;

    /**
     * 操作组合名称
     */
    @NotNull
    @TableField("fOperationCombineName")
    private String operationCombineName;

    /**
     * 终端类型
     */
    @NotNull
    @TableField("fTerminalType")
    private String terminalType;

    /**
     * 搜索引擎
     */
    @NotNull
    @TableField("fSearchEngine")
    private String searchEngine;

    /**
     * 机器分组
     */
    @NotNull
    @TableField("fMachineGroup")
    private String machineGroup;

    /**
     * 测试间隔日期（每隔多少天生成一批测试数据）
     */
    @NotNull
    @TableField("fTestIntervalDay")
    private Integer testIntervalDay;

    /**
     * 测试词数
     */
    @NotNull
    @TableField("fTestKeywordCount")
    private Integer testKeywordCount;

    /**
     * 测试词排名区间
     */
    @NotNull
    @Pattern(regexp = "^\\d+$", message = "请输入数字")
    @TableField("fTestkeywordRankBegin")
    private String testkeywordRankBegin;

    /**
     * 测试词排名区间
     */
    @NotNull
    @Pattern(regexp = "^\\d+$", message = "请输入数字")
    @TableField("fTestkeywordRankEnd")
    private String testkeywordRankEnd;

    /**
     * 刷量
     */
    @NotNull
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

    public String getOperationCombineName() {
        return operationCombineName;
    }

    public void setOperationCombineName(String operationCombineName) {
        this.operationCombineName = operationCombineName;
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

    @Override
    public String toString() {
        return "AlgorithmTestPlan{" +
            "uuid=" + uuid +
            ", algorithmTestPlanName='" + algorithmTestPlanName + '\'' +
            ", operationCombineName='" + operationCombineName + '\'' +
            ", terminalType='" + terminalType + '\'' +
            ", searchEngine='" + searchEngine + '\'' +
            ", machineGroup='" + machineGroup + '\'' +
            ", testIntervalDay=" + testIntervalDay +
            ", testKeywordCount=" + testKeywordCount +
            ", testkeywordRankBegin='" + testkeywordRankBegin + '\'' +
            ", testkeywordRankEnd='" + testkeywordRankEnd + '\'' +
            ", optimizePlanCount=" + optimizePlanCount +
            ", status=" + status +
            ", executeQueryTime=" + executeQueryTime +
            ", excuteStatus=" + excuteStatus +
            ", createBy='" + createBy + '\'' +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }
}
