package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.UpdateBearPawNumCriteria;
import com.keymanager.monitoring.service.UpdateKeywordBearsPawNumberService;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Decription ExternalUpdateKeywordBearsPawNumber
 * @Author: rwxian
 * @Date 2019/9/27 15:08
 */
@RestController
@RequestMapping("/external/updateKeywordBearPawNumber")
public class ExternalUpdateKeywordBearsPawNumController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalUpdateKeywordBearsPawNumController.class);

    @Autowired
    private UpdateKeywordBearsPawNumberService updateKeywordBearsPawNumberService;

    /**
     * 提供给python程序初始化CUSTOMER_KEYWORD_DOMAIN_MAP的接口
     * @param bearPawCriteria
     * @return
     */
    @RequestMapping(value = "/cacheCustomerKeywordDomainMap", method = RequestMethod.POST)
    public ResponseEntity<?> cacheCustomerKeywordDomainMap(@RequestBody UpdateBearPawNumCriteria bearPawCriteria) {
        try {
            if (validUser(bearPawCriteria.getUserName(), bearPawCriteria.getPassword())) {
                updateKeywordBearsPawNumberService.cacheCustomerKeywordDomainMap();
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 提供给Python的接口，返回需要处理的域名，每次请求返回20个
     * @param bearPawCriteria
     * @return
     */
    @RequestMapping(value = "/getNeedHandleDomain", method = RequestMethod.POST)
    public ResponseEntity<?> getNeedHandleDomain(@RequestBody UpdateBearPawNumCriteria bearPawCriteria) {
        try {
            if (validUser(bearPawCriteria.getUserName(), bearPawCriteria.getPassword())) {
                return new ResponseEntity<Object>(updateKeywordBearsPawNumberService.getCustomerKeywordDomains(), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Python调用，更新熊掌号
     * @param map
     * @return
     */
    @RequestMapping(value = "/updateBearPawNumber")
    public ResponseEntity updateBearPawNumber(@RequestBody Map<String, Object> map) {
        String userName = (String) map.get("userName");
        String password = (String) map.get("password");
        try {
            if (validUser(userName, password)) {
                Map data = (Map) map.get("data");
                for (Entry<String, String> next : (Iterable<Entry<String, String>>) data.entrySet()) {
                    String url = next.getKey();
                    String bearPaw = next.getValue();
                    updateKeywordBearsPawNumberService.updateBearPaw(url, bearPaw);
                }
                return new ResponseEntity(HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
