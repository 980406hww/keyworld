package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.Cookie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CookieDao extends BaseMapper<Cookie> {

    void updateCookieStatus(@Param("cookies") List<Cookie> cookies, @Param("status") String status);

    List<Cookie> findCookies(@Param("total")int total, @Param("searchEngine")String searchEngine, @Param("status")String status);
}
