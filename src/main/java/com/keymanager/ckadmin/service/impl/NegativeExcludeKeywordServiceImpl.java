package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.NegativeExcludeKeywordDao;
import com.keymanager.ckadmin.entity.NegativeExcludeKeyword;
import com.keymanager.ckadmin.service.NegativeExcludeKeywordService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("negativeExcludeKeywordService2")
public class NegativeExcludeKeywordServiceImpl extends ServiceImpl<NegativeExcludeKeywordDao, NegativeExcludeKeyword> implements NegativeExcludeKeywordService {

    @Resource(name = "negativeExcludeKeywordDao2")
    private NegativeExcludeKeywordDao negativeExcludeKeywordDao;

    @Override
    public List<String> getNegativeExcludeKeyword() {
        return negativeExcludeKeywordDao.getNegativeExcludeKeyword();
    }
}
