package cacao.friends.shop.infra.mail;

import java.io.InputStream;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Profile({"local", "dev"})
//@Component
public class ConsoleMailSender implements JavaMailSender {

	@Override
	public void send(SimpleMailMessage arg0) throws MailException {
		log.info(arg0.getText());
	}

	@Override
	public void send(SimpleMailMessage... arg0) throws MailException {
	}

	@Override
	public MimeMessage createMimeMessage() {
		return null;
	}

	@Override
	public MimeMessage createMimeMessage(InputStream arg0) throws MailException {
		return null;
	}

	@Override
	public void send(MimeMessage arg0) throws MailException {
	}

	@Override
	public void send(MimeMessage... arg0) throws MailException {
	}

	@Override
	public void send(MimeMessagePreparator arg0) throws MailException {
	}

	@Override
	public void send(MimeMessagePreparator... arg0) throws MailException {
	}

}
