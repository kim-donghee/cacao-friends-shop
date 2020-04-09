package cacao.friends.shop.modules.account;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cacao.friends.shop.modules.CharacterKind.CharacterKind;
import cacao.friends.shop.modules.account.form.JoinForm;
import cacao.friends.shop.modules.account.form.NotificationsForm;
import cacao.friends.shop.modules.address.form.AddressForm;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService  implements UserDetailsService {
	
	private final AccountRepository accountRepository;
	
	private final ModelMapper modelMapper;
	
	private final JavaMailSender javaMailSender;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
		Account account = accountRepository.findByUsername(emailOrUsername);
		if(account == null) {
			account = accountRepository.findByEmail(emailOrUsername);
		}
		if(account == null) {
			throw new UsernameNotFoundException(emailOrUsername);
		}
		
		return new UserAccount(account);
	}
	
	// 저장 -> 메시지 전송
	public Account saveNewAccount(JoinForm joinForm) {
		joinForm.setPassword(passwordEncoder.encode(joinForm.getPassword()));
		Account account = modelMapper.map(joinForm, Account.class);
		
		account.generateEmailToken();
		
		Account newAccount = accountRepository.save(account);

		sendJoinConfirmEmail(newAccount, "Cacao Friends Shop, 회원 가입 인증", 
				"/account/check-email-token?token=" + newAccount.getEmailCheckToken() +
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
	
	public void login(Account account) {
		UsernamePasswordAuthenticationToken token = 
				new UsernamePasswordAuthenticationToken(new UserAccount(account), account.getPassword(), 
						List.of(new SimpleGrantedAuthority("ROLE_USER")));
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(token);
	}
	
	public void complateJoin(Account account) {
		account.completeJoin();
	}
	
	public void updateAddress(Account account, AddressForm addressForm) {
		account.updateAddress(addressForm);
		accountRepository.save(account);
	}
	
	public void updatePassword(Account account, String newPassword) {
		account.setPassword(passwordEncoder.encode(newPassword));
		accountRepository.save(account);
	}
	
	public void updateNotifications(Account account, CharacterKind pickTag, NotificationsForm notificationsForm) {
		modelMapper.map(notificationsForm, account);
		account.setPickTag(pickTag);
		accountRepository.save(account);
	}
	
	// 패스워드 없이 로그인하기 위해 가입한 이메일에 토큰을 만들어서 전송
	public void sendLoginLink(Account account) {
		account.generateEmailToken();
		sendJoinConfirmEmail(account, "Cacao Friends Shop, 이메일 인증",
				"/account/login-by-email?token=" + account.getEmailCheckToken() +
				"&email=" + account.getEmail());		
	}

}
