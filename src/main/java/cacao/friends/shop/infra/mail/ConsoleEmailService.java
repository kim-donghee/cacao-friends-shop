package cacao.friends.shop.infra.mail;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("local")
public class ConsoleEmailService implements EmailService {
	@Override
	public void sendEmail(EmailMessage emailMessage) {
		log.info("sent email : {}", emailMessage.getMessage());
	}
}
