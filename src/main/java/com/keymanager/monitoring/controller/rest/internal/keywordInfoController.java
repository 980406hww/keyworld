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

    @RequiresPermissions("/internal/keywordInfo/searchKeywordInfos")
    @RequestMapping(value = "/searchKeywordInfos", method = RequestMethod.GET)
    public ModelAndView searchKeywordInfos(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructKeywordInfoModelAndView(request,new KeywordInfoCriteria(), currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/keywordInfo/searchKeywordInfos")
    @RequestMapping(value = "/searchKeywordInfos", method = RequestMethod.POST)
    public ModelAndView searchKeywordInfoPost(HttpServletRequest request, KeywordInfoCriteria keywordInfoCriteria) {

        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructKeywordInfoModelAndView(request,keywordInfoCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
    }

    private ModelAndView constructKeywordInfoModelAndView(HttpServletRequest request,KeywordInfoCriteria KeywordInfoCriteria, int currentPageNumber, int pageSize) {
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        KeywordInfoCriteria.setTerminalType(terminalType);
        ModelAndView modelAndView = new ModelAndView("keywordInfo/keywordInfo");
        Page<KeywordInfo> page = keywordInfoService.searchKeywordInfos(new Page<KeywordInfo>(currentPageNumber,pageSize), KeywordInfoCriteria);
        modelAndView.addObject("KeywordInfoCriteria", KeywordInfoCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }




}
