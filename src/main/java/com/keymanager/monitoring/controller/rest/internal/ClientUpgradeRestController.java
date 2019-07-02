package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.ClientUpgrade;
import com.keymanager.monitoring.service.ClientUpgradeService;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/internal/clientUpgrade")
public class ClientUpgradeRestController {

    private static Logger logger = LoggerFactory.getLogger(ClientUpgradeRestController.class);

    @Autowired
    private ClientUpgradeService clientUpgradeService;

    @RequiresPermissions("/internal/clientUpgrade/searchClientUpgrades")
    @RequestMapping(value = "/searchClientUpgrades", method = RequestMethod.GET)
    public ModelAndView searchClientUpgrades(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructRegativeRankoModelAndView(currentPageNumber, pageSize, request);
    }

    @RequiresPermissions("/internal/clientUpgrade/searchClientUpgrades")
    @RequestMapping(value = "/searchClientUpgrades", method = RequestMethod.POST)
    public ModelAndView searchClientUpgradePost(HttpServletRequest request) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructRegativeRankoModelAndView(Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize), request);
    }

    private ModelAndView constructRegativeRankoModelAndView(int currentPageNumber, int pageSize, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("client/clientUpgrade");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        Page<ClientUpgrade> page = clientUpgradeService.searchClientUpgrades(new Page<ClientUpgrade>(currentPageNumber, pageSize), terminalType);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/clientUpgrade/saveClientUpgrade")
    @RequestMapping(value = "/saveClientUpgrade", method = RequestMethod.POST)
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

    @RequestMapping(value = "/getClientUpgrade/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getClientUpgrade(@PathVariable("uuid") Long uuid) {
        try {
            return new ResponseEntity<Object>(clientUpgradeService.selectById(uuid), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientUpgrade/deleteClientUpgrade")
    @RequestMapping(value = "/deleteClientUpgrade/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteClientUpgrade(@PathVariable("uuid") Long uuid) {
        try {
            clientUpgradeService.deleteById(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientUpgrade/saveClientUpgrade")
    @RequestMapping(value = "/updateClientUpgradeStatus", method = RequestMethod.POST)
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
