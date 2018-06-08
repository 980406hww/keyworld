package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplicationMarketCriteria;
import com.keymanager.monitoring.entity.ApplicationMarket;
import com.keymanager.monitoring.entity.ServerAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * Created by shunshikj22 on 2017/9/5.
 */
public interface ApplicationMarketDao extends BaseMapper<ApplicationMarket> {

    List<ApplicationMarket> getmarketInfo();

    List<ApplicationMarket> searchpplicationMarket(Page<ApplicationMarket> page,@Param("applicationMarketCriteria") ApplicationMarketCriteria applicationMarketCriteria);
}
