package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.ConfigService;
import java.util.Arrays;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CommonController
 * @Description 通用数据请求类
 * @Author lhc
 * @Date 2019/9/5 15:37
 * @Version 1.0
 */
@RestController
@RequestMapping("/internal/common")
public class CommonController {

    private static Logger logger = LoggerFactory.getLogger(CommonController.class);

    @Resource(name = "configService2")
    private ConfigService configService;

    @PostMapping("/getSearchEngines")
    public ResultBean getSearchEngineMap(@RequestBody Map<String, String> requestMap) {
        try {
            String terminalType = (String) requestMap.get("terminalType");
            String[] searchEngines = configService.getSearchEngines(terminalType);
            Arrays.sort(searchEngines);
            return new ResultBean(200, "获取搜索引擎列表成功", searchEngines);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400,"未知错误!");
        }
    }

}
