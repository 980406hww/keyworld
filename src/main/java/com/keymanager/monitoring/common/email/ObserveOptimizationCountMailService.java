package com.keymanager.monitoring.common.email;

import com.keymanager.monitoring.vo.OptimizationCountVO;
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

public class ObserveOptimizationCountMailService {

    private static final String DEFAULT_ENCODING = "utf-8";
    private static Logger logger = LoggerFactory.getLogger(ObserveOptimizationCountMailService.class);
    private JavaMailSender mailSender;
    private String emailFrom;
    private Template optimizationCountMailTemplate;

    public void sendObserveOptimizationCountMail(String email, List<OptimizationCountVO> groupOptimizationCountInfo, List<OptimizationCountVO> keywordOptimizationCountInfo) throws Exception{
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
        helper.setTo(email);
        helper.setFrom(emailFrom);
        helper.setSubject("刷量异常警告");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("groupOptimizationCountInfo", groupOptimizationCountInfo);
        context.put("keywordOptimizationCountInfo", keywordOptimizationCountInfo);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(optimizationCountMailTemplate, context);
        helper.setText(content, true);
        mailSender.send(msg);
        logger.info("HTML版邮件已发送");
    }

    public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
    }

    public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
        optimizationCountMailTemplate = freemarkerConfiguration.getTemplate("optimizationCountMailTemplate.ftl", DEFAULT_ENCODING);
    }

    public void setEmailFrom(String emailFrom) {
    this.emailFrom = emailFrom;
  }

}
