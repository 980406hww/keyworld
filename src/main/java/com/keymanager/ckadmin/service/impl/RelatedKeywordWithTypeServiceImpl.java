package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.RelatedKeywordWithTypeDao;
import com.keymanager.ckadmin.entity.RelatedKeyWordWithType;
import com.keymanager.ckadmin.service.RelatedKeywordWithTypeService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("relatedKeywordWithTypeService2")
public class RelatedKeywordWithTypeServiceImpl extends ServiceImpl<RelatedKeywordWithTypeDao, RelatedKeyWordWithType> implements RelatedKeywordWithTypeService {

    @Resource(name = "relatedKeywordWithTypeDao2")
    private RelatedKeywordWithTypeDao relatedKeywordWithTypeDao;

    @Override
    public List<RelatedKeyWordWithType> findRelatedKeywordWithType(String mainKeyword) {
        return relatedKeywordWithTypeDao.findRelatedKeywordWithType(mainKeyword);
    }
}
