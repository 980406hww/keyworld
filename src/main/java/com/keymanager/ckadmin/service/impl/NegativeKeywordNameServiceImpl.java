package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.NegativeKeywordNameCriteria;
import com.keymanager.ckadmin.dao.NegativeKeywordNameDao;
import com.keymanager.ckadmin.entity.NegativeKeywordName;
import com.keymanager.ckadmin.service.NegativeKeywordNameService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("negativeKeywordNameService2")
public class NegativeKeywordNameServiceImpl extends ServiceImpl<NegativeKeywordNameDao, NegativeKeywordName> implements NegativeKeywordNameService {

    @Resource(name = "negativeKeywordNameDao2")
    private NegativeKeywordNameDao negativeKeywordNameDao;

    @Override
    public Page<NegativeKeywordName> searchNegativeKeywordNames(NegativeKeywordNameCriteria criteria) {
        Page<NegativeKeywordName> page = new Page<>(criteria.getPage(), criteria.getLimit());
        page.setOrderByField("fUpdateTime");
        Wrapper<NegativeKeywordName> wrapper = new EntityWrapper<>();
        if (null != criteria.getGroup() && !"".equals(criteria.getGroup())) {
            wrapper.eq("fGroup", criteria.getGroup());
        }
        if (null != criteria.getHasEmail() && !"".equals(criteria.getHasEmail())) {
            wrapper.isNotNull("fEmail");
            wrapper.where("fEmail != {0}", "");
        }
        return selectPage(page, wrapper);
    }

    @Override
    public List<String> getGroups() {
        return negativeKeywordNameDao.getGroups();
    }

    @Override
    public List<NegativeKeywordName> findAllNegativeKeywordName(NegativeKeywordNameCriteria criteria) {
        Wrapper<NegativeKeywordName> wrapper = new EntityWrapper<>();
        wrapper.eq("fGroup", criteria.getGroup());
        if (null != criteria.getHasEmail() && !"".equals(criteria.getHasEmail())) {
            wrapper.isNotNull("fEmail");
            wrapper.where("fEmail != {0}", "");
        }
        return negativeKeywordNameDao.selectList(wrapper);
    }
}
