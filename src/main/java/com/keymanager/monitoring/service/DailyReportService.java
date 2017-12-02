package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.monitoring.dao.DailyReportDao;
import com.keymanager.monitoring.entity.DailyReport;
import com.keymanager.monitoring.entity.DailyReportItem;
import com.keymanager.monitoring.enums.DailyReportStatusEnum;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.ZipCompressor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DailyReportService extends ServiceImpl<DailyReportDao, DailyReport> {
	private static Logger logger = LoggerFactory.getLogger(DailyReportService.class);

	@Autowired
	private DailyReportDao dailyReportDao;

	@Autowired
	private DailyReportItemService dailyReportItemService;

	public void triggerReportGeneration(String terminalType, String customerUuids){
		DailyReport dailyReport = new DailyReport();
		dailyReport.setTriggerTime(new Date());
		dailyReport.setCompleteTime(null);
		dailyReport.setTerminalType(terminalType);
		dailyReport.setStatus(DailyReportStatusEnum.New.name());
		dailyReport.setCreateTime(new Date());
		dailyReportDao.insert(dailyReport);
		if(StringUtils.isNotEmpty(customerUuids)){
			String [] customerUuidArray = customerUuids.trim().split(",");
			int dailyReportUuid = dailyReportDao.selectLastId();
			for(String customerUuid : customerUuidArray){
				dailyReportItemService.createDailyReportItem(dailyReportUuid, Integer.parseInt(customerUuid));
			}
		}
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
			dailyReportItemService.generateDailyReport(dailyReport.getTerminalType(), dailyReport.getUuid(), dailyReportItem.getUuid());
		}else{
			dailyReport.setStatus(DailyReportStatusEnum.Completed.name());
			dailyReport.setCompleteTime(new Date());

			String path = Utils.getWebRootPath();
			String zipFileName = String.format("/dailyreport/DailyReport_%s_%d.zip", Utils.formatDatetime(Utils.getCurrentTimestamp(),
					"yyyy.MM.dd"), dailyReport.getUuid());
			String reportFolder = String.format("%sdailyreport/%d/", path, dailyReport.getUuid());

			dailyReport.setReportPath(zipFileName);
			dailyReportDao.updateById(dailyReport);
			ZipCompressor.zipMultiFile(reportFolder, path + zipFileName);

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
}
