package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.NegativeSiteContactKeywordDao;
import com.keymanager.ckadmin.entity.NegativeSiteContactKeyword;
import com.keymanager.ckadmin.service.NegativeSiteContactKeywordService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("negativeSiteContactKeywordService2")
public class NegativeSiteContactKeywordServiceImpl extends ServiceImpl<NegativeSiteContactKeywordDao, NegativeSiteContactKeyword> implements NegativeSiteContactKeywordService {

    @Resource(name = "negativeSiteContactKeywordDao2")
    private NegativeSiteContactKeywordDao negativeSiteContactKeywordDao;

    @Override
    public List<String> getContactKeyword() {
        return negativeSiteContactKeywordDao.getContactKeyword();
    }
}
