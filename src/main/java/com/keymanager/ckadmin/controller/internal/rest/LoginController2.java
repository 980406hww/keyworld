package com.keymanager.ckadmin.controller.internal.rest;


import com.keymanager.ckadmin.common.result.ResultBean;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController2 {

    @PostMapping("/changeBusinessType")
    public ResultBean changeBusinessType(HttpSession session, String businessType){
        try {
            session.setAttribute("entryType",businessType);

            return new ResultBean(200,"success", session.getAttribute("entryType"));
        }catch (Exception e){
            e.printStackTrace();
            return new ResultBean(400,"fail");
        }

    }

}
