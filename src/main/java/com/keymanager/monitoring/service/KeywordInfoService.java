package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.enums.CollectMethod;
import com.keymanager.monitoring.common.sms.SmsService;
import com.keymanager.monitoring.dao.KeywordInfoDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.KeywordInfo;
import com.keymanager.monitoring.vo.KeywordInfoVO;
import com.keymanager.monitoring.vo.RequireDeleteKeywordVO;
import com.keymanager.util.Constants;
import com.keymanager.util.PropertiesUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
		KeywordInfoVO keywordInfoVO = keywordInfoSynchronizeService.getKeywordList();
		for (KeywordInfo keyword : keywordInfoVO) {
			String spliterStr = keyword.getSpliterStr();
			String[] searchEngineInfo = keyword.getSearchEngine().split("_");
			String[] keywordInfos = keyword.getKeywordInfo().split("\n");

			if(keyword.getOperationType().equals("add")) {
				Customer customer = customerService.findCustomerByUserName(keyword.getUserName(), searchEngineInfo[0]);
				if(customer != null) {
					Config config = configService.getConfig(Constants.CONFIG_TYPE_DEFAULT_OPTIMIZE_GROUPNAME, keyword.getSearchEngine());
					for (String keywordInfo : keywordInfos) {
						String[] info = keywordInfo.split(spliterStr);
						CustomerKeyword customerKeyword = new CustomerKeyword();
						customerKeyword.setCustomerUuid(customer.getUuid());
						customerKeyword.setType(customer.getEntryType());
						customerKeyword.setSearchEngine(searchEngineInfo[0]);
						customerKeyword.setTerminalType(searchEngineInfo[1]);
						customerKeyword.setKeyword(info[0]);
						customerKeyword.setUrl(info[1]);
						customerKeyword.setOptimizeGroupName(config.getValue());
						customerKeyword.setServiceProvider("baidutop123");
						customerKeyword.setCurrentIndexCount(100);
						customerKeyword.setCollectMethod(CollectMethod.PerMonth.name());
						customerKeywordService.addCustomerKeyword(customerKeyword, null);
					}
				}
			} else if(keyword.getOperationType().equals("delete")) {
				List<RequireDeleteKeywordVO> requireDeleteKeywordVOs = new ArrayList<RequireDeleteKeywordVO>();
				hasRequireDeleteKeyword = true;
				for (String keywordInfo : keywordInfos) {
					String[] info = keywordInfo.split(spliterStr);
					RequireDeleteKeywordVO requireDeleteKeywordVO = new RequireDeleteKeywordVO();
					requireDeleteKeywordVO.setSearchEngine(searchEngineInfo[0]);
					requireDeleteKeywordVO.setTerminalType(searchEngineInfo[1]);
					requireDeleteKeywordVO.setKeyword(info[0]);
					requireDeleteKeywordVO.setUrl(info[1]);
					requireDeleteKeywordVOs.add(requireDeleteKeywordVO);
				}
				if(CollectionUtils.isNotEmpty(requireDeleteKeywordVOs)) {
					customerKeywordService.batchUpdateRequireDalete(requireDeleteKeywordVOs);
				}
			}
		}
		// 同步数据库
		if(keywordInfoVO.size() > 0) {
			keywordInfoDao.batchInsertKeyword(keywordInfoVO);
			keywordInfoSynchronizeService.deleteKeywordList();
		}

		if(hasRequireDeleteKeyword) {
			smsService.sendSms(PropertiesUtil.phoneFrom, "系统标记了需要删除的关键词，请注意查看！");
		}
	}

}
