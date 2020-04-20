package cacao.friends.shop.modules.member.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cacao.friends.shop.modules.member.MemberRepository;
import cacao.friends.shop.modules.member.form.JoinForm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JoinFormValidator implements Validator {
	
	private final MemberRepository memberRepository;

	@Override
	public boolean supports(Class<?> clazz) {
		return JoinForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		JoinForm joinForm = (JoinForm) target;
		
		if(memberRepository.existsByEmail(joinForm.getEmail())) {
			errors.rejectValue("email", "invalid.email", new Object[] {joinForm.getEmail()}, 
					"이미 사용중인 이메일입니다.");
		}
		
		if(memberRepository.existsByUsername(joinForm.getUsername())) {
			errors.rejectValue("username", "invalid.username", new Object[] {joinForm.getUsername()}, 
					"이미 사용중인 닉네임입니다.");
		}
		
	}

}
