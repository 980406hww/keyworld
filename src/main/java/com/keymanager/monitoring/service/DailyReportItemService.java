package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerKeywordCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.excel.operator.CustomerKeywordDailyReportExcelWriter;
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

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerKeywordService customerKeywordService;

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
		CustomerKeywordCriteria customerKeywordCriteria = new CustomerKeywordCriteria();
		customerKeywordCriteria.setTerminalType(terminalType);
		customerKeywordCriteria.setCustomerUuid(new Long(dailyReportItem.getCustomerUuid()));
		customerKeywordCriteria.setStatus("1");
		customerKeywordCriteria.setOrderingElement("fSequence");
		customerKeywordCriteria.setOrderingRule("ASC");
		List<CustomerKeyword> customerKeywords = customerKeywordService.searchCustomerKeywords(customerKeywordCriteria);

		if (!Utils.isEmpty(customerKeywords)) {
			Customer customer = customerService.getCustomer(dailyReportItem.getCustomerUuid());
			CustomerKeywordDailyReportExcelWriter excelWriter = new CustomerKeywordDailyReportExcelWriter(terminalType, dailyReportItem
					.getCustomerUuid() + "", dailyReportUuid);
			excelWriter.writeDataToExcel(customerKeywords, customer.getContactPerson());
		}
		dailyReportItem.setStatus(DailyReportStatusEnum.Completed.name());
		dailyReportItem.setUpdateTime(new Date());
		dailyReportItemDao.updateById(dailyReportItem);
	}

	public DailyReportItem findDailyReportItem(long dailyReportUuid, String status){
		return dailyReportItemDao.findDailyReportItem(dailyReportUuid, status);
	}
}
