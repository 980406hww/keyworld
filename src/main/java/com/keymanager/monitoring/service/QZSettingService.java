package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.enums.CollectMethod;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.dao.QZSettingDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.CustomerKeywordSourceEnum;
import com.keymanager.monitoring.enums.QZCaptureTitleLogStatusEnum;
import com.keymanager.monitoring.enums.QZSettingStatusEnum;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.vo.*;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordVO;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QZSettingService extends ServiceImpl<QZSettingDao, QZSetting> {

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
	private MachineInfoService machineInfoService;

	@Autowired
	private QZKeywordRankInfoService qzKeywordRankInfoService;

	@Autowired
	private QZCategoryTagService qzCategoryTagService;

	@Autowired
    private CustomerExcludeKeywordService customerExcludeKeywordService;

	@Autowired
	private CaptureRankJobService captureRankJobService;

	@Autowired
	private OperationCombineService operationCombineService;

	public QZSetting getAvailableQZSetting(){
		List<QZSetting> qzSettings = qzSettingDao.getAvailableQZSettings();
		QZSetting qzSetting = null;
		if(CollectionUtils.isNotEmpty(qzSettings)){
            boolean nullFlag = true;
            int i = 0;
            while (nullFlag && i < qzSettings.size()) {
                qzSetting = qzSettings.get(i);
                List<String> allStandardSpecies = qzChargeRuleService.getAllStandardSpecies(qzSetting.getUuid());
                if (CollectionUtils.isNotEmpty(allStandardSpecies)) {
                    nullFlag = false;
                    qzSetting.setStandardSpecies(allStandardSpecies);
                } else {
                    i++;
                }
                startQZSettingForUpdateKeyword(qzSetting.getUuid());
            }
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

	public void saveQZSetting(QZSetting qzSetting, String userName){
		if (qzSetting.getUuid() != null) {
			//修改qzSetting表
			QZSetting existingQZSetting = qzSettingDao.selectById(qzSetting.getUuid());
			existingQZSetting.setDomain(qzSetting.getDomain());
			existingQZSetting.setSearchEngine(qzSetting.getSearchEngine());
			existingQZSetting.setBearPawNumber(qzSetting.getBearPawNumber());
			existingQZSetting.setUpdateStatus(qzSetting.getUpdateStatus());
			existingQZSetting.setCustomerUuid(qzSetting.getCustomerUuid());
			existingQZSetting.setPcGroup(qzSetting.getPcGroup());
			existingQZSetting.setPhoneGroup(qzSetting.getPhoneGroup());
			existingQZSetting.setType(qzSetting.getType());
			existingQZSetting.setAutoCrawlKeywordFlag(qzSetting.isAutoCrawlKeywordFlag());
			existingQZSetting.setIgnoreNoIndex(qzSetting.isIgnoreNoIndex());
            existingQZSetting.setIgnoreNoOrder(qzSetting.isIgnoreNoOrder());
            existingQZSetting.setGroupMaxCustomerKeywordCount(qzSetting.getGroupMaxCustomerKeywordCount());
			existingQZSetting.setUpdateInterval(qzSetting.getUpdateInterval());
			existingQZSetting.setfIsMonitor(qzSetting.getfIsMonitor());
			existingQZSetting.setfIsReady(qzSetting.getfIsReady());
			existingQZSetting.setUpdateTime(new Date());
			existingQZSetting.setCaptureCurrentKeywordCountTime(qzSetting.getCaptureCurrentKeywordCountTime());
			existingQZSetting.setCaptureCurrentKeywordStatus(qzSetting.getCaptureCurrentKeywordStatus());
			existingQZSetting.setCrawlerStatus(Constants.QZ_SETTING_CRAWLER_STATUS_NEW);

			//修改部分
			List<QZOperationType> oldOperationTypes = qzOperationTypeService.searchQZOperationTypesIsDelete(qzSetting.getUuid());
			List<QZOperationType> updOperationTypes = qzSetting.getQzOperationTypes();
			this.updateOperationTypeAndChargeRule(oldOperationTypes, updOperationTypes, qzSetting.getUuid(), qzSetting.getCustomerUuid(), qzSetting.getfIsMonitor(), userName);
			List<QZKeywordRankInfo> existingQZKeywordRankInfos = qzKeywordRankInfoService.searchExistingQZKeywordRankInfo(qzSetting.getUuid(), new QZSettingSearchCriteria());
			this.updateQZKeywordRankInfo(existingQZKeywordRankInfos, updOperationTypes, existingQZSetting);
			// 修改标签
			List<QZCategoryTag> existingQZCategoryTags = qzCategoryTagService.searchCategoryTagByQZSettingUuid(qzSetting.getUuid());
			List<QZCategoryTag> updateQZCategoryTags = qzSetting.getQzCategoryTags();
			qzCategoryTagService.updateQZCategoryTag(existingQZCategoryTags, updateQZCategoryTags, qzSetting.getUuid());
			qzSettingDao.updateById(existingQZSetting);
		} else {
			qzSetting.setUpdateTime(new Date());
			qzSetting.setRenewalStatus(1);
			qzSettingDao.insert(qzSetting);
			Long qzSettingUuid  = new Long(qzSettingDao.selectLastId());//插入qzSetting是的uuid
			for (QZOperationType qzOperationType : qzSetting.getQzOperationTypes()){
				qzOperationType.setQzSettingUuid(qzSettingUuid);
				qzOperationTypeService.insert(qzOperationType);
				String standardSpecies = null;
				boolean isExtraRankInfo = false;
				if (CollectionUtils.isNotEmpty(qzOperationType.getQzChargeRules())) {
					standardSpecies = qzOperationType.getQzChargeRules().iterator().next().getStandardSpecies();
					for(QZChargeRule qzChargeRule : qzOperationType.getQzChargeRules()){
						qzChargeRule.setQzOperationTypeUuid(qzOperationType.getUuid());
						qzChargeRuleService.insert(qzChargeRule);
					}
				} else {
					isExtraRankInfo = true;
					standardSpecies = "aiZhan";
				}
				if (null != standardSpecies) {
					qzKeywordRankInfoService.addQZKeywordRankInfo(qzSetting.getUuid(), qzOperationType.getOperationType(), standardSpecies, !isExtraRankInfo);
					if (standardSpecies.equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
						if (qzSetting.getSearchEngine().equals(Constants.SEARCH_ENGINE_BAIDU)) {
							qzKeywordRankInfoService.addQZKeywordRankInfo(qzSetting.getUuid(), qzOperationType.getOperationType(), "aiZhan", false);
						}
						if (qzSetting.getfIsMonitor()) {
							captureRankJobService.qzAddCaptureRankJob(qzOperationType.getGroup(), qzSettingUuid, qzSetting.getCustomerUuid(), qzOperationType.getOperationType(), userName);
						}
					}
				}
			}
			// 添加标签
			for (QZCategoryTag qzCategoryTag : qzSetting.getQzCategoryTags()) {
				qzCategoryTag.setQzSettingUuid(qzSettingUuid);
				qzCategoryTagService.insert(qzCategoryTag);
			}
		}
	}

	private void updateOperationTypeAndChargeRule(List<QZOperationType> oldOperationTypes, List<QZOperationType> newOperationTypes, Long qzSettingUuid, long customerUuid, boolean isMonitor, String userName){
		Map<String, QZOperationType> oldOperationTypeMap = new HashMap<>();
		for (QZOperationType qzOperationType : oldOperationTypes) {
		    oldOperationTypeMap.put(qzOperationType.getOperationType(), qzOperationType);
		}

		for(QZOperationType newOperationType : newOperationTypes) {
			QZOperationType oldOperationType = oldOperationTypeMap.get(newOperationType.getOperationType());
			if(oldOperationType != null) {
			    this.updateOperationTypeAndChargeRuleEqual(oldOperationType, newOperationType, qzSettingUuid, customerUuid, isMonitor, userName);
				oldOperationTypeMap.remove(newOperationType.getOperationType());
			} else {
				//添加一条
				newOperationType.setQzSettingUuid(qzSettingUuid);
				qzOperationTypeService.insert(newOperationType);
				Long uuid = Long.valueOf(qzOperationTypeService.selectLastId());
				if (isMonitor && newOperationType.getQzChargeRules().iterator().next().getStandardSpecies().equals(Constants.
						QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
					captureRankJobService.qzAddCaptureRankJob(newOperationType.getGroup(), qzSettingUuid, customerUuid, newOperationType.getOperationType(), userName);
				}
				for (QZChargeRule qzChargeRule : newOperationType.getQzChargeRules()){
					qzChargeRule.setQzOperationTypeUuid(uuid);
					qzChargeRuleService.insert(qzChargeRule);
				}
			}
		}

		for(QZOperationType oldOperationType : oldOperationTypeMap.values()) {
			if (CollectionUtils.isNotEmpty(oldOperationType.getQzChargeRules())) {
				if (oldOperationType.getQzChargeRules().iterator().next().getStandardSpecies().equals(Constants.
						QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
					captureRankJobService.deleteCaptureRankJob(qzSettingUuid, oldOperationType.getOperationType());
				}
			}
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
			qzSettingDao.updateQZSettingGroup(qzSetting);
		}
	}

	private void updateOperationTypeAndChargeRuleEqual(QZOperationType oldOperationType, QZOperationType newOperationType, Long qzSettingUuid, long customerUuid, boolean isMonitor, String userName) {
		oldOperationType.setUpdateTime(new Date());
		oldOperationType.setOperationType(newOperationType.getOperationType());
		oldOperationType.setOptimizationType(newOperationType.getOptimizationType());
		oldOperationType.setStandardType(newOperationType.getStandardType());
		oldOperationType.setGroup(newOperationType.getGroup());
		oldOperationType.setSubDomainName(newOperationType.getSubDomainName());
		oldOperationType.setMonitorRemark(newOperationType.getMonitorRemark());
		oldOperationType.setMaxKeywordCount(newOperationType.getMaxKeywordCount());
		oldOperationType.setIsDeleted(0); //只要是发生改变那么就让它的状态为0
		qzOperationTypeService.updateById(oldOperationType);

		boolean isDeleteDesignationWord = false;
		if (CollectionUtils.isNotEmpty(oldOperationType.getQzChargeRules())) {
			QZChargeRule qzChargeRule = oldOperationType.getQzChargeRules().iterator().next();
			if (qzChargeRule.getStandardSpecies().equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
                if (newOperationType.getQzChargeRules().iterator().next().getStandardSpecies().equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
                    if (!isMonitor) {
                        isDeleteDesignationWord = true;
                    }
                } else {
                    CaptureRankJob existCaptureRankJob = captureRankJobService.findExistCaptureRankJob(qzSettingUuid, newOperationType.getOperationType());
                    if (null != existCaptureRankJob) {
                        isDeleteDesignationWord = true;
                    }
                }
			}
		}

        if (isDeleteDesignationWord) {
            captureRankJobService.deleteCaptureRankJob(qzSettingUuid, newOperationType.getOperationType());
        }
		//删除规则
		qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationType.getUuid());

		// 保存新规则
		boolean hasDesignationWord = false;
		for (QZChargeRule qzChargeRule : newOperationType.getQzChargeRules()) {
			if (!hasDesignationWord && qzChargeRule.getStandardSpecies().equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
				hasDesignationWord = true;
			}
			qzChargeRule.setQzOperationTypeUuid(oldOperationType.getUuid());
			qzChargeRuleService.insert(qzChargeRule);
		}
		if (isMonitor && hasDesignationWord) {
			CaptureRankJob existCaptureRankJob = captureRankJobService.findExistCaptureRankJob(qzSettingUuid, newOperationType.getOperationType());
			if (null == existCaptureRankJob) {
				captureRankJobService.qzAddCaptureRankJob(newOperationType.getGroup(), qzSettingUuid, customerUuid, newOperationType.getOperationType(), userName);
			}
		}
	}

	private void updateQZKeywordRankInfo(List<QZKeywordRankInfo> existingQZKeywordRankInfos, List<QZOperationType> qzOperationTypes, QZSetting qzSetting){
		Map<String, Map<String, QZKeywordRankInfo>> existingQZKeywordRankInfoMap = new HashMap<>();
		for (QZKeywordRankInfo qzKeywordRankInfo : existingQZKeywordRankInfos) {
            Map<String, QZKeywordRankInfo> qzKeywordRankInfoMap = existingQZKeywordRankInfoMap.get(qzKeywordRankInfo.getTerminalType());
            if (null != qzKeywordRankInfoMap){
		        qzKeywordRankInfoMap.put(qzKeywordRankInfo.getWebsiteType(), qzKeywordRankInfo);
            } else {
                qzKeywordRankInfoMap = new HashMap<>();
                qzKeywordRankInfoMap.put(qzKeywordRankInfo.getWebsiteType(), qzKeywordRankInfo);
            }
            existingQZKeywordRankInfoMap.put(qzKeywordRankInfo.getTerminalType(), qzKeywordRankInfoMap);
		}

		Map<String, String> standardSpeciesMap = new HashMap<>();
		for (QZOperationType qzOperationType : qzOperationTypes) {
            Map<String, QZKeywordRankInfo> qzKeywordRankInfoMap = existingQZKeywordRankInfoMap.get(qzOperationType.getOperationType());
            String standardSpecies = null;
            Set<String> existingStandardSpeciesSet = new HashSet<>();
            if (null != qzKeywordRankInfoMap) {
				if (CollectionUtils.isNotEmpty(qzOperationType.getQzChargeRules())) {
					QZChargeRule qzChargeRule = qzOperationType.getQzChargeRules().iterator().next();
					QZKeywordRankInfo qzKeywordRankInfo = qzKeywordRankInfoMap.get(qzChargeRule.getStandardSpecies());
					if (null != qzKeywordRankInfo && qzKeywordRankInfo.getDataProcessingStatus()) {
						existingStandardSpeciesSet.add(qzChargeRule.getStandardSpecies());
						if (qzChargeRule.getStandardSpecies().equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
							existingStandardSpeciesSet.add("aiZhan");
						}
					} else {
						if (!"other".equals(qzChargeRule.getStandardSpecies())) {
							standardSpecies = qzChargeRule.getStandardSpecies();
						}
					}
				} else {
					existingStandardSpeciesSet.add("aiZhan");
				}
                for (String existingStandardSpecies: existingStandardSpeciesSet) {
                    qzKeywordRankInfoMap.remove(existingStandardSpecies);
                }
            } else {
            	if (CollectionUtils.isNotEmpty(qzOperationType.getQzChargeRules())) {
					QZChargeRule qzChargeRule = qzOperationType.getQzChargeRules().iterator().next();
					if (!"other".equals(qzChargeRule.getStandardSpecies())) {
						standardSpecies = qzChargeRule.getStandardSpecies();
					}
				} else { // 辅助优化, 采集爱站曲线，不计算达标
            		if (qzSetting.getSearchEngine().equals(Constants.SEARCH_ENGINE_BAIDU)) {
						qzKeywordRankInfoService.addQZKeywordRankInfo(qzSetting.getUuid(), qzOperationType.getOperationType(), "aiZhan", false);
					}
				}
            }
            if (null != standardSpecies) {
				standardSpeciesMap.put(qzOperationType.getOperationType(), standardSpecies);
            }
		}

		for (Map.Entry<String, String> entry : standardSpeciesMap.entrySet()) {
			qzKeywordRankInfoService.addQZKeywordRankInfo(qzSetting.getUuid(), entry.getKey(), entry.getValue(), true);
			if (entry.getValue().equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
				if (qzSetting.getSearchEngine().equals(Constants.SEARCH_ENGINE_BAIDU)) {
					qzKeywordRankInfoService.addQZKeywordRankInfo(qzSetting.getUuid(), entry.getKey(), "aiZhan", false);
				}
			}
        }

        for (Map.Entry<String, Map<String, QZKeywordRankInfo>> entry : existingQZKeywordRankInfoMap.entrySet()) {
            for (QZKeywordRankInfo qzKeywordRankInfo : entry.getValue().values()) {
            	qzKeywordRankInfoService.deleteById(qzKeywordRankInfo);
            }
        }
	}

	public Page<QZSetting> searchQZSetting(Page<QZSetting> page, QZSettingSearchCriteria qzSettingSearchCriteria){
		page.setRecords(qzSettingDao.searchQZSettings(page, qzSettingSearchCriteria));
		this.addingQZKeywordRankInfo(page, qzSettingSearchCriteria);
		return page;
	}

	private void addingQZKeywordRankInfo (Page<QZSetting> page, QZSettingSearchCriteria qzSettingSearchCriteria){
        for(QZSetting qzSetting : page.getRecords()){
            List<QZKeywordRankInfo> qzKeywordRankInfos = qzKeywordRankInfoService.searchExistingQZKeywordRankInfo(qzSetting.getUuid(), qzSettingSearchCriteria);
            if (CollectionUtils.isNotEmpty(qzKeywordRankInfos)) {
				Map<String, Map<String, JSONObject>> qzKeywordRankInfoMap = new HashMap<>();
				for (QZKeywordRankInfo qzKeywordRankInfo : qzKeywordRankInfos) {
					this.calculatedQZKeywordRankInfo(qzKeywordRankInfo);
					Map<String, JSONObject> jSONObjectMap = qzKeywordRankInfoMap.get(qzKeywordRankInfo.getTerminalType());
					if (null != jSONObjectMap && !jSONObjectMap.isEmpty()) {
						jSONObjectMap.put(qzKeywordRankInfo.getWebsiteType(), new JSONObject().fromObject(qzKeywordRankInfo));
					} else {
						jSONObjectMap = new HashMap<>();
						jSONObjectMap.put(qzKeywordRankInfo.getWebsiteType(), new JSONObject().fromObject(qzKeywordRankInfo));
					}
					qzKeywordRankInfoMap.put(qzKeywordRankInfo.getTerminalType(), jSONObjectMap);
				}
				qzSetting.setQzKeywordRankInfoMap(qzKeywordRankInfoMap);
				qzChargeRuleService.getChargeRuleTotalPrice(qzSetting, qzSetting.getUuid(), qzSettingSearchCriteria.getTerminalType());
			}
        }
    }

    private void calculatedQZKeywordRankInfo(QZKeywordRankInfo qzKeywordRankInfo) {
        Map<String, Object> map;
        if (!StringUtils.isBlank(qzKeywordRankInfo.getTopTen())) {
            map = calculate(qzKeywordRankInfo.getTopTen());
            qzKeywordRankInfo.setTopTenNum((Integer) map.get("topNum"));
        }
        if (!StringUtils.isBlank(qzKeywordRankInfo.getTopFifty())) {
            map = calculate(qzKeywordRankInfo.getTopFifty());
            qzKeywordRankInfo.setTopFiftyNum((Integer) map.get("topNum"));
        }
    }

    public Map<String, Object> calculate(String topString) {
	    Map<String, Object> map = new HashMap<>();
        String[] topArr = topString.replace("[", "").replace("]", "").split(", ");
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
                    String pcCustomerExcludeKeywords = customerExcludeKeywordService.getCustomerExcludeKeyword(Long.valueOf(qzSettingCriteria.getQzSetting().getCustomerUuid()), qzSettingCriteria.getQzSetting().getUuid(), TerminalTypeEnum.PC.toString(), qzSettingCriteria.getQzSetting().getDomain());
                    String phoneCustomerExcludeKeywords = customerExcludeKeywordService.getCustomerExcludeKeyword(Long.valueOf(qzSettingCriteria.getQzSetting().getCustomerUuid()), qzSettingCriteria.getQzSetting().getUuid(), TerminalTypeEnum.Phone.toString(), qzSettingCriteria.getQzSetting().getDomain());
                    Set pcExcludeKeyword = new HashSet();
                    Set phoneExcludeKeyword = new HashSet();
                    if (null != pcCustomerExcludeKeywords) {
                        pcExcludeKeyword.addAll(Arrays.asList(pcCustomerExcludeKeywords.split(",")));
                    }
                    if (null != phoneCustomerExcludeKeywords) {
                        phoneExcludeKeyword.addAll(Arrays.asList(phoneCustomerExcludeKeywords.split(",")));
                    }
					if (CollectionUtils.isNotEmpty(insertingCustomerKeywords)) {
						List<CustomerKeyword> customerKeywords = new ArrayList<>();
                        QZSetting qzSetting = qzSettingDao.selectById(qzSettingCriteria.getQzSetting().getUuid());
                        List<CustomerKeywordOptimizeGroupCriteria> customerKeywordOptimizeGroupCriteriaList = customerKeywordService.searchOptimizeGroupNameAndCount(insertingCustomerKeywords.get(0).getOptimizeGroupName());
                        for (CustomerKeyword customerKeyword : insertingCustomerKeywords) {
                            if (TerminalTypeEnum.PC.equals(customerKeyword.getTerminalType())){
                                if (!pcExcludeKeyword.isEmpty()){
                                    if (pcExcludeKeyword.contains(customerKeyword.getKeyword())){
                                        customerKeyword.setOptimizeGroupName("zanting");
                                        customerKeyword.setMachineGroup("Pause");
                                    }
                                }
                            } else {
                                if (!phoneExcludeKeyword.isEmpty()){
                                    if (phoneExcludeKeyword.contains(customerKeyword.getKeyword())){
                                        customerKeyword.setOptimizeGroupName("zanting");
										customerKeyword.setMachineGroup("Pause");
                                    }
                                }
                            }
							customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.Capture.name());
                            if (!"zanting".equals(customerKeyword.getOptimizeGroupName())){
                                CustomerKeywordOptimizeGroupCriteria customerKeywordOptimizeGroupCriteria = customerKeywordService.matchOptimizeGroupName(customerKeywordOptimizeGroupCriteriaList, customerKeyword.getOptimizeGroupName(), qzSetting.getGroupMaxCustomerKeywordCount());
                                customerKeyword.setOptimizeGroupName(customerKeywordOptimizeGroupCriteria.getOptimizeGroupName());
                            }
                            customerKeywords.add(customerKeyword);
                        }
						customerKeywordService.addCustomerKeyword(customerKeywords, qzSettingCriteria.getUserName());
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

	public void deleteAll(List<String> uuids){
		for(String uuid : uuids){
			deleteOne(Long.valueOf(uuid));
		}
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
		captureRankJobService.deleteCaptureRankJob(uuid, null);
		qzSettingDao.deleteById(uuid);
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

	public QZSettingSearchGroupInfoVO getQZSettingGroupInfo (QZSettingSearchGroupInfoCriteria qzSettingSearchGroupInfoCriteria) {
		QZSettingSearchGroupInfoVO qzSettingSearchGroupInfoVo = new QZSettingSearchGroupInfoVO();
		qzSettingSearchGroupInfoVo.setCustomerKeywordCount(qzSettingDao.getQZSettingGroupInfo(qzSettingSearchGroupInfoCriteria));
		qzSettingSearchGroupInfoVo.setMachineCount(machineInfoService.getMachineCount(qzSettingSearchGroupInfoCriteria
						.getOptimizeGroupName(), qzSettingSearchGroupInfoCriteria.getTerminalType()));
		qzSettingSearchGroupInfoVo.setOperationCombineName(operationCombineService.getOperationCombineName(
				qzSettingSearchGroupInfoCriteria.getOptimizeGroupName()));
		qzSettingSearchGroupInfoVo.setCategoryTagNames(qzCategoryTagService.findTagNames(qzSettingSearchGroupInfoCriteria.getQzSettingUuid()));
		return qzSettingSearchGroupInfoVo;
	}

	public void saveQZSettingCustomerKeywords (QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria, String userName) {
		for (String terminalType : qzSettingSaveCustomerKeywordsCriteria.getTerminalTypes()) {
			List<CustomerKeyword> customerKeywords = new ArrayList<>();
			String customerExcludeKeywords = customerExcludeKeywordService.getCustomerExcludeKeyword(qzSettingSaveCustomerKeywordsCriteria.getCustomerUuid(), qzSettingSaveCustomerKeywordsCriteria.getQzSettingUuid(), terminalType, qzSettingSaveCustomerKeywordsCriteria.getDomain());
			Set excludeKeyword = new HashSet();
			if (null != customerExcludeKeywords) {
				excludeKeyword.addAll(Arrays.asList(customerExcludeKeywords.split(",")));
			}
            QZSetting qzSetting = qzSettingDao.searchGroupMaxCustomerKeywordCount(qzSettingSaveCustomerKeywordsCriteria.getCustomerUuid(), terminalType,  qzSettingSaveCustomerKeywordsCriteria.getDomain());
            List<CustomerKeywordOptimizeGroupCriteria> customerKeywordOptimizeGroupCriteriaList = customerKeywordService.searchOptimizeGroupNameAndCount(qzSettingSaveCustomerKeywordsCriteria.getOptimizeGroupName());
			for (String keyword : qzSettingSaveCustomerKeywordsCriteria.getKeywords()) {
				CustomerKeyword customerKeyword = new CustomerKeyword();
				customerKeyword.setQzSettingUuid(qzSettingSaveCustomerKeywordsCriteria.getQzSettingUuid());
				customerKeyword.setCustomerUuid(qzSettingSaveCustomerKeywordsCriteria.getCustomerUuid());
				customerKeyword.setType(qzSettingSaveCustomerKeywordsCriteria.getType());
				customerKeyword.setSearchEngine(qzSettingSaveCustomerKeywordsCriteria.getSearchEngine());
				customerKeyword.setTerminalType(terminalType);
				customerKeyword.setUrl(qzSettingSaveCustomerKeywordsCriteria.getDomain());
				customerKeyword.setKeywordEffect(qzSettingSaveCustomerKeywordsCriteria.getKeywordEffect());
				customerKeyword.setServiceProvider("baidutop123");
				customerKeyword.setManualCleanTitle(true);
				customerKeyword.setCollectMethod(CollectMethod.PerMonth.name());
				customerKeyword.setCurrentIndexCount(-1);
				customerKeyword.setPositionFirstFee(-1d);
				customerKeyword.setBearPawNumber(qzSettingSaveCustomerKeywordsCriteria.getBearPawNumber());
                customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.Specify.name());
				customerKeyword.setOptimizeGroupName(qzSettingSaveCustomerKeywordsCriteria.getOptimizeGroupName());
				if (!excludeKeyword.isEmpty()){
					if (excludeKeyword.contains(keyword)){
						customerKeyword.setOptimizeGroupName("zanting");
						customerKeyword.setMachineGroup("Pause");
					}
				}
				if (!"zanting".equals(customerKeyword.getOptimizeGroupName()) && customerKeyword.getOptimizeGroupName().equals(TerminalTypeEnum.PC.name().equals(terminalType) ? qzSetting.getPcGroup() : qzSetting.getPhoneGroup())){
                    CustomerKeywordOptimizeGroupCriteria customerKeywordOptimizeGroupCriteria = customerKeywordService.matchOptimizeGroupName(customerKeywordOptimizeGroupCriteriaList, customerKeyword.getOptimizeGroupName(), qzSetting.getGroupMaxCustomerKeywordCount());
                    customerKeyword.setOptimizeGroupName(customerKeywordOptimizeGroupCriteria.getOptimizeGroupName());
                }
				customerKeyword.setKeyword(keyword);
				customerKeywords.add(customerKeyword);
			}
			customerKeywordService.addCustomerKeyword(customerKeywords, userName);
		}
    }

	public void excludeQZSettingCustomerKeywords (QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        qzSettingExcludeCustomerKeywordsCriteria.setDomain(qzSettingExcludeCustomerKeywordsCriteria.getDomain().replace("http://","").replace("https://","").replace("www.","").split("/")[0]);
		customerKeywordService.excludeCustomerKeyword(qzSettingExcludeCustomerKeywordsCriteria);
        customerExcludeKeywordService.excludeCustomerKeywords(qzSettingExcludeCustomerKeywordsCriteria);
	}

	public CustomerExcludeKeyword echoExcludeKeyword(QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria){
        return customerExcludeKeywordService.echoExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria);
    }

    public List<String> getAvailableOptimizationGroups (GroupSettingCriteria groupSettingCriteria) {
		return qzSettingDao.getAvailableOptimizationGroups(groupSettingCriteria);
    }

    public QZSetting searchGroupMaxCustomerKeywordCount(Long customerUuid, String terminalType, String url){
        url = url.replace("www.","").replace("http://","").replace("https://","").split("/")[0];
        return qzSettingDao.searchGroupMaxCustomerKeywordCount(customerUuid, terminalType,  url);
    }

    public void startMonitorImmediately(String uuids, String userName){
        if(StringUtils.isNotEmpty(uuids)){
            String[] qzSettingUuids = uuids.split(",");
            for (String uuid : qzSettingUuids) {
				QZSetting qzSetting = qzSettingDao.selectById(Long.valueOf(uuid));
				if (null != qzSetting.getPcGroup()) {
					CaptureRankJob existCaptureRankJob = captureRankJobService.findExistCaptureRankJob(Long.valueOf(uuid), TerminalTypeEnum.PC.name());
					if (null == existCaptureRankJob) {
						captureRankJobService.qzAddCaptureRankJob(qzSetting.getPcGroup(), Long.valueOf(uuid), qzSetting.getCustomerUuid(), TerminalTypeEnum.PC.name(), userName);
					}
				}
				if (null != qzSetting.getPhoneGroup()) {
					CaptureRankJob existCaptureRankJob = captureRankJobService.findExistCaptureRankJob(Long.valueOf(uuid), TerminalTypeEnum.Phone.name());
					if (null == existCaptureRankJob) {
						captureRankJobService.qzAddCaptureRankJob(qzSetting.getPhoneGroup(), Long.valueOf(uuid), qzSetting.getCustomerUuid(), TerminalTypeEnum.Phone.name(), userName);
					}
				}
            }
            qzSettingDao.startMonitorImmediately(uuids);
        }
    }

	public void updateCrawlerStatus (Long[] uuids) {
		qzSettingDao.updateCrawlerStatus(uuids);
	}

	public List<ExternalQzSettingVO> getQZSettingTask (int crawlerHour, int taskNumber) {
		return qzSettingDao.getQZSettingTask(crawlerHour, taskNumber);
	}

	public void updateQzSetting (QZSetting qzSetting) {
		qzSettingDao.updateQzSetting(qzSetting);
	}

	public void updateQZKeywordEffectImmediately (String uuids) {
		if(StringUtils.isNotEmpty(uuids)) {
            String[] qzSettingUuids = uuids.split(",");
            for (String uuid : qzSettingUuids) {
                QZSetting qzSetting = qzSettingDao.selectById(Long.valueOf(uuid));
				if (null != qzSetting.getPcGroup()) {
					customerKeywordService.updateCustomerKeywordEffect(qzSetting.getCustomerUuid(), TerminalTypeEnum.PC.name(), qzSetting.getPcGroup());
				}
				if (null != qzSetting.getPhoneGroup()) {
					customerKeywordService.updateCustomerKeywordEffect(qzSetting.getCustomerUuid(), TerminalTypeEnum.Phone.name(), qzSetting.getPhoneGroup());
				}
            }
		}
	}

	public Map<String, String> searchQZSettingSearchEngineMap(QZSettingSearchCriteria criteria, int record) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put(Constants.ALL_SEARCH_ENGINE, TerminalTypeEnum.PC.name());
		map.put(Constants.ALL_SEARCH_ENGINE + TerminalTypeEnum.Phone.name(), TerminalTypeEnum.Phone.name());
		if ((!"".equals(criteria.getDomain()) || !"".equals(criteria.getCustomerUuid())) && record == 1) {
			List<QZSettingVO> qzSettingVos = qzSettingDao.searchQZSettingSearchEngines(criteria.getCustomerUuid(), criteria.getDomain());
			for (QZSettingVO qzSettingVo : qzSettingVos) {
				if (null != qzSettingVo.getPcGroup()) {
					map.put(qzSettingVo.getSearchEngine(), TerminalTypeEnum.PC.name());
				}
				if (null != qzSettingVo.getPhoneGroup()){
					map.put(qzSettingVo.getSearchEngine() + TerminalTypeEnum.Phone.name(), TerminalTypeEnum.Phone.name());
				}
			}
		} else {
			map.putAll(Constants.SEARCH_ENGINE_MAP);
		}
		if (!map.containsKey(criteria.getSearchEngine()) && !map.containsKey(criteria.getSearchEngine() + criteria.getTerminalType())) {
			Map.Entry<String, String> next = map.entrySet().iterator().next();
			if ("".equals(criteria.getSearchEngine()) && TerminalTypeEnum.Phone.name().equals(criteria.getTerminalType())) {
				criteria.setSearchEngine(Constants.ALL_SEARCH_ENGINE);
				criteria.setTerminalType(TerminalTypeEnum.Phone.name());
			} else {
				criteria.setSearchEngine(next.getKey().substring(0, next.getKey().contains("P") ? next.getKey().indexOf('P') : next.getKey().length()));
				criteria.setTerminalType(next.getValue());
			}
		}
		return map;
	}

	public void updateQZSettingRenewalStatus(List<Long> uuids, Integer renewalStatus) {
		qzSettingDao.updateQZSettingRenewalStatus(uuids, renewalStatus);
	}
}
