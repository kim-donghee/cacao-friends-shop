package cacao.friends.shop.modules.account.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import cacao.friends.shop.modules.account.form.PasswordForm;

@Component
public class PasswordFormValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordForm.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PasswordForm passwordForm = (PasswordForm) target;
		if(!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfirm())) {
			errors.rejectValue("newPassword", "wrong.value", "입력한 새 패스워드가 일치하지 않습니다.");
			errors.rejectValue("newPasswordConfirm", "wrong.value", "입력한 새 패스워드가 일치하지 않습니다.");
		}
	}

}
