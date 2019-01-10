package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.enums.CollectMethod;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.dao.QZSettingDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.QZCaptureTitleLogStatusEnum;
import com.keymanager.monitoring.enums.QZSettingStatusEnum;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.vo.CustomerKeywordSummaryInfoVO;
import com.keymanager.monitoring.vo.DateRangeTypeVO;
import com.keymanager.monitoring.vo.QZSettingSearchClientGroupInfoVO;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordVO;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Autowired
	private ClientStatusService clientStatusService;

	@Autowired
	private QZKeywordRankInfoService qzKeywordRankInfoService;

	@Autowired
	private QZCategoryTagService qzCategoryTagService;

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

	public void completeQZSetting(Long uuid, boolean downloadTimesUsed, boolean pcKeywordExceedMaxCount, boolean phoneKeywordExceedMaxCount){
		QZSetting qzSetting = qzSettingDao.selectById(uuid);
		if(qzSetting != null){
			if(downloadTimesUsed){
				qzSetting.setUpdateStatus(QZSettingStatusEnum.DownloadTimesUsed.getValue());
			} else {
				qzSetting.setUpdateStatus(QZSettingStatusEnum.Completed.getValue());
			}
			if (pcKeywordExceedMaxCount) {
				qzSetting.setPcKeywordExceedMaxCount(pcKeywordExceedMaxCount);
			}
			if(phoneKeywordExceedMaxCount) {
				qzSetting.setPhoneKeywordExceedMaxCount(phoneKeywordExceedMaxCount);
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
			existingQZSetting.setBearPawNumber(qzSetting.getBearPawNumber());
			existingQZSetting.setUpdateStatus(qzSetting.getUpdateStatus());
			existingQZSetting.setCustomerUuid(qzSetting.getCustomerUuid());
			existingQZSetting.setPcGroup(qzSetting.getPcGroup());
			existingQZSetting.setPhoneGroup(qzSetting.getPhoneGroup());
			existingQZSetting.setType(qzSetting.getType());
			existingQZSetting.setAutoCrawlKeywordFlag(qzSetting.isAutoCrawlKeywordFlag());
			existingQZSetting.setIgnoreNoIndex(qzSetting.isIgnoreNoIndex());
			existingQZSetting.setIgnoreNoOrder(qzSetting.isIgnoreNoOrder());
			existingQZSetting.setUpdateInterval(qzSetting.getUpdateInterval());
			existingQZSetting.setUpdateTime(new Date());
			existingQZSetting.setCaptureCurrentKeywordCountTime(qzSetting.getCaptureCurrentKeywordCountTime());
			existingQZSetting.setCaptureCurrentKeywordStatus(qzSetting.getCaptureCurrentKeywordStatus());
			existingQZSetting.setCrawlerStatus(Constants.QZ_SETTING_CRAWLER_STATUS_NEW);

			//修改部分
			List<QZOperationType> OldOperationTypes = qzOperationTypeService.searchQZOperationTypesIsDelete(qzSetting.getUuid());
			List<QZOperationType> updOperationTypes = qzSetting.getQzOperationTypes();
			updateOpretionTypeAndChargeRule(OldOperationTypes,updOperationTypes,qzSetting.getUuid());
			List<QZKeywordRankInfo> existingQZKeywordRankInfoList = qzKeywordRankInfoService.searchExistingQZKeywordRankInfo(qzSetting.getUuid(), new QZSettingSearchCriteria());
			updateQZKeywordRankInfo(existingQZKeywordRankInfoList, updOperationTypes, existingQZSetting);
			// 修改标签
			List<QZCategoryTag> existingQZCategoryTags = qzCategoryTagService.searchCategoryTagByQZSettingUuid(qzSetting.getUuid());
			List<QZCategoryTag> updateQZCategoryTags = qzSetting.getQzCategoryTags();
			qzCategoryTagService.updateQZCategoryTag(existingQZCategoryTags, updateQZCategoryTags, qzSetting.getUuid());
			qzSettingDao.updateById(existingQZSetting);
		}else{
			qzSetting.setUpdateTime(new Date());
			qzSettingDao.insert(qzSetting);
			Long qzSettingUuid  = new Long(qzSettingDao.selectLastId());//插入qzSetting是的uuid
			for (QZOperationType qzOperationType : qzSetting.getQzOperationTypes()){
				qzOperationType.setQzSettingUuid(qzSettingUuid);
				qzOperationTypeService.insert(qzOperationType);
				// 在qzKeywordRankInfo表中插入对应终端类型的记录
				QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
				qzKeywordRankInfo.setQzSettingUuid(qzSettingUuid);
				qzKeywordRankInfo.setTerminalType(qzOperationType.getOperationType());
                qzKeywordRankInfoService.insert(qzKeywordRankInfo);

				for(QZChargeRule qzChargeRule : qzOperationType.getQzChargeRules()){
					qzChargeRule.setQzOperationTypeUuid(qzOperationType.getUuid());
					qzChargeRuleService.insert(qzChargeRule);
				}
			}
			// 添加标签
			for (QZCategoryTag qzCategoryTag : qzSetting.getQzCategoryTags()) {
				qzCategoryTag.setQzSettingUuid(qzSettingUuid);
				qzCategoryTagService.insert(qzCategoryTag);
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
		oldOperationType.setSubDomainName(newOperationType.getSubDomainName());
		oldOperationType.setMaxKeywordCount(newOperationType.getMaxKeywordCount());

		oldOperationType.setIsDeleted(0); //只要是发生改变那么就让它的状态为0
		qzOperationTypeService.updateById(oldOperationType);

		//删除规则
		qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationType.getUuid());
		for (QZChargeRule qzChargeRule : newOperationType.getQzChargeRules()) {
			qzChargeRule.setQzOperationTypeUuid(oldOperationType.getUuid());
			qzChargeRuleService.insert(qzChargeRule);
		}
	}

	public void updateQZKeywordRankInfo(List<QZKeywordRankInfo> existingQZKeywordRankInfoList, List<QZOperationType> qzOperationTypeList, QZSetting qzSetting){
		Map<String, QZKeywordRankInfo> existingQZKeywordRankInfoMap = new HashMap<String, QZKeywordRankInfo>();
		for (QZKeywordRankInfo qzKeywordRankInfo : existingQZKeywordRankInfoList) {
            existingQZKeywordRankInfoMap.put(qzKeywordRankInfo.getTerminalType(), qzKeywordRankInfo);
		}
		for (QZOperationType qzOperationType : qzOperationTypeList) {
            QZKeywordRankInfo qzKeywordRankInfo = existingQZKeywordRankInfoMap.get(qzOperationType.getOperationType());
            if (null != qzKeywordRankInfo) {
                existingQZKeywordRankInfoMap.remove(qzOperationType.getOperationType());
            } else {
                qzKeywordRankInfo = new QZKeywordRankInfo();
                qzKeywordRankInfo.setQzSettingUuid(qzSetting.getUuid());
                qzKeywordRankInfo.setTerminalType(qzOperationType.getOperationType());
                qzKeywordRankInfoService.insert(qzKeywordRankInfo);
            }
		}

		for (QZKeywordRankInfo qzKeywordRankInfo : existingQZKeywordRankInfoMap.values()) {
			if (qzKeywordRankInfo.getTerminalType().equals("PC")) {
				qzSetting.setPcCreateTopTenNum(null);
				qzSetting.setPcCreateTopFiftyNum(null);
			}
			if (qzKeywordRankInfo.getTerminalType().equals("Phone")) {
				qzSetting.setPhoneCreateTopTenNum(null);
				qzSetting.setPhoneCreateTopFiftyNum(null);
			}
		    qzKeywordRankInfoService.deleteById(qzKeywordRankInfo);
        }
	}

	public Page<QZSetting> searchQZSetting(Page<QZSetting> page, QZSettingSearchCriteria qzSettingSearchCriteria){
		page.setRecords(qzSettingDao.searchQZSettings(page, qzSettingSearchCriteria));
		addingQZKeywordRankInfo(page, qzSettingSearchCriteria);
		return page;
	}

	public Page<QZSetting> addingQZKeywordRankInfo (Page<QZSetting> page, QZSettingSearchCriteria qzSettingSearchCriteria){
        for(QZSetting qzSetting : page.getRecords()){
            List<QZKeywordRankInfo> qzKeywordRankInfos = qzKeywordRankInfoService.searchExistingQZKeywordRankInfo(qzSetting.getUuid(), qzSettingSearchCriteria);
            Map<String, JSONObject> qzKeywordRankInfoMap = new HashMap<String, JSONObject>();
            for (QZKeywordRankInfo qzKeywordRankInfo : qzKeywordRankInfos) {
				calculatedQZKeywordRankInfo(qzKeywordRankInfo);
				qzKeywordRankInfoMap.put(qzKeywordRankInfo.getTerminalType(), new JSONObject().fromObject(qzKeywordRankInfo));
            }
            qzSetting.setQzKeywordRankInfoMap(qzKeywordRankInfoMap);
        }
        return page;
    }

    public QZKeywordRankInfo calculatedQZKeywordRankInfo(QZKeywordRankInfo qzKeywordRankInfo) {
        Map<String, Object> map;
        if (!StringUtils.isBlank(qzKeywordRankInfo.getTopTen())) {
            map = calculate(qzKeywordRankInfo.getTopTen());
            qzKeywordRankInfo.setTopTenIncrement((Integer) map.get("increment"));
            qzKeywordRankInfo.setTopTenNum((Integer) map.get("topNum"));
        }
        if (!StringUtils.isBlank(qzKeywordRankInfo.getTopTwenty())) {
            map = calculate(qzKeywordRankInfo.getTopTwenty());
            qzKeywordRankInfo.setTopTwentyIncrement((Integer) map.get("increment"));
        }
        if (!StringUtils.isBlank(qzKeywordRankInfo.getTopThirty())) {
            map = calculate(qzKeywordRankInfo.getTopThirty());
            qzKeywordRankInfo.setTopThirtyIncrement((Integer) map.get("increment"));
        }
        if (!StringUtils.isBlank(qzKeywordRankInfo.getTopForty())) {
            map = calculate(qzKeywordRankInfo.getTopForty());
            qzKeywordRankInfo.setTopFortyIncrement((Integer) map.get("increment"));
        }
        if (!StringUtils.isBlank(qzKeywordRankInfo.getTopFifty())) {
            map = calculate(qzKeywordRankInfo.getTopFifty());
            qzKeywordRankInfo.setTopFiftyIncrement((Integer) map.get("increment"));
            qzKeywordRankInfo.setTopFiftyNum((Integer) map.get("topNum"));
        }
	    return qzKeywordRankInfo;
    }

    public Map<String, Object> calculate(String topString) {
	    Map<String, Object> map = new HashMap<String, Object>();
        String[] topArr = topString.replace("[", "").replace("]", "").split(", ");
        map.put("increment", Integer.parseInt(topArr[0]) - Integer.parseInt(topArr[1]));
        map.put("topNum", Integer.parseInt(topArr[0]));
        return map;
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
			qzSetting.setQzCategoryTags(qzCategoryTagService.searchCategoryTagByQZSettingUuid(qzSetting.getUuid()));
		}
		return qzSetting;
	}

	public void updateImmediately(String uuids){
		if(StringUtils.isNotEmpty(uuids)){
			qzSettingDao.updateImmediately(uuids);
		}
	}

	private CustomerKeyword createCustomerKeyword(QZSettingCriteria qzSettingCriteria, QZOperationType qzOperationType, CustomerKeywordVO customerKeywordVO) {
		CustomerKeyword customerKeyword = new CustomerKeyword();
		customerKeyword.setQzSettingUuid(qzSettingCriteria.getQzSetting().getUuid());
		customerKeyword.setKeyword(customerKeywordVO.getKeyword());
		customerKeyword.setUrl(customerKeywordVO.getUrl());
		customerKeyword.setBearPawNumber(qzSettingCriteria.getQzSetting().getBearPawNumber());
		customerKeyword.setTitle(customerKeywordVO.getTitle());
		customerKeyword.setOrderNumber(customerKeywordVO.getOrderNumber());
		customerKeyword.setCurrentIndexCount(customerKeywordVO.getCurrentIndexCount());
		customerKeyword.setCustomerUuid(qzSettingCriteria.getQzSetting().getCustomerUuid());
		customerKeyword.setOptimizePlanCount(customerKeywordVO.getCurrentIndexCount() + 8);
		customerKeyword.setOptimizeRemainingCount(customerKeywordVO.getCurrentIndexCount() + 8);
		customerKeyword.setServiceProvider("baidutop123");
		customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
		customerKeyword.setCollectMethod(CollectMethod.PerMonth.name());
		customerKeyword.setType(qzSettingCriteria.getQzSetting().getType());
		customerKeyword.setStartOptimizedTime(Utils.getCurrentTimestamp());
		customerKeyword.setStatus(1);
		customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
		customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
		customerKeyword.setInitialIndexCount(customerKeywordVO.getCurrentIndexCount());
		customerKeyword.setTerminalType(qzOperationType.getOperationType());
		customerKeyword.setOptimizeGroupName(qzOperationType.getGroup());
		return customerKeyword;
	}

	public void updateResult(QZSettingCriteria qzSettingCriteria) throws Exception {
		if (!qzSettingCriteria.isDownloadTimesUsed()){
			if (CollectionUtils.isNotEmpty(qzSettingCriteria.getCustomerKeywordVOs())) {
				List<QZOperationType> qzOperationTypes = qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(qzSettingCriteria.getQzSetting().getUuid());
				List<CustomerKeywordSummaryInfoVO> customerKeywordSummaryInfoVOs = customerKeywordService.searchCustomerKeywordSummaryInfo(qzSettingCriteria.getQzSetting().getType(), (long)qzSettingCriteria.getQzSetting().getCustomerUuid());
				Map<String, Set<String>> customerKeywordSummaryInfoMaps = new HashMap<String, Set<String>>();
				int pcKeywordCountNum = 0, phoneKeywordCountNum = 0;
				if(CollectionUtils.isNotEmpty(customerKeywordSummaryInfoVOs)){
					for(CustomerKeywordSummaryInfoVO customerKeywordSummaryInfoVO : customerKeywordSummaryInfoVOs) {
						if (customerKeywordSummaryInfoVO.getStatus() > 0){
							if (customerKeywordSummaryInfoVO.getTerminalType().equals(TerminalTypeEnum.PC.name())) {
								pcKeywordCountNum++;
							} else {
								phoneKeywordCountNum++;
							}
						}
						Set<String> terminalTypeSet = customerKeywordSummaryInfoMaps.get(customerKeywordSummaryInfoVO.getKeyword());
						if (terminalTypeSet == null) {
							terminalTypeSet = new HashSet<String>();
							customerKeywordSummaryInfoMaps.put(customerKeywordSummaryInfoVO.getKeyword(), terminalTypeSet);
						}
						terminalTypeSet.add(customerKeywordSummaryInfoVO.getTerminalType());
					}
				}
				for(QZOperationType qzOperationType : qzOperationTypes){
					if (qzOperationType.getOperationType().equals(TerminalTypeEnum.PC.name())) {
						pcKeywordCountNum = qzOperationType.getMaxKeywordCount() - pcKeywordCountNum;
					} else {
						phoneKeywordCountNum = qzOperationType.getMaxKeywordCount() - phoneKeywordCountNum;
					}
				}
				QZCaptureTitleLog qzCaptureTitleLog = new QZCaptureTitleLog();
				qzCaptureTitleLog.setStatus(QZCaptureTitleLogStatusEnum.New.getValue());
				if(CollectionUtils.isNotEmpty(qzOperationTypes)) {
					List<CustomerKeyword> insertingCustomerKeywords = new ArrayList<CustomerKeyword>();
					for (CustomerKeywordVO customerKeywordVO : qzSettingCriteria.getCustomerKeywordVOs()) {
						for(QZOperationType qzOperationType : qzOperationTypes){
							if(!(customerKeywordSummaryInfoMaps.containsKey(customerKeywordVO.getKeyword()) && customerKeywordSummaryInfoMaps.get(customerKeywordVO.getKeyword()).contains(qzOperationType.getOperationType()))) {
								if(StringUtils.isBlank(customerKeywordVO.getTerminalType()) || qzOperationType.getOperationType().equals(customerKeywordVO.getTerminalType())) {
									if (qzOperationType.getOperationType().equals(TerminalTypeEnum.PC.name())) {
										if (pcKeywordCountNum > 0) {
											CustomerKeyword customerKeyword = createCustomerKeyword(qzSettingCriteria,qzOperationType,customerKeywordVO);
											insertingCustomerKeywords.add(customerKeyword);
											pcKeywordCountNum--;
										}
									} else {
										if (phoneKeywordCountNum > 0) {
											CustomerKeyword customerKeyword = createCustomerKeyword(qzSettingCriteria,qzOperationType,customerKeywordVO);
											insertingCustomerKeywords.add(customerKeyword);
											phoneKeywordCountNum--;
										}
									}
								}
							}
						}
					}

					for(QZOperationType qzOperationType : qzOperationTypes){
						qzCaptureTitleLog.setTerminalType(qzOperationType.getOperationType());
						qzCaptureTitleLog.setQzOperationTypeUuid(qzOperationType.getUuid());
						qzCaptureTitleLogService.addQZCaptureTitleLog(qzCaptureTitleLog);
						// 全站下不存在的终端类型，默认其是达标的
						if (pcKeywordCountNum == 0) {
							qzSettingCriteria.getQzSetting().setPcKeywordExceedMaxCount(true);
						}
						if (phoneKeywordCountNum == 0) {
							qzSettingCriteria.getQzSetting().setPhoneKeywordExceedMaxCount(true);
						}
					}

					if (CollectionUtils.isNotEmpty(insertingCustomerKeywords)) {
						customerKeywordService.addCustomerKeywords(insertingCustomerKeywords, qzSettingCriteria.getUserName());
					}
				}
			}
		}
		QZSetting qzSetting = qzSettingCriteria.getQzSetting();
		this.completeQZSetting(qzSetting.getUuid(), qzSettingCriteria.isDownloadTimesUsed(), qzSetting.isPcKeywordExceedMaxCount(), qzSetting.isPhoneKeywordExceedMaxCount());
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
		if(CollectionUtils.isNotEmpty(qzOperationTypes)){
			for(QZOperationType qzOperationType : qzOperationTypes){
				qzChargeRuleService.deleteByQZOperationTypeUuid(qzOperationType.getUuid());
			}
			qzOperationTypeService.deleteByQZSettingUuid(uuid);
		}
        qzKeywordRankInfoService.deleteByQZSettingUuid(uuid);
		List<QZCategoryTag> qzCategoryTags = qzCategoryTagService.searchCategoryTagByQZSettingUuid(uuid);
		if (CollectionUtils.isNotEmpty(qzCategoryTags)) {
			for (QZCategoryTag qzCategoryTag : qzCategoryTags) {
				qzCategoryTagService.deleteById(qzCategoryTag.getUuid());
			}
		}
		qzSettingDao.deleteById(uuid);
	}

	public void deleteAll(List<String> uuids){
		for(String uuid : uuids){
			deleteOne(Long.valueOf(uuid));
		}
	}

	public void updateQZSettingStatus(List<Long> uuids, Integer status) {
		qzSettingDao.updateQZSettingStatus(uuids, status);
	}

	public List<QZSetting> getAvailableQZSettings(){
		return qzSettingDao.getAvailableQZSettings();
	}

	public void detectExceedMaxCountFlag(){
		qzSettingDao.updateExceedMaxCountFlag(false, false);
		List<Long> pcExceedMaxCountUuids = qzSettingDao.getKeywordExceedMaxCount(TerminalTypeEnum.PC.name());
		if(CollectionUtils.isNotEmpty(pcExceedMaxCountUuids)){
			qzSettingDao.updatePCExceedMaxCountFlag(true, pcExceedMaxCountUuids);
		}

		List<Long> phoneExceedMaxCountUuids = qzSettingDao.getKeywordExceedMaxCount(TerminalTypeEnum.Phone.name());
		if(CollectionUtils.isNotEmpty(phoneExceedMaxCountUuids)){
			qzSettingDao.updatePhoneExceedMaxCountFlag(true, phoneExceedMaxCountUuids);
		}
	}

	public QZSettingSearchClientGroupInfoVO getQZSettingClientGroupInfo (QZSettingSearchClientGroupInfoCriteria qzSettingSearchClientGroupInfoCriteria) {
		QZSettingSearchClientGroupInfoVO qzSettingSearchClientGroupInfoVO = new QZSettingSearchClientGroupInfoVO();
		qzSettingSearchClientGroupInfoVO.setCustomerKeywordCount(qzSettingDao.getQZSettingClientGroupInfo(qzSettingSearchClientGroupInfoCriteria));
		qzSettingSearchClientGroupInfoVO.setClientStatusVOs(clientStatusService.getClientStatusVOs(qzSettingSearchClientGroupInfoCriteria));
		qzSettingSearchClientGroupInfoVO.setCategoryTagNames(qzCategoryTagService.findTagNamesByQZSettingUuid(qzSettingSearchClientGroupInfoCriteria.getQzSettingUuid()));
		return qzSettingSearchClientGroupInfoVO;
	}

	public void saveQZSettingCustomerKeywords (QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria, String userName) {
		CustomerKeyword customerKeyword = new CustomerKeyword();
		customerKeyword.setQzSettingUuid(qzSettingSaveCustomerKeywordsCriteria.getQzSettingUuid());
		customerKeyword.setCustomerUuid(qzSettingSaveCustomerKeywordsCriteria.getCustomerUuid());
		customerKeyword.setType(qzSettingSaveCustomerKeywordsCriteria.getType());
		customerKeyword.setSearchEngine(qzSettingSaveCustomerKeywordsCriteria.getSearchEngine());
		customerKeyword.setTerminalType(qzSettingSaveCustomerKeywordsCriteria.getTerminalType());
		customerKeyword.setUrl(qzSettingSaveCustomerKeywordsCriteria.getDomain());
		customerKeyword.setOptimizeGroupName(qzSettingSaveCustomerKeywordsCriteria.getOptimizeGroupName());
		customerKeyword.setServiceProvider("baidutop123");
		customerKeyword.setManualCleanTitle(true);
		customerKeyword.setCollectMethod(CollectMethod.PerMonth.name());
        customerKeyword.setCurrentIndexCount(-1);
        customerKeyword.setPositionFirstFee(-1d);
		for (String keyword : qzSettingSaveCustomerKeywordsCriteria.getKeywords()) {
			customerKeyword.setKeyword(keyword);
			customerKeywordService.addCustomerKeyword(customerKeyword, userName);
		}
    }
}
