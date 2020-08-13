package cacao.friends.shop.modules.member;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cacao.friends.shop.infra.mail.EmailMessage;
import cacao.friends.shop.infra.mail.EmailService;
import cacao.friends.shop.infra.mail.EmailMessageCreator;
import lombok.RequiredArgsConstructor;

@Async
@Component
@RequiredArgsConstructor
public class MemberSendEmail {
	
	private final EmailService emailService;
	
	private final EmailMessageCreator emailMessageCreator;
	
	public void sendJoinConfirmEmail(Member member) {
		String username = member.getUsername();
		String email = member.getEmail();
		String token = member.getEmailCheckToken();
		String subject = "Cacao Friends Shop, 회원 가입 인증";
		String link = "/member/check-email-token?token=" + token + "&email=" + email;
		String linkName = "이메일 인증하기";
		String message = "서비스를 사용하려면 '이메일 인증하기'를 클릭해주세요.";
		EmailMessage emailMessage = 
				emailMessageCreator.create(username, email, subject, link, linkName, message);
		emailService.sendEmail(emailMessage);
	}
	
	public void sendLoginLink(Member member) {
		String username = member.getUsername();
		String email = member.getEmail();
		String token = member.getEmailCheckToken();
		String subject = "Cacao Friends Shop, 로그인 링크";
		String link = "/member/login-by-email?token=" + token + "&email=" + email;
		String linkName = "로그인 링크";
		String message = "Cacao Friends Shop에 로그인 하려면 '로그인 링크'를 클릭해주세요.";
		EmailMessage emailMessage = 
				emailMessageCreator.create(username, email, subject, link, linkName, message);
		emailService.sendEmail(emailMessage);
	}
	
}
