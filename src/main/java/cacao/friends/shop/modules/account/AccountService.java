package cacao.friends.shop.modules.account;

import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.account.form.JoinForm;
import cacao.friends.shop.modules.account.form.Notifications;
import cacao.friends.shop.modules.address.form.AddressForm;
import cacao.friends.shop.modules.tag.Tag;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
	
	private final AccountRepository accountRepository;
	
	private final ModelMapper modelMapper;
	
	private final JavaMailSender javaMailSender;
	
	// 저장 -> 메시지 전송
	public Account saveNewAccount(JoinForm joinForm) {
		Account account = modelMapper.map(joinForm, Account.class);
		
		account.generateEmailToken();
		
		Account newAccount = accountRepository.save(account);
		
		sendJoinConfirmEmail(newAccount, "Cacao Friends Shop, 회원 가입 인증", 
				"/check-email-token?token=" + newAccount.getEmailCheckToken() +
				"&email=" + newAccount.getEmail());
		
		return newAccount;
	}

	// 메시지 전송
	private void sendJoinConfirmEmail(Account account, String subject, String message) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(account.getEmail());
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		javaMailSender.send(mailMessage);
	}
	
	public void complateJoin(Account account) {
		account.completeJoin();
	}
	
	public void updateAddress(Account account, AddressForm addressForm) {
		account.updateAddress(addressForm);
		accountRepository.save(account);
	}
	
	public void updatePassword(Account account, String password) {
		account.setPassword(password);
		accountRepository.save(account);
	}
	
	public void updateNotifications(Account account, Tag pickTag, Notifications notifications) {
		modelMapper.map(notifications, account);
		account.setPickTag(pickTag);
		accountRepository.save(account);
	}
	
	// 패스워드 없이 로그인하기 위해 가입한 이메일에 토큰을 만들어서 전송
	public void sendLoginLink(Account account) {
		account.generateEmailToken();
		sendJoinConfirmEmail(account, "Cacao Friends Shop, 이메일 인증",
				"/login-by-email?token=" + account.getEmailCheckToken() +
				"&email=" + account.getEmail());		
	}

}
