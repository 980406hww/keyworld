package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.db.DBUtil;
import com.keymanager.enums.CollectMethod;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.dao.QZSettingDao;
import com.keymanager.monitoring.entity.QZCaptureTitleLog;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.enums.QZCaptureTitleLogStatusEnum;
import com.keymanager.monitoring.enums.QZSettingStatusEnum;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordVO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
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

	@Autowired
	private QZOperationTypeService  qzOperationTypeService;

	@Autowired
	private QZChargeRuleService qzChargeRuleService;


	public QZSetting getAvailableQZSetting(){
		List<QZSetting> qzSettings = this.getQZSettings();
		QZSetting qzSetting = null;
		if(CollectionUtils.isNotEmpty(qzSettings)){
			qzSetting = qzSettings.get(0);
			startQZSetting(qzSetting.getUuid());
		}
		return qzSetting;
	}

	//获取当前词量
	public QZSetting captureCurrentKeywordCount(){
		List<QZSetting> qzSettings = this.getQZSettings("CurrentKeyword");
		QZSetting qzSetting = null;
		if(CollectionUtils.isNotEmpty(qzSettings)){
			qzSetting = qzSettings.get(0);
			startQZSetting(qzSetting.getUuid(),"CurrentKeyword");
		}
		return qzSetting;
	}

	public List<QZSetting> getQZSettings(){
		return qzSettingDao.getAvailableQZSettings();
	}

	///根据捕捉时间排序ublic
	List<QZSetting> getQZSettings(String CurrentKeyword){  return qzSettingDao.captureCurrentKeyword(); }

	public void startQZSetting(Long uuid){
		QZSetting qzSetting = qzSettingDao.selectById(uuid);
		if(qzSetting != null){
			qzSetting.setUpdateStatus(QZSettingStatusEnum.Processing.getValue());
			qzSetting.setUpdateStartTime(new Date());
			qzSettingDao.updateById(qzSetting);
		}
	}
	//更新时间
	public void startQZSetting(Long uuid,String CurrentKeyword){
		QZSetting qzSetting = qzSettingDao.selectById(uuid);
		if(qzSetting != null){
			qzSetting.setCaptureCurrentKeywordStatus(QZSettingStatusEnum.Processing.getValue());
			qzSetting.setCaptureCurrentKeywordCountTime(new Date());
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
			//修改qzSetting表
			QZSetting existingQZSetting = qzSettingDao.selectById(qzSetting.getUuid());
			existingQZSetting.setDomain(qzSetting.getDomain());
			existingQZSetting.setUpdateStatus(qzSetting.getUpdateStatus());
			existingQZSetting.setCustomerUuid(qzSetting.getCustomerUuid());
			existingQZSetting.setPcGroup(qzSetting.getPcGroup());
			existingQZSetting.setPhoneGroup(qzSetting.getPhoneGroup());
			existingQZSetting.setType(qzSetting.getType());
			existingQZSetting.setIgnoreNoIndex(qzSetting.isIgnoreNoIndex());
			existingQZSetting.setIgnoreNoOrder(qzSetting.isIgnoreNoOrder());
			existingQZSetting.setUpdateInterval(qzSetting.getUpdateInterval());
			existingQZSetting.setUpdateTime(new Date());
			existingQZSetting.setCaptureCurrentKeywordCountTime(qzSetting.getCaptureCurrentKeywordCountTime());
			existingQZSetting.setCaptureCurrentKeywordStatus(qzSetting.getCaptureCurrentKeywordStatus());
			qzSettingDao.updateById(existingQZSetting);
			//修改部分
			List<QZOperationType> OldOperationTypes = qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(qzSetting.getUuid());
			List<QZOperationType> updOperationTypes = qzSetting.getQzOperationTypes();
			updateOpretionTypeAndChargeRule(OldOperationTypes,updOperationTypes,qzSetting.getUuid());
		}else{
			qzSetting.setUpdateTime(new Date());
			qzSettingDao.insert(qzSetting);
			Long qzSettingUuid  = new Long(qzSettingDao.selectLastId());//插入qzSetting是的uuid
			for (QZOperationType qzOperationType : qzSetting.getQzOperationTypes()){
				qzOperationType.setQzSettingUuid(qzSettingUuid);
				qzOperationTypeService.insert(qzOperationType);
				Long qzOperationTypeUuid  = new Long(qzOperationTypeService.selectLastId());//插入qzOperationType时的uuid
				for(QZChargeRule qzChargeRule : qzOperationType.getQzChargeRules() ){
					qzChargeRule.setQzOperationTypeUuid(qzOperationTypeUuid);
					qzChargeRuleService.insert(qzChargeRule);
				}
			}
		}
	}

	public void updateOpretionTypeAndChargeRule(List<QZOperationType> oldOperationTypes, List<QZOperationType> newOperationTypes, Long qzSettingUuid ){
		if(oldOperationTypes.size()==newOperationTypes.size()){
			updateOpretionTypeAndChargeRuleEqual(oldOperationTypes,newOperationTypes);
		}else if(oldOperationTypes.size()>newOperationTypes.size()){//2>1
			//判断是否为相同类型
			QZOperationType newOperationTypePC = newOperationTypes.get(0);
			if(newOperationTypePC.getOperationtype().equals("PC")){
				updateOpretionTypeAndChargeRule(oldOperationTypes,newOperationTypes);
				qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationTypes.get(0).getUuid());
				qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationTypes.get(1).getUuid());
				//逻辑删除
				oldOperationTypes.get(1).setUpdateTime(new Date());
				oldOperationTypes.get(1).setIsDeleted(0);
				qzOperationTypeService.updateById(oldOperationTypes.get(1));
				//添加规则
				for(QZChargeRule qzChargeRule : newOperationTypePC.getQzChargeRules()){
					qzChargeRule.setQzOperationTypeUuid(oldOperationTypes.get(0).getUuid());
					qzChargeRuleService.insert(qzChargeRule);
				}
			}
			if(newOperationTypePC.getOperationtype().equals("Phone")){
				oldOperationTypes.get(1).setUpdateTime(new Date());
				oldOperationTypes.get(1).setOperationtype(newOperationTypePC.getOperationtype());
				oldOperationTypes.get(1).setInitialKeywordCount(newOperationTypePC.getInitialKeywordCount());
				oldOperationTypes.get(1).setCurrentKeywordCount(newOperationTypePC.getCurrentKeywordCount());
				oldOperationTypes.get(1).setGroup(newOperationTypePC.getGroup());
				qzOperationTypeService.updateById(oldOperationTypes.get(1));
				qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationTypes.get(0).getUuid());
				qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationTypes.get(1).getUuid());
				//逻辑删除
				oldOperationTypes.get(0).setUpdateTime(new Date());
				oldOperationTypes.get(0).setIsDeleted(0);
				qzOperationTypeService.updateById(oldOperationTypes.get(0));
				//添加规则
				for(QZChargeRule qzChargeRule : newOperationTypePC.getQzChargeRules()){
					qzChargeRule.setQzOperationTypeUuid(oldOperationTypes.get(1).getUuid());
					qzChargeRuleService.insert(qzChargeRule);
				}
			}
		}else{//1<2
			//如果数据中真的只有一条数据那么就执行添加操作,如果有2条那么就将原有的状态为0的取出进行修改操作
			List<QZOperationType> qzOperationTypeIsDeletes = qzOperationTypeService.searchQZOperationTypesIsDelete(qzSettingUuid);
			if(qzOperationTypeIsDeletes.size()==2){//
				updateOpretionTypeAndChargeRuleEqual(qzOperationTypeIsDeletes,newOperationTypes);
			}else{
				updateOpretionTypeAndChargeRule(oldOperationTypes,newOperationTypes);
				//删除规则
				qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationTypes.get(0).getUuid());
				//添加一条
				newOperationTypes.get(1).setQzSettingUuid(qzSettingUuid);
				qzOperationTypeService.insert(newOperationTypes.get(1));
				Long uuid = Long.valueOf(qzOperationTypeService.selectLastId());
				for (QZChargeRule qzChargeRule : newOperationTypes.get(0).getQzChargeRules()){
					qzChargeRule.setQzOperationTypeUuid(oldOperationTypes.get(0).getUuid());
					qzChargeRuleService.insert(qzChargeRule);
				}
				for(QZChargeRule qzChargeRule : newOperationTypes.get(1).getQzChargeRules()){
					qzChargeRule.setQzOperationTypeUuid(uuid);
					qzChargeRuleService.insert(qzChargeRule);
				}
			}
		}
	}
	public void updateOpretionTypeAndChargeRule(List<QZOperationType> oldOperationTypes, List<QZOperationType> newOperationTypes){
		oldOperationTypes.get(0).setUpdateTime(new Date());
		oldOperationTypes.get(0).setOperationtype(newOperationTypes.get(0).getOperationtype());
		oldOperationTypes.get(0).setInitialKeywordCount(newOperationTypes.get(0).getInitialKeywordCount());
		oldOperationTypes.get(0).setCurrentKeywordCount(newOperationTypes.get(0).getCurrentKeywordCount());
		oldOperationTypes.get(0).setGroup(newOperationTypes.get(0).getGroup());
		qzOperationTypeService.updateById(oldOperationTypes.get(0));
	}
	public void updateOpretionTypeAndChargeRuleEqual(List<QZOperationType> oldOperationTypes, List<QZOperationType> newOperationTypes){
		for(int i =0 ;i<oldOperationTypes.size();i++ ){
			oldOperationTypes.get(i).setUpdateTime(new Date());
			oldOperationTypes.get(i).setOperationtype(newOperationTypes.get(i).getOperationtype());
			oldOperationTypes.get(i).setInitialKeywordCount(newOperationTypes.get(i).getInitialKeywordCount());
			oldOperationTypes.get(i).setCurrentKeywordCount(newOperationTypes.get(i).getCurrentKeywordCount());
			oldOperationTypes.get(i).setGroup(newOperationTypes.get(i).getGroup());
			oldOperationTypes.get(i).setIsDeleted(0);//只要是发生改变那么就让它的状态为1
			qzOperationTypeService.updateById(oldOperationTypes.get(i));
			//删除规则
			qzChargeRuleService.deleteByQZOperationTypeUuid(oldOperationTypes.get(i).getUuid());
			for(QZChargeRule qzChargeRule : newOperationTypes.get(i).getQzChargeRules()){
				qzChargeRule.setQzOperationTypeUuid(oldOperationTypes.get(i).getUuid());
				qzChargeRuleService.insert(qzChargeRule);
			}
		}
	}

	public List<QZSetting> searchQZSettingsByUuids(String uuids){
		return qzSettingDao.searchQZSettingsByUuids(uuids);
	}

	//查询QZSetting
	public List<QZSetting> searchQZSettings(Long uuid, Long customerUuid, String domain, String group, String updateStatus){
		//在封装成一个	QZSetting对象
		List<QZSetting> qzSettings  = qzSettingDao.searchQZSettings(uuid, customerUuid, domain, group, updateStatus);
		if(uuid!=null){
			for(QZSetting qzSetting : qzSettings){
				//通过uuid找到对应得操作类型表（多条数据）  ---->operationTypeUuid
				List<QZOperationType> qzOperationTypes  =  qzOperationTypeService.searchQZOperationTypesIsDelete(qzSetting.getUuid());
				//通过operationTypeUuid主键去查询规则表（多条数据）
				for(QZOperationType qzOperationType : qzOperationTypes){
					List<QZChargeRule> qzChargeRules = qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(qzOperationType.getUuid());
					qzOperationType.setQzChargeRules(qzChargeRules);
				}
				qzSetting.setQzOperationTypes(qzOperationTypes);
			}
		}
		return qzSettings;
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
					List<QZOperationType> qzOperationTypes = qzOperationTypeService.searchQZOperationTypesIsDelete(qzSettingCriteria.getQzSetting().getUuid());

					if(CollectionUtils.isNotEmpty(qzOperationTypes)) {
						conn = DBUtil.getConnection("keyword");
						List<CustomerKeywordVO> insertingCustomerKeywordVOs = new ArrayList<CustomerKeywordVO>();
						for (CustomerKeywordVO customerKeywordVO : qzSettingCriteria.getCustomerKeywordVOs()) {
							customerKeywordVO.setCustomerUuid(qzSettingCriteria.getQzSetting().getCustomerUuid());
							customerKeywordVO.setOptimizePlanCount(customerKeywordVO.getCurrentIndexCount() + 8);
							customerKeywordVO.setServiceProvider("baidutop123");
							customerKeywordVO.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
							customerKeywordVO.setCollectMethod(CollectMethod.PerMonth.name());
							customerKeywordVO.setType(qzSettingCriteria.getQzSetting().getType());
							customerKeywordVO.setStartOptimizedTime(Utils.getCurrentTimestamp());
							customerKeywordVO.setStatus(1);
							customerKeywordVO.setCreateTime(Utils.getCurrentTimestamp());
							customerKeywordVO.setUpdateTime(Utils.getCurrentTimestamp());
							customerKeywordVO.setInitialIndexCount(customerKeywordVO.getCurrentIndexCount());
							insertingCustomerKeywordVOs.add(customerKeywordVO);
						}
						if (CollectionUtils.isNotEmpty(insertingCustomerKeywordVOs)) {
							QZCaptureTitleLog qzCaptureTitleLog = new QZCaptureTitleLog();
							qzCaptureTitleLog.setStatus(QZCaptureTitleLogStatusEnum.New.getValue());
							for(QZOperationType qzOperationType : qzOperationTypes){
								qzCaptureTitleLog.setTerminalType(qzOperationType.getOperationtype());
								qzCaptureTitleLog.setQzOperationTypeUuid(qzOperationType.getUuid());
								qzCaptureTitleLogService.addQZCaptureTitleLog(qzCaptureTitleLog);
								for (CustomerKeywordVO customerKeywordVO : insertingCustomerKeywordVOs) {
									customerKeywordVO.setTerminalType(qzOperationType.getOperationtype());
									customerKeywordVO.setOptimizeGroupName(qzOperationType.getGroup());
								}
								manager.addCustomerKeywords(conn, insertingCustomerKeywordVOs);
							}
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

	//更新当前词量
	public void updateCurrentKeywordCount(QZSettingCriteria qzSettingCriteria, String terminalType) {
		QZSetting updQZSetting = qzSettingCriteria.getQzSetting();
		QZSetting oleQZSetting = qzSettingDao.selectById(updQZSetting.getUuid());
		List<QZOperationType> oldOperationTypes = qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(oleQZSetting.getUuid());
		List<QZOperationType> updOperationTypes = updQZSetting.getQzOperationTypes();
		if(updQZSetting != null){
			if(qzSettingCriteria.isDownloadTimesUsed()){
				oleQZSetting.setCaptureCurrentKeywordStatus(QZSettingStatusEnum.DownloadTimesUsed.getValue());
			}else {
				oleQZSetting.setCaptureCurrentKeywordStatus(QZSettingStatusEnum.Completed.getValue());
			}
		}
		//如果数据库只有一条
		if(oldOperationTypes.size()==1){
			if(oldOperationTypes.get(0).getOperationtype().equals("PC")){
				this.updateQZOperationType(oldOperationTypes.get(0),updOperationTypes.get(0));
			}else{
				this.updateQZOperationType(oldOperationTypes.get(0),updOperationTypes.get(1));
			}
		}else{
			for (int i = 0; i < updOperationTypes.size(); i++) {
				this.updateQZOperationType(oldOperationTypes.get(i),updOperationTypes.get(i));
			}
		}
		qzSettingDao.updateById(oleQZSetting);
	}

	//用于更新当前词量以及达标日期
	public void updateQZOperationType(QZOperationType oldOperationType, QZOperationType updOperationType){
		if(oldOperationType.getOperationtype().equals("PC")){
			oldOperationType.setCurrentKeywordCount(updOperationType.getCurrentKeywordCount());
			if(null==oldOperationType.getReachTargetDate()
					&&null!=qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(oldOperationType.getUuid())){
				QZChargeRule qzChargeRulePC = qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(oldOperationType.getUuid()).get(0);
				//如果比规则表中的初始词量大
				if(updOperationType.getCurrentKeywordCount()>=qzChargeRulePC.getStartKeywordCount()){
					oldOperationType.setReachTargetDate(new Date());//达标日期
					oldOperationType.setNextChargeDate(DateUtils.addMonths(new Date(),1));//下次收费日期:加一个月
				}
			}
			qzOperationTypeService.updateById(oldOperationType);
		}
		if(oldOperationType.getOperationtype().equals("Phone")){
			oldOperationType.setCurrentKeywordCount(updOperationType.getCurrentKeywordCount());
			if(null==oldOperationType.getReachTargetDate()
					&&null!=qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(oldOperationType.getUuid())){
				QZChargeRule qzChargeRulePC = qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(oldOperationType.getUuid()).get(0);
				//如果比规则表中的初始词量大
				if(updOperationType.getCurrentKeywordCount()>=qzChargeRulePC.getStartKeywordCount()){
					oldOperationType.setReachTargetDate(new Date());//达标日期
					oldOperationType.setNextChargeDate(DateUtils.addMonths(new Date(),1));//下次收费日期:加一个月
				}
			}
			qzOperationTypeService.updateById(oldOperationType);
		}
	}

	public boolean deleteOne(Long uuid){
		//根据数据库中的uuid去查询
		List<QZOperationType> qzOperationTypes =  qzOperationTypeService.searchQZOperationTypesByQZSettingUuid(uuid);
		if(qzOperationTypes.size()>0){
			for(QZOperationType qzOperationType : qzOperationTypes){
				qzChargeRuleService.deleteByQZOperationTypeUuid(qzOperationType.getUuid());
			}
			qzOperationTypeService.deleteByQZSettingUuid(uuid);
		}
		qzSettingDao.deleteById(uuid);
		return true;
	}

	public boolean deleteAll(List<String> uuids){
		for(String uuid : uuids){
			deleteOne(Long.valueOf(uuid));
		}
		return true;
	}
}
