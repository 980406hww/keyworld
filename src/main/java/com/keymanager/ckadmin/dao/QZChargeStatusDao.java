package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.QZChargeStatus;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 收费状态表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-09-21
 */
@Repository("qzChargeStatusDao2")
public interface QZChargeStatusDao extends BaseMapper<QZChargeStatus> {

}
