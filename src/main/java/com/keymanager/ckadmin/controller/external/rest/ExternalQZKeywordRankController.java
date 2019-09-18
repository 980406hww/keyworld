package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.service.QZKeywordRankInfoService;
import com.keymanager.ckadmin.vo.ExternalQZKeywordRankInfoResultVO;
import com.keymanager.ckadmin.vo.ExternalQZSettingVO;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhoukai
 * @Date 2018/12/6 11:33
 **/

@RestController
@RequestMapping(value = "/external/qzkeywordrank")
public class ExternalQZKeywordRankController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalQZKeywordRankController.class);

    @Resource(name = "qzKeywordRankInfoService2")
    private QZKeywordRankInfoService qzKeywordRankInfoService;

    @RequestMapping(value = "/getQZSettingsTask2", method = RequestMethod.POST)
    public ResponseEntity<?> getQZSettingsTask(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        try {
            if (validUser(userName, password)) {
                List<ExternalQZSettingVO> qzSettingTasks = qzKeywordRankInfoService.getQZSettingTask();
                return new ResponseEntity<Object>(qzSettingTasks, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateQZSettingsTasks2", method = RequestMethod.POST)
    public ResponseEntity<?> updateQZSettingsTask(
        @RequestBody ExternalQZKeywordRankInfoResultVO externalQZKeywordRankInfoResultVo) {
        String userName = externalQZKeywordRankInfoResultVo.getUserName();
        String password = externalQZKeywordRankInfoResultVo.getPassword();
        try {
            if (validUser(userName, password)) {
                qzKeywordRankInfoService.updateQzKeywordRankInfo(externalQZKeywordRankInfoResultVo);
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
