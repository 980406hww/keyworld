package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.UpdateBearPawNumCriteria;
import com.keymanager.monitoring.service.UpdateKeywordBearsPawNumberService;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
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
@RequestMapping(value = "/external/ExternalUpdateKeywordBearPawNumber")
public class ExternalUpdateKeywordBearsPawNumController extends SpringMVCBaseController {

    @Autowired
    private UpdateKeywordBearsPawNumberService updateKeywordBearsPawNumberService;

    private ConcurrentHashMap all_domain = null;

    private String g_sessionId = null;

    /**
     * 提供给Python的接口，返回需要处理的域名，每次请求返回20个
     * @param request
     * @return
     */
    @RequestMapping(value = "/get_need_handle_domain", method = RequestMethod.POST)
    public ResponseEntity<?> getNeedHandleDomain(@RequestBody UpdateBearPawNumCriteria bearPawCriteria, HttpServletRequest request) {
        ConcurrentHashMap<String, String> resultMap = null;
        resultMap = new ConcurrentHashMap<>();
        try {
            String request_session_id = request.getSession().getId();
            if (g_sessionId == null || !request_session_id.equals(g_sessionId)) {
                if (validUser(bearPawCriteria.getUserName(), bearPawCriteria.getPassword())) {
                    String sessionId = request.getSession().getId();
                    g_sessionId = sessionId;
                    all_domain = updateKeywordBearsPawNumberService.getDistinctUrl();
                } else {
                    return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
                }
            }
            Set set = all_domain.entrySet();
            Iterator<Entry> iterator = set.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                Entry<String, String> next = iterator.next();
                String key = next.getKey();
                String value = next.getValue();
                resultMap.put(key, value);
                all_domain.remove(key);            // 已经返回的就移除
                if (++count >= 20)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Object>(resultMap, HttpStatus.OK);
    }

    /**
     * Python调用，更新熊掌号
     */
    @RequestMapping(value = "/update_bear_paw_num")
    public ResponseEntity updateBearPawNumber(@RequestBody Map map) {
        String userName = null, password = null;
        Map data = null;
        if (map.containsKey("userName")) {
            userName = (String) map.get("userName");
        }
        if (map.containsKey("password")) {
            password = (String) map.get("password");
        }
        if (map.containsKey("data")) {
            data = (Map) map.get("data");
        }
        try {
            if (validUser(userName, password)) {
                Set set = data.entrySet();
                Iterator<Entry> iterator = set.iterator();
                while (iterator.hasNext()) {
                    Entry<String, String> next = iterator.next();
                    String url = next.getKey();
                    String bearPaw = next.getValue();
                    boolean update_res = updateKeywordBearsPawNumberService.updateBearPaw(url, bearPaw);
                    if (update_res) {
                        System.out.println(url + "更新熊掌号为：" + bearPaw);
                    } else {
                        System.out.println(url + "更新熊掌号失败！");
                    }
                }
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.OK);
    }

}
