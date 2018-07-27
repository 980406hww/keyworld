package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CookieCriteria;
import com.keymanager.monitoring.entity.Cookie;
import com.keymanager.monitoring.service.CookieService;
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
@RequestMapping(value = "/internal/cookie")
public class CookieRestController {

    private static Logger logger = LoggerFactory.getLogger(CookieRestController.class);

    @Autowired
    private CookieService cookieService;

    @RequiresPermissions("/internal/cookie/searchCookies")
    @RequestMapping(value = "/searchCookies", method = RequestMethod.GET)
    public ModelAndView searchCookies(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructCookieModelAndView(new CookieCriteria(), currentPageNumber, pageSize, request);
    }

    @RequiresPermissions("/internal/cookie/searchCookies")
    @RequestMapping(value = "/searchCookies", method = RequestMethod.POST)
    public ModelAndView searchCookiesPost(HttpServletRequest request, CookieCriteria cookieCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructCookieModelAndView(cookieCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize), request);
    }

    private ModelAndView constructCookieModelAndView(CookieCriteria cookieCriteria, int currentPageNumber, int pageSize, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/cookie/cookie");
        Page<Cookie> page = cookieService.searchCookies(new Page<Cookie>(currentPageNumber,pageSize), cookieCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("cookieCriteria", cookieCriteria);
        return modelAndView;
    }

    @RequiresPermissions("/internal/cookie/saveCookieByFile")
    @RequestMapping(value = "/saveCookieByFile", method = RequestMethod.POST)
    public ResponseEntity<?> saveCookieByFile(@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "searchEngine") String searchEngine, HttpServletRequest request) {
        try {
            String path = Utils.getWebRootPath() + "txtTemp";
            File targetFile = new File(path, "cookie.txt");
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            cookieService.saveCookieByFile(targetFile.getPath(), searchEngine);
            FileUtil.delFolder(path);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }
}
