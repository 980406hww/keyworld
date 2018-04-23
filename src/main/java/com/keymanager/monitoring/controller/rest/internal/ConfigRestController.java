package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.util.Constants;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
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
        Config negativeKeywordConfig = configService.getConfig(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD, Constants.CONFIG_KEY_BAIDU);
        modelAndView.addObject("searchEngine", Constants.CONFIG_KEY_BAIDU);
        modelAndView.addObject("negativeKeywords", config.getValue());
        modelAndView.addObject("customerNegativeKeywords", negativeKeywordConfig.getValue());
        return modelAndView;
    }

    @RequestMapping(value = "/findCustomerNegativeKeywords/{searchEngine}", method = RequestMethod.POST)
    public ResponseEntity<?> findCustomerNegativeKeywords(HttpServletRequest request, @PathVariable("searchEngine") String searchEngine) {
        try {
            Config negativeKeywordConfig = configService.getConfig(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD, searchEngine);
            return new ResponseEntity<Object>(negativeKeywordConfig.getValue(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
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

    @RequestMapping(value = "/updateCustomerNegativeKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> updateCustomerNegativeKeywords(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "searchEngine") String searchEngine, HttpServletRequest request) {
        try {
            String path = Utils.getWebRootPath() + "txtTemp";
            File targetFile = new File(path, "keyword.txt");
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            configService.updateCustomerNegativeKeywords(targetFile, searchEngine);
            FileUtil.delFolder(path);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }
}
