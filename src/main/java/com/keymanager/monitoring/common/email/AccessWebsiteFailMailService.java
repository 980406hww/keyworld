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
import java.util.Map;

public class AccessWebsiteFailMailService {

    private static final String DEFAULT_ENCODING = "utf-8";
    private static Logger logger = LoggerFactory.getLogger(AccessWebsiteFailMailService.class);
    private JavaMailSender mailSender;
    private String toEmail;
    private String emailFrom;
    private Template accessURLFailMailTemplate;

    public void sendAccessWebsiteFailMail(Website website) throws Exception{
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
        helper.setTo(toEmail);
        helper.setFrom(emailFrom);
        helper.setSubject("网站访问失败警告");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("domain", website.getDomain());
        context.put("accessFailCount", website.getAccessFailCount());
        context.put("date", Utils.formatDate(website.getAccessFailTime(), "yyyy-MM-dd HH:mm:ss"));
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(accessURLFailMailTemplate, context);
        helper.setText(content, true);
        mailSender.send(msg);
        logger.info("HTML版邮件已发送");
    }

    public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
    }

    public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
      accessURLFailMailTemplate = freemarkerConfiguration.getTemplate("accessURLFailMailTemplate.ftl", DEFAULT_ENCODING);
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public void setEmailFrom(String emailFrom) {
    this.emailFrom = emailFrom;
  }

}
