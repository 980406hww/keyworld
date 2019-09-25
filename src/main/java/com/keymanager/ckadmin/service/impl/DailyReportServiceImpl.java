package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;

import com.keymanager.ckadmin.dao.DailyReportDao;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.DailyReport;
import com.keymanager.ckadmin.entity.DailyReportItem;
import com.keymanager.ckadmin.enums.DailyReportStatusEnum;
import com.keymanager.ckadmin.enums.DailyReportTriggerModeEnum;

import com.keymanager.ckadmin.enums.EntryTypeEnum;
import com.keymanager.ckadmin.enums.TerminalTypeEnum;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordDailyReportSecondSummaryExcelWriter;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordDailyReportSummaryExcelWriter;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordDailyReportTotalExcelWriter;
import com.keymanager.ckadmin.service.CaptureRankJobService;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.DailyReportItemService;
import com.keymanager.ckadmin.service.DailyReportService;
import com.keymanager.ckadmin.util.FileUtil;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.monitoring.service.KeywordInfoSynchronizeService;
import com.keymanager.util.AESUtils;
import com.keymanager.util.Constants;
import com.keymanager.util.ZipCompressor;
import com.keymanager.util.common.StringUtil;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("dailyReportService2")
public class DailyReportServiceImpl extends ServiceImpl<DailyReportDao, DailyReport> implements DailyReportService {
	private static Logger logger = LoggerFactory.getLogger(DailyReportServiceImpl.class);

	@Resource(name = "dailyReportDao2")
	private DailyReportDao dailyReportDao;

	@Resource(name = "configService2")
	private ConfigService configService;

	@Resource(name = "customerService2")
	private CustomerService customerService;

	@Resource(name = "customerKeywordService2")
	private CustomerKeywordService customerKeywordService;

	@Resource(name = "captureRankJobService2")
	private CaptureRankJobService captureRankJobService;

	@Resource(name = "dailyReportItemService2")
	private DailyReportItemService dailyReportItemService;

	@Resource(name = "keywordInfoSynchronizeService")
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

	@Override
	public void autoTriggerDailyReport(){
		Config dailyReportType = configService.getConfig(Constants.CONFIG_TYPE_DAILY_REPORT, Constants.CONFIG_TYPE_DAILY_REPORT_TYPE);
		if(dailyReportType != null) {
			if (EntryTypeEnum.bc.name().equalsIgnoreCase(dailyReportType.getValue())) {
				if(autoTriggerDailyReportCondition() && CollectionUtils.isEmpty(dailyReportDao.fetchDailyReportTriggeredInToday(null, DailyReportTriggerModeEnum.Auto.name()))) {
					long dailyReportUuid = createDailyReport(null, DailyReportTriggerModeEnum.Auto.name(), null);
					List<Long> pcCustomerUuids = customerKeywordService.getCustomerUuids(EntryTypeEnum.bc.name(), TerminalTypeEnum.PC.name());
					for (Long customerUuid : pcCustomerUuids) {
						dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.PC.name(), customerUuid.intValue());
					}

					List<Long> phoneCustomerUuids = customerKeywordService.getCustomerUuids(EntryTypeEnum.bc.name(), TerminalTypeEnum.Phone.name());
					for (Long customerUuid : phoneCustomerUuids) {
						dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.Phone.name(), customerUuid.intValue());
					}
				}
			}else{
				List<String> userIDs = customerService.getActiveDailyReportIdentifyUserIDs();
				if (CollectionUtils.isNotEmpty(userIDs)) {
					for (String userID : userIDs) {
						if (autoTriggerDailyReportCondition() && CollectionUtils.isEmpty(dailyReportDao.fetchDailyReportTriggeredInToday(userID, DailyReportTriggerModeEnum.Auto.name()))) {
							long dailyReportUuid = createDailyReport(null, DailyReportTriggerModeEnum.Auto.name(), userID);
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

	@Override
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


	@Override
	public List<DailyReport> searchCurrentDateCompletedReports(String userName){
		return dailyReportDao.searchCurrentDateCompletedReports(userName);
	}

	@Override
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
			Customer customer = customerService.getCustomer((long)dailyReportItem.getCustomerUuid());
			if(customer != null) {
				String externalAccount = StringUtil.isNotNullNorEmpty(customer.getExternalAccount()) ? customer.getExternalAccount() : customer.getContactPerson();
				externalAccount = new String(externalAccount.getBytes(), StandardCharsets.UTF_8);
				Map<String, String> summaryFeeMap = externalAccountAndSummaryFeeMap.get(externalAccount);
				if (summaryFeeMap == null) {
					summaryFeeMap = new HashMap<>();
					externalAccountAndSummaryFeeMap.put(externalAccount, summaryFeeMap);
				}
//				summaryFeeMap.put(customer.getSearchEngine() + "_" + dailyReportItem.getTerminalType(), dailyReportItem.getTodayFee() + "");
			}
		}
		return externalAccountAndSummaryFeeMap;
	}

	@Override
	public void deleteDailyReportFromAWeekAgo() {
		dailyReportDao.deleteDailyReportFromAWeekAgo();
	}

	@Override
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
