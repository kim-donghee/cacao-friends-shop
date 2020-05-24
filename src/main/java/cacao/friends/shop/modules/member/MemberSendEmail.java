package cacao.friends.shop.modules.member;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cacao.friends.shop.infra.mail.EmailMessage;
import cacao.friends.shop.infra.mail.EmailService;
import lombok.RequiredArgsConstructor;

@Async
@Component
@RequiredArgsConstructor
public class MemberSendEmail {
	
	private final EmailService emailService;
	
	public void sendEmail(Member member, String subject, String link, String linkName, String message) {
		String text = emailService.createText(member.getUsername(), link, linkName, message);
		EmailMessage emailMessage = EmailMessage.builder()
				.to(member.getEmail())
				.subject(subject)
				.text(text)
				.build();
		emailService.sendEmail(emailMessage);
	}

}
