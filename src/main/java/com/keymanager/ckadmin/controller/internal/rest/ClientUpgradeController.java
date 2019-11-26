package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.ClientUpgradeCriteria;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import com.keymanager.ckadmin.service.ClientUpgradeService;
import com.keymanager.ckadmin.util.ReflectUtils;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

    @RequiresPermissions("/internal/clientUpgrade/toClientUpgrades2")
    @RequestMapping(value = "/toClientUpgrades2", method = RequestMethod.GET)
    public ModelAndView toClientUpgrades2() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/clientUpgrades");
        return mv;
    }

    @RequiresPermissions("/internal/clientUpgrade/toClientUpgrades2")
    @RequestMapping(value = "/getClientUpgrades2")
    public ResultBean getClientUpgrades2(@RequestBody ClientUpgradeCriteria clientUpgradeCriteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(clientUpgradeCriteria.getInit())) {
            return resultBean;
        }
        try {
            Page<ClientUpgrade> page = new Page<>(clientUpgradeCriteria.getPage(), clientUpgradeCriteria.getLimit());
            String orderByField = ReflectUtils.getTableFieldValue(ClientUpgrade.class, clientUpgradeCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
            }
            if (clientUpgradeCriteria.getOrderMode() != null && clientUpgradeCriteria.getOrderMode() == 0) {
                page.setAsc(false);
            }
            page = clientUpgradeService.searchClientUpgrades(page, clientUpgradeCriteria.getTerminalType());
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/clientUpgrade/saveClientUpgrade")
    @RequestMapping(value = "/toClientUpgradeAdd", method = RequestMethod.GET)
    public ModelAndView toClientUpgradeAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("client/clientUpgradeAdd");
        return mv;
    }

    @RequiresPermissions("/internal/clientUpgrade/saveClientUpgrade")
    @RequestMapping(value = "/saveClientUpgrade2", method = RequestMethod.POST)
    public ResultBean saveClientUpgrade(@RequestBody ClientUpgrade clientUpgrade) {
        ResultBean resultBean = new ResultBean();
        String terminalType = clientUpgrade.getTerminalType();
        try {
            clientUpgradeService.saveClientUpgrade(terminalType, clientUpgrade);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
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
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/clientUpgrade/deleteClientUpgrade")
    @RequestMapping(value = "/deleteClientUpgrade2/{uuid}", method = RequestMethod.GET)
    public ResultBean deleteClientUpgrade(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            clientUpgradeService.deleteById(uuid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/clientUpgrade/deleteClientUpgrade")
    @RequestMapping(value = "/batchDeleteClientUpgrade", method = RequestMethod.POST)
    public ResultBean batchDeleteClientUpgrade(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            clientUpgradeService.batchDeleteClientUpgrade(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/clientUpgrade/saveClientUpgrade")
    @RequestMapping(value = "/updateClientUpgradeStatus2", method = RequestMethod.POST)
    public ResultBean updateClientUpgradeStatus(@RequestBody ClientUpgrade clientUpgrade) {
        ResultBean resultBean = new ResultBean();
        try {
            clientUpgradeService.updateClientUpgradeStatus(clientUpgrade.getUuid(), clientUpgrade.getStatus());
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }
}
