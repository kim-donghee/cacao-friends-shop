package cacao.friends.shop.infra.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import cacao.friends.shop.infra.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("dev")
@RequiredArgsConstructor
public class HtmlEmailService implements EmailService {
	
	private final JavaMailSender javaMailSender;
	
	private final TemplateEngine templateEngine;
	
	private final AppProperties appProperties;

	@Override
	public void sendEmail(EmailMessage emailMessage) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			messageHelper.setTo(emailMessage.getTo());
			messageHelper.setSubject(emailMessage.getSubject());
			messageHelper.setText(emailMessage.getText(), true);
			javaMailSender.send(mimeMessage);
			log.info(emailMessage.getText());
		} catch (MessagingException e) {
			log.error("failed to send email", e);
			throw new RuntimeException();
		}
	}

	@Override
	public String createText(String username, String link, String linkName, String message) {
		Context context = new Context();
		context.setVariable("username", username);
		context.setVariable("link", link);
		context.setVariable("linkName", linkName);
		context.setVariable("message", message);
		context.setVariable("host", appProperties.getHost());
		return templateEngine.process("member/mail/simple-link", context);	
	}

}
