package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.service.QZKeywordRankInfoService;
import com.keymanager.monitoring.vo.ExternalQZKeywordRankInfoResultVO;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        try {
            if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password) && validUser(userName, password)) {
                List<ExternalQzSettingVO> qzSettingTasks;
                synchronized (ExternalQZKeywordRankInfoController.class){
                     qzSettingTasks = qzKeywordRankInfoService.getQZSettingTask();
                }
                return new ResponseEntity<Object>(qzSettingTasks, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateQZSettingsTasks", method = RequestMethod.POST)
    public ResponseEntity<?> updateQZSettingsTask(@RequestBody ExternalQZKeywordRankInfoResultVO externalQZKeywordRankInfoResultVo) {
        String userName = externalQZKeywordRankInfoResultVo.getUserName();
        String password = externalQZKeywordRankInfoResultVo.getPassword();
        try {
            if(StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password) && validUser(userName, password)) {
                qzKeywordRankInfoService.updateQzKeywordRankInfo(externalQZKeywordRankInfoResultVo);
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
