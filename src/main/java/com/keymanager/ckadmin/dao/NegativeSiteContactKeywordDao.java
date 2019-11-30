package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.NegativeSiteContactKeyword;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository("negativeSiteContactKeywordDao2")
public interface NegativeSiteContactKeywordDao extends BaseMapper<NegativeSiteContactKeyword> {

    List<String> getContactKeyword();
}
