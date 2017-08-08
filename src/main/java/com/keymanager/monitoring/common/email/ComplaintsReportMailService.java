package com.keymanager.monitoring.common.email;

import com.keymanager.monitoring.vo.TSMainKeywordVO;
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

	private JavaMailSender mailSender;
	
	private String emailFrom;

	private Template complaintsReportTemplate;

	public void sendComplaintsReport(String username, String toEmail, List<TSMainKeywordVO> TSMainKeywordVOS){
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

			helper.setTo(toEmail);
			helper.setFrom(emailFrom);
			helper.setSubject("投诉未成功的反馈记录");
			Map<String, Object> context = new HashMap<String, Object>();
			context.put("username", username);
			context.put("tsMainKeywordVOS", TSMainKeywordVOS);
			String content = FreeMarkerTemplateUtils.processTemplateIntoString(complaintsReportTemplate, context);
			helper.setText(content, true);

			mailSender.send(msg);
			logger.info("HTML版邮件已发送");
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
		complaintsReportTemplate = freemarkerConfiguration.getTemplate("complaintsReportTemplate.ftl", DEFAULT_ENCODING);
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
}
