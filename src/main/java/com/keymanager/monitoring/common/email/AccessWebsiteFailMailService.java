package com.keymanager.monitoring.common.email;

import com.keymanager.monitoring.entity.Website;
import com.keymanager.util.Utils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccessWebsiteFailMailService {

    private static final String DEFAULT_ENCODING = "utf-8";
    private static Logger logger = LoggerFactory.getLogger(AccessWebsiteFailMailService.class);
    private JavaMailSender mailSender;
    private String toEmail;
    private String emailFrom;
    private Template accessURLFailMailTemplate;
    private Template accessURLSuccessMailTemplate;
    private Template expireTimeWebsiteMailTemplate;

    public void sendAccessWebsiteFailMail(List<Website> accessFailWebsites) throws Exception{
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
        String[] toEmailArr = toEmail.split(";");
        helper.setTo(toEmailArr);
        helper.setFrom(emailFrom);
        helper.setSubject("网站访问失败警告");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("accessFailWebsites", accessFailWebsites);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(accessURLFailMailTemplate, context);
        helper.setText(content, true);
        mailSender.send(msg);
        logger.info("HTML版邮件已发送");
    }

    public void sendAccessWebsiteSuccessMail(List<Website> accessSuccessWebsites) throws Exception{
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
        String[] toEmailArr = toEmail.split(";");
        helper.setTo(toEmailArr);
        helper.setFrom(emailFrom);
        helper.setSubject("网站恢复访问提醒");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("accessSuccessWebsites", accessSuccessWebsites);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(accessURLSuccessMailTemplate, context);
        helper.setText(content, true);
        mailSender.send(msg);
        logger.info("HTML版邮件已发送");
    }

    public void sendExpireTimeWebsiteFailMail(List<Website> websites) throws Exception{
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
        String[] toEmailArr = toEmail.split(";");
        helper.setTo(toEmailArr);
        helper.setFrom(emailFrom);
        helper.setSubject("域名过期提醒");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("websites", websites);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(expireTimeWebsiteMailTemplate, context);
        helper.setText(content, true);
        mailSender.send(msg);
        logger.info("域名提醒--> HTML版邮件已发送");
    }

    public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
    }

    public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
      accessURLFailMailTemplate = freemarkerConfiguration.getTemplate("accessURLFailMailTemplate.ftl", DEFAULT_ENCODING);
      accessURLSuccessMailTemplate = freemarkerConfiguration.getTemplate("accessURLSuccessMailTemplate.ftl", DEFAULT_ENCODING);
      expireTimeWebsiteMailTemplate = freemarkerConfiguration.getTemplate("expireTimeWebsiteMailTemplate.ftl", DEFAULT_ENCODING);
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public void setEmailFrom(String emailFrom) {
    this.emailFrom = emailFrom;
  }

}
