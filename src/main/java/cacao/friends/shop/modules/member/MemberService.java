package cacao.friends.shop.modules.member;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
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

import cacao.friends.shop.modules.address.form.AddressForm;
import cacao.friends.shop.modules.characterKind.CharacterKind;
import cacao.friends.shop.modules.member.form.JoinForm;
import cacao.friends.shop.modules.member.form.NotificationsForm;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService  implements UserDetailsService {
	
	private final MemberRepository repo;
	
	private final ModelMapper modelMapper;
	
	private final MemberSendEmail memberSendEmail;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
		Member member = repo.findByUsername(emailOrUsername);
		if(member == null) {
			member = repo.findByEmail(emailOrUsername);
		}
		if(member == null) {
			throw new UsernameNotFoundException(emailOrUsername);
		}
		
		return new UserMember(member);
	}
	
	// 회원가입 과정 (저장 -> 메시지 전송)
	public Member joinProcess(JoinForm joinForm) {
		Member newMember = saveNewMember(joinForm);
		sendJoinConfirmEmail(newMember);
		return newMember;
	}
	
	private Member saveNewMember(JoinForm joinForm) {
		joinForm.setPassword(passwordEncoder.encode(joinForm.getPassword()));
		Member member = modelMapper.map(joinForm, Member.class);
		Member newMember = repo.save(member);
		return newMember;
	}
	
	// 회원가입 확인 메시지
	public void sendJoinConfirmEmail(Member member) {
		member.generateEmailToken();
		memberSendEmail.sendJoinConfirmEmail(member);
	}
	
	// 이메일 로그인 이메일 전송(패스워드 없이 로그인하기 위해 가입한 이메일에 토큰 전송)
	public void sendLoginLink(Member member) {
		member.generateEmailToken();
		memberSendEmail.sendLoginLink(member);
	}
	
	public void login(Member member) {
		List<SimpleGrantedAuthority>  authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
				UsernamePasswordAuthenticationToken token =
				new UsernamePasswordAuthenticationToken(new UserMember(member), member.getPassword(), UserMember.memberAuthorities());
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(token);
	}
	
	public void completeJoin(Member member) {
		member.completeJoin();
	}
	
	public boolean validToken(Member member, String token) {
		if(member == null)
			return false;
		return member.isValidToken(token);
	}
	
	public void updateAddress(Member member, AddressForm addressForm) {
		member.updateAddress(addressForm);
		repo.save(member);
	}
	
	public void updatePassword(Member member, String newPassword) {
		member.setPassword(passwordEncoder.encode(newPassword));
		repo.save(member);
	}
	
	public void updateNotifications(Member member, CharacterKind pickCharacter, NotificationsForm notificationsForm) {
		modelMapper.map(notificationsForm, member);
		member.setPickCharacter(pickCharacter);
		repo.save(member);
	}

}
