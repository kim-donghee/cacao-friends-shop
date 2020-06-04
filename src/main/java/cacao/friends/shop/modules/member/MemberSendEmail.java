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
	
	public void sendEmail(String username, String email, String subject, String link, String linkName, String message) {
		String text = emailService.createText(username, link, linkName, message);
		EmailMessage emailMessage = new EmailMessage(email, subject, text);
		emailService.sendEmail(emailMessage);
	}
	
	public void sendJoinConfirmEmail(Member member) {
		sendEmail(member.getUsername(), member.getEmail(), "Cacao Friends Shop, 회원 가입 인증", 
				"/member/check-email-token?token=" + member.getEmailCheckToken() + "&email=" + member.getEmail(), 
				"이메일 인증하기", "서비스를 사용하려면 '이메일 인증하기'를 클릭해주세요.");
	}
	
	public void sendLoginLink(Member member) {
		sendEmail(member.getUsername(), member.getEmail(), "Cacao Friends Shop, 로그인 링크", 
				"/member/login-by-email?token=" + member.getEmailCheckToken() + "&email=" + member.getEmail(), 
				"로그인 링크", "Cacao Friends Shop에 로그인 하려면 '로그인 링크'를 클릭해주세요.");
	}

}
