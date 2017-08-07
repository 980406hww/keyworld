package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.excel.operator.CustomerKeywordDailyReportExcelWriter;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.monitoring.dao.DailyReportItemDao;
import com.keymanager.monitoring.entity.DailyReportItem;
import com.keymanager.monitoring.enums.DailyReportStatusEnum;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DailyReportItemService extends ServiceImpl<DailyReportItemDao, DailyReportItem> {
	private static Logger logger = LoggerFactory.getLogger(DailyReportItemService.class);

	@Autowired
	private DailyReportItemDao dailyReportItemDao;

	public void createDailyReportItem(int dailyReportUuid, int customerUuid){
		DailyReportItem dailyReportItem = new DailyReportItem();
		dailyReportItem.setStatus(DailyReportStatusEnum.New.name());
		dailyReportItem.setCustomerUuid(customerUuid);
		dailyReportItem.setDailyReportUuid(dailyReportUuid);
		dailyReportItem.setUpdateTime(new Date());
		dailyReportItem.setCreateTime(new Date());
		dailyReportItemDao.insert(dailyReportItem);
	}

	public void generateDailyReport(String terminalType, long dailyReportUuid, long dailyReportItemUuid) throws Exception {
		DailyReportItem dailyReportItem = dailyReportItemDao.selectById(dailyReportItemUuid);
		CustomerKeywordManager customerKeywordManager = new CustomerKeywordManager();
		String condition = String.format(" and ck.fStatus = 1 and ck.fCustomerUuid = %d and ck.fTerminalType = '%s' ", dailyReportItem
				.getCustomerUuid(), terminalType);
		List<CustomerKeywordVO> customerKeywords = customerKeywordManager.searchCustomerKeywords("keyword", 10000, 1, condition,
				"order by ck.fSequence, ck.fKeyword ", 1);
		if (!Utils.isEmpty(customerKeywords)) {
			CustomerKeywordDailyReportExcelWriter excelWriter = new CustomerKeywordDailyReportExcelWriter(terminalType, dailyReportItem
					.getCustomerUuid() + "", dailyReportUuid);
			excelWriter.writeDataToExcel(customerKeywords);
		}
		dailyReportItem.setStatus(DailyReportStatusEnum.Completed.name());
		dailyReportItem.setUpdateTime(new Date());
		dailyReportItemDao.updateById(dailyReportItem);
	}

	public DailyReportItem findDailyReportItem(long dailyReportUuid, String status){
		return dailyReportItemDao.findDailyReportItem(dailyReportUuid, status);
	}
}
