package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.util.FileUtil;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.util.Constants;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/configs")
public class ConfigController {

    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Resource(name = "configService2")
    private ConfigService configService;

    @RequiresPermissions("/internal/config/searchNegativeKeywords")
    @GetMapping(value = "/toNegativeSetting")
    public ModelAndView toCustomers() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("negativeSetting/negativeSetting");
        return mv;
    }

    @RequiresPermissions("/internal/config/searchNegativeKeywords")
    @RequestMapping(value = "/getNegativeSetting", method = RequestMethod.GET)
    public ResultBean searchNegativeKeywords() {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            Config config = configService.getConfig(Constants.CONFIG_TYPE_TJ_XG, Constants.CONFIG_KEY_NEGATIVE_KEYWORDS);
            Config negativeKeywordConfig = configService.getConfig(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD, Constants.CONFIG_KEY_BAIDU);
            Config websiteWhiteList = configService.getConfig(Constants.CONFIG_TYPE_WEBSITE_WHITE_LIST, Constants.CONFIG_KEY_URL);
            Map<String, Object> data = new HashMap<>(4);
            data.put("searchEngine", Constants.CONFIG_KEY_BAIDU);
            data.put("negativeKeywords", config.getValue());
            data.put("customerNegativeKeywords", negativeKeywordConfig.getValue());
            data.put("websiteWhiteList", websiteWhiteList.getValue());
            resultBean.setData(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/config/updateNegativeKeywords")
    @RequestMapping(value = "/updateNegativeKeywords", method = RequestMethod.POST)
    public ResultBean updateNegativeKeywords(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String negativeKeywords = (String) requestMap.get("negativeKeywords");
            configService.updateNegativeKeywordsFromConfig(negativeKeywords);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/config/refreshCustomerNegativeKeywords")
    @RequestMapping(value = "/findCustomerNegativeKeywords/{searchEngine}", method = RequestMethod.GET)
    public ResultBean findCustomerNegativeKeywords(@PathVariable("searchEngine") String searchEngine) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            Config negativeKeywordConfig = configService.getConfig(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD, searchEngine);
            resultBean.setData(negativeKeywordConfig.getValue());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/config/refreshCustomerNegativeKeywords")
    @RequestMapping(value = "/refreshCustomerNegativeKeywords", method = RequestMethod.POST)
    public ResultBean refreshCustomerNegativeKeywords(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String searchEngine = (String) requestMap.get("searchEngine");
            String negativeKeywords = (String) requestMap.get("negativeKeywords");
            configService.refreshCustomerNegativeKeywords(searchEngine, negativeKeywords);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/config/refreshWebsiteWhiteList")
    @RequestMapping(value = "/refreshWebsiteWhiteList", method = RequestMethod.POST)
    public ResultBean refreshWebsiteWhiteList(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String websiteWhiteList = (String) requestMap.get("websiteWhiteList");
            configService.refreshWebsiteWhiteList(websiteWhiteList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/config/refreshCustomerNegativeKeywords")
    @RequestMapping(value = "/getFileMsg", method = RequestMethod.POST)
    public ResultBean getFileMsg(@RequestParam(value = "file") MultipartFile file) {
        ResultBean resultBean = new ResultBean(200, "success");
        if (file.isEmpty()) {
            resultBean.setCode(400);
            return resultBean;
        }
        try {
            String path = Utils.getWebRootPath() + "temp";
            File targetFile = new File(path, "temp.txt");
            if (!targetFile.exists()) {
                if (!targetFile.mkdirs()) {
                    resultBean.setCode(400);
                    return resultBean;
                }
            }
            file.transferTo(targetFile);
            resultBean.setData(FileUtil.getFileCharset(targetFile));
            FileUtil.delFolder(path);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/config/evictAllConfigCache")
    @RequestMapping(value = "/evictAllConfigCache", method = RequestMethod.GET)
    public ResultBean evictAllConfigCache() {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            configService.evictAllConfigCache();
            logger.info("清理Config缓存");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }
}
