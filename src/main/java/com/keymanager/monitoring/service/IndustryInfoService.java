package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.common.utils.BeanUtils;
import com.keymanager.monitoring.criteria.IndustryCriteria;
import com.keymanager.monitoring.criteria.IndustryDetailCriteria;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.IndustryInfo;
import com.keymanager.monitoring.dao.IndustryInfoDao;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.excel.operator.AbstractExcelReader;
import com.keymanager.monitoring.vo.IndustryInfoVO;
import com.keymanager.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行业表 服务实现类
 * </p>
 *
 * @author zhoukai
 * @since 2019-07-05
 */
@Service
public class IndustryInfoService extends ServiceImpl<IndustryInfoDao, IndustryInfo> {

    @Autowired
    private IndustryInfoDao industryInfoDao;

    @Autowired
    private IndustryDetailService industryDetailService;

    @Autowired
    private ConfigService configService;

    public Page<IndustryInfo> searchIndustries (Page<IndustryInfo> page, IndustryCriteria industryCriteria) {
        page.setRecords(industryInfoDao.searchIndustries(page, industryCriteria));
        this.otherInfoForIndustryInfo(page);
        return page;
    }

    private void otherInfoForIndustryInfo(Page<IndustryInfo> page) {
        for (IndustryInfo industryInfo : page.getRecords()) {
            industryInfo.setDetailCount(industryDetailService.findIndustryDetailCount(industryInfo.getUuid()));
        }
    }

    public IndustryInfo getIndustry (long uuid) {
        return industryInfoDao.selectById(uuid);
    }

    public void saveIndustryInfo (IndustryInfo industryInfo, String loginName) {
        if(null == industryInfo.getUuid()) {
            IndustryInfo existingIndustryInfo = industryInfoDao.findExistingIndustryInfo(industryInfo.getIndustryName());
            if(null == existingIndustryInfo) {
                industryInfo.setUserID(loginName);
                industryInfoDao.insert(industryInfo);
            }
        } else {
            industryInfo.setUpdateTime(new Date());
            industryInfoDao.updateById(industryInfo);
        }
    }

    public void delIndustryInfo (long uuid) {
        industryInfoDao.deleteById(uuid);
        industryDetailService.delIndustryDetailsByIndustryID(uuid);
    }

    public void updateIndustryUserID (List<String> uuids, String userID) {
        industryInfoDao.updateIndustryUserID(uuids, userID);
    }

    public void deleteIndustries(String uuids) {
        industryInfoDao.deleteIndustries(Arrays.asList(uuids.split(",")));
    }

    public Map getValidIndustryInfo() {
        IndustryInfoVO industryInfoVo = industryInfoDao.getValidIndustryInfo();
        IndustryInfo industryInfo = industryInfoDao.selectById(industryInfoVo.getUuid());
        industryInfo.setStatus(1);
        industryInfo.setUpdateTime(new Date());
        industryInfoDao.updateById(industryInfo);

        Config telConfig = configService.getConfig(Constants.CONFIG_TYPE_INDUSTRY_TEL_REG, Constants.CONFIG_KEY_INDUSTRY_TEL_REG);
        Config qqConfig = configService.getConfig(Constants.CONFIG_TYPE_INDUSTRY_QQ_REG, Constants.CONFIG_KEY_INDUSTRY_QQ_REG);
        industryInfoVo.setTelReg(telConfig.getValue());
        industryInfoVo.setQqReg(qqConfig.getValue());
        return BeanUtils.toMap(industryInfoVo);
    }

    public void updateIndustryInfoDetail(IndustryDetailCriteria criteria) {
        industryDetailService.updateIndustryInfoDetail(criteria);
    }

    public void updateIndustryStatus(List<String> uuids) {
        industryInfoDao.updateIndustryStatus(uuids);
    }

    public void updateIndustryInfoStatus(long uuid) {
        IndustryInfo industryInfo = industryInfoDao.selectById(uuid);
        industryInfo.setStatus(2);
        industryInfoDao.updateById(industryInfo);
    }

    public boolean handleExcel(InputStream inputStream, String excelType, String terminalType, String userName)
            throws Exception {
        AbstractExcelReader reader = AbstractExcelReader.createExcelOperator(inputStream, excelType);
        List<IndustryInfo> industryInfos = reader.readIndustryDataFromExcel();
        this.saveIndustryInfos(industryInfos, userName, terminalType);
        return true;
    }

    private void saveIndustryInfos(List<IndustryInfo> industryInfos, String userName, String terminalType) {
        for (IndustryInfo industryInfo : industryInfos) {
            industryInfo.setTerminalType(terminalType);
            industryInfo.setStatus(0);
            this.saveIndustryInfo(industryInfo, userName);
        }
    }
}
