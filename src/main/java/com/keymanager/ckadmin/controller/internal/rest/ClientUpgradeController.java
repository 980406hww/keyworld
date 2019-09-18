package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.base.BaseCriteria;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import com.keymanager.ckadmin.service.ClientUpgradeService;
import com.keymanager.util.TerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

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
            //TODO
            //排序
            Page<ClientUpgrade> page = new Page<>(baseCriteria.getPage(), baseCriteria.getLimit());
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

    @RequestMapping(value = "/saveClientUpgrade2", method = RequestMethod.POST)
    public ResponseEntity<?> saveClientUpgrade(@RequestBody ClientUpgrade clientUpgrade, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            clientUpgradeService.saveClientUpgrade(terminalType, clientUpgrade);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getClientUpgrade2/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getClientUpgrade(@PathVariable("uuid") Long uuid) {
        try {
            return new ResponseEntity<Object>(clientUpgradeService.selectById(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deleteClientUpgrade2/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteClientUpgrade(@PathVariable("uuid") Long uuid) {
        try {
            clientUpgradeService.deleteById(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateClientUpgradeStatus2", method = RequestMethod.POST)
    public ResponseEntity<?> updateClientUpgradeStatus(@RequestBody ClientUpgrade clientUpgrade) {
        try {
            clientUpgradeService.updateClientUpgradeStatus(clientUpgrade.getUuid(), clientUpgrade.getStatus());
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
