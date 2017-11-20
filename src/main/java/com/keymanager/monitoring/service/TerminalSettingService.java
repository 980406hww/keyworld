package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.TerminalSettingDao;
import com.keymanager.monitoring.entity.TerminalSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalSettingService extends ServiceImpl<TerminalSettingDao,TerminalSetting> {
	
	@Autowired
	private TerminalSettingDao terminalSettingDao;


}
