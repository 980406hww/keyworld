package com.keymanager.monitoring.controller.rest.internal;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.KeywordInfoCriteria;
import com.keymanager.monitoring.entity.KeywordInfo;
import com.keymanager.monitoring.service.KeywordInfoService;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/5/25.
 */

@RestController
@RequestMapping(value ="/internal/keywordInfo")
public class keywordInfoController {

    @Autowired
    private KeywordInfoService keywordInfoService;

    //初始化
    @RequiresPermissions("/internal/keywordInfo/keywordInfos")
    @RequestMapping(value = "/keywordInfos", method = RequestMethod.GET)
    public ModelAndView searchWebsites(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {

        KeywordInfoCriteria KeywordInfoCriteria=new KeywordInfoCriteria();
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        if(terminalType!=null){
            KeywordInfoCriteria.setPlatform(terminalType);
            return constructWebsiteModelAndView(KeywordInfoCriteria, currentPageNumber, pageSize);
        }else {
            return constructWebsiteModelAndView(KeywordInfoCriteria, currentPageNumber, pageSize);
        }
    }


    @RequiresPermissions("/internal/keywordInfo/keywordInfos")
    @RequestMapping(value = "/keywordInfos", method = RequestMethod.POST)
    public ModelAndView searchWebsitesPost(HttpServletRequest request, KeywordInfoCriteria KeywordInfoCriteria) {

        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        String terminalType = TerminalTypeMapping.getTerminalType(request);
      if(terminalType!=null){
          KeywordInfoCriteria.setPlatform(terminalType);
          return constructWebsiteModelAndView(KeywordInfoCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        }else {
            return constructWebsiteModelAndView(KeywordInfoCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        }

    }

    //分页视图
    private ModelAndView constructWebsiteModelAndView(KeywordInfoCriteria KeywordInfoCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("keywordInfo/keywordInfo");
        Page<KeywordInfo> page = keywordInfoService.searchKeywordInfo(new Page<KeywordInfo>(currentPageNumber,pageSize), KeywordInfoCriteria);
        modelAndView.addObject("KeywordInfoCriteria", KeywordInfoCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }




}
