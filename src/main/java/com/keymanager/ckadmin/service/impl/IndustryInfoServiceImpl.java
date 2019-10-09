package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.IndustryCriteria;
import com.keymanager.ckadmin.criteria.IndustryDetailCriteria;
import com.keymanager.ckadmin.dao.IndustryInfoDao;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.IndustryInfo;
import com.keymanager.ckadmin.excel.operator.AbstractExcelReader;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.IndustryDetailService;
import com.keymanager.ckadmin.service.IndustryInfoService;
import com.keymanager.ckadmin.vo.IndustryInfoVO;
import com.keymanager.monitoring.common.utils.BeanUtils;
import com.keymanager.util.Constants;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("industryInfoService2")
public class IndustryInfoServiceImpl extends ServiceImpl<IndustryInfoDao, IndustryInfo> implements IndustryInfoService {
    @Resource(name = "industryInfoDao2")
    private IndustryInfoDao industryInfoDao;

    @Resource(name = "industryDetailService2")
    private IndustryDetailService industryDetailService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Override
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

    @Override
    public IndustryInfo getIndustry (long uuid) {
        return industryInfoDao.selectById(uuid);
    }

    @Override
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

    @Override
    public void delIndustryInfo (long uuid) {
        industryInfoDao.deleteById(uuid);
        industryDetailService.delIndustryDetailsByIndustryID(uuid);
    }

    @Override
    public void updateIndustryUserID (List<String> uuids, String userID) {
        industryInfoDao.updateIndustryUserID(uuids, userID);
    }

    @Override
    public void deleteIndustries(String uuids) {
        industryInfoDao.deleteIndustries(Arrays.asList(uuids.split(",")));
    }

    public synchronized Map getValidIndustryInfo() {
        IndustryInfoVO industryInfoVo = industryInfoDao.getValidIndustryInfo();
        if (null != industryInfoVo) {
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
        return null;
    }

    @Override
    public void updateIndustryInfoDetail(IndustryDetailCriteria criteria) {
        industryDetailService.updateIndustryInfoDetail(criteria);
    }

    @Override
    public void updateIndustryStatus(List<String> uuids) {
        industryInfoDao.updateIndustryStatus(uuids);
    }

    @Override
    public void updateIndustryInfoStatus(long uuid) {
        IndustryInfo industryInfo = industryInfoDao.selectById(uuid);
        industryInfo.setStatus(2);
        industryInfoDao.updateById(industryInfo);
    }

    @Override
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

    @Override
    public List<Map> getIndustryInfos(List<String> uuids) {
        return industryDetailService.getIndustryInfos(uuids);
    }
}
