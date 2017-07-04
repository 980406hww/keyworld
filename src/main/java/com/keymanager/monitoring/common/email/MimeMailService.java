package com.keymanager.monitoring.common.email;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * MIME邮件服务类.
 * 
 * 演示由Freemarker引擎生成的的html格式邮件, 并带有附件.
 * 
 * @author calvin
 */
public class MimeMailService {

	private static final String DEFAULT_ENCODING = "utf-8";

	private static Logger logger = LoggerFactory.getLogger(MimeMailService.class);

	private JavaMailSender mailSender;
	
	private String emailFrom;
	
	private Template changeEmailTemplate;
	
	private Template validateUserTemplate;

	private Template dailyNotificationTemplate;
	
	public void sendValidationUserMail(String toEmail, String validateUrl){
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

			helper.setTo(toEmail);
			helper.setFrom(emailFrom);
			helper.setSubject("注册验证");
			Map<String, String> context = new HashMap<String, String>();
			context.put("username", toEmail);
			context.put("content", validateUrl);
			String content = FreeMarkerTemplateUtils.processTemplateIntoString(validateUserTemplate, context);
			helper.setText(content, true);

			//File attachment = generateAttachment();
			//helper.addAttachment("mailAttachment.txt", attachment);

			mailSender.send(msg);
			logger.info("HTML版邮件已发送");
		} catch (MessagingException e) {
			logger.error("构造邮件失败", e);
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}
	
	public void sendChangeMail(String toEmail, String validateCode){
		try {
			MimeMessage msg = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

			helper.setTo(toEmail);
			helper.setFrom(emailFrom);
			helper.setSubject("用户邮箱验证");
			Map<String, String> context = new HashMap<String, String>();
			context.put("username", toEmail);
			context.put("content", validateCode);
			String content = FreeMarkerTemplateUtils.processTemplateIntoString(changeEmailTemplate, context);
			helper.setText(content, true);

			//File attachment = generateAttachment();
			//helper.addAttachment("mailAttachment.txt", attachment);

			mailSender.send(msg);
			logger.info("HTML版邮件已发送");
		} catch (MessagingException e) {
			logger.error("构造邮件失败", e);
		} catch (Exception e) {
			logger.error("发送邮件失败", e);
		}
	}
	
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	/**
	 * 获取classpath中的附件.
	 */
	private File generateAttachment() throws MessagingException {
		try {
			Resource resource = new ClassPathResource("/email/mailAttachment.txt");
			return resource.getFile();
		} catch (IOException e) {
			logger.error("构造邮件失败,附件文件不存在", e);
			throw new MessagingException("附件文件不存在", e);
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
		validateUserTemplate = freemarkerConfiguration.getTemplate("validateUserTemplate.ftl", DEFAULT_ENCODING);
		changeEmailTemplate = freemarkerConfiguration.getTemplate("changeEmailTemplate.ftl", DEFAULT_ENCODING);
		dailyNotificationTemplate = freemarkerConfiguration.getTemplate("dailyNotificationTemplate.ftl", DEFAULT_ENCODING);
	}
}
