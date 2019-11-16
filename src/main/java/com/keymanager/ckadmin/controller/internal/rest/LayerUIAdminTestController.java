package com.keymanager.ckadmin.controller.internal.rest;

import com.alibaba.fastjson.JSON;
import com.keymanager.ckadmin.common.result.Menu;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.QZRateStatisticsCountCriteria;
import com.keymanager.ckadmin.service.QZRateStatisticsService;
import com.keymanager.ckadmin.service.ResourceService;
import com.keymanager.ckadmin.vo.QZRateStatisticsCountVO;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName LayerUIAdminTestRestController
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/30 9:34
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/internal/layer")
public class LayerUIAdminTestController {

    private static Logger logger = LoggerFactory.getLogger(LayerUIAdminTestController.class);

    @Resource(name = "resourceService2")
    private ResourceService resourceService;

    @Resource(name = "qzRateStatisticsService2")
    private QZRateStatisticsService qzRateStatisticsService;

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(required = false) String url, @RequestParam(required = false) String tit, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        mv.addObject("url", url);
        if (null == url || "".equals(url)) {
            mv.addObject("first", "home");
            return mv;
        }
        List menus = (List) session.getAttribute("menus");
        if (null == menus || menus.isEmpty()) {
            menus = resourceService.selectAuthorizationResource((String) session.getAttribute("username"), null);
        }
        int i = 0, j = 0;
        String key = null;
            outFor:
        for (Object obj : menus) {
            Menu menu = (Menu) obj;
            i++;
            for (Menu m : menu.getChildren()) {
                j++;
                if (url.equals(m.getHref())) {
                    key = m.getTitle();
                    break outFor;
                }
            }
            j = 0;
        }
        mv.addObject("key", key);
        mv.addObject("tit", tit);
        mv.addObject("id", i + "-" + j);
        return mv;
    }

    @RequestMapping("/home")
    public ModelAndView toConsole() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("home/home");
        return mv;
    }

    @RequestMapping("/menu")
    public ResponseEntity<?> selectMenus(HttpServletRequest request) {
        try {
            String loginName = (String) request.getSession().getAttribute("username");
            List menus = (List) request.getSession().getAttribute("menus");
            if (null == menus || menus.isEmpty()) {
                menus = resourceService.selectAuthorizationResource(loginName, null);
                request.getSession().setAttribute("menus", menus);
            }
            return new ResponseEntity<Object>(menus, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/generateQZRateStatisticsDataMap")
    public ResultBean generateQZRateStatisticsDataMap(@RequestBody QZRateStatisticsCountCriteria qzRateStatisticsCountCriteria, HttpSession session) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            qzRateStatisticsCountCriteria.setUserID((String) session.getAttribute("username"));
            List<QZRateStatisticsCountVO> qzRateStatisticsCountVos = qzRateStatisticsService.getQZRateStatisticCount(qzRateStatisticsCountCriteria);
            resultBean.setData(qzRateStatisticsService.generateEchartsData(qzRateStatisticsCountVos));
            return resultBean;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @GetMapping(value = {"/toQZRateStatisticsDetail/{rateRange}/{terminalType}/{searchEngine}", "/toQZRateStatisticsDetail/{rateRange}/{terminalType}",
        "/toQZRateStatisticsDetail/{rateRange}/{searchEngine}", "/toQZRateStatisticsDetail/{rateRange}"})
    public ModelAndView toQZRateStatisticsDetail(@PathVariable(name = "rateRange") String rateRange, @PathVariable(name = "terminalType", required = false) String terminalType,
        @PathVariable(name = "searchEngine", required = false) String searchEngine) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("statistics/qzRateStatisticsDetail");
        try {
            Map<String, String> map = new HashMap<>(3);
            map.put("qzRateRange", rateRange);
            if (null == searchEngine) {
                map.put("searchEngine", "");
            } else {
                searchEngine = URLDecoder.decode(searchEngine, "UTF-8");
                map.put("searchEngine", searchEngine);
            }
            if (null == terminalType) {
                map.put("terminalType", "");
            } else {
                map.put("terminalType", terminalType);
            }
            mv.addObject("formData", JSON.toJSONString(map));
            return mv;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return mv;
    }
}
