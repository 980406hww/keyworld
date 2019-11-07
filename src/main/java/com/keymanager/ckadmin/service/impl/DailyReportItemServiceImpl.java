package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.dao.DailyReportItemDao;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.entity.DailyReportItem;
import com.keymanager.ckadmin.enums.DailyReportStatusEnum;
import com.keymanager.ckadmin.enums.EntryTypeEnum;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordDailyReportExcelWriter;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordDailyReportSecondExcelWriter;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.DailyReportItemService;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("dailyReportItemService2")
public class DailyReportItemServiceImpl extends ServiceImpl<DailyReportItemDao, DailyReportItem> implements DailyReportItemService {
	private static Logger logger = LoggerFactory.getLogger(DailyReportItemServiceImpl.class);

	@Resource(name = "dailyReportItemDao2")
	private DailyReportItemDao dailyReportItemDao;

	@Resource(name = "customerService2")
	private CustomerService customerService;

	@Resource(name = "configService2")
	private ConfigService configService;

	@Resource(name = "customerKeywordService2")
	private CustomerKeywordService customerKeywordService;

	@Resource(name = "performanceService2")
	private PerformanceService performanceService;

	@Override
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

	@Override
	public void generateDailyReport(long dailyReportUuid, long dailyReportItemUuid) throws Exception {
		DailyReportItem dailyReportItem = dailyReportItemDao.selectById(dailyReportItemUuid);
		dailyReportItem.setStatus(DailyReportStatusEnum.Processing.name());
		dailyReportItem.setUpdateTime(new Date());
		dailyReportItemDao.updateById(dailyReportItem);
		KeywordCriteria keywordCriteria = new KeywordCriteria();
		keywordCriteria.setTerminalType(dailyReportItem.getTerminalType());
		keywordCriteria.setCustomerUuid((long)dailyReportItem.getCustomerUuid());

		long startMilleSeconds = System.currentTimeMillis();
		List<CustomerKeyword> customerKeywords = customerKeywordService.searchCustomerKeywordsForDailyReport(keywordCriteria);
		performanceService.addPerformanceLog(dailyReportItem.getTerminalType() + ":searchCustomerKeywordsForDailyReport", System.currentTimeMillis() - startMilleSeconds, "customerUuid = " + dailyReportItem.getCustomerUuid());

		startMilleSeconds = System.currentTimeMillis();
		if (!Utils.isEmpty(customerKeywords)) {
			Customer customer = customerService.getCustomer((long)dailyReportItem.getCustomerUuid());
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

	@Override
	public DailyReportItem findDailyReportItem(long dailyReportUuid, String status){
		return dailyReportItemDao.findDailyReportItem(dailyReportUuid, status);
	}

	@Override
	public void deleteDailyReportItemFromAWeekAgo() {
		dailyReportItemDao.deleteDailyReportItemFromAWeekAgo();
	}

	@Override
	public List<DailyReportItem> searchDailyReportItems(long dailyReportUuid){
		return dailyReportItemDao.searchDailyReportItems(dailyReportUuid);
	}

	@Override
	public void deleteDailyReportItems(long dailyReportUuid) {
		dailyReportItemDao.deleteDailyReportItems(dailyReportUuid);
	}
}
