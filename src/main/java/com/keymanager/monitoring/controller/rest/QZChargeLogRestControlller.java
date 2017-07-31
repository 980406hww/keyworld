package com.keymanager.monitoring.controller.rest;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.QZChargeLog;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.service.QZChargeLogService;
import com.keymanager.monitoring.service.QZSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj01 on 2017/7/17.
 */
@RestController
@RequestMapping(value = "/spring/qzchargelog")
public class QZChargeLogRestControlller extends SpringMVCBaseController {
    @Autowired
    private QZChargeLogService qzChargeLogService;

    //点击收费按钮触发的方法
    @RequestMapping(value = "/getQZChargeLog/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getQZChargeLog(@PathVariable("uuid") Long uuid){
        return new ResponseEntity<Object>(qzChargeLogService.getQZChargeLog(uuid), HttpStatus.OK);
    }

    //插入一条收费流水表
    @RequestMapping(value = "/save" , method = RequestMethod.POST)
    public ResponseEntity<?> saveQZChargeLog(@RequestBody List<QZChargeLog> qzChargeLogs,HttpServletRequest request){
        qzChargeLogService.saveQZChargeLog(qzChargeLogs,request);
        return new ResponseEntity<Object>(qzChargeLogs, HttpStatus.OK);
    }
    //查看收费记录
    @RequestMapping(value = "/chargesList/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> chargesList(@PathVariable("uuid") Long uuid){
        return new ResponseEntity<Object>(qzChargeLogService.chargesList(uuid), HttpStatus.OK);
    }
}
