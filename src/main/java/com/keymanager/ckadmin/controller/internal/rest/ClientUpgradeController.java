package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.base.BaseCriteria;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import com.keymanager.ckadmin.service.ClientUpgradeService;
import com.keymanager.ckadmin.util.ReflectUtils;
import com.keymanager.util.TerminalTypeMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/clientUpgrade")
public class ClientUpgradeController {
    private static Logger logger = LoggerFactory.getLogger(ClientUpgradeController.class);

    @Resource(name = "clientUpgradeService2")
    private ClientUpgradeService clientUpgradeService;

    @RequestMapping(value = "/toClientUpgrades2", method = RequestMethod.GET)
    public ModelAndView toClientUpgrades2(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/clientUpgrades");
        return mv;
    }

    @RequestMapping(value = "/getClientUpgrades2")
    public ResultBean getClientUpgrades2(HttpServletRequest request, @RequestBody BaseCriteria baseCriteria){
        ResultBean resultBean = new ResultBean();
        try {
            Page<ClientUpgrade> page = new Page<>(baseCriteria.getPage(), baseCriteria.getLimit());
            String orderByField = ReflectUtils.getTableFieldValue(ClientUpgrade.class, baseCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
            }
            if (baseCriteria.getOrderMode() != null && baseCriteria.getOrderMode() == 0) {
                page.setAsc(false);
            }
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            page = clientUpgradeService.searchClientUpgrades(page, terminalType);
            resultBean.setCode(0);
            resultBean.setMsg("success");
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/toClientUpgradeAdd", method = RequestMethod.GET)
    public ModelAndView toClientUpgradeAdd(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/clientUpgradeAdd");
        return mv;
    }

    @RequestMapping(value = "/saveClientUpgrade2", method = RequestMethod.POST)
    public ResultBean saveClientUpgrade(@RequestBody ClientUpgrade clientUpgrade, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        try {
            clientUpgradeService.saveClientUpgrade(terminalType, clientUpgrade);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/getClientUpgrade2/{uuid}", method = RequestMethod.GET)
    public ResultBean getClientUpgrade(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            ClientUpgrade clientUpgrade = clientUpgradeService.selectById(uuid);
            resultBean.setCode(200);
            resultBean.setData(clientUpgrade);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/deleteClientUpgrade2/{uuid}", method = RequestMethod.GET)
    public ResultBean deleteClientUpgrade(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            clientUpgradeService.deleteById(uuid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/batchDeleteClientUpgrade", method = RequestMethod.POST)
    public ResultBean batchDeleteClientUpgrade(@RequestBody Map<String, Object> requestMap){
        ResultBean resultBean = new ResultBean();
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            clientUpgradeService.batchDeleteClientUpgrade(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/updateClientUpgradeStatus2", method = RequestMethod.POST)
    public ResultBean updateClientUpgradeStatus(@RequestBody ClientUpgrade clientUpgrade) {
        ResultBean resultBean = new ResultBean();
        try {
            clientUpgradeService.updateClientUpgradeStatus(clientUpgrade.getUuid(), clientUpgrade.getStatus());
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }
}
