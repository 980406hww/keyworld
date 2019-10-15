package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.NegativeKeywordName;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository("negativeKeywordNameDao2")
public interface NegativeKeywordNameDao extends BaseMapper<NegativeKeywordName> {

    List<String> getGroups();
}
