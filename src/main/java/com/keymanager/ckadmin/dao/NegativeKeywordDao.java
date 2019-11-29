package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.NegativeKeyword;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository("negativeKeywordDao2")
public interface NegativeKeywordDao extends BaseMapper<NegativeKeyword> {

    List<String> getNegativeKeyword();
}
