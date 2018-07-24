package com.keymanager.monitoring.common.email;

import com.keymanager.monitoring.entity.NegativeRank;
import com.keymanager.monitoring.entity.NegativeStandardSetting;
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

public class NegativeStandardSettingMailService {

    private static final String DEFAULT_ENCODING = "utf-8";
    private static Logger logger = LoggerFactory.getLogger(NegativeStandardSettingMailService.class);
    private JavaMailSender mailSender;
    private String emailFrom;
    private Template negativeStandardSettingTemplate;

    public void sendNegativeStandardSettingMail(String email, List<NegativeStandardSetting> negativeStandardSettingList, List<NegativeRank> negativeRankList,String userName) throws Exception{
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);
        helper.setTo(email);
        helper.setFrom(emailFrom);
        helper.setSubject("负面达标设置提醒");
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("negativeStandardSettingList", negativeStandardSettingList);
        context.put("negativeRankList",negativeRankList);
        context.put("userName",userName);
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(negativeStandardSettingTemplate, context);
        helper.setText(content, true);
        mailSender.send(msg);
        logger.info("HTML版邮件已发送");
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) throws IOException {
        negativeStandardSettingTemplate = freemarkerConfiguration.getTemplate("negativeStandardSettingMailTemplate.ftl", DEFAULT_ENCODING);
    }

    public void setEmailFrom(String emailFrom) {
    this.emailFrom = emailFrom;
  }

}
