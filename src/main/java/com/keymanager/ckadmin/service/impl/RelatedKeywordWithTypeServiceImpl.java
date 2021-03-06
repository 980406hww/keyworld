package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.RelatedKeywordWithTypeCriteria;
import com.keymanager.ckadmin.dao.RelatedKeywordWithTypeDao;
import com.keymanager.ckadmin.entity.RelatedKeyWordWithType;
import com.keymanager.ckadmin.service.RelatedKeywordWithTypeService;
import java.util.Date;
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

    @Override
    public void saveRelatedKeywordWithType(RelatedKeywordWithTypeCriteria criteria) {
        RelatedKeyWordWithType relatedKeyWordWithTypeCriteria = new RelatedKeyWordWithType();
        relatedKeyWordWithTypeCriteria.setMainKeyword(criteria.getMainKeyword());
        relatedKeyWordWithTypeCriteria.setRelatedKeyword(criteria.getRelatedKeyword());
        RelatedKeyWordWithType existRelatedKeywordWithType = relatedKeywordWithTypeDao.selectOne(relatedKeyWordWithTypeCriteria);

        if (existRelatedKeywordWithType == null) {
            relatedKeyWordWithTypeCriteria.setCreateTime(new Date());
            relatedKeyWordWithTypeCriteria.setType(criteria.getType());
            relatedKeywordWithTypeDao.insert(relatedKeyWordWithTypeCriteria);
        } else {
            existRelatedKeywordWithType.setUpdateTime(new Date());
            existRelatedKeywordWithType.setType(criteria.getType());
            relatedKeywordWithTypeDao.updateById(existRelatedKeywordWithType);
        }
    }

    @Override
    public void deleteRelatedKeywordWithType(String mainKeyword, String relatedKeyword) {
        relatedKeywordWithTypeDao.deleteRelatedKeywordWithType(mainKeyword, relatedKeyword);
    }
}
