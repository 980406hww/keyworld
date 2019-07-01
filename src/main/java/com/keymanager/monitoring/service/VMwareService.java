package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.monitoring.dao.MachineInfoDao;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.MachineInfo;
import com.keymanager.util.Constants;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.mo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class VMwareService extends ServiceImpl<MachineInfoDao, MachineInfo> {
	private static Logger logger = LoggerFactory.getLogger(VMwareService.class);

	private static ServiceInstance si;
	private static Folder rootFolder;

	@Autowired
	private ConfigService configService;

	private void reConnect() {
		try {
			Config configForIP = configService.getConfig(Constants.CONFIG_KEY_VMWARE, "IP");
			Config configForUserName = configService.getConfig(Constants.CONFIG_KEY_VMWARE, "UserName");
			Config configForPassword = configService.getConfig(Constants.CONFIG_KEY_VMWARE, "Password");

			VMClientSesion session = new VMClientSesion(configForIP.getValue(), configForUserName.getValue(), configForPassword.getValue());
			URL url = new URL("https", session.getHost(), "/sdk");
			si = new ServiceInstance(url, session.getUsername(), session.getPassword(), true);
			rootFolder = si.getRootFolder();
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
	}

	public String restartVPS(String vmName){
		try {
			if(si == null){
				reConnect();
			}
			ManagedEntity mes = new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmName);
			if(mes == null){
				reConnect();
				mes = new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmName);
			}
			if (mes != null) {
				VirtualMachine virtualMachine = (VirtualMachine) mes;
				VirtualMachinePowerState state = virtualMachine.getRuntime().powerState;//通电状态
				if (state.equals(VirtualMachinePowerState.poweredOn)) {
					virtualMachine.resetVM_Task();
				}else{
					virtualMachine.powerOnVM_Task(null);
				}
			}
			return "Success";
		}catch (Exception e){
			logger.error(e.getMessage());
			return e.getMessage();
		}
	}

	public String getVPSStatus(String vmName){
		try {
			if(si == null){
				reConnect();
			}
			ManagedEntity mes = new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmName);
			if(mes == null){
				reConnect();
				mes = new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", vmName);
			}
			if (mes != null) {
				VMClientStatus clientStatus = new VMClientStatus();
				VirtualMachine virtualMachine = (VirtualMachine) mes;
				clientStatus.setPowerStatus(virtualMachine.getRuntime().powerState.name());
				clientStatus.setConnectionStatus(virtualMachine.getRuntime().connectionState.name());

				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(clientStatus);
			}
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		return null;
	}

	class VMClientStatus implements java.io.Serializable{
		String powerStatus;
		String connectionStatus;

		public String getPowerStatus() {
			return powerStatus;
		}

		public void setPowerStatus(String powerStatus) {
			this.powerStatus = powerStatus;
		}

		public String getConnectionStatus() {
			return connectionStatus;
		}

		public void setConnectionStatus(String connectionStatus) {
			this.connectionStatus = connectionStatus;
		}
	}

	static class VMClientSesion implements java.io.Serializable {
		private String host;//vcent url
		private String username;//vcent 用户名
		private String password;//vcent 密码
		// set 和  get
		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
		//默认构造函数
		public VMClientSesion(){
			super();
		}
		//构造函数
		public VMClientSesion(String host, String username, String password) {
			super();
			this.host = host;
			this.username = username;
			this.password = password;
		}
	}
}
