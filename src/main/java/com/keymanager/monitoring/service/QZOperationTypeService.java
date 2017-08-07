package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.QZOperationTypeDao;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.entity.QZSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QZOperationTypeService extends ServiceImpl<QZOperationTypeDao, QZOperationType> {
	
	@Autowired
	private QZOperationTypeDao qzOperationTypeDao;

	public List<QZOperationType> searchQZOperationTypesByQZSettingUuid(Long uuid){
		return  qzOperationTypeDao.searchQZOperationTypesByQZSettingUuid(uuid);
	}

	public List<QZOperationType> searchQZOperationTypesIsDelete(Long uuid){
		return  qzOperationTypeDao.searchQZOperationTypesIsDelete(uuid);
	}

	public int selectLastId(){
		return qzOperationTypeDao.selectLastId();
	}

	public  void deleteByQZSettingUuid(Long qzSettingUuid){
		qzOperationTypeDao.deleteByQZSettingUuid(qzSettingUuid);
	};

	//过期缴费
	public  List<QZSetting> expiredCharge(){
		return qzOperationTypeDao.expiredCharge();
	}
	//当天缴费
	public  List<QZSetting> nowCharge(){
		return qzOperationTypeDao.nowCharge();
	}
	//三天缴费
	public  List<QZSetting> threeCharge(){
		return qzOperationTypeDao.threeCharge();
	}
	//七天缴费
	public  List<QZSetting> sevenCharge(){
		return qzOperationTypeDao.sevenCharge();
	}
}
