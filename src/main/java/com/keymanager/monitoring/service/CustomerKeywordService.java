package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.monitoring.dao.CustomerKeywordDao;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.QZCaptureTitleLog;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.enums.QZCaptureTitleLogStatusEnum;
import com.keymanager.value.CustomerKeywordForCaptureTitle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerKeywordService extends ServiceImpl<CustomerKeywordDao, CustomerKeyword> {
	private static Logger logger = LoggerFactory.getLogger(CustomerKeywordService.class);

	@Autowired
	private QZCaptureTitleLogService qzCaptureTitleLogService;

	@Autowired
	private QZSettingService qzSettingService;

	public String searchCustomerKeywordForCaptureTitle(String terminalType) throws Exception{
		QZCaptureTitleLog qzCaptureTitleLog = qzCaptureTitleLogService.getAvailableQZSetting(QZCaptureTitleLogStatusEnum.Processing.getValue(), terminalType);
		if(qzCaptureTitleLog == null){
			qzCaptureTitleLog = qzCaptureTitleLogService.getAvailableQZSetting(QZCaptureTitleLogStatusEnum.New.getValue(), terminalType);
			if(qzCaptureTitleLog != null){
				qzCaptureTitleLogService.startQZCaptureTitleLog(qzCaptureTitleLog.getUuid());
			}
		}
		if(qzCaptureTitleLog == null){
			return "";
		}
		CustomerKeywordManager manager = new CustomerKeywordManager();
		CustomerKeywordForCaptureTitle captureTitle = manager.searchCustomerKeywordForCaptureTitle("keyword", qzCaptureTitleLog.getTerminalType(),
				qzCaptureTitleLog.getGroup(), qzCaptureTitleLog.getCustomerUuid());

		if(captureTitle == null){
			qzCaptureTitleLogService.completeQZCaptureTitleLog(qzCaptureTitleLog.getUuid());
			manager.deleteEmptyTitleCustomerKeyword("keyword", qzCaptureTitleLog.getTerminalType(), qzCaptureTitleLog.getCustomerUuid(),
					qzCaptureTitleLog.getType(), qzCaptureTitleLog.getGroup());
			return "";
		}else{
			QZSetting qzSetting = qzSettingService.selectById(qzCaptureTitleLog.getQzSettingUuid());
			captureTitle.setWholeUrl(qzSetting.getDomain());
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(captureTitle);
		}
	}

	public String searchCustomerKeywordForCaptureTitle(String groupName, String terminalType) throws Exception{
		CustomerKeywordManager manager = new CustomerKeywordManager();
		return manager.searchCustomerKeywordForCaptureTitle("keyword", terminalType, groupName);
	}
}
