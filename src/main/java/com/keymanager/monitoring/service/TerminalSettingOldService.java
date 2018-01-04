package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.TerminalSettingOldDao;
import com.keymanager.monitoring.entity.TerminalSettingOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalSettingOldService extends ServiceImpl<TerminalSettingOldDao,TerminalSettingOld> {
	
	@Autowired
	private TerminalSettingOldDao terminalSettingOldDao;


}
