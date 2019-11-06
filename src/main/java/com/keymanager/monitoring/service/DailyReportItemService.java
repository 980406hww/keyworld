package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerKeywordCriteria;
import com.keymanager.monitoring.dao.DailyReportItemDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.DailyReportItem;
import com.keymanager.monitoring.enums.DailyReportStatusEnum;
import com.keymanager.monitoring.enums.EntryTypeEnum;
import com.keymanager.monitoring.excel.operator.CustomerKeywordDailyReportExcelWriter;
import com.keymanager.monitoring.excel.operator.CustomerKeywordDailyReportSecondExcelWriter;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
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

	@Autowired
	private CustomerService customerService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private CustomerKeywordService customerKeywordService;


	@Autowired
	private PerformanceService performanceService;

	public void createDailyReportItem(long dailyReportUuid, String terminalType, int customerUuid){
		DailyReportItem dailyReportItem = new DailyReportItem();
		dailyReportItem.setStatus(DailyReportStatusEnum.New.name());
		dailyReportItem.setCustomerUuid(customerUuid);
		dailyReportItem.setDailyReportUuid(dailyReportUuid);
		dailyReportItem.setTerminalType(terminalType);
		dailyReportItem.setUpdateTime(new Date());
		dailyReportItem.setCreateTime(new Date());
		dailyReportItemDao.insert(dailyReportItem);
	}

	public void generateDailyReport(long dailyReportUuid, long dailyReportItemUuid) throws Exception {
		DailyReportItem dailyReportItem = dailyReportItemDao.selectById(dailyReportItemUuid);
		dailyReportItem.setStatus(DailyReportStatusEnum.Processing.name());
		dailyReportItem.setUpdateTime(new Date());
		dailyReportItemDao.updateById(dailyReportItem);
		CustomerKeywordCriteria customerKeywordCriteria = new CustomerKeywordCriteria();
		customerKeywordCriteria.setTerminalType(dailyReportItem.getTerminalType());
		customerKeywordCriteria.setCustomerUuid(new Long(dailyReportItem.getCustomerUuid()));

		long startMilleSeconds = System.currentTimeMillis();
		List<CustomerKeyword> customerKeywords = customerKeywordService.searchCustomerKeywordsForDailyReport(customerKeywordCriteria);
		performanceService.addPerformanceLog(dailyReportItem.getTerminalType() + ":searchCustomerKeywordsForDailyReport", System.currentTimeMillis() - startMilleSeconds, "customerUuid = " + dailyReportItem.getCustomerUuid());

		startMilleSeconds = System.currentTimeMillis();
		if (!Utils.isEmpty(customerKeywords)) {
			Customer customer = customerService.getCustomer(dailyReportItem.getCustomerUuid());
			Config dailyReportType = configService.getConfig(Constants.CONFIG_TYPE_DAILY_REPORT, Constants.CONFIG_TYPE_DAILY_REPORT_TYPE);
			if(dailyReportType != null) {
				double todayFee = 0;
				if (EntryTypeEnum.qt.name().equalsIgnoreCase(dailyReportType.getValue())) {
					CustomerKeywordDailyReportExcelWriter excelWriter = new CustomerKeywordDailyReportExcelWriter(dailyReportItem.getTerminalType(), dailyReportItem
							.getCustomerUuid() + "", dailyReportUuid);
					todayFee = excelWriter.writeDataToExcel(customerKeywords, customer.getExternalAccount(), customer.getContactPerson(), dailyReportItem.getTerminalType());
				} else {
					CustomerKeywordDailyReportSecondExcelWriter excelWriter = new CustomerKeywordDailyReportSecondExcelWriter(dailyReportItem.getTerminalType(), dailyReportItem
							.getCustomerUuid() + "", dailyReportUuid);
					todayFee = excelWriter.writeDataToExcel(customerKeywords, new String(customer.getContactPerson().getBytes(),"utf-8"), customer.getContactPerson(), dailyReportItem.getTerminalType());
				}
				dailyReportItem.setTodayFee(todayFee);
			}
		}
		dailyReportItem.setStatus(DailyReportStatusEnum.Completed.name());
		dailyReportItem.setUpdateTime(new Date());
		dailyReportItemDao.updateById(dailyReportItem);
		performanceService.addPerformanceLog(dailyReportItem.getTerminalType() + ":generate report", System.currentTimeMillis() - startMilleSeconds, "customerUuid = " + dailyReportItem.getCustomerUuid());
	}

	public DailyReportItem findDailyReportItem(long dailyReportUuid, String status){
		return dailyReportItemDao.findDailyReportItem(dailyReportUuid, status);
	}

	public void deleteDailyReportItemFromAWeekAgo() {
		dailyReportItemDao.deleteDailyReportItemFromAWeekAgo();
	}

	public List<DailyReportItem> searchDailyReportItems(long dailyReportUuid){
		return dailyReportItemDao.searchDailyReportItems(dailyReportUuid);
	}

	public void deleteDailyReportItems(long dailyReportUuid) {
		dailyReportItemDao.deleteDailyReportItems(dailyReportUuid);
	}
}
