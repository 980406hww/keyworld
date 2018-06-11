package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.service.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(value = "/external/applicationMarket")
public class ExternalApplicationMarketRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalApplicationMarketRestController.class);

	@Autowired
	private ServerAddressService serverAddressService;

	@Autowired
	private VpnInfoService vpnInfoService;

	@Autowired
	private ApplicationMarketService applicationMarketService;

	@Autowired
	private ApplyKeywordService applyKeywordService;

	@Autowired
	private ApplyInfoService applyInfoService;

	//获取VPN信息
	@RequestMapping(value = "/getVpnInfo" , method = RequestMethod.POST)
	public ResponseEntity<?> getVpnInfo(HttpServletRequest request){
		String userName = (String) request.getParameter("userName");
		String password = (String) request.getParameter("password");
		String imeiStr = (String) request.getParameter("imeiStr");
		try {
			if(!StringUtils.isNotBlank(imeiStr)){
				return new ResponseEntity<Object>(false,HttpStatus.OK);
			}else {
				if(validUser(userName, password)){
					VpnInfo vpnInfo = new VpnInfo();
					List<VpnInfo> vpnInfos = vpnInfoService.selectVpnImei(imeiStr);
					if(vpnInfos.size() == 0){
						List<VpnInfo> vpnInfoList = vpnInfoService.selectVpnImei(null);
						if(vpnInfoList.size() > 0){
							int num = new Random().nextInt(vpnInfoList.size());
							vpnInfo = vpnInfoList.get(num);
							vpnInfo.setImei(imeiStr);
							vpnInfoService.updateById(vpnInfo);
						}
					}else {
						vpnInfo = vpnInfos.get(0);
					}
					//返回JSON串
					return new ResponseEntity<Object>((JSONObject.fromObject(vpnInfo)).toString(),HttpStatus.OK);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
	}

	//获取服务器地址
	@RequestMapping(value = "/getServerAddress" , method = RequestMethod.POST)
	public ResponseEntity<?> getServerAddress(HttpServletRequest request) {
		String userName = (String) request.getParameter("userName");
		String password = (String) request.getParameter("password");
		try {
			if(validUser(userName,password)){
				List<ServerAddress> serverAddressesList = serverAddressService.selectList(null);
				if(serverAddressesList.size()>0){
					int num = new Random().nextInt(serverAddressesList.size());
					String serverAddressStr = serverAddressesList.get(num).getServerAddress();
					return new ResponseEntity<Object>(serverAddressStr,HttpStatus.OK);
				}
            }
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
	}

	//获取应用市场信息
	@RequestMapping(value = "/getMarketInfo" , method = RequestMethod.POST)
	public ResponseEntity<?> getMarketInfo(HttpServletRequest request) {
		String userName = (String) request.getParameter("userName");
		String password = (String) request.getParameter("password");
		try {
			if(validUser(userName,password)){
				ApplicationMarket applicationMarket = new ApplicationMarket();
				List<ApplicationMarket> applicationMarketList = applicationMarketService.getmarketInfo();
				if(applicationMarketList.size() > 0){
					int num = new Random().nextInt(applicationMarketList.size());
					applicationMarket = applicationMarketList.get(num);
					String fileType = applicationMarket.getFileType();
					String [] fileTypes = fileType.split(",");
					applicationMarket.setfFileTypes(fileTypes);
					//返回JSON串
					return new ResponseEntity<Object>((JSONObject.fromObject(applicationMarket)).toString(),HttpStatus.OK);
				}
            }
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
	}

	//获取应用和关键词信息信息
	@RequestMapping(value = "/getApplyKeyword" , method = RequestMethod.POST)
	public ResponseEntity<?> getMasrketInfo(HttpServletRequest request) {
 		String userName = (String) request.getParameter("userName");
		String password = (String) request.getParameter("password");
		try {
			if(validUser(userName,password)){
				List<ApplyInfo> applyInfoList = applyInfoService.selectList(null);
				ApplyInfo applyInfo = new ApplyInfo();
				if(applyInfoList.size() > 0){
					int num = new Random().nextInt(applyInfoList.size());
					applyInfo = applyInfoList.get(num);
					String[] keywordList = applyKeywordService.getKeywordApplyUuid(applyInfo.getUuid());
					applyInfo.setCreateTime(null);
					applyInfo.setKeywords(keywordList);
					//返回JSON串
					return new ResponseEntity<Object>((JSONObject.fromObject(applyInfo)).toString(),HttpStatus.OK);
				}
            }
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(null,HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateApplyKeyword" , method = RequestMethod.POST)
	public ResponseEntity<?> updateApplyKeyword(HttpServletRequest request) {
		String userName = (String) request.getParameter("userName");
		String password = (String) request.getParameter("password");
		Long applyUuid = (Integer.valueOf((String) request.getParameter("applyUuid"))).longValue();
		Boolean isSuccess = Boolean.parseBoolean((String) request.getParameter("isSuccess"));
		String keyword = (String) request.getParameter("keyword");
		try {
			if(validUser(userName,password)){
				ApplyKeyword applyKeyword = applyKeywordService.selectApplyKeyword(applyUuid,keyword);
				if(!StringUtils.isEmpty(applyKeyword)){
					int brushNumber = applyKeyword.getBrushNumber();
					applyKeyword.setBrushNumber(++brushNumber);
					applyKeywordService.updateById(applyKeyword);
					return new ResponseEntity<Object>(true,HttpStatus.OK);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
