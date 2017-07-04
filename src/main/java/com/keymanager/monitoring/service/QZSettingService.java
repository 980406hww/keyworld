package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.db.DBUtil;
import com.keymanager.enums.CollectMethod;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.dao.QZSettingDao;
import com.keymanager.monitoring.entity.QZCaptureTitleLog;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.enums.QZCaptureTitleLogStatusEnum;
import com.keymanager.monitoring.enums.QZSettingOperationTypeEnum;
import com.keymanager.monitoring.enums.QZSettingStatusEnum;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QZSettingService extends ServiceImpl<QZSettingDao, QZSetting> {
	private static Logger logger = LoggerFactory.getLogger(QZSettingService.class);

	@Autowired
	private QZSettingDao qzSettingDao;

	@Autowired
	private QZCaptureTitleLogService qzCaptureTitleLogService;

	public QZSetting getAvailableQZSetting(){
		List<QZSetting> qzSettings = this.getQZSettings();
		QZSetting qzSetting = null;
		if(CollectionUtils.isNotEmpty(qzSettings)){
			qzSetting = qzSettings.get(0);
			startQZSetting(qzSetting.getUuid());
		}
		return qzSetting;
	}

	public List<QZSetting> getQZSettings(){
		return qzSettingDao.getAvailableQZSettings();
	}

	public void startQZSetting(Long uuid){
		QZSetting qzSetting = qzSettingDao.selectById(uuid);
		if(qzSetting != null){
			qzSetting.setUpdateStatus(QZSettingStatusEnum.Processing.getValue());
			qzSetting.setUpdateStartTime(new Date());
			qzSettingDao.updateById(qzSetting);
		}
	}

	public void completeQZSetting(Long uuid, boolean downloadTimesUsed){
		QZSetting qzSetting = qzSettingDao.selectById(uuid);
		if(qzSetting != null){
			if(downloadTimesUsed){
				qzSetting.setUpdateStatus(QZSettingStatusEnum.DownloadTimesUsed.getValue());
			}else {
				qzSetting.setUpdateStatus(QZSettingStatusEnum.Completed.getValue());
			}
			qzSetting.setUpdateEndTime(new Date());
			qzSettingDao.updateById(qzSetting);
		}
	}

	public void saveQZSetting(QZSetting qzSetting){
		if(qzSetting.getUuid() != null){
			QZSetting existingQZSetting = qzSettingDao.selectById(qzSetting.getUuid());
			existingQZSetting.setDomain(qzSetting.getDomain());
			existingQZSetting.setUpdateStatus(qzSetting.getUpdateStatus());
			existingQZSetting.setCustomerUuid(qzSetting.getCustomerUuid());
			existingQZSetting.setPcGroup(qzSetting.getPcGroup());
			existingQZSetting.setPhoneGroup(qzSetting.getPhoneGroup());
			existingQZSetting.setType(qzSetting.getType());
			existingQZSetting.setOperationType(qzSetting.getOperationType());
			existingQZSetting.setIgnoreNoIndex(qzSetting.isIgnoreNoIndex());
			existingQZSetting.setIgnoreNoOrder(qzSetting.isIgnoreNoOrder());
			existingQZSetting.setUpdateInterval(qzSetting.getUpdateInterval());
			existingQZSetting.setUpdateTime(new Date());
			qzSettingDao.updateById(existingQZSetting);
		}else{
			qzSetting.setUpdateTime(new Date());
			qzSettingDao.insert(qzSetting);
		}
	}

	public List<QZSetting> searchQZSettingsByUuids(String uuids){
		return qzSettingDao.searchQZSettingsByUuids(uuids);
	}

	public List<QZSetting> searchQZSettings(Long uuid, Long customerUuid, String domain, String group, String updateStatus){
		return qzSettingDao.searchQZSettings(uuid, customerUuid, domain, group, updateStatus);
	}

	public void updateImmediately(String uuids){
		if(StringUtils.isNotEmpty(uuids)){
			String [] uuidArray = uuids.split(",");
			for(String uuid : uuidArray){
				QZSetting qzSetting = this.qzSettingDao.selectById(uuid);
				qzSetting.setUpdateStatus(null);
				qzSetting.setUpdateTime(new Date());
				qzSettingDao.updateById(qzSetting);
			}
		}
	}

	public void updateResult(QZSettingCriteria qzSettingCriteria, String terminalType) {
		if (!qzSettingCriteria.isDownloadTimesUsed()){
			if (CollectionUtils.isNotEmpty(qzSettingCriteria.getCustomerKeywordVOs())) {
				CustomerKeywordManager manager = new CustomerKeywordManager();
				Connection conn = null;
				try {
					conn = DBUtil.getConnection("keyword");
					List<CustomerKeywordVO> insertingCustomerKeywordVOs = new ArrayList<CustomerKeywordVO>();
					for (CustomerKeywordVO customerKeywordVO : qzSettingCriteria.getCustomerKeywordVOs()) {
						customerKeywordVO.setCustomerUuid(qzSettingCriteria.getQzSetting().getCustomerUuid());
						customerKeywordVO.setOptimizePlanCount(customerKeywordVO.getCurrentIndexCount() + 8);
//						customerKeywordVO.setOptimizeGroupName(qzSettingCriteria.getQzSetting().getGroup());
						customerKeywordVO.setServiceProvider("baidutop123");
						customerKeywordVO.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
						customerKeywordVO.setCollectMethod(CollectMethod.PerMonth.name());
						customerKeywordVO.setType(qzSettingCriteria.getQzSetting().getType());
						customerKeywordVO.setStartOptimizedTime(Utils.getCurrentTimestamp());
						customerKeywordVO.setStatus(1);
						customerKeywordVO.setCreateTime(Utils.getCurrentTimestamp());
						customerKeywordVO.setUpdateTime(Utils.getCurrentTimestamp());
						customerKeywordVO.setInitialIndexCount(customerKeywordVO.getCurrentIndexCount());
						customerKeywordVO.setTerminalType(qzSettingCriteria.getQzSetting().getOperationType());
						customerKeywordVO.setOptimizeGroupName(QZSettingOperationTypeEnum.PC.getValue().equals(qzSettingCriteria.getQzSetting
								().getOperationType()) ? qzSettingCriteria.getQzSetting().getPcGroup() : qzSettingCriteria.getQzSetting()
								.getPhoneGroup());

						insertingCustomerKeywordVOs.add(customerKeywordVO);
					}
					if (CollectionUtils.isNotEmpty(insertingCustomerKeywordVOs)) {
						QZCaptureTitleLog qzCaptureTitleLog = new QZCaptureTitleLog();
						qzCaptureTitleLog.setQzSettingUuid(qzSettingCriteria.getQzSetting().getUuid());
						qzCaptureTitleLog.setStatus(QZCaptureTitleLogStatusEnum.New.getValue());
						if (QZSettingOperationTypeEnum.Both.getValue().equals(qzSettingCriteria.getQzSetting().getOperationType())) {
							qzCaptureTitleLog.setTerminalType(TerminalTypeEnum.PC.name());
							qzCaptureTitleLogService.addQZCaptureTitleLog(qzCaptureTitleLog);
							for (CustomerKeywordVO customerKeywordVO : insertingCustomerKeywordVOs) {
								customerKeywordVO.setTerminalType(TerminalTypeEnum.PC.name());
								customerKeywordVO.setOptimizeGroupName(qzSettingCriteria.getQzSetting().getPcGroup());
							}
							manager.addCustomerKeywords(conn, insertingCustomerKeywordVOs);

							qzCaptureTitleLog.setTerminalType(TerminalTypeEnum.Phone.name());
							qzCaptureTitleLogService.addQZCaptureTitleLog(qzCaptureTitleLog);
							for (CustomerKeywordVO customerKeywordVO : insertingCustomerKeywordVOs) {
								customerKeywordVO.setTerminalType(TerminalTypeEnum.Phone.name());
								customerKeywordVO.setOptimizeGroupName(qzSettingCriteria.getQzSetting().getPhoneGroup());
							}
							manager.addCustomerKeywords(conn, insertingCustomerKeywordVOs);
						} else {
							manager.addCustomerKeywords(conn, insertingCustomerKeywordVOs);
							qzCaptureTitleLog.setTerminalType(qzSettingCriteria.getQzSetting().getOperationType());
							qzCaptureTitleLogService.addQZCaptureTitleLog(qzCaptureTitleLog);
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					logger.error(ex.getLocalizedMessage());
				} finally {
					DBUtil.closeConnection(conn);
				}
			}
		}
		QZSetting qzSetting = qzSettingCriteria.getQzSetting();
		this.completeQZSetting(qzSetting.getUuid(), qzSettingCriteria.isDownloadTimesUsed());
	}
}
