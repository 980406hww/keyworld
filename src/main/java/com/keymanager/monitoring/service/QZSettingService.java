package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.db.DBUtil;
import com.keymanager.enums.CollectMethod;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.criteria.QZSettingSearchCriteria;
import com.keymanager.monitoring.dao.QZSettingDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.QZCaptureTitleLogStatusEnum;
import com.keymanager.monitoring.enums.QZSettingStatusEnum;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.vo.DateRangeTypeVO;
import com.keymanager.monitoring.vo.QZSettingVO;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordVO;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.text.DateFormat;
import java.util.*;

@Service
public class QZSettingService extends ServiceImpl<QZSettingDao, QZSetting> {
	private static Logger logger = LoggerFactory.getLogger(QZSettingService.class);

	@Autowired
	private QZSettingDao qzSettingDao;

	@Autowired
	private QZCaptureTitleLogService qzCaptureTitleLogService;

	@Autowired
	private QZOperationTypeService  qzOperationTypeService;

	@Autowired
	private QZChargeRuleService qzChargeRuleService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerKeywordService customerKeywordService;

	public QZSetting getAvailableQZSetting(){
		List<QZSetting> qzSettings = qzSettingDao.getAvailableQZSettings();
		QZSetting qzSetting = null;
		if(CollectionUtils.isNotEmpty(qzSettings)){
			qzSetting = qzSettings.get(0);
			startQZSettingForUpdateKeyword(qzSetting.getUuid());
		}
		return qzSetting;
	}

	//获取当前词量
	public QZSetting getQZSettingsForCaptureCurrentKeyword(){
		List<QZSetting> qzSettings = qzSettingDao.captureCurrentKeyword();
		QZSetting qzSetting = null;
		if(CollectionUtils.isNotEmpty(qzSettings)){
			qzSetting = qzSettings.get(0);
			startQZSettingForCaptureCurrentKeyword(qzSetting.getUuid());
		}
		return qzSetting;
	}

	public void startQZSettingForUpdateKeyword(Long uuid){
		QZSetting qzSetting = qzSettingDao.selectById(uuid);
		if(qzSetting != null){
			qzSetting.setUpdateStatus(QZSettingStatusEnum.Processing.getValue());
			qzSetting.setUpdateStartTime(new Date());
			qzSettingDao.updateById(qzSetting);
		}
	}
	//更新时间
	public void startQZSettingForCaptureCurrentKeyword(Long uuid){
		QZSetting qzSetting = qzSettingDao.selectById(uuid);
		if(qzSetting != null){
			qzSetting.setCaptureCurrentKeywordStatus(QZSettingStatusEnum.Processing.getValue());
			qzSetting.setCaptureCurrentKeywordCountTime(new Date());
			qzSettingDao.updateById(qzSetting);
		}
	}

	public void completeQZSetting(Long uuid, boolean downloadTimesUsed){
		QZSetting qzSetting = qzSettingDao.selectById(uuid);
		if(qzSetting != null){
			if(downloadTimesUsed){
				qzSetting.setUpdateStatus(QZSettingStatusEnum.DownloadTimesUsed.getValue());
			}else {
				qzSetting.setUpdateStatus(QZSettingStatusEnum.Completed.getValue());
			}
			qzSetting.setUpdateEndTime(new Date());
			qzSettingDao.updateById(qzSetting);
		}
	}

	public void saveQZSetting(QZSetting qzSetting){
		if(qzSetting.getUuid() != null){
			//修改qzSetting表
			QZSetting existingQZSetting = qzSettingDao.selectById(qzSetting.getUuid());
			existingQZSetting.setDomain(qzSetting.getDomain());
			existingQZSetting.setUpdateStatus(qzSetting.getUpdateStatus());
			existingQZSetting.setCustomerUuid(qzSetting.getCustomerUuid());
			existingQZSetting.setPcGroup(qzSetting.getPcGroup());
			existingQZSetting.setPhoneGroup(qzSetting.getPhoneGroup());
			existingQZSetting.setType(qzSetting.getType());
			existingQZSetting.setIgnoreNoIndex(qzSetting.isIgnoreNoIndex());
			existingQZSetting.setIgnoreNoOrder(qzSetting.isIgnoreNoOrder());
			existingQZSetting.setUpdateInterval(qzSetting.getUpdateInterval());
			existingQZSetting.setUpdateTime(new Date());
			existingQZSetting.setCaptureCurrentKeywordCountTime(qzSetting.getCaptureCurrentKeywordCountTime());
			existingQZSetting.setCaptureCurrentKeywordStatus(qzSetting.getCaptureCurrentKeywordStatus());
			qzSettingDao.updateById(existingQZSetting);
			//修改部分
			List<QZOperationType> OldOperationTypes = qzOperationTypeService.searchQZOperationTypesIsDelete(qzSetting.getUuid());
			List<QZOperationType> updOperationTypes = qzSetting.getQzOperationTypes();
			updateOpretionTypeAndChargeRule(OldOperationTypes,updOperationTypes,qzSetting.getUuid());
		}else{
			qzSetting.setUpdateTime(new Date());
			qzSettingDao.insert(qzSetting);
			Long qzSettingUuid  = new Long(qzSettingDao.selectLastId());//插入qzSetting是的uuid
			for (QZOperationType qzOperationType : qzSetting.getQzOperationTypes()){
				qzOperationType.setQzSettingUuid(qzSettingUuid);
				qzOperationTypeService.insert(qzOperationType);
				Long qzOperationTypeUuid  = new Long(qzOperationTypeService.selectLastId());//插入qzOperationType时的uuid
				for(QZChargeRule qzChargeRule : qzOperationType.getQzChargeRules() ){
					qzChargeRule.setQzOperationTypeUuid(qzOperationTypeUuid);
					qzChargeRuleService.insert(qzChargeRule);
				}
			}
		}
	}

	public void updateOpretionTypeAndChargeRule(List<QZOperationType> oldOperationTypes, List<QZOperationType> newOperationTypes, Long qzSettingUuid ){
		Map<String,QZOperationType> oldOperationTypeMap = new HashMap<String, QZOperationType>();
		for (QZOperationType qzOperationType : oldOperationTypes) {
			oldOperationTypeMap.put(qzOperationType.getOperationType(), qzOperationType);
		}

		for(QZOperationType newOperationType : newOperationTypes) {
			QZOperationType oldOperationType = oldOperationTypeMap.get(newOperationType.getOperationType());
			if(oldOperationType != null) {
				updateOpretionTypeAndChargeRuleEqual(oldOperationType, newOperationType);
				oldOperationTypeMap.remove(newOperationType.getOperationType());
			}else {
				//添加一条
				newOperationType.setQzSettingUuid(qzSettingUuid);
				qzOperationTypeService.insert(newOperationType);
				Long uuid = Long.valueOf(qzOperationTypeService.selectLastId());
				for (QZChargeRule qzChargeRule : newOperationType.getQzChargeRules()){
					qzChargeRule.setQzOperationTypeUuid(uuid);
					qzChargeRuleService.insert(qzChargeRule);
				}
			}
		}

		for(QZOperationType oldOperationType : oldOperationTypeMap.values()) {
			qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationType.getUuid());
			oldOperationType.setUpdateTime(new Date());
			oldOperationType.setIsDeleted(1);
			qzOperationTypeService.updateById(oldOperationType);
			QZSetting qzSetting = qzSettingDao.selectById(oldOperationType.getQzSettingUuid());
			if(oldOperationType.getOperationType().equals(TerminalTypeEnum.PC.name())) {
				qzSetting.setPcGroup(null);
			} else {
				qzSetting.setPhoneGroup(null);
			}
			qzSettingDao.deleteQZSettingGroup(qzSetting);
		}
	}

	public void updateOpretionTypeAndChargeRuleEqual(QZOperationType oldOperationType,
			QZOperationType newOperationType) {
		oldOperationType.setUpdateTime(new Date());
		oldOperationType.setOperationType(newOperationType.getOperationType());
		oldOperationType.setInitialKeywordCount(newOperationType.getInitialKeywordCount());
		oldOperationType.setCurrentKeywordCount(newOperationType.getCurrentKeywordCount());
		oldOperationType.setGroup(newOperationType.getGroup());
		oldOperationType.setIsDeleted(0);//只要是发生改变那么就让它的状态为1
		qzOperationTypeService.updateById(oldOperationType);
		//删除规则
		qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationType.getUuid());
		for (QZChargeRule qzChargeRule : newOperationType.getQzChargeRules()) {
			qzChargeRule.setQzOperationTypeUuid(oldOperationType.getUuid());
			qzChargeRuleService.insert(qzChargeRule);
		}
	}

	public Page<QZSetting> searchQZSetting(Page<QZSetting> page, QZSettingSearchCriteria qzSettingSearchCriteria){
		page.setRecords(qzSettingDao.searchQZSettings(page, qzSettingSearchCriteria));
		return page;
	}

	public Map<String,Integer> getChargeRemindData() {
		Map<String,Integer> dateRangeTypeMap = new HashMap<String, Integer>();
		List<DateRangeTypeVO> chargeRemindDataList = qzSettingDao.getChargeRemindData();
		int expiredChargeSize  = 0;
		int nowChargeSize = 0;
		int threeChargeSize = 0;
		int sevenChargeSize = 0;

		for (DateRangeTypeVO dateRangeTypeVO : chargeRemindDataList) {
			int intervalDays = Utils.getIntervalDays(new Date(),dateRangeTypeVO.getNextChargeDate());
			if(intervalDays < 0) {
				expiredChargeSize++;
			} else if(intervalDays == 0) {
				nowChargeSize++;
			} else if(intervalDays <= 3) {
				threeChargeSize++;
			} else if(intervalDays <= 7) {
				sevenChargeSize++;
			}
		}
		dateRangeTypeMap.put("expiredChargeSize",expiredChargeSize);
		dateRangeTypeMap.put("nowChargeSize",nowChargeSize);
		dateRangeTypeMap.put("threeChargeSize",threeChargeSize);
		dateRangeTypeMap.put("sevenChargeSize",sevenChargeSize);
		return dateRangeTypeMap;
	}

	public QZSetting getQZSetting(Long uuid){
		QZSetting qzSetting = qzSettingDao.selectById(uuid);
		if(qzSetting != null) {
			Customer customer = customerService.getCustomer(qzSetting.getCustomerUuid());
			if(customer != null) {
				qzSetting.setContactPerson(customer.getContactPerson());
			}
			List<QZOperationType> qzOperationTypes = qzOperationTypeService.searchQZOperationTypesIsDelete(qzSetting.getUuid());
			for (QZOperationType qzOperationType : qzOperationTypes) {
				List<QZChargeRule> qzChargeRules = qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(qzOperationType.getUuid());
				qzOperationType.setQzChargeRules(qzChargeRules);
			}
			qzSetting.setQzOperationTypes(qzOperationTypes);
		}
		return qzSetting;
	}

	public void updateImmediately(String uuids){
		if(StringUtils.isNotEmpty(uuids)){
			String [] uuidArray = uuids.split(",");
			for(String uuid : uuidArray){
				QZSetting qzSetting = this.qzSettingDao.selectById(uuid);
				qzSetting.setUpdateStatus(null);
				qzSetting.setUpdateTime(new Date());
				qzSettingDao.updateById(qzSetting);
			}
		}
	}

	public void updateResult(QZSettingCriteria qzSettingCriteria, String terminalType) throws Exception {
		if (!qzSettingCriteria.isDownloadTimesUsed()){
			if (CollectionUtils.isNotEmpty(qzSettingCriteria.getCustomerKeywordVOs())) {
				List<QZOperationType> qzOperationTypes = qzOperationTypeService.searchQZOperationTypesIsDelete(qzSettingCriteria.getQzSetting().getUuid());

				if(CollectionUtils.isNotEmpty(qzOperationTypes)) {
					List<CustomerKeyword> insertingCustomerKeywords = new ArrayList<CustomerKeyword>();
					for (CustomerKeywordVO customerKeywordVO : qzSettingCriteria.getCustomerKeywordVOs()) {
						CustomerKeyword customerKeyword = new CustomerKeyword();
						customerKeyword.setKeyword(customerKeywordVO.getKeyword());
						customerKeyword.setUrl(customerKeywordVO.getUrl());
						customerKeyword.setTitle(customerKeywordVO.getTitle());
						customerKeyword.setOrderNumber(customerKeywordVO.getOrderNumber());
						customerKeyword.setCurrentIndexCount(customerKeywordVO.getCurrentIndexCount());

						customerKeyword.setCustomerUuid(qzSettingCriteria.getQzSetting().getCustomerUuid());
						customerKeyword.setOptimizePlanCount(customerKeywordVO.getCurrentIndexCount() + 8);
						customerKeyword.setServiceProvider("baidutop123");
						customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
						customerKeyword.setCollectMethod(CollectMethod.PerMonth.name());
						customerKeyword.setType(qzSettingCriteria.getQzSetting().getType());
						customerKeyword.setStartOptimizedTime(Utils.getCurrentTimestamp());
						customerKeyword.setStatus(1);
						customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
						customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
						customerKeyword.setInitialIndexCount(customerKeywordVO.getCurrentIndexCount());
						insertingCustomerKeywords.add(customerKeyword);
					}
					if (CollectionUtils.isNotEmpty(insertingCustomerKeywords)) {
						QZCaptureTitleLog qzCaptureTitleLog = new QZCaptureTitleLog();
						qzCaptureTitleLog.setStatus(QZCaptureTitleLogStatusEnum.New.getValue());
						for(QZOperationType qzOperationType : qzOperationTypes){
							qzCaptureTitleLog.setTerminalType(qzOperationType.getOperationType());
							qzCaptureTitleLog.setQzOperationTypeUuid(qzOperationType.getUuid());
							qzCaptureTitleLogService.addQZCaptureTitleLog(qzCaptureTitleLog);
							for (CustomerKeyword customerKeyword : insertingCustomerKeywords) {
								customerKeyword.setTerminalType(qzOperationType.getOperationType());
								customerKeyword.setOptimizeGroupName(qzOperationType.getGroup());
							}
							customerKeywordService.addCustomerKeywords(insertingCustomerKeywords, qzSettingCriteria.getUserName());
						}
					}
				}
			}
		}
		QZSetting qzSetting = qzSettingCriteria.getQzSetting();
		this.completeQZSetting(qzSetting.getUuid(), qzSettingCriteria.isDownloadTimesUsed());
	}

	//更新当前词量
	public void updateCurrentKeywordCount(QZSettingCriteria qzSettingCriteria) {
		QZSetting updQZSetting = qzSettingCriteria.getQzSetting();
		if(updQZSetting != null){
			QZSetting oldQZSetting = qzSettingDao.selectById(updQZSetting.getUuid());
			List<QZOperationType> oldOperationTypes = qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(oldQZSetting.getUuid());
			List<QZOperationType> updOperationTypes = updQZSetting.getQzOperationTypes();

			Map<String, Long> currentKeywordCountMap = new HashMap<String, Long>();
			for(QZOperationType operationType : updOperationTypes){
				currentKeywordCountMap.put(operationType.getOperationType(), operationType.getCurrentKeywordCount());
				if(operationType.getCurrentKeywordCount()!=null){
					oldQZSetting.setCaptureCurrentKeywordStatus(QZSettingStatusEnum.Completed.getValue());
				} else {
					oldQZSetting.setCaptureCurrentKeywordStatus(QZSettingStatusEnum.Processing.getValue());
				}
			}

			for(QZOperationType oldOperationType : oldOperationTypes){
				this.updateQZOperationType(oldOperationType, currentKeywordCountMap.get(oldOperationType.getOperationType()));
			}
			qzSettingDao.updateById(oldQZSetting);
		}
	}

	//用于更新当前词量以及达标日期
	private void updateQZOperationType(QZOperationType oldOperationType, Long currentKeywordCount){
		oldOperationType.setCurrentKeywordCount(currentKeywordCount);
		if(oldOperationType.getInitialKeywordCount() == null){
			oldOperationType.setInitialKeywordCount(currentKeywordCount);
		}
		if(currentKeywordCount != null && null == oldOperationType.getReachTargetDate()){
			List<QZChargeRule> qzChargeRules = qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(oldOperationType.getUuid());
			if(qzChargeRules.size() > 0) {
				QZChargeRule qzChargeRulePC = qzChargeRules.get(0);
				//如果比规则表中的初始词量大
				if (currentKeywordCount >= qzChargeRulePC.getStartKeywordCount()) {
					oldOperationType.setReachTargetDate(new Date());//达标日期
					oldOperationType.setNextChargeDate(DateUtils.addMonths(new Date(), 1));//下次收费日期:加一个月
				}
			}
		}
		oldOperationType.setUpdateTime(new Date());
		qzOperationTypeService.updateById(oldOperationType);
	}

	public void deleteOne(Long uuid){
		//根据数据库中的uuid去查询
		List<QZOperationType> qzOperationTypes =  qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(uuid);
		if(qzOperationTypes.size()>0){
			for(QZOperationType qzOperationType : qzOperationTypes){
				qzChargeRuleService.deleteByQZOperationTypeUuid(qzOperationType.getUuid());
			}
			qzOperationTypeService.deleteByQZSettingUuid(uuid);
		}
		qzSettingDao.deleteById(uuid);
	}

	public void deleteAll(List<String> uuids){
		for(String uuid : uuids){
			deleteOne(Long.valueOf(uuid));
		}
	}

}
