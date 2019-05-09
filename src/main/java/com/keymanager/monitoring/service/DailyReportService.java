package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.monitoring.dao.DailyReportDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.DailyReport;
import com.keymanager.monitoring.entity.DailyReportItem;
import com.keymanager.monitoring.enums.DailyReportStatusEnum;
import com.keymanager.monitoring.enums.DailyReportTriggerModeEnum;
import com.keymanager.monitoring.enums.EntryTypeEnum;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.excel.operator.CustomerKeywordDailyReportSummaryExcelWriter;
import com.keymanager.monitoring.excel.operator.CustomerKeywordDailyReportTotalExcelWriter;
import com.keymanager.util.*;
import com.keymanager.util.common.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DailyReportService extends ServiceImpl<DailyReportDao, DailyReport> {
	private static Logger logger = LoggerFactory.getLogger(DailyReportService.class);

	@Autowired
	private DailyReportDao dailyReportDao;

	@Autowired
	private ConfigService configService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private CaptureRankJobService captureRankJobService;

	@Autowired
	private DailyReportItemService dailyReportItemService;

	@Autowired
	private KeywordInfoSynchronizeService keywordInfoSynchronizeService;

	public void autoTriggerDailyReport(){
		if(!captureRankJobService.hasCaptureRankJob() && dailyReportDao.fetchDailyReportTriggeredInToday(DailyReportTriggerModeEnum.Auto.name()) == null){
			long dailyReportUuid = createDailyReport(null, DailyReportTriggerModeEnum.Auto.name());
			List<Long> pcCustomerUuids = customerKeywordService.getCustomerUuids(EntryTypeEnum.bc.name(), TerminalTypeEnum.PC.name());
			for(Long customerUuid : pcCustomerUuids){
				dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.PC.name(), customerUuid.intValue());
			}

			List<Long> phoneCustomerUuids = customerKeywordService.getCustomerUuids(EntryTypeEnum.bc.name(), TerminalTypeEnum.Phone.name());
			for(Long customerUuid : phoneCustomerUuids){
				dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.Phone.name(), customerUuid.intValue());
			}
		}
	}

	public void triggerReportGeneration(String terminalType, String customerUuids){
		long dailyReportUuid = createDailyReport(terminalType, DailyReportTriggerModeEnum.Manual.name());
		if(StringUtils.isNotEmpty(customerUuids)){
			String [] customerUuidArray = customerUuids.trim().split(",");
			for(String customerUuid : customerUuidArray){
				dailyReportItemService.createDailyReportItem(dailyReportUuid, terminalType, Integer.parseInt(customerUuid));
			}
		}
	}

	private long createDailyReport(String terminalType, String triggerMode) {
		DailyReport dailyReport = new DailyReport();
		dailyReport.setTriggerTime(new Date());
		dailyReport.setCompleteTime(null);
		dailyReport.setTriggerMode(triggerMode);
		dailyReport.setTerminalType(terminalType);
		dailyReport.setStatus(DailyReportStatusEnum.New.name());
		dailyReport.setCreateTime(new Date());
		dailyReportDao.insert(dailyReport);
		return dailyReportDao.selectLastId();
	}

	public void generateReport() throws Exception {
		List<DailyReport> dailyReports = dailyReportDao.findByStatus(DailyReportStatusEnum.Processing.name());
		DailyReport dailyReport = null;
		if(CollectionUtils.isEmpty(dailyReports)){
			dailyReports = dailyReportDao.findByStatus(DailyReportStatusEnum.New.name());
			if(CollectionUtils.isNotEmpty(dailyReports)){
				dailyReport = dailyReports.get(0);
				dailyReport.setStatus(DailyReportStatusEnum.Processing.name());
				dailyReport.setUpdateTime(new Date());
				dailyReportDao.updateById(dailyReport);
			}
		}else{
			dailyReport = dailyReports.get(0);
		}
		if(dailyReport == null){
			return;
		}
		DailyReportItem dailyReportItem = dailyReportItemService.findDailyReportItem(dailyReport.getUuid(), DailyReportStatusEnum.New.name());
		if(dailyReportItem != null){
			dailyReportItemService.generateDailyReport(dailyReport.getUuid(), dailyReportItem.getUuid());
		}else{
			Config webPathConfig = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_WEBPATH);
			String webPath = webPathConfig.getValue();
			String username = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_USERNAME).getValue();
			String password = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_PASSWORD).getValue();
			Map map = new HashMap();
			map.put("username", username);
			map.put("password", password);

			dailyReport.setStatus(DailyReportStatusEnum.Completed.name());
			dailyReport.setCompleteTime(new Date());
			Map<String, Map<String, String>> externalAccountAndSummaryFeeMap = generateDailyReportSummaryData(dailyReport.getUuid());
			String path = Utils.getWebRootPath();
			CustomerKeywordDailyReportTotalExcelWriter totalExcelWriter = new CustomerKeywordDailyReportTotalExcelWriter(dailyReport.getUuid());
			for(String externalAccount : externalAccountAndSummaryFeeMap.keySet()){
				Config config = configService.getConfig(Constants.DAILY_REPORT_PERCENTAGE, externalAccount);
				CustomerKeywordDailyReportSummaryExcelWriter excelWriter = new CustomerKeywordDailyReportSummaryExcelWriter(dailyReport.getUuid(), externalAccount);
				excelWriter.writeDailySummaryRow(externalAccountAndSummaryFeeMap.get(externalAccount), config == null ? 1d : Double.parseDouble(config.getValue()));
				excelWriter.writeDataToExcel(externalAccount);
				String dailyReportFolder = String.format("%sdailyreport/%d/", path, dailyReport.getUuid());
				String loginUserReportFolder = dailyReportFolder + externalAccount + "/";
				map.put("userID", externalAccount);
				String reportPassword = keywordInfoSynchronizeService.getUserReportInfo(webPath, map);
				ZipCompressor.createEncryptionZip(loginUserReportFolder, dailyReportFolder + String.format("%s_%s.zip", externalAccount, Utils.formatDatetime(Utils.getCurrentTimestamp(),
						"yyyy.MM.dd")), StringUtils.isNotEmpty(reportPassword) ? AESUtils.decrypt(reportPassword) : externalAccount + Utils.getCurrentDate());
				FileUtil.delFolder(loginUserReportFolder);

				totalExcelWriter.initSheet(externalAccount);
				totalExcelWriter.writeDailyTotalTitle(0);
				totalExcelWriter.writeDailyTotalRow(externalAccountAndSummaryFeeMap.get(externalAccount), config == null ? 1d : Double.parseDouble(config.getValue()));
			}
			totalExcelWriter.writeDataToExcel();
			String zipFileName = String.format("/dailyreport/TotalReport_%s_%d.zip", Utils.formatDatetime(Utils.getCurrentTimestamp(),
					"yyyy.MM.dd"), dailyReport.getUuid());
			String reportFolder = String.format("%sdailyreport/%d/", path, dailyReport.getUuid());

			dailyReport.setReportPath(zipFileName);
			dailyReportDao.updateById(dailyReport);
			Config config = configService.getConfig(Constants.CONFIG_TYPE_ZIP_ENCRYPTION, Constants.CONFIG_KEY_PASSWORD);
			ZipCompressor.createEncryptionZip(reportFolder, path + zipFileName, config.getValue() + Utils.getCurrentDate());

			FileUtil.delFolder(reportFolder);
		}
	}

	public List<DailyReport> searchCurrentDateCompletedReports(String terminalType){
		return dailyReportDao.searchCurrentDateCompletedReports(terminalType);
	}

	public void resetDailyReportExcel(String terminalType, String customerUuids) {
		String srcFilePath = Utils.getWebRootPath() + "CustomerKeywordDailyReport.xls";
		if(StringUtils.isNotEmpty(customerUuids)){
			String [] customerUuidArray = customerUuids.trim().split(",");
			for(String customerUuid : customerUuidArray){
				String dailyReportFilePath = Utils.getWebRootPath() + "dailyreport/" + terminalType + "/" + customerUuid + ".xls";
				FileUtil.copyFile(srcFilePath, dailyReportFilePath, true);
			}
		}
	}

	private Map<String, Map<String, String>> generateDailyReportSummaryData(long dailyReportUuid){
		List<DailyReportItem> dailyReportItems = dailyReportItemService.searchDailyReportItems(dailyReportUuid);
		Map<String, Map<String, String>> externalAccountAndSummaryFeeMap = new HashMap<String, Map<String, String>>();
		for(DailyReportItem dailyReportItem : dailyReportItems){
			Customer customer = customerService.getCustomer(dailyReportItem.getCustomerUuid());
			if(customer != null && StringUtil.isNotNullNorEmpty(customer.getExternalAccount())) {
				Map<String, String> summaryFeeMap = externalAccountAndSummaryFeeMap.get(customer.getExternalAccount());
				if (summaryFeeMap == null) {
					summaryFeeMap = new HashMap<String, String>();
					externalAccountAndSummaryFeeMap.put(customer.getExternalAccount(), summaryFeeMap);
				}
				summaryFeeMap.put(customer.getSearchEngine() + "_" + dailyReportItem.getTerminalType(), dailyReportItem.getTodayFee() + "");
			}
		}
		return externalAccountAndSummaryFeeMap;
	}

	public void deleteDailyReportFromAWeekAgo() {
		dailyReportDao.deleteDailyReportFromAWeekAgo();
	}

	public void removeDailyReportInToday(){
		DailyReport dailyReport = dailyReportDao.fetchDailyReportTriggeredInToday(DailyReportTriggerModeEnum.Auto.name());
		if(dailyReport != null){
			dailyReportItemService.deleteDailyReportItems(dailyReport.getUuid());
			dailyReportDao.deleteById(dailyReport.getUuid());
		}
	}
}
