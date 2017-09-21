package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.db.DBUtil;
import com.keymanager.manager.VPSOpenInfoManager;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.value.VPSOpenInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;

@RestController
@RequestMapping(value = "/internal/vpsopeninfo")
public class VPSOpenInfoRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(VPSOpenInfoRestController.class);

	@RequestMapping(value = "/saveVPSOpenInfo", method = RequestMethod.POST)
	public ResponseEntity<?> saveVPSOpenInfo(@RequestBody VPSOpenInfoVO vpsOpenInfoVO){
		VPSOpenInfoManager manager = new VPSOpenInfoManager();
		Connection conn = null;
		try {
			conn = DBUtil.getConnection("keyword");
			manager.updateVPSConfigInfo(conn, vpsOpenInfoVO);
		}catch (Exception ex){
			logger.error("getVPSOpenInfo error" + ex.getMessage());
			return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}finally {
			DBUtil.closeConnection(conn);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value ="/delete/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> deleteQZSetting(@PathVariable("uuid") Long uuid){
		VPSOpenInfoManager manager = new VPSOpenInfoManager();
		try {
			manager.deleteVPSOpenInfoVO("keyword", uuid + "");
		}catch(Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/getVPSOpenInfo/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<?> getVPSOpenInfo(@PathVariable("uuid") int uuid){
		VPSOpenInfoManager manager = new VPSOpenInfoManager();
		VPSOpenInfoVO vpsOpenInfoVO = null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection("keyword");
			vpsOpenInfoVO = manager.getVPSOpenInfoVO(conn, uuid);
		}catch (Exception ex){
			logger.error("getVPSOpenInfo error" + ex.getMessage());
		}finally {
			DBUtil.closeConnection(conn);
		}
		return new ResponseEntity<Object>(vpsOpenInfoVO, HttpStatus.OK);
	}
}
