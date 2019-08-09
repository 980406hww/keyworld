package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.common.csrf.CsrfToken;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.KeywordManagerSessionCriteria;
import com.keymanager.monitoring.entity.KeywordManagerSession;
import com.keymanager.monitoring.service.KeywordManagerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhoukai
 * @since 2019-08-09
 */
@Controller
@RequestMapping("/internal/keywordManagerSession")
public class KeywordManagerSessionController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(KeywordManagerSessionController.class);

    @Autowired
    private KeywordManagerSessionService keywordManagerSessionService;

    @CsrfToken(create = true)
    @RequestMapping(value = "/searchKeywordManagerSessions", method = RequestMethod.GET)
    public ModelAndView searchKeywordManagerSessionsGet(@RequestParam(defaultValue = "1") int currentPageNumber,
                                            @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructKeywordManagerSessionModelAndView(request, new KeywordManagerSessionCriteria(), currentPageNumber, pageSize);
    }

    @CsrfToken(create = true)
    @RequestMapping(value = "/searchKeywordManagerSessions", method = RequestMethod.POST)
    public ModelAndView searchKeywordManagerSessionsPost(HttpServletRequest request, KeywordManagerSessionCriteria keywordManagerSessionCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if ((null == currentPageNumber && null == pageSize) || keywordManagerSessionCriteria.getResetPagingParam()) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructKeywordManagerSessionModelAndView(request, keywordManagerSessionCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
    }

    private ModelAndView constructKeywordManagerSessionModelAndView(HttpServletRequest request, KeywordManagerSessionCriteria keywordManagerSessionCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/views/keywordManagerSession");
        Page<KeywordManagerSession> page = keywordManagerSessionService.searchKeywordManagerSession(new Page<KeywordManagerSession>(currentPageNumber, pageSize), keywordManagerSessionCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

}
