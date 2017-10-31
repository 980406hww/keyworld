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
import com.keymanager.util.common.StringUtil;
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

    public List<String> getNegativeGroup() {
        return negativeKeywordNameDao.getNegativeGroup();
    }

    public void insertBatchByTxtFile(File file, String group) throws Exception {
        List<String> companyNames = FileUtil.readTxtFile(file,"UTF-8");
        int rowCount = 5000;
        int listSize = companyNames.size();
        if(!Utils.isEmpty(companyNames)) {
            if(listSize < rowCount) {
                negativeKeywordNameDao.insertBatchByList(group, companyNames);
            } else {
                int insertCount = listSize / rowCount;
                for(int i = 0; i < insertCount; i++) {
                    negativeKeywordNameDao.insertBatchByList(group, companyNames.subList(0, rowCount));
                    companyNames.subList(0, rowCount).clear();
                }
                if(!Utils.isEmpty(companyNames)) {
                    negativeKeywordNameDao.insertBatchByList(group, companyNames);
                }
            }
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

    public void updateNegativeKeywordName(NegativeInfoVO negativeInfoVO) {
        int negativeCount = negativeInfoVO.getNegativeInfos().size();
        NegativeKeywordName negativeKeywordName = negativeKeywordNameDao.selectById(negativeInfoVO.getUuid());
        if(TerminalTypeEnum.PC.name().equals(negativeInfoVO.getTerminalType())) {
            negativeKeywordName.setRankCaptured(true);
            negativeKeywordName.setSelectCaptured(true);
            negativeKeywordName.setRelevantCaptured(true);
            negativeKeywordName.setRankExistNegative(negativeCount > 0 ? true : false);
            negativeKeywordName.setRankNegativeCount(negativeCount);

            if(StringUtils.isBlank(negativeInfoVO.getOfficialWebsiteUrl())) {
                negativeKeywordName.setOfficialUrl(null);
            } else {
                negativeKeywordName.setOfficialUrl(negativeInfoVO.getOfficialWebsiteUrl());
            }

            if(StringUtils.isBlank(negativeInfoVO.getEmailAddress())) {
                negativeKeywordName.setEmail(null);
            } else {
                negativeKeywordName.setEmail(negativeInfoVO.getEmailAddress());
            }

            if(StringUtils.isBlank(negativeInfoVO.getSuggestionNegativeKeyword())) {
                negativeKeywordName.setSelectExistNegative(false);
                negativeKeywordName.setSelectNegativeKeyword(null);
            } else {
                negativeKeywordName.setSelectExistNegative(true);
                negativeKeywordName.setSelectNegativeKeyword(negativeInfoVO.getSuggestionNegativeKeyword());
            }

            if(StringUtils.isBlank(negativeInfoVO.getRelativeNegativeKeyword())) {
                negativeKeywordName.setRelevantExistNegative(false);
                negativeKeywordName.setRelevantNegativeKeyword(null);
            } else {
                negativeKeywordName.setRelevantExistNegative(true);
                negativeKeywordName.setRelevantNegativeKeyword(negativeInfoVO.getRelativeNegativeKeyword());
            }
        } else {
            negativeKeywordName.setPhoneCaptured(true);
            negativeKeywordName.setPhoneRankNegativeCount(negativeCount);
            if(StringUtils.isBlank(negativeInfoVO.getSuggestionNegativeKeyword())) {
                negativeKeywordName.setPhoneSelectExistNegative(false);
                negativeKeywordName.setPhoneSelectNegativeKeyword(null);
            } else {
                negativeKeywordName.setPhoneSelectExistNegative(true);
                negativeKeywordName.setPhoneSelectNegativeKeyword(negativeInfoVO.getSuggestionNegativeKeyword());
            }

            if(StringUtils.isBlank(negativeInfoVO.getRelativeNegativeKeyword())) {
                negativeKeywordName.setPhoneRelevantExistNegative(false);
                negativeKeywordName.setPhoneRelevantNegativeKeyword(null);
            } else {
                negativeKeywordName.setPhoneRelevantExistNegative(true);
                negativeKeywordName.setPhoneRelevantNegativeKeyword(negativeInfoVO.getRelativeNegativeKeyword());
            }
        }
        negativeKeywordNameDao.updateById(negativeKeywordName);
    }
}
