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
	}
}
