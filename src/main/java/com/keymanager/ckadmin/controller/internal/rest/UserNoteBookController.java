package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.UserNoteBookService;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/notebook")
public class UserNoteBookController {

    private static Logger logger = LoggerFactory.getLogger(UserNoteBookController.class);

    @Resource(name = "userNoteBookService2")
    private UserNoteBookService userNoteBookService;

    //    @RequiresPermissions("/internal/usernotebook/searchUserNoteBooks")
    @RequiresPermissions("/internal/customer/searchCustomers")
    @RequestMapping(value = "/getUserNoteBooks", method = RequestMethod.POST)
    public ResultBean getUserNoteBooks(@RequestBody Map<String, Object> resultMap) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        Long customerUuid = Long.parseLong((String) resultMap.get("customerUuid"));
        Integer searchAll = (Integer) resultMap.get("searchAll");
        String terminalType = (String) resultMap.get("terminalType");
        try {
            resultBean.setData(userNoteBookService
                .findUserNoteBooks(customerUuid, terminalType, searchAll));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
