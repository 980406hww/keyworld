package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CookieCriteria;
import com.keymanager.monitoring.entity.Cookie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CookieDao extends BaseMapper<Cookie> {

    List<Cookie> searchCookies(Page<Cookie> page, @Param("cookieCriteria")CookieCriteria cookieCriteria);

    Cookie getCookie(@Param("searchEngine")String searchEngine);
}
