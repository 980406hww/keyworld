package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.NegativeExcludeKeyword;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository("negativeExcludeKeywordDao2")
public interface NegativeExcludeKeywordDao extends BaseMapper<NegativeExcludeKeyword> {

    List<String> getNegativeExcludeKeyword();
}
