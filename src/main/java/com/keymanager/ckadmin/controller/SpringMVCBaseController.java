package com.keymanager.ckadmin.controller;

import com.keymanager.monitoring.common.shiro.ShiroUser;
import com.keymanager.monitoring.vo.ExtendedUsernamePasswordToken;
import com.keymanager.util.Utils;
import com.keymanager.util.ZipCompressor;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(
        SpringMVCBaseController.class);

    @Autowired
    private HttpServletRequest httpRequest;


    public Double getDouble(String key) {
        String value = httpRequest.getParameter(key);
        if (StringUtils.isNotBlank(value)) {
            return Double.parseDouble(value);
        }
        return null;
    }

    public Integer getInteger(String key) {
        String value = httpRequest.getParameter(key);
        if (StringUtils.isNotBlank(value)) {
            return Integer.parseInt(value);
        }
        return null;
    }

    public Long getLong(String key) {
        String value = httpRequest.getParameter(key);
        if (StringUtils.isNotBlank(value)) {
            return Long.parseLong(value);
        }
        return null;
    }

    public String getString(String key) {
        return httpRequest.getParameter(key);
    }

    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public ShiroUser getCurrentUser() {
        ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    public void downFile(String fileName, String password) {
        try {
            String path = Thread.currentThread().getContextClassLoader().getResource("").toURI()
                .getPath();
            ZipCompressor.createEncryptionZip(path + fileName.substring(0, fileName.indexOf(".")),
                Utils.getWebRootPath() + fileName, password);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public void downExcelFile(HttpServletResponse response, String fileName, byte[] buffer) {
        try {
            response.reset();
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + buffer.length);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validUser(String userName, String password) throws Exception {
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            return true;
        } else {
            ExtendedUsernamePasswordToken token = new ExtendedUsernamePasswordToken(userName,
                password);
            token.setEntryType("External");
            try {
                user.login(token);
                return true;
            } catch (Exception ex) {
                throw ex;
            }
        }
    }

}
