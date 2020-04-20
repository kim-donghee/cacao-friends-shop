package cacao.friends.shop.modules.member.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class JoinForm {
	
	@NotBlank
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
	private String username;
	
	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	@Length(min = 8, max = 50)
	private String password;

}
