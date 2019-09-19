package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.service.QZKeywordRankInfoService;
import com.keymanager.ckadmin.vo.ExternalQZKeywordRankInfoResultVO;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/external/qzkeywordrank")
public class ExternalQZKeywordRankController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalQZKeywordRankController.class);

    @Resource(name = "qzKeywordRankInfoService2")
    private QZKeywordRankInfoService qzKeywordRankInfoService;

    @PostMapping(value = "/getQZSettingsTask2")
    public ResponseEntity<?> getQZSettingsTask(HttpServletRequest request) {
        try {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            if (validUser(userName, password)) {
                return new ResponseEntity<Object>(qzKeywordRankInfoService.getQZSettingTask(),
                    HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/updateQZSettingsTasks2")
    public ResponseEntity<?> updateQZSettingsTask(
        @RequestBody ExternalQZKeywordRankInfoResultVO externalQZKeywordRankInfoResultVo) {
        try {
            String userName = externalQZKeywordRankInfoResultVo.getUserName();
            String password = externalQZKeywordRankInfoResultVo.getPassword();
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
