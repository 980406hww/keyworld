package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.QzChargeMon;
import com.keymanager.ckadmin.vo.QZChargeMonCountVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("qzChargeMonDao2")
public interface QzChargeMonDao extends BaseMapper<QzChargeMon> {

    List<QZChargeMonCountVO> getQZChargeMonData(@Param("condition") Map<String, Object> condition);
}
