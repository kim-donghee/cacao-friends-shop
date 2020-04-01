package cacao.friends.shop.modules.account;

import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.account.form.JoinForm;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
	
	private final AccountRepository accountRepository;
	
	private final ModelMapper modelMapper;
	
	private final JavaMailSender javaMailSender;
	
	public Account processNewAccount(JoinForm joinForm) {
		Account account = modelMapper.map(joinForm, Account.class);
		
		account.generateEmailToken();
		
		Account newAccount = accountRepository.save(account);
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(newAccount.getEmail());
		mailMessage.setSubject("Cacao Friends Shop, 회원 가입 인증");
		mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() +
				"&email=" + newAccount.getEmail());
		javaMailSender.send(mailMessage);
		
		return newAccount;
	}

}
