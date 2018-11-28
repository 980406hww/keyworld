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
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.excel.operator.CustomerKeywordDailyReportSummaryExcelWriter;
import com.keymanager.util.Constants;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.ZipCompressor;
import javafx.beans.binding.DoubleBinding;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.crypto.hash.Hash;
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
	private CaptureRankJobService captureRankJobService;

	@Autowired
	private DailyReportItemService dailyReportItemService;

	public void autoTriggerDailyReport(){
		if(!captureRankJobService.hasCaptureRankJob() && !dailyReportDao.hasDailyReportTriggeredInToday(DailyReportTriggerModeEnum.Auto.name())){
			long dailyReportUuid = createDailyReport(null, DailyReportTriggerModeEnum.Auto.name());
			Config pcDailyReportCustomerUuids = configService.getConfig(Constants.CONFIG_TYPE_DAILY_REPORT, TerminalTypeEnum.PC.name());
			if(StringUtils.isNotEmpty(pcDailyReportCustomerUuids.getValue())){
				String [] customerUuidArray = pcDailyReportCustomerUuids.getValue().trim().split(",");
				for(String customerUuid : customerUuidArray){
					dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.PC.name(), Integer.parseInt(customerUuid));
				}
			}

			Config phoneDailyReportCustomerUuids = configService.getConfig(Constants.CONFIG_TYPE_DAILY_REPORT, TerminalTypeEnum.Phone.name());
			if(StringUtils.isNotEmpty(phoneDailyReportCustomerUuids.getValue())){
				String [] customerUuidArray = phoneDailyReportCustomerUuids.getValue().trim().split(",");
				for(String customerUuid : customerUuidArray){
					dailyReportItemService.createDailyReportItem(dailyReportUuid, TerminalTypeEnum.Phone.name(), Integer.parseInt(customerUuid));
				}
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
			dailyReport.setStatus(DailyReportStatusEnum.Completed.name());
			dailyReport.setCompleteTime(new Date());
			Map<String, Map<String, String>> loginNameAndSummaryFeeMap = generateDailyReportSummaryData(dailyReport.getUuid());
			String path = Utils.getWebRootPath();
			for(String loginName : loginNameAndSummaryFeeMap.keySet()){
				Config config = configService.getConfig(Constants.DAILY_REPORT_PERCENTAGE, loginName);
				CustomerKeywordDailyReportSummaryExcelWriter excelWriter = new CustomerKeywordDailyReportSummaryExcelWriter(dailyReport.getUuid(), loginName);
				excelWriter.writeDailySummaryRow(loginNameAndSummaryFeeMap.get(loginName), config == null ? 1d : Double.parseDouble(config.getValue()));
				String dailyReportFolder = String.format("%sdailyreport/%d/", path, dailyReport.getUuid());
				String loginUserReportFolder = dailyReportFolder + loginName + "/";
				ZipCompressor.createEncryptionZip(loginUserReportFolder, dailyReportFolder + String.format("%s_%s.zip", loginName, Utils.formatDatetime(Utils.getCurrentTimestamp(),
						"yyyy.MM.dd")), loginName + Utils.getCurrentDate());
				FileUtil.delFolder(loginUserReportFolder);
			}
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
		Map<String, Map<String, String>> loginNameAndSummaryFeeMap = new HashMap<String, Map<String, String>>();
		for(DailyReportItem dailyReportItem : dailyReportItems){
			Customer customer = customerService.getCustomer(dailyReportItem.getCustomerUuid());
			Map<String, String> summaryFeeMap = loginNameAndSummaryFeeMap.get(customer.getLoginName());
			if(summaryFeeMap == null){
				summaryFeeMap = new HashMap<String, String>();
				loginNameAndSummaryFeeMap.put(customer.getLoginName(), summaryFeeMap);
			}
			summaryFeeMap.put(customer.getSearchEngine() + "_" + dailyReportItem.getTerminalType(), dailyReportItem.getTodayFee() + "");
		}
		return loginNameAndSummaryFeeMap;
	}

	public void deleteDailyReportFromAWeekAgo() {
		dailyReportDao.deleteDailyReportFromAWeekAgo();
	}
}
