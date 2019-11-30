package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.NegativeKeywordDao;
import com.keymanager.ckadmin.entity.NegativeKeyword;
import com.keymanager.ckadmin.service.NegativeKeywordService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("negativeKeywordService2")
public class NegativeKeywordServiceImpl extends ServiceImpl<NegativeKeywordDao, NegativeKeyword> implements NegativeKeywordService {

    @Resource(name = "negativeKeywordDao2")
    private NegativeKeywordDao negativeKeywordDao;

    @Override
    public List<String> getNegativeKeyword() {
        return negativeKeywordDao.getNegativeKeyword();
    }
}
