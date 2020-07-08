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
	
	private final MemberRepository memberRepository;
	
	private final ModelMapper modelMapper;
	
	private final MemberSendEmail memberSendEmail;
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
		Member member = memberRepository.findByUsername(emailOrUsername);
		if(member == null) {
			member = memberRepository.findByEmail(emailOrUsername);
		}
		if(member == null) {
			throw new UsernameNotFoundException(emailOrUsername);
		}
		
		return new UserMember(member);
	}
	
	// 저장 -> 메시지 전송
	public Member saveNewAccount(JoinForm joinForm) {
		joinForm.setPassword(passwordEncoder.encode(joinForm.getPassword()));
		Member member = modelMapper.map(joinForm, Member.class);
		Member newMember = memberRepository.save(member);
		sendJoinConfirmEmail(newMember);
		return newMember;
	}
	
	public void sendJoinConfirmEmail(Member member) {
		member.generateEmailToken();
		memberSendEmail.sendJoinConfirmEmail(member);
	}
	
	// 패스워드 없이 로그인하기 위해 가입한 이메일에 토큰을 만들어서 전송
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
	
	public void updateAddress(Member member, AddressForm addressForm) {
		member.updateAddress(addressForm);
		memberRepository.save(member);
	}
	
	public void updatePassword(Member member, String newPassword) {
		member.setPassword(passwordEncoder.encode(newPassword));
		memberRepository.save(member);
	}
	
	public void updateNotifications(Member member, CharacterKind pickCharacter, NotificationsForm notificationsForm) {
		modelMapper.map(notificationsForm, member);
		member.setPickCharacter(pickCharacter);
		memberRepository.save(member);
	}

}
