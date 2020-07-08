package cacao.friends.shop.modules.member;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import cacao.friends.shop.infra.mail.EmailMessage;
import cacao.friends.shop.infra.mail.EmailService;
import cacao.friends.shop.infra.mail.EmailText;
import lombok.RequiredArgsConstructor;

@Async
@Component
@RequiredArgsConstructor
public class MemberSendEmail {
	
	private final EmailService emailService;
	
	private final EmailText emailText;
	
	public void sendJoinConfirmEmail(Member member) {
		String text = emailText.create(member.getUsername(),
				"/member/check-email-token?token=" + member.getEmailCheckToken() + "&email=" + member.getEmail(), 
				"이메일 인증하기", "서비스를 사용하려면 '이메일 인증하기'를 클릭해주세요.");
		String subject = "Cacao Friends Shop, 회원 가입 인증";
		sendEmail(member, text, subject);
	}
	
	public void sendLoginLink(Member member) {
		String text = emailText.create(member.getUsername(),
				"/member/login-by-email?token=" + member.getEmailCheckToken() + "&email=" + member.getEmail(),
				"로그인 링크", "Cacao Friends Shop에 로그인 하려면 '로그인 링크'를 클릭해주세요.");
		String subject = "Cacao Friends Shop, 로그인 링크";
		sendEmail(member, text, subject);
	}
	
	private void sendEmail(Member member, String text, String subject) {
		EmailMessage emailMessage = new EmailMessage(member.getEmail(), subject, text);
		emailService.sendEmail(emailMessage);
	}

}
