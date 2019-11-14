package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.NegativeKeywordNameCriteria;
import com.keymanager.ckadmin.dao.NegativeKeywordNameDao;
import com.keymanager.ckadmin.entity.NegativeKeywordName;
import com.keymanager.ckadmin.service.NegativeKeywordNameService;
import com.keymanager.ckadmin.util.FileUtil;
import com.keymanager.ckadmin.util.Utils;
import java.io.File;
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
        if (null != criteria.getInit() && "init".equals(criteria.getInit())) {
            wrapper.where("1 != 1", "");
        } else {
            if (null != criteria.getGroup() && !"".equals(criteria.getGroup())) {
                wrapper.eq("fGroup", criteria.getGroup());
            }
            if (null != criteria.getHasEmail() && !"".equals(criteria.getHasEmail())) {
                wrapper.isNotNull("fEmail");
                wrapper.where("fEmail != {0}", "");
            }
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

    @Override
    public void insertBatchByTxtFile(File file, String group) {
        List<String> companyNames = FileUtil.readTxtFile(file, FileUtil.getFileCharset(file));
        int rowCount = 5000;
        if (!Utils.isEmpty(companyNames)) {
            int listSize = companyNames.size();
            int insertCount = listSize / 5000;
            for (int i = 0; i < insertCount; i++) {
                negativeKeywordNameDao.insertBatchByList(group, companyNames.subList(0, rowCount));
                companyNames.subList(0, rowCount).clear();
            }
            if (!Utils.isEmpty(companyNames)) {
                negativeKeywordNameDao.insertBatchByList(group, companyNames);
            }

        }
    }
}
