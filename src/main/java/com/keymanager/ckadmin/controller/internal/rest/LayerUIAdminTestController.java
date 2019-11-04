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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/index")
    public ModelAndView toIndex() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
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
            List<Menu> menus = resourceService.selectAuthorizationResource(loginName, null);
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
            Map map = qzRateStatisticsService.generateEchartsData(qzRateStatisticsCountVos);
            resultBean.setData(map);
            return resultBean;
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
    }

    @RequestMapping("/toQZRateStatisticsDetail/{terminalType}/{searchEngine}/{rateRange}")
    public ModelAndView toQZRateStatisticsDetail(@PathVariable(name = "terminalType") String terminalType, @PathVariable(name = "searchEngine") String searchEngine,
        @PathVariable(name = "rateRange") String rateRange) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("statistics/qzRateStatisticsDetail");
        try {
            Map<String, String> map = new HashMap<>(3);
            map.put("qzRateRange", rateRange);
            if ("null".equals(searchEngine)) {
                map.put("searchEngine", "");
            } else {
                searchEngine = URLDecoder.decode(searchEngine, "UTF-8");
                map.put("searchEngine", searchEngine);
            }
            if ("null".equals(terminalType)) {
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
