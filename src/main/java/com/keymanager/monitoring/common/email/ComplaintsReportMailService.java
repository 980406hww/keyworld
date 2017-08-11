package com.keymanager.monitoring.common.email;

import com.keymanager.monitoring.entity.TSNegativeKeyword;
import com.keymanager.monitoring.service.TSNegativeKeywordService;
import com.keymanager.monitoring.vo.ComplaintMailVO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * 投诉邮件服务类.
 *
 * 演示由Freemarker引擎生成的的html格式邮件, 并带有附件.
 *
 * @author calvin
 */

public class ComplaintsReportMailService {

  private static final String DEFAULT_ENCODING = "utf-8";

  private static Logger logger = LoggerFactory.getLogger(ComplaintsReportMailService.class);

  @Autowired
  private TSNegativeKeywordService tsNegativeKeywordService;

  private JavaMailSender mailSender;

  private String emailFrom;
  private String name;
  private String toEmail;

  private Template complaintsReportTemplateOver2weeks;
  private Template complaintsReportTemplateOver3Times;

  public void sendComplaintsReportOver2weeks(
      List<ComplaintMailVO> PCOver2WeekstSmainKeywordVOS,
      List<ComplaintMailVO> phoneOver2WeekstSmainKeywordVOS) throws Exception{
      MimeMessage msg = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
      String[] toEmailArr = toEmail.split(";");
      helper.setTo(toEmailArr);
      helper.setFrom(emailFrom);
      helper.setSubject("两周内投诉失败的反馈记录");
      Map<String, Object> context = new HashMap<String, Object>();
      context.put("username", new String(name.getBytes("ISO8859-1"),"utf-8"));
      context.put("PCOver2weekstSmainKeyWordVOS", PCOver2WeekstSmainKeywordVOS);
      context.put("PhoneOver2weekstSmainKeyWordVOS", phoneOver2WeekstSmainKeywordVOS);
      String content = FreeMarkerTemplateUtils.processTemplateIntoString(complaintsReportTemplateOver2weeks, context);
      helper.setText(content, true);
      mailSender.send(msg);
      logger.info("HTML版邮件已发送");

  }

  public void sendComplaintsReportOver3Times(
      List<ComplaintMailVO> PCOver3TimestSmainKeywordVOS,
      List<ComplaintMailVO> phoneOver3TimestSmainKeywordVOS) throws Exception{
      MimeMessage msg = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
      String[] toEmailArr = toEmail.split(";");
      helper.setTo(toEmailArr);
      helper.setFrom(emailFrom);
      helper.setSubject("反复出现3次投诉失败的反馈记录");
      Map<String, Object> context = new HashMap<String, Object>();
//      context.put("username", name);
      context.put("username", new String(name.getBytes("ISO8859-1"),"utf-8"));
      context.put("PCOver3TimestSmainKeywordVOS", PCOver3TimestSmainKeywordVOS);
      context.put("phoneOver3TimestSmainKeywordVOS", phoneOver3TimestSmainKeywordVOS);
      String content = FreeMarkerTemplateUtils.processTemplateIntoString(complaintsReportTemplateOver3Times, context);
      helper.setText(content, true);

      mailSender.send(msg);
      logger.info("HTML版邮件已发送");
  }


  /**
   * Spring的MailSender.
   */
  public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * 注入Freemarker引擎配置,构造Freemarker 邮件内容模板.
   */
  public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
    // 根据freemarkerConfiguration的templateLoaderPath载入文件.
    complaintsReportTemplateOver2weeks = freemarkerConfiguration
        .getTemplate("complaintsReportTemplateOver2weeks.ftl", DEFAULT_ENCODING);
    complaintsReportTemplateOver3Times = freemarkerConfiguration
        .getTemplate("complaintsReportTemplateOver3Times.ftl", DEFAULT_ENCODING);
  }

  public void setEmailFrom(String emailFrom) {
    this.emailFrom = emailFrom;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setToEmail(String toEmail) {
    this.toEmail = toEmail;
  }
}
