package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.util.Constants;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/config")
public class ConfigRestController {

    private static Logger logger = LoggerFactory.getLogger(ConfigRestController.class);

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/searchNegativeKeywords", method = RequestMethod.GET)
    public ModelAndView searchNegativeKeywords(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/negativeKeywords/negativeKeywords");
        Config config = configService.getConfig(Constants.CONFIG_TYPE_TJ_XG, Constants.CONFIG_KEY_NEGATIVE_KEYWORDS);
        modelAndView.addObject("negativeKeywords", config.getValue());
        return modelAndView;
    }

    @RequestMapping(value = "/updateNegativeKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> updateNegativeKeywords(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            String negativeKeywords = (String)requestMap.get("negativeKeywords");
            configService.updateNegativeKeywordsFromConfig(negativeKeywords);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

}
