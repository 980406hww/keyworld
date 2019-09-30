package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.service.QZSettingService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shunshikj40
 */
@RestController
@RequestMapping("/external/simpleseodatasync/")
public class ExternalSimpleSeoManagementDataSync extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalSimpleSeoManagementDataSync.class);

    @Autowired
    private QZSettingService qzSettingService;

    @PostMapping("getCustomerKeywordForDataSync")
    public ResponseEntity<?> getCustomerKeywordForDataSync(@RequestBody Map<String, String> requestMap) {
        String userName = requestMap.get("userName");
        String password = requestMap.get("password");
        try {
            if (validUser(userName, password)) {
                // todo 从全局缓存map中拿数据  一次拿一个客户id下的所有关键词，站点信息，曲线信息
                String key = requestMap.get("key");
                Map<String, Object> map = qzSettingService.getCustomerKeywordForDataSync(key);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
