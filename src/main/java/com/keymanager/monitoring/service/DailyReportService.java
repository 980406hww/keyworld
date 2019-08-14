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
import com.keymanager.monitoring.excel.operator.CustomerKeywordDailyReportSecondSummaryExcelWriter;
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

	private boolean autoTriggerDailyReportCondition(){
		Config dailyReportType = configService.getConfig(Constants.CONFIG_TYPE_DAILY_REPORT, Constants.CONFIG_TYPE_DAILY_REPORT_TYPE);
		if(dailyReportType != null){
			if(EntryTypeEnum.bc.name().equalsIgnoreCase(dailyReportType.getValue())){
				return !captureRankJobService.hasUncompletedCaptureRankJob(null, "China");
			}else{
				List<Long> customerUuids = customerService.getActiveDailyReportIdentifyCustomerUuids(null);
				if (CollectionUtils.isNotEmpty(customerUuids)) {
					List<String> groupNames = customerKeywordService.getGroups(customerUuids);
					return !captureRankJobService.hasUncompletedCaptureRankJob(groupNames, null);
				}
			}
		}
		return false;
	}

	public void autoTriggerDailyReport(){
		List<String> userIDs = customerService.getActiveDailyReportIdentifyUserIDs();
		if (CollectionUtils.isNotEmpty(userIDs)) {
			for (String userID : userIDs) {
				if(autoTriggerDailyReportCondition() && CollectionUtils.isEmpty(dailyReportDao.fetchDailyReportTriggeredInToday(userID, DailyReportTriggerModeEnum.Auto.name()))){
					long dailyReportUuid = createDailyReport(null, DailyReportTriggerModeEnum.Auto.name(), userID);

					Config dailyReportType = configService.getConfig(Constants.CONFIG_TYPE_DAILY_REPORT, Constants.CONFIG_TYPE_DAILY_REPORT_TYPE);
					if(dailyReportType != null) {
						if (EntryTypeEnum.bc.name().equalsIgnoreCase(dailyReportType.getValue())) {
							List<Long> pcCustomerUuids = customerKeywordService.getCustomerUuids(userID, EntryTypeEnum.bc.name(), TerminalTypeEnum.PC.name());
							for (Long customerUuid : pcCustomerUuids) {
								dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.PC.name(), customerUuid.intValue());
							}

							List<Long> phoneCustomerUuids = customerKeywordService.getCustomerUuids(userID, EntryTypeEnum.bc.name(), TerminalTypeEnum.Phone.name());
							for (Long customerUuid : phoneCustomerUuids) {
								dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.Phone.name(), customerUuid.intValue());
							}
						} else {
							List<Long> customerUuids = customerService.getActiveDailyReportIdentifyCustomerUuids(userID);
							if (CollectionUtils.isNotEmpty(customerUuids)) {
								for (Long customerUuid : customerUuids) {
									dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.PC.name(), customerUuid.intValue());
								}
								for (Long customerUuid : customerUuids) {
									dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.Phone.name(), customerUuid.intValue());
								}
							}
						}
					}
				}
			}
		}
	}

	private long createDailyReport(String terminalType, String triggerMode, String userID) {
		DailyReport dailyReport = new DailyReport();
		dailyReport.setUserID(userID);
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
		DailyReport dailyReport = dailyReportDao.findByStatus(DailyReportStatusEnum.Processing.name());
		if (null == dailyReport) {
			dailyReport = dailyReportDao.findByStatus(DailyReportStatusEnum.New.name());
			if (null != dailyReport) {
				dailyReport.setStatus(DailyReportStatusEnum.Processing.name());
				dailyReport.setUpdateTime(new Date());
				dailyReportDao.updateById(dailyReport);
			}
		}
		if(dailyReport == null){
			return;
		}
		DailyReportItem dailyReportItem = dailyReportItemService.findDailyReportItem(dailyReport.getUuid(), DailyReportStatusEnum.New.name());
		if(dailyReportItem != null){
			dailyReportItemService.generateDailyReport(dailyReport.getUuid(), dailyReportItem.getUuid());
		}else{
			Config dailyReportType = configService.getConfig(Constants.CONFIG_TYPE_DAILY_REPORT, Constants.CONFIG_TYPE_DAILY_REPORT_TYPE);
			if(EntryTypeEnum.bc.name().equalsIgnoreCase(dailyReportType.getValue())) {
				generateReportForBC(dailyReport);
			}else{
				generateReportForOther(dailyReport);
			}
		}
	}

	private void generateReportForBC(DailyReport dailyReport) throws Exception {
		Map map = new HashMap();
		Config webPathConfig = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_WEBPATH);
		String webPath = webPathConfig.getValue();
		String username = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_USERNAME).getValue();
		String password = configService.getConfig(Constants.CONFIG_TYPE_KEYWORD_INFO_SYNCHRONIZE, Constants.CONFIG_KEY_PASSWORD).getValue();
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
            String reportPassword = "";
			reportPassword = keywordInfoSynchronizeService.getUserReportInfo(webPath, map);
			reportPassword = (StringUtils.isNotEmpty(reportPassword) ? AESUtils.decrypt(reportPassword) : externalAccount) + Utils.getCurrentDate();
            ZipCompressor.createEncryptionZip(loginUserReportFolder, dailyReportFolder + String.format("%s_%s.zip", externalAccount, Utils.formatDatetime(Utils.getCurrentTimestamp(),
                    "yyyy.MM.dd")), reportPassword);
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

	private void generateReportForOther(DailyReport dailyReport) throws Exception {
		dailyReport.setStatus(DailyReportStatusEnum.Completed.name());
		dailyReport.setCompleteTime(new Date());
		Map<String, Map<String, String>> externalAccountAndSummaryFeeMap = generateDailyReportSummaryData(dailyReport.getUuid());
		String path = Utils.getWebRootPath();
		for(String externalAccount : externalAccountAndSummaryFeeMap.keySet()){
			Config config = configService.getConfig(Constants.DAILY_REPORT_PERCENTAGE, externalAccount);
			CustomerKeywordDailyReportSecondSummaryExcelWriter excelWriter = new CustomerKeywordDailyReportSecondSummaryExcelWriter(dailyReport.getUuid(), externalAccount);
			excelWriter.writeDailySummaryRow(externalAccountAndSummaryFeeMap.get(externalAccount), config == null ? 1d : Double.parseDouble(config.getValue()));
			excelWriter.writeDataToExcel(externalAccount);
			String dailyReportFolder = String.format("%sdailyreport/%d/", path, dailyReport.getUuid());
			String loginUserReportFolder = dailyReportFolder + externalAccount + "/";
			String reportPassword = "";
			ZipCompressor.createEncryptionZip(loginUserReportFolder, dailyReportFolder + String.format("%s_%s.zip", externalAccount, Utils.formatDatetime(Utils.getCurrentTimestamp(),
					"yyyy.MM.dd")), reportPassword);
			FileUtil.delFolder(loginUserReportFolder);

		}
		String zipFileName = String.format("/dailyreport/TotalReport_%s_%d.zip", Utils.formatDatetime(Utils.getCurrentTimestamp(),
				"yyyy.MM.dd"), dailyReport.getUuid());
		String reportFolder = String.format("%sdailyreport/%d/", path, dailyReport.getUuid());

		dailyReport.setReportPath(zipFileName);
		dailyReportDao.updateById(dailyReport);
		Config config = configService.getConfig(Constants.CONFIG_TYPE_ZIP_ENCRYPTION, Constants.CONFIG_KEY_PASSWORD);
		ZipCompressor.createEncryptionZip(reportFolder, path + zipFileName, "");

		FileUtil.delFolder(reportFolder);
	}


	public List<DailyReport> searchCurrentDateCompletedReports(String userName){
		return dailyReportDao.searchCurrentDateCompletedReports(userName);
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

	private Map<String, Map<String, String>> generateDailyReportSummaryData(long dailyReportUuid) throws Exception{
		List<DailyReportItem> dailyReportItems = dailyReportItemService.searchDailyReportItems(dailyReportUuid);
		Map<String, Map<String, String>> externalAccountAndSummaryFeeMap = new HashMap<String, Map<String, String>>();
		for(DailyReportItem dailyReportItem : dailyReportItems){
			Customer customer = customerService.getCustomer(dailyReportItem.getCustomerUuid());
			if(customer != null) {
				String externalAccount = StringUtil.isNotNullNorEmpty(customer.getExternalAccount()) ? customer.getExternalAccount() : customer.getContactPerson();
				externalAccount = new String(externalAccount.getBytes(),"utf-8");
				Map<String, String> summaryFeeMap = externalAccountAndSummaryFeeMap.get(externalAccount);
				if (summaryFeeMap == null) {
					summaryFeeMap = new HashMap<String, String>();
					externalAccountAndSummaryFeeMap.put(externalAccount, summaryFeeMap);
				}
				summaryFeeMap.put(customer.getSearchEngine() + "_" + dailyReportItem.getTerminalType(), dailyReportItem.getTodayFee() + "");
			}
		}
		return externalAccountAndSummaryFeeMap;
	}

	public void deleteDailyReportFromAWeekAgo() {
		dailyReportDao.deleteDailyReportFromAWeekAgo();
	}

	public void removeDailyReportInToday(String userID){
		List<DailyReport> dailyReports = dailyReportDao.fetchDailyReportTriggeredInToday(userID, DailyReportTriggerModeEnum.Auto.name());
		if (CollectionUtils.isNotEmpty(dailyReports)) {
			for (DailyReport dailyReport : dailyReports) {
				dailyReportItemService.deleteDailyReportItems(dailyReport.getUuid());
				dailyReportDao.deleteById(dailyReport.getUuid());
			}
		}
	}
}
