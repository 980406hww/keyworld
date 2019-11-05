package com.keymanager.ckadmin.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.keymanager.ckadmin.entity.AlgorithmTestDataStatistics;
import java.util.Date;

/**
 * <p>
 * 算法执行统计信息表
 * </p>
 *
 * @author lhc
 * @since 2019-08-27
 */
public class AlgorithmTestDataStatisticsVo extends AlgorithmTestDataStatistics {

    private static final long serialVersionUID = 1L;

     /**
     * 算法测试关键字分组
     */
    private String keywordGroup;

    /**
     * 算法测试客户名
     */
    private String customerName;

    /**
     * 算法测试实际词数量
     */
    private Integer actualKeywordCount;

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

    @Override
    public Integer getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getAlgorithmTestPlanUuid() {
        return algorithmTestPlanUuid;
    }

    @Override
    public void setAlgorithmTestPlanUuid(String algorithmTestPlanUuid) {
        this.algorithmTestPlanUuid = algorithmTestPlanUuid;
    }

    @Override
    public String getAlgorithmTestTaskUuid() {
        return algorithmTestTaskUuid;
    }

    @Override
    public void setAlgorithmTestTaskUuid(String algorithmTestTaskUuid) {
        this.algorithmTestTaskUuid = algorithmTestTaskUuid;
    }

    @Override
    public String getContactPerson() {
        return contactPerson;
    }

    @Override
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    @Override
    public Integer getTopTenCount() {
        return topTenCount;
    }

    @Override
    public void setTopTenCount(Integer topTenCount) {
        this.topTenCount = topTenCount;
    }

    @Override
    public Integer getZeroOptimizedCount() {
        return zeroOptimizedCount;
    }

    @Override
    public void setZeroOptimizedCount(Integer zeroOptimizedCount) {
        this.zeroOptimizedCount = zeroOptimizedCount;
    }

    @Override
    public String getRankChangeRate() {
        return rankChangeRate;
    }

    @Override
    public void setRankChangeRate(String rankChangeRate) {
        this.rankChangeRate = rankChangeRate;
    }

    @Override
    public Date getRecordDate() {
        return recordDate;
    }

    @Override
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "AlgorithmTestDataStatisticsVo{" +
                "keywordGroup='" + keywordGroup + '\'' +
                ", customerName='" + customerName + '\'' +
                ", actualKeywordCount=" + actualKeywordCount +
                ", uuid=" + uuid +
                ", algorithmTestPlanUuid='" + algorithmTestPlanUuid + '\'' +
                ", algorithmTestTaskUuid='" + algorithmTestTaskUuid + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", topTenCount=" + topTenCount +
                ", zeroOptimizedCount=" + zeroOptimizedCount +
                ", rankChangeRate='" + rankChangeRate + '\'' +
                ", recordDate=" + recordDate +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
