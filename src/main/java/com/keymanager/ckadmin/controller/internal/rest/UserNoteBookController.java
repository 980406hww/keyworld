package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.UserNoteBook;
import com.keymanager.ckadmin.service.UserNoteBookService;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
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

    @RequiresPermissions("/internal/usernotebook/searchUserNoteBooks")
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

    @RequiresPermissions("/internal/usernotebook/searchUserNoteBooks")
    @RequestMapping(value = "/saveUserNoteBook", method = RequestMethod.POST)
    public ResultBean saveUserNoteBook(@RequestBody Map<String, Object> resultMap,
        HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            UserNoteBook userNoteBook = new UserNoteBook();
            String uuid = (String) resultMap.get("uuid");
            if (null != uuid) {
                userNoteBook.setUuid(Long.parseLong(uuid));
            }
            userNoteBook.setCustomerUuid(Long.parseLong((String) resultMap.get("customerUuid")));
            userNoteBook.setContent((String) resultMap.get("content"));
            userNoteBook.setNotesPerson((String) session.getAttribute("username"));
            userNoteBook.setTerminalType((String) resultMap.get("terminalType"));
            int affectedRows = userNoteBookService.saveUserNoteBook(userNoteBook);
            if (affectedRows > 0) {
                return resultBean;
            } else {
                resultBean.setCode(400);
                return resultBean;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
