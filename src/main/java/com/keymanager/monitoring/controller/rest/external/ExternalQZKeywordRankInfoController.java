package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.QZKeywordRankInfo;
import com.keymanager.monitoring.service.QZKeywordRankInfoService;
import com.keymanager.monitoring.vo.ExternalQzKeywordRankInfoVO;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import com.keymanager.util.Constants;import org.aspectj.apache.bcel.classfile.Constant;
import org.aspectj.apache.bcel.classfile.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author zhoukai
 * @Date 2018/12/6 11:33
 **/

@RestController
@RequestMapping(value = "/external/qzsetting")
public class ExternalQZKeywordRankInfoController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalQZSettingRestController.class);
    @Autowired
    private QZKeywordRankInfoService qzKeywordRankInfoService;

    @RequestMapping(value = "/getQZSettingsTask", method = RequestMethod.POST)
    public ResponseEntity<?> getQZSettingsTask(HttpServletRequest request) {
        String userName = (String) request.getParameter("userName");
        String password = (String) request.getParameter("password");
        try {
            if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password) && validUser(userName, password)) {
                List<ExternalQzSettingVO> qzSettingTasks = qzKeywordRankInfoService.getQZSettingTask();
                return new ResponseEntity<Object>(qzSettingTasks, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(false, HttpStatus.OK);
    }

    @RequestMapping(value = "/updateQZSettingsTasks", method = RequestMethod.POST)
    public ResponseEntity<?> updateQZSettingsTask(@RequestBody ExternalQzKeywordRankInfoVO externalQzKeywordRankInfoVO,HttpServletRequest request) {
        String userName = externalQzKeywordRankInfoVO.getUserName();
        String password = externalQzKeywordRankInfoVO.getPassword();
        try {
            if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password) && validUser(userName, password)) {
                qzKeywordRankInfoService.updateQzKeywordRankInfo(externalQzKeywordRankInfoVO);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(false, HttpStatus.OK);
    }
}
