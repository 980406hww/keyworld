package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.ComplaintsReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComplaintsReportSchedule {
	
	private static Logger logger = LoggerFactory.getLogger(ComplaintsReportSchedule.class);

	@Autowired
	private ComplaintsReportService complaintsReportService;

	public void runTask(){
		logger.info("============= "+" ComplaintsReport Task "+"===================");
		try {
			complaintsReportService.sendComplaintsReports();
		} catch (Exception e) {
			logger.error("Send ComplaintsReport is error" + e.getMessage());
			e.printStackTrace();
		}
	}
}
