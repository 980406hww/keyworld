package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.sms.SmsService;
import com.keymanager.monitoring.dao.KeywordInfoDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.KeywordInfo;
import com.keymanager.monitoring.vo.KeywordInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public void synchronizeKeyword() {
		boolean hasRequireDeleteKeyword = false;
		KeywordInfoVO keywordInfoVO = keywordInfoSynchronizeService.getKeywordList();
		for (KeywordInfo keyword : keywordInfoVO) {
			String spliterStr = keyword.getSpliterStr();
			String[] searchEngineInfo = keyword.getSearchEngine().split("_");
			String[] keywordInfos = keyword.getKeywordInfo().split("\n");

			if(keyword.getOperationType().equals("add")) {
				Customer customer = customerService.findCustomerByUserName(keyword.getUserName() + "_" + keyword.getSearchEngine());
				if(customer != null) {
					Config config = configService.getConfig("DefaultOptimizeGroupName", keyword.getSearchEngine());
					for (String keywordInfo : keywordInfos) {
						String[] info = keywordInfo.split(spliterStr);
						CustomerKeyword customerKeyword = new CustomerKeyword();
						customerKeyword.setType(customer.getEntryType());
						customerKeyword.setSearchEngine(searchEngineInfo[0]);
						customerKeyword.setTerminalType(searchEngineInfo[1]);
						customerKeyword.setKeyword(info[0]);
						customerKeyword.setUrl(info[1]);
						customerKeyword.setOptimizeGroupName(config.getValue());
						customerKeywordService.addCustomerKeyword(customerKeyword, null);
					}
				}
			} else if(keyword.getOperationType().equals("delete")) {
				hasRequireDeleteKeyword = true;
				for (String keywordInfo : keywordInfos) {
					String[] info = keywordInfo.split(spliterStr);
					customerKeywordService.updateCustomerKeywordRequireDalete(searchEngineInfo[0], searchEngineInfo[1], info[0], info[0]);
				}
			}
		}
		// 同步数据库
		keywordInfoDao.batchInsertKeyword(keywordInfoVO);

		if(hasRequireDeleteKeyword) {
			//smsService.sendSms("", "hello world");
		}
	}

}
