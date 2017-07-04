package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.QZCaptureTitleLogDao;
import com.keymanager.monitoring.entity.QZCaptureTitleLog;
import com.keymanager.monitoring.enums.QZCaptureTitleLogStatusEnum;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class QZCaptureTitleLogService extends ServiceImpl<QZCaptureTitleLogDao, QZCaptureTitleLog> {
	
	@Autowired
	private QZCaptureTitleLogDao qzCaptureTitleLogDao;

	public QZCaptureTitleLog getAvailableQZSetting(String status, String terminalType){
		List<QZCaptureTitleLog> qzCaptureTitleLogs = this.getAvailableQZCaptureTitleLog(status, terminalType);
		QZCaptureTitleLog qzCaptureTitleLog = null;
		if(CollectionUtils.isNotEmpty(qzCaptureTitleLogs)){
			qzCaptureTitleLog = qzCaptureTitleLogs.get(0);
			startQZCaptureTitleLog(qzCaptureTitleLog.getUuid());
		}
		return qzCaptureTitleLog;
	}

	public List<QZCaptureTitleLog> getAvailableQZCaptureTitleLog(String status, String terminalType){
		return qzCaptureTitleLogDao.getAvailableQZCaptureTitleLog(status, terminalType);
	}

	public void startQZCaptureTitleLog(Long uuid){
		QZCaptureTitleLog qzCaptureTitleLog = qzCaptureTitleLogDao.selectById(uuid);
		if(qzCaptureTitleLog != null){
			qzCaptureTitleLog.setStatus(QZCaptureTitleLogStatusEnum.Processing.getValue());
			qzCaptureTitleLog.setStartTime(new Date());
			qzCaptureTitleLogDao.updateById(qzCaptureTitleLog);
		}
	}

	public void completeQZCaptureTitleLog(Long uuid){
		QZCaptureTitleLog qzCaptureTitleLog = qzCaptureTitleLogDao.selectById(uuid);
		if(qzCaptureTitleLog != null){
			qzCaptureTitleLog.setStatus(QZCaptureTitleLogStatusEnum.Completed.getValue());
			qzCaptureTitleLog.setEndTime(new Date());
			qzCaptureTitleLogDao.updateById(qzCaptureTitleLog);
		}
	}

	public void addQZCaptureTitleLog(QZCaptureTitleLog qzCaptureTitleLog){
		qzCaptureTitleLog.setUpdateTime(new Date());
		qzCaptureTitleLogDao.insert(qzCaptureTitleLog);
	}
}
