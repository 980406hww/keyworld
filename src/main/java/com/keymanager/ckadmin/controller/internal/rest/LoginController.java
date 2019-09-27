package com.keymanager.ckadmin.controller.internal.rest;


import com.keymanager.ckadmin.common.result.ResultBean;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class LoginController {

    @RequestMapping("/changeBusinessType")
    public ResultBean changeBusinessType(HttpSession session, String businessType){
        try {
            session.setAttribute("entryType",businessType);
            return new ResultBean(200,"success");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultBean(400,"fail");
        }

    }

}
