package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.NegativeKeywordNameCriteria;
import com.keymanager.monitoring.criteria.NegativeListCriteria;
import com.keymanager.monitoring.dao.NegativeKeywordNameDao;
import com.keymanager.monitoring.dao.NegativeListDao;
import com.keymanager.monitoring.entity.NegativeKeywordName;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.vo.NegativeInfoVO;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/10/24.
 */
@Service
public class NegativeKeywordNameService extends ServiceImpl<NegativeKeywordNameDao, NegativeKeywordName> {

    private static Logger logger = LoggerFactory.getLogger(NegativeKeywordNameService.class);

    @Autowired
    private NegativeKeywordNameDao negativeKeywordNameDao;

    public Page<NegativeKeywordName> searchNegativeKeywordNames(Page<NegativeKeywordName> page, NegativeKeywordNameCriteria negativeKeywordNameCriteria) {
        page.setRecords(negativeKeywordNameDao.searchNegativeKeywordNames(page, negativeKeywordNameCriteria));
        return page;
    }

    public List<NegativeKeywordName> findAllNegativeKeywordName(NegativeKeywordNameCriteria negativeKeywordNameCriteria) {
        return negativeKeywordNameDao.searchNegativeKeywordNames(negativeKeywordNameCriteria);
    }

    public void insertBatchByTxtFile(File file, String group) throws Exception {
        List<String> companyNames = FileUtil.readTxtFile(file,"UTF-8");
        for (String companyName : companyNames) {
            NegativeKeywordName negativeKeywordName = new NegativeKeywordName();
            negativeKeywordName.setGroup(group);
            negativeKeywordName.setName(companyName);
            negativeKeywordName.setHandled(false);
            negativeKeywordNameDao.addNegativeKeywordName(negativeKeywordName);
        }
    }

    public NegativeKeywordName getNegativeKeywordName(String type, String group) {
        NegativeKeywordName negativeKeywordName = null;
        if(type.equals(TerminalTypeEnum.PC.name())) {
            negativeKeywordName = negativeKeywordNameDao.getPCNegativeKeywordName(group);
        } else {
            negativeKeywordName = negativeKeywordNameDao.getPhoneNegativeKeywordName(group);
        }
        return negativeKeywordName;
    }

    public void updateNegativeQueryStatus(String type, Long uuid) {
        NegativeKeywordName negativeKeywordName = negativeKeywordNameDao.selectById(uuid);
        if(type.equals(TerminalTypeEnum.PC.name())) {
            negativeKeywordName.setRankQueried(true);
            negativeKeywordName.setSelectQueried(true);
            negativeKeywordName.setRelevantQueried(true);
        } else {
            negativeKeywordName.setPhoneQueried(true);
        }
        negativeKeywordNameDao.updateById(negativeKeywordName);
    }

    public void updateOfficialUrlAndEmail(NegativeInfoVO negativeInfoVO) {
        boolean updateFalg = false;
        NegativeKeywordName negativeKeywordName = negativeKeywordNameDao.selectById(negativeInfoVO.getUuid());
        if(StringUtils.isNotBlank(negativeInfoVO.getOfficialWebsiteUrl()) && StringUtils.isBlank(negativeKeywordName.getOfficialUrl())){
            negativeKeywordName.setOfficialUrl(negativeInfoVO.getOfficialWebsiteUrl());
            updateFalg = true;
        }

        if(StringUtils.isNotBlank(negativeInfoVO.getEmailAddress()) && StringUtils.isBlank(negativeKeywordName.getEmail())) {
            negativeKeywordName.setEmail(negativeInfoVO.getEmailAddress());
            updateFalg = true;
        }

        if(updateFalg) {
            negativeKeywordNameDao.updateById(negativeKeywordName);
        }
    }

    public void updateNegativeKeywordNameByType(String type, NegativeInfoVO negativeInfoVO) {
        int negativeCount = 0;
        NegativeKeywordName negativeKeywordName = negativeKeywordNameDao.selectById(negativeInfoVO.getUuid());
        // TODO
        if(TerminalTypeEnum.PC.name().equals(type)) {
            negativeKeywordName.setRankExistNegative(negativeInfoVO.getNegativeInfos().size() > 0 ? true : false);

        } else {

        }
    }
}
