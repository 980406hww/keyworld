package com.keymanager.monitoring.common.email;

import com.keymanager.monitoring.service.TSNegativeKeywordService;
import com.keymanager.monitoring.vo.ComplaintMailVO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgetPasswordMailService {

  private static final String DEFAULT_ENCODING = "utf-8";
  private static Logger logger = LoggerFactory.getLogger(ForgetPasswordMailService.class);
  private JavaMailSender mailSender;
  private String emailFrom;
  private Template forgetPasswordMailTemplate;

  public void sendForgetPasswordMail(String loginName,String toEmail,String mailContent) throws Exception{
      MimeMessage msg = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
      helper.setTo(toEmail);
      helper.setFrom(emailFrom);
      helper.setSubject("找回密码");
      Map<String, Object> context = new HashMap<String, Object>();
      context.put("loginName", loginName);
      context.put("mailContent", mailContent);
      String content = FreeMarkerTemplateUtils.processTemplateIntoString(forgetPasswordMailTemplate, context);
      helper.setText(content, true);
      mailSender.send(msg);
      logger.info("HTML版邮件已发送");
  }

  public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
      forgetPasswordMailTemplate = freemarkerConfiguration.getTemplate("forgetPasswordMailTemplate.ftl", DEFAULT_ENCODING);
  }

  public void setEmailFrom(String emailFrom) {
    this.emailFrom = emailFrom;
  }

}
