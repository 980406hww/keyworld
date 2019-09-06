package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;

/**
 * <p>
 * 算法执行统计信息表
 * </p>
 *
 * @author lhc
 * @since 2019-08-27
 */
@TableName("t_algorithm_test_data_statistics")
public class AlgorithmTestDataStatistics {

    private static final long serialVersionUID = 1L;

    /**
     * 算法执行统计信息表ID
     */
    @TableId(value = "fUuid", type = IdType.AUTO)
    private Integer uuid;

     /**
     * 算法测试计划ID
     */
    @TableField("fAlgorithmTestPlanUuid")
    private String algorithmTestPlanUuid;

    /**
     * 算法测试任务ID
     */
    @TableField("fAlgorithmTestTaskUuid")
    private String algorithmTestTaskUuid;

    /**
     * 用户联系人名称
     */
    @TableField("fContactPerson")
    private String contactPerson;
    /**
     * 首页个数
     */
    @TableField("fTopTenCount")
    private Integer topTenCount;

    /**
     * 没刷量个数
     */
    @TableField("fZeroOptimizedCount")
    private Integer zeroOptimizedCount;

    /**
     * 排名首页率
     */
    @TableField("fRankChangeRate")
    private String rankChangeRate;

    /**
     * 数据记录日期
     */
    @TableField("fRecordDate")
    private Date recordDate;

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

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public String getAlgorithmTestPlanUuid() {
        return algorithmTestPlanUuid;
    }

    public void setAlgorithmTestPlanUuid(String algorithmTestPlanUuid) {
        this.algorithmTestPlanUuid = algorithmTestPlanUuid;
    }

    public String getAlgorithmTestTaskUuid() {
        return algorithmTestTaskUuid;
    }

    public void setAlgorithmTestTaskUuid(String algorithmTestTaskUuid) {
        this.algorithmTestTaskUuid = algorithmTestTaskUuid;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Integer getTopTenCount() {
        return topTenCount;
    }

    public void setTopTenCount(Integer topTenCount) {
        this.topTenCount = topTenCount;
    }

    public Integer getZeroOptimizedCount() {
        return zeroOptimizedCount;
    }

    public void setZeroOptimizedCount(Integer zeroOptimizedCount) {
        this.zeroOptimizedCount = zeroOptimizedCount;
    }

    public String getRankChangeRate() {
        return rankChangeRate;
    }

    public void setRankChangeRate(String rankChangeRate) {
        this.rankChangeRate = rankChangeRate;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
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
