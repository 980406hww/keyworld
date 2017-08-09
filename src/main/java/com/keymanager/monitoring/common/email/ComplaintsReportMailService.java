package com.keymanager.monitoring.common.email;

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

  private Template complaintsReportTemplateOver2weeks;
  private Template complaintsReportTemplateOver3Times;

  public void sendComplaintsReportOver2weeks(String username, String toEmail,
      List<ComplaintMailVO> PCOver2WeekstSmainKeywordVOS,
      List<ComplaintMailVO> phoneOver2WeekstSmainKeywordVOS) {
    try {
      MimeMessage msg = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
      String[] toEmailArr = toEmail.split(",");
      helper.setTo(toEmailArr);
      helper.setFrom(emailFrom);
      helper.setSubject("两周内投诉失败的反馈记录");
      Map<String, Object> context = new HashMap<String, Object>();
      context.put("username", username);
      context.put("PCOver2weekstSmainKeyWordVOS", PCOver2WeekstSmainKeywordVOS);
      context.put("PhoneOver2weekstSmainKeyWordVOS", phoneOver2WeekstSmainKeywordVOS);
      String content = FreeMarkerTemplateUtils.processTemplateIntoString(
          complaintsReportTemplateOver2weeks, context);
      helper.setText(content, true);
      mailSender.send(msg);
      logger.info("HTML版邮件已发送");
//      if (CollectionUtils.isNotEmpty(PCOver2WeekstSmainKeywordVOS)) {
//        TSNegativeKeyword tsNegativeKeyword = new TSNegativeKeyword();
//        for (ComplaintMailVO tSmainKeyWordVO : phoneOver2WeekstSmainKeywordVOS) {
//          tsNegativeKeyword.setUuid(tSmainKeyWordVO.getTsNegativeKeywordUuid());
//          if (null == tSmainKeyWordVO.getPcEmailSentOver2Weeks()
//              || tSmainKeyWordVO.getPcEmailSentOver2Weeks() == 0) {
//            tsNegativeKeyword.setPcEmailSentOver2Weeks(1);
//          }
//          tsNegativeKeywordService.updateById(tsNegativeKeyword);
//        }
//      }
//      if (CollectionUtils.isNotEmpty(phoneOver2WeekstSmainKeywordVOS)) {
//        TSNegativeKeyword tsNegativeKeyword = new TSNegativeKeyword();
//        for (ComplaintMailVO tSmainKeyWordVO : phoneOver2WeekstSmainKeywordVOS) {
//          tsNegativeKeyword.setUuid(tSmainKeyWordVO.getTsNegativeKeywordUuid());
//          if (null == tSmainKeyWordVO.getPcEmailSentOver2Weeks()
//              || tSmainKeyWordVO.getPcEmailSentOver2Weeks() == 0) {
//            tsNegativeKeyword.setPcEmailSentOver2Weeks(1);
//          }
//          tsNegativeKeywordService.updateById(tsNegativeKeyword);
//        }
//      }
    } catch (MessagingException e) {
      logger.error("构造邮件失败", e);
    } catch (Exception e) {
      logger.error("发送邮件失败", e);
    }
  }

  public void sendComplaintsReportOver3Times(String username, String toEmail,
      List<ComplaintMailVO> PCOver3TimestSmainKeywordVOS,
      List<ComplaintMailVO> phoneOver3TimestSmainKeywordVOS) {
    try {
      MimeMessage msg = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
      String[] toEmailArr = toEmail.split(",");
      helper.setTo(toEmailArr);
      helper.setFrom(emailFrom);
      helper.setSubject("反复出现3次投诉失败的反馈记录");
      Map<String, Object> context = new HashMap<String, Object>();
      context.put("username", username);
      context.put("PCOver3TimestSmainKeywordVOS", PCOver3TimestSmainKeywordVOS);
      context.put("phoneOver3TimestSmainKeywordVOS", phoneOver3TimestSmainKeywordVOS);
      String content = FreeMarkerTemplateUtils.processTemplateIntoString(
          complaintsReportTemplateOver3Times, context);
      helper.setText(content, true);

      mailSender.send(msg);
      logger.info("HTML版邮件已发送");
//      if (CollectionUtils.isNotEmpty(PCOver3TimestSmainKeywordVOS)) {
//        TSNegativeKeyword tsNegativeKeyword = new TSNegativeKeyword();
//        for (ComplaintMailVO tSmainKeyWordVO : PCOver3TimestSmainKeywordVOS) {
//          tsNegativeKeyword.setUuid(tSmainKeyWordVO.getTsNegativeKeywordUuid());
//          if (null == tSmainKeyWordVO.getPcEmailSentOver3Times()
//              || tSmainKeyWordVO.getPcEmailSentOver3Times() == 0) {
//            tsNegativeKeyword.setPcEmailSentOver3Times(1);
//          }
//          tsNegativeKeywordService.updateById(tsNegativeKeyword);
//        }
//      }
//      if (CollectionUtils.isNotEmpty(phoneOver3TimestSmainKeywordVOS)) {
//        TSNegativeKeyword tsNegativeKeyword = new TSNegativeKeyword();
//        for (ComplaintMailVO tSmainKeyWordVO : phoneOver3TimestSmainKeywordVOS) {
//          tsNegativeKeyword.setUuid(tSmainKeyWordVO.getTsNegativeKeywordUuid());
//          if (null == tSmainKeyWordVO.getPcEmailSentOver3Times()
//              || tSmainKeyWordVO.getPcEmailSentOver3Times() == 0) {
//            tsNegativeKeyword.setPcEmailSentOver3Times(1);
//          }
//          tsNegativeKeywordService.updateById(tsNegativeKeyword);
//        }
//      }
    } catch (MessagingException e) {
      logger.error("构造邮件失败", e);
    } catch (Exception e) {
      logger.error("发送邮件失败", e);
    }
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
}
