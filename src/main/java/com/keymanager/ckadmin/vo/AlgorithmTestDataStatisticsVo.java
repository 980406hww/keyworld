package com.keymanager.ckadmin.vo;

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
public class AlgorithmTestDataStatisticsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 算法执行统计信息表ID
     */
    private Integer uuid;

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
     * 用户联系人名称
     */
    private String contactPerson;
    /**
     * 首页个数
     */
    private Integer topTenCount;

    /**
     * 没刷量个数
     */
    private Integer zeroOptimizedCount;

    /**
     * 排名首页率
     */
    private String rankChangeRate;

    /**
     * 数据记录日期
     */
    private Date recordDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
