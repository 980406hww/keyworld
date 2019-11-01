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
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.CustomerKeywordVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
	private QZKeywordRankInfoService qzKeywordRankInfoService;

	@Autowired
	private QZCategoryTagService qzCategoryTagService;

	@Autowired
    private CustomerExcludeKeywordService customerExcludeKeywordService;

	@Autowired
	private CaptureRankJobService captureRankJobService;

	@Autowired
	private OperationCombineService operationCombineService;

	@Autowired
	private ConfigService configService;

	public void syncQZCustomerKeyword() {
		// todo 读取配置表需要同步的客户网站标签
		Config config = configService.getConfig(Constants.CONFIG_TYPE_SYNC_QZ_CUSTOMER_KEYWORD, Constants.CONFIG_KEY_SYNC_QZ_CUSTOMER_TAG);
		if (null != config) {
			String syncQZCustomerTagStr = config.getValue();
			if (StringUtil.isNotNullNorEmpty(syncQZCustomerTagStr)) {
				String[] syncQZCustomerTags = syncQZCustomerTagStr.split(",");
				for (String qzCustomerTag : syncQZCustomerTags) {
					// todo 根据网站标签查找操作中的站点信息，进行转储，利用站点id转储站点曲线信息（百度就要爱站/5118，非百度就要指定词）
					List<QZSettingForSync> qzSettingForSyncs = qzSettingDao.getAvailableQZSettingsByTagName(qzCustomerTag);
					List<QZKeywordRankForSync> qzKeywordRanks = new ArrayList<>();
					if (CollectionUtils.isNotEmpty(qzSettingForSyncs)) {
						for (QZSettingForSync qzSettingForSync : qzSettingForSyncs) {
							String searchEngine = qzSettingForSync.getSearchEngine();
							int limitRow = qzSettingForSync.getPcGroup() != null ? 1 : 0;
							limitRow = qzSettingForSync.getPhoneGroup() != null ? (limitRow + 1) : limitRow;
							List<QZKeywordRankForSync> qzKeywordRankForSyncs = qzKeywordRankInfoService.getQZKeywordRankInfoByQZSettingUuid(qzSettingForSync.getQsId(), searchEngine, limitRow);
							if (CollectionUtils.isNotEmpty(qzKeywordRankForSyncs)) {
								qzKeywordRanks.addAll(qzKeywordRankForSyncs);
							}
							// todo 转储关键词信息
							customerKeywordService.batchInsertCustomerKeywordByCustomerUuid(qzSettingForSync.getCustomerId(), qzSettingForSync.getQsId());
						}
						// todo 转储站点曲线信息
						if (CollectionUtils.isNotEmpty(qzKeywordRanks)) {
							qzKeywordRankInfoService.replaceQZKeywordRanks(qzKeywordRanks);
						}
						// todo 转储站点信息
						qzSettingDao.replaceQZSettings(qzSettingForSyncs, qzCustomerTag);
					}
				}
			}
		}
	}

	public ExternalQzSettingVO getAvailableQZSetting(){
		ExternalQzSettingVO externalQzSettingVO = qzSettingDao.selectQZSettingForAutoOperate();
		if(externalQzSettingVO != null){
			List<String> typeList = qzOperationTypeService.getQZSettingStandardSpecies(externalQzSettingVO.getUuid());
			if(CollectionUtils.isNotEmpty(typeList)){
				externalQzSettingVO.setKeywordType(StringUtils.join(typeList,","));
			}
			startQZSettingForUpdateKeyword(externalQzSettingVO.getUuid());
		}
		return externalQzSettingVO;
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
				qzSetting.setPcKeywordExceedMaxCount(true);
			}
			if(phoneKeywordExceedMaxCount) {
				qzSetting.setPhoneKeywordExceedMaxCount(true);
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
			existingQZSetting.setUpdateInterval(qzSetting.getUpdateInterval());
			existingQZSetting.setUpdateTime(new Date());
			existingQZSetting.setCaptureCurrentKeywordCountTime(qzSetting.getCaptureCurrentKeywordCountTime());
			existingQZSetting.setCaptureCurrentKeywordStatus(qzSetting.getCaptureCurrentKeywordStatus());
			existingQZSetting.setCrawlerStatus(Constants.QZ_SETTING_CRAWLER_STATUS_NEW);

			//修改部分
			List<QZOperationType> oldOperationTypes = qzOperationTypeService.searchQZOperationTypesIsDelete(qzSetting.getUuid());
			List<QZOperationType> updOperationTypes = qzSetting.getQzOperationTypes();
			this.updateOperationTypeAndChargeRule(oldOperationTypes, updOperationTypes,
				qzSetting.getUuid(), qzSetting.getCustomerUuid(), userName);
			List<QZKeywordRankInfo> existingQZKeywordRankInfos = qzKeywordRankInfoService.searchExistingQZKeywordRankInfo(qzSetting.getUuid(), null, null);
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
			// 插入qzSetting是的uuid
			Long qzSettingUuid = (long) qzSettingDao.selectLastId();
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
						captureRankJobService.qzAddCaptureRankJob(qzOperationType.getGroup(), qzSettingUuid, qzSetting.getCustomerUuid(), qzOperationType.getOperationType(), userName);
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

	private void updateOperationTypeAndChargeRule(List<QZOperationType> oldOperationTypes,
		List<QZOperationType> newOperationTypes, Long qzSettingUuid, long customerUuid,
		String userName) {
		Map<String, QZOperationType> oldOperationTypeMap = new HashMap<>();
		for (QZOperationType qzOperationType : oldOperationTypes) {
		    oldOperationTypeMap.put(qzOperationType.getOperationType(), qzOperationType);
		}

		for(QZOperationType newOperationType : newOperationTypes) {
			QZOperationType oldOperationType = oldOperationTypeMap.get(newOperationType.getOperationType());
			if(oldOperationType != null) {
				this.updateOperationTypeAndChargeRuleEqual(oldOperationType, newOperationType,
					qzSettingUuid, customerUuid, userName);
				oldOperationTypeMap.remove(newOperationType.getOperationType());
			} else {
				//添加一条
				newOperationType.setQzSettingUuid(qzSettingUuid);
				qzOperationTypeService.insert(newOperationType);
				Long uuid = (long) qzOperationTypeService.selectLastId();
				if (newOperationType.getQzChargeRules().iterator().next().getStandardSpecies().equals(Constants.
						QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
					captureRankJobService
						.qzAddCaptureRankJob(newOperationType.getGroup(), qzSettingUuid,
							customerUuid, newOperationType.getOperationType(), userName);
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

	private void updateOperationTypeAndChargeRuleEqual(QZOperationType oldOperationType,
		QZOperationType newOperationType, Long qzSettingUuid, long customerUuid, String userName) {
		oldOperationType.setUpdateTime(new Date());
		oldOperationType.setOperationType(newOperationType.getOperationType());
		oldOperationType.setOptimizationType(newOperationType.getOptimizationType());
		oldOperationType.setStandardType(newOperationType.getStandardType());
		oldOperationType.setGroup(newOperationType.getGroup());
		oldOperationType.setSubDomainName(newOperationType.getSubDomainName());
		oldOperationType.setMonitorRemark(newOperationType.getMonitorRemark());
		oldOperationType.setMaxKeywordCount(newOperationType.getMaxKeywordCount());
		// 只要是发生改变那么就让它的状态为0
		oldOperationType.setIsDeleted(0);
		qzOperationTypeService.updateById(oldOperationType);

		boolean isDeleteDesignationWord = false;
		if (CollectionUtils.isNotEmpty(oldOperationType.getQzChargeRules())) {
			QZChargeRule qzChargeRule = oldOperationType.getQzChargeRules().iterator().next();
			if (qzChargeRule.getStandardSpecies()
				.equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
				if (!newOperationType.getQzChargeRules().iterator().next().getStandardSpecies()
					.equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
					CaptureRankJob existCaptureRankJob = captureRankJobService
						.findExistCaptureRankJob(qzSettingUuid,
							newOperationType.getOperationType());
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
			if (!hasDesignationWord && qzChargeRule.getStandardSpecies()
				.equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)) {
				hasDesignationWord = true;
			}
			qzChargeRule.setQzOperationTypeUuid(oldOperationType.getUuid());
			qzChargeRuleService.insert(qzChargeRule);
		}
		if (hasDesignationWord) {
			CaptureRankJob existCaptureRankJob = captureRankJobService
				.findExistCaptureRankJob(qzSettingUuid, newOperationType.getOperationType());
			if (null == existCaptureRankJob) {
				captureRankJobService
					.qzAddCaptureRankJob(newOperationType.getGroup(), qzSettingUuid, customerUuid,
						newOperationType.getOperationType(), userName);
			}
		}
	}

	private void updateQZKeywordRankInfo(List<QZKeywordRankInfo> existingQZKeywordRankInfos, List<QZOperationType> qzOperationTypes, QZSetting qzSetting){
		Map<String, Map<String, QZKeywordRankInfo>> existingQZKeywordRankInfoMap = new HashMap<>(2);
		for (QZKeywordRankInfo qzKeywordRankInfo : existingQZKeywordRankInfos) {
            Map<String, QZKeywordRankInfo> qzKeywordRankInfoMap = existingQZKeywordRankInfoMap.get(qzKeywordRankInfo.getTerminalType());
            if (null != qzKeywordRankInfoMap){
		        qzKeywordRankInfoMap.put(qzKeywordRankInfo.getWebsiteType(), qzKeywordRankInfo);
            } else {
                qzKeywordRankInfoMap = new HashMap<>(4);
                qzKeywordRankInfoMap.put(qzKeywordRankInfo.getWebsiteType(), qzKeywordRankInfo);
            }
            existingQZKeywordRankInfoMap.put(qzKeywordRankInfo.getTerminalType(), qzKeywordRankInfoMap);
		}

		Map<String, String> standardSpeciesMap = new HashMap<>(2);
		for (QZOperationType qzOperationType : qzOperationTypes) {
            Map<String, QZKeywordRankInfo> qzKeywordRankInfoMap = existingQZKeywordRankInfoMap.get(qzOperationType.getOperationType());
            String standardSpecies = null;
            Set<String> existingStandardSpeciesSet = new HashSet<>();
            if (null != qzKeywordRankInfoMap) {
				if (CollectionUtils.isNotEmpty(qzOperationType.getQzChargeRules())) {
					QZChargeRule qzChargeRule = qzOperationType.getQzChargeRules().iterator().next();
					String newStandardSpecies = qzChargeRule.getStandardSpecies();
					QZKeywordRankInfo qzKeywordRankInfo = qzKeywordRankInfoMap.get(newStandardSpecies);
					if (null != qzKeywordRankInfo && qzKeywordRankInfo.getDataProcessingStatus()) {
						existingStandardSpeciesSet.add(newStandardSpecies);
						if (newStandardSpecies.equals(Constants.QZ_CHARGE_RULE_STANDARD_SPECIES_DESIGNATION_WORD)
								&& qzSetting.getSearchEngine().equals(Constants.SEARCH_ENGINE_BAIDU)) {
							existingStandardSpeciesSet.add("aiZhan");
						}
					} else {
						if (!"other".equals(newStandardSpecies)) {
							standardSpecies = newStandardSpecies;
						}
					}
				} else {
					existingStandardSpeciesSet.add("aiZhan");
				}
				existingStandardSpeciesSet.add("xt");
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
		return page;
	}

    private void initQZKeywordRankInfoVo(QZKeywordRankInfo qzKeywordRankInfo, QZKeywordRankInfoVO rankInfoVo) {
		rankInfoVo.setUuid(qzKeywordRankInfo.getUuid());
		rankInfoVo.setQzSettingUuid(qzKeywordRankInfo.getQzSettingUuid());
		rankInfoVo.setTerminalType(qzKeywordRankInfo.getTerminalType());
		rankInfoVo.setWebsiteType(qzKeywordRankInfo.getWebsiteType());
		rankInfoVo.setDataProcessingStatus(qzKeywordRankInfo.getDataProcessingStatus());
		String[] topTen = calculate(qzKeywordRankInfo.getTopTen());
		String[] topFifty = calculate(qzKeywordRankInfo.getTopFifty());
		if (null != topTen) {
			rankInfoVo.setTopTenNum(Integer.parseInt(topTen[topTen.length - 1]));
			rankInfoVo.setTopFiftyNum(Integer.parseInt(topFifty[topFifty.length - 1]));
		} else {
			rankInfoVo.setTopTenNum(0);
			rankInfoVo.setTopFiftyNum(0);
		}
		rankInfoVo.setCreateTopTenNum(qzKeywordRankInfo.getCreateTopTenNum() == null ? 0 : qzKeywordRankInfo.getCreateTopTenNum());
		rankInfoVo.setCreateTopFiftyNum(qzKeywordRankInfo.getCreateTopFiftyNum() == null ? 0 : qzKeywordRankInfo.getCreateTopFiftyNum());
		rankInfoVo.setTopTen(topTen);
		rankInfoVo.setTopTwenty(calculate(qzKeywordRankInfo.getTopTwenty()));
		rankInfoVo.setTopThirty(calculate(qzKeywordRankInfo.getTopThirty()));
		rankInfoVo.setTopForty(calculate(qzKeywordRankInfo.getTopForty()));
		rankInfoVo.setTopFifty(topFifty);
		rankInfoVo.setTopHundred(calculate(qzKeywordRankInfo.getTopHundred()));
		rankInfoVo.setDate(calculate(qzKeywordRankInfo.getDate()));
		rankInfoVo.setBaiduRecord(calculate(qzKeywordRankInfo.getBaiduRecord()));
		rankInfoVo.setBaiduRecordFullDate(calculate(qzKeywordRankInfo.getBaiduRecordFullDate()));
		rankInfoVo.setAchieveTime(qzKeywordRankInfo.getAchieveTime());
    }

    public String[] calculate(String targetStr) {
		String[] split = null;
		if (StringUtil.isNotNullNorEmpty(targetStr)) {
			split = targetStr.replace("[", "").replace("]", "").replaceAll("'", "").split(", ");
			Collections.reverse(Arrays.asList(split));
		}
		return split;
    }

	public Map<String, Object> searchQZKeywordRankInfo(long uuid, String terminalType, String optimizeGroupName, Long customerUuid) {
		List<QZKeywordRankInfo> qzKeywordRankInfos = qzKeywordRankInfoService.searchExistingQZKeywordRankInfo(uuid, terminalType, null);
		Map<String, Object> rankInfoVoMap = new HashMap<>(8);
		if (CollectionUtils.isNotEmpty(qzKeywordRankInfos)) {
			int price = 0;
			for (QZKeywordRankInfo qzKeywordRankInfo : qzKeywordRankInfos) {
				QZKeywordRankInfoVO qzKeywordRankInfoVo = new QZKeywordRankInfoVO();
				this.initQZKeywordRankInfoVo(qzKeywordRankInfo, qzKeywordRankInfoVo);
				price += qzKeywordRankInfo.getCurrentPrice() == null ? 0 : qzKeywordRankInfo.getCurrentPrice();
				rankInfoVoMap.put(qzKeywordRankInfo.getWebsiteType(), qzKeywordRankInfoVo);
			}
			rankInfoVoMap.put("price", price);
			this.getQZSettingGroupInfo(rankInfoVoMap, uuid, terminalType, optimizeGroupName, customerUuid);
		}
		return rankInfoVoMap;
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
		customerKeyword.setQzSettingUuid(qzSettingCriteria.getExternalQzSettingVO().getUuid());
		customerKeyword.setKeyword(customerKeywordVO.getKeyword());
		customerKeyword.setUrl(customerKeywordVO.getUrl());
		customerKeyword.setBearPawNumber(qzSettingCriteria.getExternalQzSettingVO().getBearPawNumber());
		customerKeyword.setTitle(customerKeywordVO.getTitle());
		customerKeyword.setOrderNumber(customerKeywordVO.getOrderNumber());
		customerKeyword.setCurrentIndexCount(customerKeywordVO.getCurrentIndexCount());
		customerKeyword.setCustomerUuid(qzSettingCriteria.getExternalQzSettingVO().getCustomerUuid());
		customerKeyword.setOptimizePlanCount(customerKeywordVO.getCurrentIndexCount() + 8);
		customerKeyword.setOptimizeRemainingCount(customerKeywordVO.getCurrentIndexCount() + 8);
		customerKeyword.setServiceProvider("baidutop123");
		customerKeyword.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
		customerKeyword.setCollectMethod(CollectMethod.PerMonth.name());
		customerKeyword.setType(qzSettingCriteria.getExternalQzSettingVO().getType());
		customerKeyword.setStartOptimizedTime(Utils.getCurrentTimestamp());
		customerKeyword.setStatus(1);
		customerKeyword.setCreateTime(Utils.getCurrentTimestamp());
		customerKeyword.setUpdateTime(Utils.getCurrentTimestamp());
		customerKeyword.setInitialIndexCount(customerKeywordVO.getCurrentIndexCount());
		customerKeyword.setTerminalType(qzOperationType.getOperationType());
		customerKeyword.setOptimizeGroupName(qzOperationType.getGroup());
		customerKeyword.setMachineGroup("sqz");
		return customerKeyword;
	}

	public void updateResult(QZSettingCriteria qzSettingCriteria) throws Exception {
		boolean pcKeywordExceedMaxCount = false;
		boolean phoneKeywordExceedMaxCount = false;
		if (!qzSettingCriteria.isDownloadTimesUsed()){
			if (CollectionUtils.isNotEmpty(qzSettingCriteria.getCustomerKeywordVOs())) {
				List<QZOperationType> qzOperationTypes = qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(qzSettingCriteria.getExternalQzSettingVO().getUuid());
				//取用户全站类别下所有关键字
				List<CustomerKeywordSummaryInfoVO> customerKeywordSummaryInfoVOs = customerKeywordService.searchCustomerKeywordSummaryInfo(qzSettingCriteria.getExternalQzSettingVO().getType(), qzSettingCriteria.getExternalQzSettingVO().getCustomerUuid());
				Map<String, Set<String>> customerKeywordSummaryInfoMaps = new HashMap<String, Set<String>>();
				int pcKeywordCountNum = 0, phoneKeywordCountNum = 0;
				//关键字分类
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
							pcKeywordExceedMaxCount = true;
						}
						if (phoneKeywordCountNum == 0) {
							phoneKeywordExceedMaxCount = true;
						}
					}
                    String pcCustomerExcludeKeywords = customerExcludeKeywordService.getCustomerExcludeKeyword((long) qzSettingCriteria.getExternalQzSettingVO().getCustomerUuid(),
							qzSettingCriteria.getExternalQzSettingVO().getUuid(), TerminalTypeEnum.PC.toString(), qzSettingCriteria.getExternalQzSettingVO().getDomain());
                    String phoneCustomerExcludeKeywords = customerExcludeKeywordService.getCustomerExcludeKeyword((long) qzSettingCriteria.getExternalQzSettingVO().getCustomerUuid(),
							qzSettingCriteria.getExternalQzSettingVO().getUuid(), TerminalTypeEnum.Phone.toString(), qzSettingCriteria.getExternalQzSettingVO().getDomain());

                    Set<String> pcExcludeKeyword = new HashSet<>();
                    Set<String> phoneExcludeKeyword = new HashSet<String>();
                    if (null != pcCustomerExcludeKeywords) {
                        pcExcludeKeyword.addAll(Arrays.asList(pcCustomerExcludeKeywords.split(",")));
                    }
                    if (null != phoneCustomerExcludeKeywords) {
                        phoneExcludeKeyword.addAll(Arrays.asList(phoneCustomerExcludeKeywords.split(",")));
                    }
					if (CollectionUtils.isNotEmpty(insertingCustomerKeywords)) {
						List<CustomerKeyword> customerKeywords = new ArrayList<>();
                        for (CustomerKeyword customerKeyword : insertingCustomerKeywords) {
                            if (TerminalTypeEnum.PC.name().equals(customerKeyword.getTerminalType())){
                                if (!pcExcludeKeyword.isEmpty()){
                                    if (pcExcludeKeyword.contains(customerKeyword.getKeyword())){
										customerKeyword.setStatus(3);
                                    }
                                }
                            } else {
                                if (!phoneExcludeKeyword.isEmpty()){
                                    if (phoneExcludeKeyword.contains(customerKeyword.getKeyword())){
										customerKeyword.setStatus(3);
                                    }
                                }
                            }
							customerKeyword.setCustomerKeywordSource(CustomerKeywordSourceEnum.Capture.name());
                            customerKeywords.add(customerKeyword);
                        }
						customerKeywordService.addCustomerKeyword(customerKeywords, qzSettingCriteria.getUserName());
					}
				}
			}
		}
		this.completeQZSetting(qzSettingCriteria.getExternalQzSettingVO().getUuid(), qzSettingCriteria.isDownloadTimesUsed(), pcKeywordExceedMaxCount, phoneKeywordExceedMaxCount);
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

	private void getQZSettingGroupInfo(Map<String, Object> rankInfoVoMap, long uuid, String terminalType, String optimizeGroupName, Long customerUuid) {
		rankInfoVoMap.put("customerKeywordCount", qzSettingDao.getQZSettingGroupInfo(terminalType, optimizeGroupName, customerUuid));
		rankInfoVoMap.put("operationCombineName", operationCombineService.getOperationCombineName(optimizeGroupName));
		rankInfoVoMap.put("categoryTagNames", qzCategoryTagService.findTagNames(uuid));
		rankInfoVoMap.put("standardTime", qzOperationTypeService.getStandardTime(uuid, terminalType));
	}

	public void saveQZSettingCustomerKeywords (QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria, String userName) {
		for (String terminalType : qzSettingSaveCustomerKeywordsCriteria.getTerminalTypes()) {
			List<CustomerKeyword> customerKeywords = new ArrayList<>();
			String customerExcludeKeywords = customerExcludeKeywordService.getCustomerExcludeKeyword(qzSettingSaveCustomerKeywordsCriteria.getCustomerUuid(), qzSettingSaveCustomerKeywordsCriteria.getQzSettingUuid(), terminalType, qzSettingSaveCustomerKeywordsCriteria.getDomain());
			Set<String> excludeKeyword = new HashSet<String>();
			if (null != customerExcludeKeywords) {
				excludeKeyword.addAll(Arrays.asList(customerExcludeKeywords.split(",")));
			}
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
				customerKeyword.setMachineGroup(qzSettingSaveCustomerKeywordsCriteria.getMachineGroupName());
				if (!excludeKeyword.isEmpty()){
					if (excludeKeyword.contains(keyword)){
						customerKeyword.setStatus(3);
					}
				}
				customerKeyword.setKeyword(keyword);
				customerKeyword.setOptimizePlanCount(10);
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

    public void updateCrawlerStatus(List<Long> uuids) {
        qzSettingDao.updateCrawlerStatus(uuids);
    }

    public List<ExternalQzSettingVO> getQZSettingTask(int crawlerHour, int taskNumber) {
        return qzSettingDao.getQZSettingTask(crawlerHour, taskNumber);
    }

	public void updateQzSetting (QZSetting qzSetting) {
		qzSettingDao.updateQzSetting(qzSetting);
	}

	public Map<String, String> searchQZSettingSearchEngineMap(QZSettingSearchCriteria criteria, int record) {
		Map<String, String> map = new LinkedHashMap<>();
		map.put(Constants.ALL_SEARCH_ENGINE, TerminalTypeEnum.PC.name());
		map.put(Constants.ALL_SEARCH_ENGINE + TerminalTypeEnum.Phone.name(), TerminalTypeEnum.Phone.name());
		boolean displayExistTabFlag = (!"".equals(criteria.getDomain()) || !"".equals(criteria.getCustomerUuid())) && record == 1;
		if (displayExistTabFlag) {
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

	public void updateQzCategoryTags(List<String> uuids, List<QZCategoryTag> targetQZCategoryTags) {
		for (String uuid : uuids) {
			List<QZCategoryTag> existingQZCategoryTags = qzCategoryTagService.searchCategoryTagByQZSettingUuid(Long.parseLong(uuid));
			qzCategoryTagService.updateQZCategoryTag(existingQZCategoryTags, targetQZCategoryTags, Long.parseLong(uuid));
		}
	}

	public void generateRankingCurve() {
		List<QZSetting> settings = qzSettingDao.searchAllQZSettingForGenerateRankingCurve();
		if (CollectionUtils.isNotEmpty(settings)) {
			for (QZSetting qzSetting : settings) {
				if (null != qzSetting) {
					String terminalType;
					if (null != qzSetting.getPcGroup()) {
						terminalType = "PC";
						generateRankingCurveRankInfo(qzSetting, terminalType);
					}
					if (null != qzSetting.getPhoneGroup()) {
						terminalType = "Phone";
						generateRankingCurveRankInfo(qzSetting, terminalType);
					}
				}
			}
		}
	}

	private void generateRankingCurveRankInfo(QZSetting qzSetting, String terminalType) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

		CustomerKeywordRankingCountVO countVo = customerKeywordService
			.getCustomerKeywordRankingCount(qzSetting.getCustomerUuid(),
				"PC".equals(terminalType) ? qzSetting.getPcGroup() : qzSetting.getPhoneGroup());
		List<QZKeywordRankInfo> rankInfos = qzKeywordRankInfoService
			.searchExistingQZKeywordRankInfo(qzSetting.getUuid(), terminalType, "xt");

		if (CollectionUtils.isNotEmpty(rankInfos)) {
			if (rankInfos.size() > 1) {
				handlingQKErrorData(qzSetting, terminalType);
				saveQZRankingCurveRankInfo(qzSetting.getUuid(), terminalType, countVo, null);
			} else {
				QZKeywordRankInfo rankInfo = rankInfos.iterator().next();
				String dateStr = rankInfo.getDate();
				if (StringUtil.isNullOrEmpty(dateStr)) {
					fillRankInfo(rankInfo, countVo, 1);
					rankInfo.setDate("['" + sdf.format(date) + "']");
				} else {
					String[] dateStrings = dateStr.substring(1, dateStr.length() - 1).split(", ");
					String nowDate = dateStrings[0];
					if (nowDate.equals("'" + sdf.format(date) + "'")) {
						// 为当天数据, 替换数据
						if (dateStrings.length == 1) {
							rankInfo.setTopTen("[" + countVo.getTopTenNum() + "]");
							rankInfo.setTopTwenty("[" + countVo.getTopTwentyNum() + "]");
							rankInfo.setTopThirty("[" + countVo.getTopThirtyNum() + "]");
							rankInfo.setTopForty("[" + countVo.getTopFortyNum() + "]");
							rankInfo.setTopFifty("[" + countVo.getTopFiftyNum() + "]");
						} else {
							fillRankInfo(rankInfo, countVo, 0);
						}
					} else {
						// 不为当天, 添加数据
						fillRankInfo(rankInfo, countVo, 1);
						rankInfo
							.setDate(fillData(rankInfo.getDate(), "'" + sdf.format(date) + "'", 1));
					}
				}
				qzKeywordRankInfoService.saveQZKeywordRankInfo(rankInfo);
			}
		} else {
			saveQZRankingCurveRankInfo(qzSetting.getUuid(), terminalType, countVo, null);
		}
	}

    private void fillRankInfo(QZKeywordRankInfo rankInfo, CustomerKeywordRankingCountVO countVo, int fillMode) {
        rankInfo.setTopTen(fillData(rankInfo.getTopTen(), countVo.getTopTenNum() + "", fillMode));
        rankInfo.setTopTwenty(fillData(rankInfo.getTopTwenty(), countVo.getTopTwentyNum() + "", fillMode));
        rankInfo.setTopThirty(fillData(rankInfo.getTopThirty(), countVo.getTopThirtyNum() + "", fillMode));
        rankInfo.setTopForty(fillData(rankInfo.getTopForty(), countVo.getTopFortyNum() + "", fillMode));
        rankInfo.setTopFifty(fillData(rankInfo.getTopFifty(), countVo.getTopFiftyNum() + "", fillMode));
    }

    private String fillData(String str, String num, int fillMode) {
        if (str == null) {
            return "[" + num + "]";
        }
        StringBuilder sb = new StringBuilder(str);
        if (fillMode == 1) { // 添加
            sb.replace(0, 1, "[" + num + ", ");
        } else { // 替换
            int index = sb.indexOf(",");
            sb.replace(0, index, "[" + num);
        }
        return sb.toString();
    }

	private void saveQZRankingCurveRankInfo(long qzSettingUuid, String terminalType,
		CustomerKeywordRankingCountVO countVo, Integer qzSettingKeywordCount) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");

		QZKeywordRankInfo rankInfo = new QZKeywordRankInfo();
		rankInfo.setQzSettingUuid(qzSettingUuid);
		rankInfo.setTerminalType(terminalType);
		rankInfo.setWebsiteType("xt");
		rankInfo.setDataProcessingStatus(false);

		if (null == qzSettingKeywordCount) {
			fillRankInfo(rankInfo, countVo, 1);
			rankInfo.setDate("['" + sdf.format(date) + "']");
		} else {
			rankInfo.setBaiduRecord(fillData(rankInfo.getBaiduRecord(), "'" + qzSettingKeywordCount + "'", 1));
			rankInfo.setBaiduRecordFullDate("['" + sdf.format(date) + "']");
		}
		qzKeywordRankInfoService.saveQZKeywordRankInfo(rankInfo);
	}

    public void generateQZSettingKeywordCountCurve() {
		List<QZSetting> qzSettings = qzSettingDao.searchAllQZSettingForGenerateRankingCurve();
		if (CollectionUtils.isNotEmpty(qzSettings)) {
			for (QZSetting qzSetting : qzSettings) {
				if (null != qzSetting) {
					String terminalType;
					if (null != qzSetting.getPcGroup()) {
						terminalType = "PC";
						generateQZSettingKeywordCountCurveRankInfo(qzSetting, terminalType);
					}
					if (null != qzSetting.getPhoneGroup()) {
						terminalType = "Phone";
						generateQZSettingKeywordCountCurveRankInfo(qzSetting, terminalType);
					}
				}
			}
		}
	}

	private void generateQZSettingKeywordCountCurveRankInfo(QZSetting qzSetting,
		String terminalType) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		int qzSettingKeywordCount = customerKeywordService
			.getQZSettingKeywordCount(qzSetting.getCustomerUuid(), "PC"
				.equals(terminalType) ? qzSetting.getPcGroup() : qzSetting.getPhoneGroup());
		List<QZKeywordRankInfo> rankInfos = qzKeywordRankInfoService
			.searchExistingQZKeywordRankInfo(qzSetting.getUuid(), terminalType, "xt");

		if (CollectionUtils.isNotEmpty(rankInfos)) {
			if (rankInfos.size() > 1) {
				handlingQKErrorData(qzSetting, terminalType);
				saveQZRankingCurveRankInfo(qzSetting.getUuid(), terminalType, null,
					qzSettingKeywordCount);
			} else {
				QZKeywordRankInfo rankInfo = rankInfos.iterator().next();
				String dateStr = rankInfo.getBaiduRecordFullDate();
				if (StringUtil.isNullOrEmpty(dateStr)) {
					rankInfo.setBaiduRecord("['" + qzSettingKeywordCount + "']");
					rankInfo.setBaiduRecordFullDate("['" + sdf.format(date) + "']");
				} else {
					String[] dateStrings = dateStr.substring(1, dateStr.length() - 1).split(", ");
					String nowDate = dateStrings[0];
					if (nowDate.equals("'" + sdf.format(date) + "'")) {
						// 为当天数据, 替换数据
						if (dateStrings.length == 1) {
							rankInfo.setBaiduRecord("['" + qzSettingKeywordCount + "']");
						} else {
							rankInfo.setBaiduRecord(fillData(rankInfo.getBaiduRecord(),
								"'" + qzSettingKeywordCount + "'", 0));
						}
					} else {
						// 不为当天, 添加数据
						rankInfo.setBaiduRecord(
							fillData(rankInfo.getBaiduRecord(), "'" + qzSettingKeywordCount + "'",
								1));
						rankInfo.setBaiduRecordFullDate(fillData(rankInfo.getBaiduRecordFullDate(),
							"'" + sdf.format(date) + "'", 1));
					}
				}
				qzKeywordRankInfoService.saveQZKeywordRankInfo(rankInfo);
			}
		} else {
			saveQZRankingCurveRankInfo(qzSetting.getUuid(), terminalType, null,
				qzSettingKeywordCount);
		}

	}

	private void handlingQKErrorData(QZSetting qzSetting, String terminalType) {
		List<QZKeywordRankInfo> rankInfos = qzKeywordRankInfoService
			.searchExistingQZKeywordRankInfo(qzSetting.getUuid(), terminalType, null);
		for (QZKeywordRankInfo keywordRankInfo : rankInfos) {
			qzKeywordRankInfoService.deleteById(keywordRankInfo.getUuid());
			if (!"xt".equals(keywordRankInfo.getWebsiteType())) {
				QZKeywordRankInfo qzKeywordRankInfo = new QZKeywordRankInfo();
				qzKeywordRankInfo.setQzSettingUuid(keywordRankInfo.getQzSettingUuid());
				qzKeywordRankInfo.setTerminalType(keywordRankInfo.getTerminalType());
				qzKeywordRankInfo.setWebsiteType(keywordRankInfo.getWebsiteType());
				qzKeywordRankInfo
					.setDataProcessingStatus(keywordRankInfo.getDataProcessingStatus());
				qzKeywordRankInfo.setCreateTopTenNum(keywordRankInfo.getCreateTopTenNum());
				qzKeywordRankInfo
					.setCreateTopFiftyNum(keywordRankInfo.getCreateTopFiftyNum());
				qzKeywordRankInfoService.insert(qzKeywordRankInfo);

				QZSetting setting = qzSettingDao.selectById(qzSetting.getUuid());
				setting.setCrawlerStatus("new");
				setting.setUpdateTime(new Date());
				qzSettingDao.updateById(setting);
			}
		}
	}

	public String findQZCustomer(String domain) {
		return qzSettingDao.findQZCustomer(domain);
	}
}
