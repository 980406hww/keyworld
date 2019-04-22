package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.enums.CollectMethod;
import com.keymanager.monitoring.common.sms.SmsService;
import com.keymanager.monitoring.criteria.KeywordInfoCriteria;
import com.keymanager.monitoring.dao.KeywordInfoDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.vo.KeywordInfoVO;
import com.keymanager.monitoring.vo.RequireDeleteKeywordVO;
import com.keymanager.util.Constants;
import com.keymanager.util.common.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeywordInfoService extends ServiceImpl<KeywordInfoDao, KeywordInfo> {
	
	@Autowired
	private KeywordInfoDao keywordInfoDao;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private SmsService smsService;

	@Autowired
	private KeywordInfoSynchronizeService keywordInfoSynchronizeService;
	public void synchronizeKeyword() throws Exception {
		boolean hasRequireDeleteKeyword = false;
		Config webPathConfig = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_WEBPATH);
		if(webPathConfig != null) {
			String webPath = webPathConfig.getValue();
			String username = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_USERNAME).getValue();
			String password = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_PASSWORD).getValue();
			String intervalMinutes = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_INTERVAL_MINUTES).getValue();
			Map map = new HashMap();
			map.put("username", username);
			map.put("password", password);
			map.put("minutes", intervalMinutes);

			KeywordInfoVO keywordInfoVO = keywordInfoSynchronizeService.getKeywordList(webPath, map);
			List<Long> uuids = new ArrayList<Long>();
			for (KeywordInfo keyword : keywordInfoVO) {
				uuids.add(keyword.getId());
				String spliterStr = keyword.getSpliterStr();
				String[] searchEngineInfo = keyword.getSearchEngine().split("_");
				String[] keywordInfos = keyword.getKeywordInfo().split("\n");
				Customer customer = customerService.findCustomerByExternalAccountInfo(keyword.getUserName(), searchEngineInfo[0]);
				if (customer != null) {
					if (keyword.getOperationType().equals("add")) {
						int maxSequence = customerKeywordService.getMaxSequence(searchEngineInfo[1], customer.getEntryType(), customer.getUuid());
						Config config = configService.getConfig(Constants.CONFIG_TYPE_DEFAULT_OPTIMIZE_GROUPNAME, keyword.getSearchEngine());
						List<CustomerKeyword> customerKeywords = new ArrayList<>();
						for (String keywordInfo : keywordInfos) {
							String[] info = keywordInfo.split(spliterStr);
							CustomerKeyword customerKeyword = new CustomerKeyword();
							customerKeyword.setCustomerUuid(customer.getUuid());
							customerKeyword.setType(customer.getEntryType());
							customerKeyword.setSearchEngine(searchEngineInfo[0]);
							customerKeyword.setTerminalType(searchEngineInfo[1]);
							customerKeyword.setKeyword(info[0].trim());
							String url = info[1].trim();
							if (url.substring(url.length() - 1).equals("/")) {
								url = url.substring(0, url.length() - 1);
							}
							customerKeyword.setUrl(url);
							customerKeyword.setOriginalUrl(url);
							customerKeyword.setOptimizeGroupName(config.getValue());
							customerKeyword.setManualCleanTitle(true);
							customerKeyword.setServiceProvider("baidutop123");
							customerKeyword.setCollectMethod(CollectMethod.PerMonth.name());
							customerKeyword.setSequence(++maxSequence);
							customerKeywordService.supplementIndexAndPriceFromExisting(customerKeyword);
							if (customerKeyword.getCurrentIndexCount() == null) {
								customerKeyword.setCurrentIndexCount(-1);
								customerKeyword.setPositionFirstFee(-1d);
							}
							customerKeywords.add(customerKeyword);
						}
						customerKeywordService.addCustomerKeyword(customerKeywords, null);
					} else if (keyword.getOperationType().equals("delete")) {
						List<RequireDeleteKeywordVO> requireDeleteKeywordVOs = new ArrayList<RequireDeleteKeywordVO>();
						hasRequireDeleteKeyword = true;
						for (String keywordInfo : keywordInfos) {
							String[] info = keywordInfo.split(spliterStr);
							RequireDeleteKeywordVO requireDeleteKeywordVO = new RequireDeleteKeywordVO();
							requireDeleteKeywordVO.setCustomerUuid(customer.getUuid());
							requireDeleteKeywordVO.setEntryType(customer.getEntryType());
							requireDeleteKeywordVO.setSearchEngine(searchEngineInfo[0]);
							requireDeleteKeywordVO.setTerminalType(searchEngineInfo[1]);
							requireDeleteKeywordVO.setKeyword(info[0].trim());
							requireDeleteKeywordVO.setUrl(info[1].trim());
							requireDeleteKeywordVOs.add(requireDeleteKeywordVO);
						}
						if (CollectionUtils.isNotEmpty(requireDeleteKeywordVOs)) {
							customerKeywordService.batchUpdateRequireDalete(requireDeleteKeywordVOs);
						}
					}
				}
			}

			// 同步数据库
			if (keywordInfoVO.size() > 0) {
				map.put("uuids", uuids);
				keywordInfoDao.batchInsertKeyword(keywordInfoVO);
				keywordInfoSynchronizeService.deleteKeywordList(webPath, map);
			}

//		if(hasRequireDeleteKeyword) {
//			Config config = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_MOBILE);
//			if(StringUtils.isNotBlank(config.getValue())) {
//				smsService.sendSms(config.getValue(), "系统标记了需要删除的关键词，请注意查看！");
//			}
//		}
		}
	}


	public Page<KeywordInfo> searchKeywordInfos(Page<KeywordInfo> page, KeywordInfoCriteria KeywordInfoCriteria) {
		List<KeywordInfo> keywordInfos = keywordInfoDao.searchKeywordInfos(page, KeywordInfoCriteria);
		for(KeywordInfo keywordInfo : keywordInfos){
			if(StringUtils.isNotEmpty(keywordInfo.getKeywordInfo())) {
				keywordInfo.setKeywordCount(keywordInfo.getKeywordInfo().split("\n").length);
			}
		}
		page.setRecords(keywordInfos);
		return page;
	}
}
