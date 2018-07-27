package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.ClientCookie;
import com.keymanager.monitoring.entity.Cookie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClientCookieDao extends BaseMapper<ClientCookie> {

    ClientCookie findClientCookieByClientID(@Param("clientID")String clientID);

}
